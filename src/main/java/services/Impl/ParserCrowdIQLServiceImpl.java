package services.Impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.sun.prism.Material;

import Cluster.ClusterImpl;
import FunctionsSupport.AlgorithmIn;
import FunctionsSupport.Parser;
import daos.Interface.RAlgorithmDao;
import daos.Interface.RTableDao;
import daos.Interface.RTableListDao;
import domains.RAlgorithm;
import domains.RTable;
import domains.RTableList;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import services.Interface.ParserCrowdIQLService;

public class ParserCrowdIQLServiceImpl implements ParserCrowdIQLService {
	
	private RAlgorithmDao rAlgorithmDao;
	
	private RTableDao rTableDao;
	
	private RTableListDao rTableListDao;
	
	private Integer[] targetsBlank;
	private Integer[] showingsBlank;
	private Double top_kPerc; 
	
	public RTableListDao getrTableListDao() {
		return rTableListDao;
	}

	public void setrTableListDao(RTableListDao rTableListDao) {
		this.rTableListDao = rTableListDao;
	}

	public RTableDao getrTableDao() {
		return rTableDao;
	}

	public void setrTableDao(RTableDao rTableDao) {
		this.rTableDao = rTableDao;
	}

	public RAlgorithmDao getrAlgorithmDao() {
		return rAlgorithmDao;
	}

	public void setrAlgorithmDao(RAlgorithmDao rAlgorithmDao) {
		this.rAlgorithmDao = rAlgorithmDao;
	}

	private Parser parser;

	@Override
	public String parser(String sql, String userID, String tablename) {
		// TODO Auto-generated method stub
		//先要找对对应的jsontable数据
		RTable rTable = rTableDao.findByIDName(userID, tablename).get(0);
		
		return process(sql, userID, tablename, JSONObject.fromObject(rTable.getJsontable()));
	}

	//找出元素的正则表达式
	private String[] regex(String element){
				
		String[] regex = new String[]{"(.+)\\[(\\d+)\\]\\[(\\d+)\\]","(.+)\\[(\\d+)\\]()","(.+)()()"};
		String[] elements = new String[3];
				
		for(int i=0;i<3;i++){
			Pattern pattern = Pattern.compile(regex[i]);
			Matcher matcher = pattern.matcher(element);
			while (matcher.find()) { 	
				elements[0] = matcher.group(1);
				elements[1] = matcher.group(2);
				elements[2] = matcher.group(3);
				return elements;
			}		
		}
		return elements;
	}
		
	//根据a[2][3],找到对应的jsontable中内容,用于showing,algorithm
	//返回结果字符串，可能是字符串，可能是一维数组，也可能是二维数组
	private String findAttribute(String[] subattributes, JSONObject jsonTable){
			
		String result = null;
		String attribute_name = subattributes[0];
		int first_number;
		int second_number;
			
		if (!subattributes[1].equals("")) {
			first_number = Integer.valueOf(subattributes[1]);
			if (!subattributes[2].equals("")) {
				second_number = Integer.valueOf(subattributes[2]);
				//这里需要把行列对应起来
				if (attribute_name.equals("rows")) {
					result = ((JSONArray)jsonTable.get("data")).getJSONArray(first_number).getString(second_number);
				}else if (attribute_name.equals("columns")) {
						result = ((JSONArray)jsonTable.get("data")).getJSONArray(second_number).getString(first_number);
					}else {
						result = ((JSONArray)jsonTable.get(attribute_name)).getJSONArray(first_number).getString(second_number);
					}
				}else {
					//需要区分行列对应
					if (attribute_name.equals("rows")) {
						result = ((JSONArray)jsonTable.get("data")).getString(first_number);					
					}else if (attribute_name.equals("columns")) {
						JSONArray columns = new JSONArray();
						for (int i=0;i<jsonTable.getJSONArray("data").size();i++){
							columns.add(jsonTable.getJSONArray("data").getJSONArray(i).getString(first_number));
						}
						result = columns.toString();
					}else {
						result = ((JSONArray)jsonTable.get(attribute_name)).getString(first_number);		
					}
				}
			}else {
				//需要区分行列对应
				if (attribute_name.equals("rows")) {
					result = jsonTable.get("data").toString();		
				}else if (attribute_name.equals("columns")) {
					int row_length = jsonTable.getJSONArray("data").size();
					int column_length = jsonTable.getJSONArray("data").getJSONArray(0).size();
					String[][] columns = new String[column_length][row_length];
					
					//引入json方便输出二维数组
					JSONObject jsonObject = new JSONObject();
					for (int i=0;i<row_length;i++){
						for(int j=0;j<column_length;j++){
							columns[j][i] = jsonTable.getJSONArray("data").getJSONArray(i).getString(j);
						}
						jsonObject.put("columns", columns);
						result = jsonObject.get("columns").toString();
						}
				}else {
					result = jsonTable.get(attribute_name).toString();			
				}
			}
			
			return result;
		}
		
