package services.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import FunctionsSupport.AlgorithmIn;
import FunctionsSupport.Parser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import services.Interface.ParserCrowdIQLService;

public class ParserCrowdIQLServiceImpl implements ParserCrowdIQLService {
	
	private Parser parser;

	@Override
	public String parser(String sql, JSONObject jsonTable) {
		// TODO Auto-generated method stub
		//返回的值是一个Map转换而成的JSONObject
		return process(sql, jsonTable);
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
	private boolean insertAttribute(String[] subattributes, JSONObject jsonTable) {
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
				return true;
			}else {
				//这个直接放一个属性名即可
				jsonTable.put(attribute_name, "");
				return true;
			}
		}

	//根据众包用户获取值之后，填充select,update的值.
	//value 具体形态需要更具subattribute来定，可能是一维，或者字符串。理论上不可能是二维数组，没有这样的任务
	private boolean fillContent(String[] subattributes,String value, JSONObject jsonTable){

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
		return true;	
	}
	
	private String process(String sql, JSONObject jsonTable){
			
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
				
				for(int i=0;i<showing.length;i++){
					String[] attribute = showing[i].split("\\.");
					//通过id号对应表格，这里table直接定义为readTable，所以这里element[0]没啥用
					//这里获取三要素 attribute[3][4],里面有可能为""，注意不是null
					String[] subattributes = regex(attribute[1]);
					//得到showing具体元素值,result可能是一维数组，也可能是二维，ArrayList.fromObject可以直接转换
					String result = findAttribute(subattributes, jsonTable);
					results.add(result);
				}
				
				map_result.put("showing", results);
			}
			
			//insert不需要返回值
			if (map_element.get("insert")!=null) {
				String[] insert = (String[]) map_element.get("insert");
				for(int i=0;i<insert.length;i++){
					String[] attribute = insert[i].split("\\.");
					String[] subattributes = regex(attribute[1]);
					
					//插入新的一行，一列，新的属性
					if (insertAttribute(subattributes, jsonTable)) {
						System.out.println("插入成功！");		
					}else {
						System.out.println("插入失败！");
					}
				}
			}
			
			//select需要返回前端值,但返回的还是之前map里的东西
			if (map_element.get("select")!=null){
				String[] select = (String[]) map_element.get("select");
				map_result.put("select", select);
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
				
				Map<String, String[]> using = (Map<String, String[]>) map_element.get("using");
				//需要遍历map
				Set<String> sets = using.keySet();
				for(String algorithm : sets){
				
					//获取算法作用的属性
					String[] elements = using.get(algorithm);
					for(int i=0;i<elements.length;i++){
						
						//获取属性中的内容 table01.rows[][]
						String[] attribute = elements[i].split("\\.");
						//通过id号对应表格，这里table直接定义为table01，所以这里attribute[0]没啥用
						//输出的是rows[][]
						//System.out.println(attribute[1]);
						//这里获取三要素 attribute[3][4],里面有可能为""，注意不是null
						String[] subattributes = regex(attribute[1]);
						//findAttribute()返回的即是算法作用属性的具体内容，以字符串形式返回，但其可能是数组
						String findresult = findAttribute(subattributes, jsonTable);
						
						//这里需要利用java反射机制，根据算法名字调用不同算法
						//先要区分是否是外部算法,执行时无区别，但是外部算法需要存储在数据库中，以方便删除
						String algorithm_name = algorithm.split(":")[1];
						AlgorithmIn algorithmIn = new AlgorithmIn();
						ArrayList<String> items = algorithmIn.find(algorithm_name, findresult);
						//这里还需要对items处理一下，提取前端需要的数据，数据是一个数组。处理交给前端吧
						results.add(items);
					}
				}
				map_result.put("top_k", results);
			}
			return JSONObject.fromObject(map_result).toString();
		}
	
}