	//插入对应的内容，用于insert rows[2] columns[3](对应表头个数也要变化) 新增2行 3列
	private JSONObject insertAttribute(String[] subattributes, JSONObject jsonTable) {
			String attribute_name = subattributes[0];
			int first_number;
			
			if (!subattributes[1].equals("")) {
				first_number = Integer.valueOf(subattributes[1]);
				
				int row_length = jsonTable.getJSONArray("data").size();
				int column_length = jsonTable.getJSONArray("data").getJSONArray(0).size();
				
				//这里需要区分行列对应	
				//这里需要获取对应属性 行列的 数值，这里假定不是空表，则其行列值根据已有的数组来判断
				
				if (attribute_name.equals("rows")) {
					//初始化这个数组
					String[] new_attribute = new String[column_length];
					for(int i=0;i<column_length;i++){
						new_attribute[i]="";
					}
					//循环放入初始化的元素
					for(int j=0;j<first_number;j++){
						jsonTable.getJSONArray("data").add(new_attribute);		
					}
				}else if (attribute_name.equals("columns")) {
					for (int i = 0; i < first_number ; i++) {
						for (int j = 0; j < row_length; j++) {
							jsonTable.getJSONArray("data").getJSONArray(j).add("");
						}
						jsonTable.getJSONArray("headers").add("");
					}
				}else {
					System.out.println("这个属性不能增加[]");
				}
			}else {
				//这个直接放一个属性名即可
				jsonTable.put(attribute_name, "");
			}
			return jsonTable;
		}

	//根据众包用户获取值之后，填充select,update的值.
	//value 具体形态需要更具subattribute来定，可能是一维，或者字符串。理论上不可能是二维数组，没有这样的任务
	public JSONObject fillContent(String attributes,String value, JSONObject jsonTable){
		
		String[] subattributes = regex(attributes);
		String attribute_name = subattributes[0];
		int first_number;
		int second_number;
		if (!subattributes[1].equals("")) {
			first_number = Integer.valueOf(subattributes[1]);
			if (!subattributes[2].equals("")) {
				//这里value肯定是单纯字符串
				second_number = Integer.valueOf(subattributes[2]);
				if (attribute_name.equals("rows")) {
					//这种表达方式太累赘
					((JSONArray)((JSONArray)jsonTable.get("data")).get(first_number)).remove(second_number);
					((JSONArray)((JSONArray)jsonTable.get("data")).get(first_number)).add(second_number,value);
				}else if (attribute_name.equals("columns")) {
					((JSONArray)((JSONArray)jsonTable.get("data")).get(second_number)).remove(first_number);
					((JSONArray)((JSONArray)jsonTable.get("data")).get(second_number)).add(first_number,value);
				}else {
					((JSONArray)((JSONArray)jsonTable.get(attribute_name)).get(first_number)).remove(second_number);
					((JSONArray)((JSONArray)jsonTable.get(attribute_name)).get(first_number)).add(second_number,value);
				}
			}else {
				//这里value肯定是一个一维数组
				JSONArray jsonArray = JSONArray.fromObject(value);					
				
				if (attribute_name.equals("rows")) {
					//这里value应该是一个数组，
					((JSONArray)jsonTable.get("data")).remove(first_number);
					((JSONArray)jsonTable.get("data")).add(first_number, jsonArray);
				}else if (attribute_name.equals("columns")) {
					//需要把第2列数据全部替换掉，替换的数一定是个数组
					int row_length =jsonTable.getJSONArray("data").size();
					for (int i = 0; i < row_length; i++) {
						jsonTable.getJSONArray("data").getJSONArray(i).remove(first_number);
						jsonTable.getJSONArray("data").getJSONArray(i).add(first_number, jsonArray.get(i));
					}
				}else {
					((JSONArray)jsonTable.get(attribute_name)).remove(first_number);
					((JSONArray)jsonTable.get(attribute_name)).add(first_number, value);	
				}
			}
		}else {
			//这里value肯定是字符串
			jsonTable.put(attribute_name, value);
		}
		return jsonTable;
	}
	
	//这部分只有showing有用，on部分不再使用
	//先判断数据库中是否存在这个tablelist
	//然后把包里面的表格全部转换成二维数组
	//这样总的就是一个三维数组,但返回值是String，不过这个String本质是三维数组
	//ArrayList<String>和String的互相转换关系没有，所以用JSONArray更加方便
	private String getTableList(String userID, String tablelist){
		
		if (!rTableListDao.findByIDName(userID, tablelist).isEmpty()) {
			//在tablelists目录下找到对应文件夹（里面文件csv格式），然后然后将里面每一张表格转成二维数组
			File file = new File("WEB-INF/tablelists/"+tablelist);
			File[] tables = file.listFiles();
			JSONArray tablesJSON = new JSONArray();
			for (int i = 0; i < tables.length; i++) {
				String tablename = tables[i].getName();
				ArrayList<String[]> readList = new ArrayList<>();
				try {
					CsvReader reader = new CsvReader("WEB-INF/tablelists/"+tablelist+"/"+tablename,',',Charset.forName("utf-8"));
				    while(reader.readRecord()){ //逐行读入数据      
				        readList.add(reader.getValues());  
				    }              
				    reader.close();
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tablesJSON.add(JSONArray.fromObject(readList));
			}
			return tablesJSON.toString();
		}
		return null;
	}
	
	private String process(String sql, String userID, String tablename, JSONObject jsonTable){
			
			//得到每个元素，且操作对应的table
			parser=new Parser();
			//这里的map里面的内容都还是原始sql的，没有转换，Object可能null，一维，二维数组
			Map<String, Object> map_element = parser.getElements(sql);
			
			//构造一个用于返回真实值的Map
			Map<String, Object> map_result = new HashMap<>();
			
			//showing需要返回前端值
			if (map_element.get("showing")!=null) {
				String[] showing = (String[]) map_element.get("showing");
				JSONArray results = new JSONArray();
				
				//这里处理方式太繁琐，代码冗余！！！
				for(int i=0;i<showing.length;i++){
					String result;
					//若是以cluster(开头，则需要先把里面的数据取出来，然后变成二维数组传入，
					//返回值是一个JSONArray的String，本质是一个二维数组
					if (showing[i].startsWith("cluster(")) {
						String cluster_target = showing[i].substring(8, showing[i].length()-1).split(";")[0];
						String cluster_top = showing[i].substring(8, showing[i].length()-1).split(";")[1];
						if (showing[i].startsWith("tablelist[")) {
							result = getTableList(userID, showing[i].substring(10, showing[i].length()-1));
							//这个result是一个三维数组，里面包含表头，先要解析出每一个二维数组，分离表头和数据
							//数据聚类之后，再和表头合并，然后组成三维数组
							JSONArray tables = JSONArray.fromObject(result); //三维
							for (int j = 0; j < tables.size(); j++) {
								JSONArray table = tables.getJSONArray(j); //二维
								JSONArray headers = table.getJSONArray(0);
								
								//聚类好的二维表
								JSONArray ptable = JSONArray.fromObject(
										ClusterImpl.process(table.remove(0).toString(),Double.valueOf(cluster_top)));
								ptable.add(0, headers);
								
								tables.remove(j);
								tables.add(j, ptable);
							}
							//这样出来的tables就是处理好的三维数组
							result = tables.toString();
							result = "tablelist:"+result;
							
						}else {
							String[] attribute = showing[i].split("\\.");
							
							//通过id号对应表格，这里table直接定义为readTable，所以这里element[0]没啥用
							//这里获取三要素 attribute[3][4],里面有可能为""，注意不是null
							String[] subattributes = regex(attribute[1]);
							//得到showing具体元素值,result只能是二维，ArrayList.fromObject可以直接转换
							result = findAttribute(subattributes, jsonTable);
							result = ClusterImpl.process(result, Double.valueOf(cluster_top));
							//这里需要加入标志符：
							String ahead = subattributes[0];
							for (int j = 1; j < subattributes.length; j++) {
								if (!subattributes[j].equals("")) {
									ahead = ahead + "-" + subattributes[j];	
								}
							}
							result = ahead+":"+result;
						}	
					}else {
						if (showing[i].startsWith("tablelist[")) {
							result = getTableList(userID, showing[i].substring(10, showing[i].length()-1));
							result = "tablelist:"+result;
						}else {
							String[] attribute = showing[i].split("\\.");
							//通过id号对应表格，这里table直接定义为readTable，所以这里element[0]没啥用
							//这里获取三要素 attribute[3][4],里面有可能为""，注意不是null
							String[] subattributes = regex(attribute[1]);
							//得到showing具体元素值,result可能是一维数组，也可能是二维，ArrayList.fromObject可以直接转换
							result = findAttribute(subattributes, jsonTable);
							//这里需要加入标志符：
							String ahead = subattributes[0];
							for (int j = 1; j < subattributes.length; j++) {
								if (!subattributes[j].equals("")) {
									ahead = ahead + "-" + subattributes[j];	
								}
							}
							result = ahead+":"+result;
						}	
					}
					results.add(result);
				}
				
				map_result.put("showing_contents", results);
				
				//这里results需要解析出blanks个数
				//以 " 出现的个数即可判断，个数/2就是 showing内容单元格数
				showingsBlank = new Integer[results.size()];
				for (int j = 0; j < showingsBlank.length; j++) {
					String content = results.getString(j);
					int count = 0;
					Pattern pattern = Pattern.compile("\"");
					Matcher matcher = pattern.matcher(content);
					while (matcher.find()) {
						count++;
					}
					showingsBlank[j] = count;
				}
			}
			
			//insert不需要返回值
			if (map_element.get("insert")!=null) {
				String[] insert = (String[]) map_element.get("insert");
				for(int i=0;i<insert.length;i++){
					String[] attribute = insert[i].split("\\.");
					String[] subattributes = regex(attribute[1]);
					
					//插入新的一行，一列，新的属性， 这里jsonTable需要迭代计算
					jsonTable = insertAttribute(subattributes, jsonTable);
				}
				//最后把最新的jsontable插入数据库
				RTable rTable = rTableDao.findByIDName(userID, tablename).get(0);
				rTable.setJsontable(jsonTable.toString());
				rTableDao.update(rTable);
				//插入语句和其他语句不通，本质不需要返回值
				return null;
			}
			
			//select需要返回前端值,但返回的还是之前map里的东西
			if (map_element.get("select")!=null){
				String[] select = (String[]) map_element.get("select");
				//这里计算出select的单元格数
				targetsBlank = new Integer[select.length];
				for (int i = 0; i < select.length; i++) {
					targetsBlank[i] = getAttributeNumber(select[i], jsonTable);
				}
				map_result.put("sqlTarget", select);
			}
			if (map_element.get("update")!=null){
				String[] update = (String[]) map_element.get("update");
				map_result.put("update", update);
			}
			
			//using也需要返回前端值
			if (map_element.get("using")!=null){
				//用于返回的数组，是一个二维数组， [ [top-k],[top-k],  ]
				//每一维针对一个select的内容，形成的top-k，按顺序排列
				ArrayList<ArrayList<String>> results = new ArrayList<>();
				
				Map<String, ArrayList<ArrayList<String>>> using = (Map<String, ArrayList<ArrayList<String>>>)
						map_element.get("using");
				//需要遍历map
				Set<String> sets = using.keySet();
				for(String algorithm : sets){
				
					//获取算法作用的属性
					ArrayList<ArrayList<String>> elements = using.get(algorithm);
					//第一个for循环是算法依次作用的属性 on table.headers,table.rows ,互相之间无关系
					for(int i=0;i<elements.size();i++){
						//这里属性要作为一个数组形式传到算法中,大小已知
						String[] attributes = new String[elements.get(i).size()];
						
						//第二个for循环是 on (table.name,table.rows) 可能存在带括号的数组，
						//不带括号的数组中都只有1个,里面的属性需要作为一个整体计算
						for (int j = 0; j < elements.get(i).size(); j++) {
							//先要获取属性
							String element = elements.get(i).get(j);
							String findresult;
							//如果这个属性是tablelist开头的则结果是 表示路径的字符串
							if (element.startsWith("tablelist[")) {
								String tablelistName = element.substring(10, element.length()-1);
								if (!rTableListDao.findByIDName(userID, tablelistName).isEmpty()) {
									findresult = "WEB-INF/tablelists/"+tablelistName;
								}else{
									findresult = null;
								}
							}else{
								//获取属性中的内容 table01.rows[][]
								String[] attribute = element.split("\\.");
								//通过id号对应表格，这里table直接定义为table01，所以这里attribute[0]没啥用
								//输出的是rows[][]
								//System.out.println(attribute[1]);
								//这里获取三要素 attribute[3][4],里面有可能为""，注意不是null
								String[] subattributes = regex(attribute[1]);
								//findAttribute()返回的即是算法作用属性的具体内容，以字符串形式返回，但其可能是数组
								findresult = findAttribute(subattributes, jsonTable);
							}
							//将找到的具体内容放入数组中
							attributes[j]=findresult;
						}
						//这里需要利用java反射机制，根据算法名字调用不同算法
						//先要区分是否是外部算法,执行时无区别，但是外部算法需要存储在数据库中，以方便删除
						
						//先要判断是否有top-k提示，才能分离出数字
						String algorithm_name_top = algorithm.split(":")[1];
						String algorithm_name = null;
						String algorithm_top = null;
						if (algorithm_name_top.contains(";")) {
							algorithm_name = algorithm_name_top.split(";")[0];
							algorithm_top = algorithm_name_top.split(";")[1];	
						}else{
							algorithm_name = algorithm_name_top;
							algorithm_top = "0";
						}
						
						//判断数据库中，该用户是否存在这个算法
						if (!rAlgorithmDao.findByIDAlgorithm(userID, algorithm_name).isEmpty()) {
							AlgorithmIn algorithmIn = new AlgorithmIn();
							ArrayList<String> items = algorithmIn.find(algorithm_name, attributes, Integer.valueOf(algorithm_top) );
							//这里还需要对items处理一下，提取前端需要的数据，数据是一个数组。处理交给前端吧
							results.add(items);
						}else {
							results.add(null);
						}
					}
				}
				map_result.put("showing_contents", results);
				
				//计算使用算法的比例
				top_kPerc = results.size() / (double)targetsBlank.length;
			}
			return JSONObject.fromObject(map_result).toString();
		}

	@Override
	public boolean uploadAlgorithm(String userID, String algorithmname) {
		// TODO Auto-generated method stub
		//先要判断是否存在这个算法
		File file = new File("WEB-INF/lib/"+algorithmname);
		
		if (file.exists()) {
			rAlgorithmDao.save(new RAlgorithm(Integer.valueOf(userID), algorithmname));	
			return true;
		}
		return false;			
	}

	@Override
	public boolean returnTable(JSONObject jsonTable, String tableID) {
		// TODO Auto-generated method stub
		String path = "WEB-INF/uploadTables/"+tableID;
		String csvWriteFile = path;
	     CsvWriter writer = new CsvWriter(csvWriteFile, ',', Charset.forName("utf-8"));  
	     
	     ArrayList<String[]> writelist = new ArrayList<>();
	     
	     //先放入表头
	     int length = ((JSONArray)jsonTable.get("headers")).size();
	     String[] headers = new String[length];
	     ((JSONArray)jsonTable.get("headers")).toArray(headers);
	     
	     writelist.add(headers);
	     
	     //再放入row值
	     int length_row = ((JSONArray)jsonTable.get("data")).size();
	     for(int i=0;i<length_row;i++){
	    	 int length_column = ((JSONArray)((JSONArray)jsonTable.get("data")).get(i)).size();
	    	 String[] rows = new String[length_column];
	    	 ((JSONArray)((JSONArray)jsonTable.get("data")).get(i)).toArray(rows);
	    	 writelist.add(rows);
	     }
	     
	     for(int row=0;row<writelist.size();row++){
	       	try {
				writer.writeRecord(writelist.get(row));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 writer.close();

	     return true;
	}

	
	@Override
	public Integer getAttributeNumber(String attribute, JSONObject jsonTable) {
		// TODO Auto-generated method stub
		String[] subattributes = regex(attribute);
		String attribute_name = subattributes[0];

		if (!subattributes[1].equals("")) {
			if (!subattributes[2].equals("")) {
				//这里value肯定是单纯字符串
				return 1;
			}else {
				if (attribute_name.equals("rows")) {
					//这里应该是一个数组
					return jsonTable.getJSONArray("data").getJSONArray(0).size();
				}else if (attribute_name.equals("columns")) {
					//这里二维数组长度是columns[2]的长度
					return jsonTable.getJSONArray("data").size();
				}
			}
		}else {
			return 1;
		}
		return 0;
	}

	@Override
	public Integer[] getTargetsBlank() {
		// TODO Auto-generated method stub
		return targetsBlank;
	}

	@Override
	public Integer[] getShowingsBlank() {
		// TODO Auto-generated method stub
		return showingsBlank;
	}

	@Override
	public Double getTop_kPerc() {
		// TODO Auto-generated method stub
		return top_kPerc;
	}
	
}
