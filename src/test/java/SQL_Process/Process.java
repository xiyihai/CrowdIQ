package SQL_Process;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Algorithm_Process.Algorithm;
import Algorithm_Process.AlgorithmIn;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Process {

	private ReadTable readTable;
	private Parser parser;
	private ReturnTable returnTable;
	private AlgorithmIn algorithmIn;
	
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
	private String findAttribute(String[] subattributes){
		
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
					result = ((JSONArray)readTable.jsonTable.get("data")).getJSONArray(first_number).getString(second_number);
				}else if (attribute_name.equals("columns")) {
					result = ((JSONArray)readTable.jsonTable.get("data")).getJSONArray(second_number).getString(first_number);
				}else {
					result = ((JSONArray)readTable.jsonTable.get(attribute_name)).getJSONArray(first_number).getString(second_number);
				}
			}else {
				//需要区分行列对应
				if (attribute_name.equals("rows")) {
					result = ((JSONArray)readTable.jsonTable.get("data")).getString(first_number);					
				}else if (attribute_name.equals("columns")) {
					JSONArray columns = new JSONArray();
					for (int i=0;i<readTable.jsonTable.getJSONArray("data").size();i++){
						columns.add(readTable.jsonTable.getJSONArray("data").getJSONArray(i).getString(first_number));
					}
					result = columns.toString();
				}else {
					result = ((JSONArray)readTable.jsonTable.get(attribute_name)).getString(first_number);		
				}
			}
		}else {
			//需要区分行列对应
			if (attribute_name.equals("rows")) {
				result = readTable.jsonTable.get("data").toString();		
			}else if (attribute_name.equals("columns")) {
				int row_length = readTable.jsonTable.getJSONArray("data").size();
				int column_length = readTable.jsonTable.getJSONArray("data").getJSONArray(0).size();
				String[][] columns = new String[column_length][row_length];
				
				//引入json方便输出二维数组
				JSONObject jsonObject = new JSONObject();
				for (int i=0;i<row_length;i++){
					for(int j=0;j<column_length;j++){
						columns[j][i] = readTable.jsonTable.getJSONArray("data").getJSONArray(i).getString(j);
					}
					jsonObject.put("columns", columns);
					result = jsonObject.get("columns").toString();
					}
			}else {
				result = readTable.jsonTable.get(attribute_name).toString();			
			}
		}
		return result;
	}
	
	//插入对应的内容，用于insert rows[2] columns[3](对应表头个数也要变化) 新增2行 3列
	private boolean insertAttribute(String[] subattributes) {
		String attribute_name = subattributes[0];
		int first_number;
		
		if (!subattributes[1].equals("")) {
			first_number = Integer.valueOf(subattributes[1]);
			
			int row_length = readTable.jsonTable.getJSONArray("data").size();
			int column_length = readTable.jsonTable.getJSONArray("data").getJSONArray(0).size();
			
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
					readTable.jsonTable.getJSONArray("data").add(new_attribute);		
				}
			}else if (attribute_name.equals("columns")) {
				for (int i = 0; i < first_number ; i++) {
					for (int j = 0; j < row_length; j++) {
						readTable.jsonTable.getJSONArray("data").getJSONArray(j).add("");
					}
					readTable.jsonTable.getJSONArray("headers").add("");
				}
			}else {
				System.out.println("这个属性不能增加[]");
			}
			return true;
		}else {
			//这个直接放一个属性名即可
			readTable.jsonTable.put(attribute_name, "");
			return true;
		}
	}

	//根据众包用户获取值之后，填充select,update的值
	private boolean fillContent(String[] subattributes,String value){

		String attribute_name = subattributes[0];
		int first_number;
		int second_number;
		if (!subattributes[1].equals("")) {
			first_number = Integer.valueOf(subattributes[1]);
			if (!subattributes[2].equals("")) {
				second_number = Integer.valueOf(subattributes[2]);
				if (attribute_name.equals("rows")) {
					//这种表达方式太累赘
					((JSONArray)((JSONArray)readTable.jsonTable.get("data")).get(first_number)).remove(second_number);
					((JSONArray)((JSONArray)readTable.jsonTable.get("data")).get(first_number)).add(second_number,value);
				}else if (attribute_name.equals("columns")) {
					((JSONArray)((JSONArray)readTable.jsonTable.get("data")).get(second_number)).remove(first_number);
					((JSONArray)((JSONArray)readTable.jsonTable.get("data")).get(second_number)).add(first_number,value);
				}else {
					((JSONArray)((JSONArray)readTable.jsonTable.get(attribute_name)).get(first_number)).remove(second_number);
					((JSONArray)((JSONArray)readTable.jsonTable.get(attribute_name)).get(first_number)).add(second_number,value);
				}
			}else {
				if (attribute_name.equals("rows")) {
					//这里value应该是一个数组，这里假设都是同一个value,需要伪造
					JSONArray jsonArray = new JSONArray();
					int length = ((JSONArray)readTable.jsonTable.get("data")).getJSONArray(first_number).size();
					for (int i = 0; i < length; i++) {
						jsonArray.add(value);						
					}
					
					((JSONArray)readTable.jsonTable.get("data")).remove(first_number);
					((JSONArray)readTable.jsonTable.get("data")).add(first_number, jsonArray);
				}else if (attribute_name.equals("columns")) {
					//需要把第2列数据全部替换掉，替换的数一定是个数组,这里都是同一个value
					int row_length = readTable.jsonTable.getJSONArray("data").size();
					int column_length = readTable.jsonTable.getJSONArray("data").getJSONArray(0).size();
					for (int i = 0; i < row_length; i++) {
						readTable.jsonTable.getJSONArray("data").getJSONArray(i).remove(first_number);
						readTable.jsonTable.getJSONArray("data").getJSONArray(i).add(first_number, value);
					}
				}else {
					((JSONArray)readTable.jsonTable.get(attribute_name)).remove(first_number);
					((JSONArray)readTable.jsonTable.get(attribute_name)).add(first_number, value);	
				}
			}
		}else {
			readTable.jsonTable.put(attribute_name, value);
		}
		return true;	
	}
	
	public void process(String sql,String path) throws IOException{
		
		//获取jsontable，在readtable.jsontable中
		readTable = new ReadTable();
		readTable.tranfer(path);
	
		//得到每个元素，且操作对应的table
		parser=new Parser();
		Map<String, Object> map_element = parser.getElements(sql);
		
		if (map_element.get("showing")!=null) {
			String[] showing = (String[]) map_element.get("showing");
			ArrayList<String> results = new ArrayList<>();
			for(int i=0;i<showing.length;i++){
				String[] attribute = showing[i].split("\\.");
				//通过id号对应表格，这里table直接定义为readTable，所以这里element[0]没啥用
				//这里获取三要素 attribute[3][4],里面有可能为""，注意不是null
				String[] subattributes = regex(attribute[1]);
				String result = findAttribute(subattributes);
				//这里需要加入标志符：
				String ahead = subattributes[0];
				for (int j = 1; j < subattributes.length; j++) {
					if (!subattributes[j].equals("")) {
						ahead = ahead + "-" + subattributes[j];	
					}
				}
				result = ahead+":"+result;
				results.add(result);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("showing", JSONArray.fromObject(results));
			
			System.out.println(jsonObject);
			
		}
		if (map_element.get("insert")!=null) {
			String[] insert = (String[]) map_element.get("insert");
			for(int i=0;i<insert.length;i++){
				String[] attribute = insert[i].split("\\.");
				String[] subattributes = regex(attribute[1]);
				
				//插入新的一行，一列，新的属性
				if (insertAttribute(subattributes)) {
					System.out.println("插入成功！");		
				}else {
					System.out.println("插入失败！");
				}
			}
			System.out.println(readTable.jsonTable.toString());
		}
		if (map_element.get("select")!=null){
			String[] select = (String[]) map_element.get("select");
			for(int i=0;i<select.length;i++){
				String[] attribute = select[i].split("\\.");
				String[] subattributes = regex(attribute[1]);
				
				//对于select的内容，首先需要定位，然后根据获取的答案填充到定位的地方；
				//假设众包已获取值，这里需要填充相应的数据
				String value = "select的值";
				if (fillContent(subattributes,value)) {
					System.out.println("select成功！");
				}else {
					System.out.println("select失败！");
				}
				System.out.println(readTable.jsonTable.toString());
			}
		}
		if (map_element.get("update")!=null){
			String[] select = (String[]) map_element.get("update");
			for(int i=0;i<select.length;i++){
				String[] attribute = select[i].split("\\.");
				String[] subattributes = regex(attribute[1]);
				
				//对于update的内容，首先需要定位，然后根据获取的答案填充到定位的地方；
				//假设众包已获取值，这里需要填充相应的数据，这里的类型有对应的数组，或者字符串，这里先假定为字符串
				String value = "update的值";
				if (fillContent(subattributes,value)) {
					System.out.println("update成功！");
				}else {
					System.out.println("update失败！");
				}
				System.out.println(readTable.jsonTable.toString());
			}
		}
		if (map_element.get("using")!=null){
			Map<String, String[]> using = (Map<String, String[]>) map_element.get("using");
			//需要遍历map
			Set<String> sets = using.keySet();
			for(String algorithm : sets){
				//输出算法名字
				System.out.println(algorithm);
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
					System.out.println(findAttribute(subattributes));
					
					//这里需要利用java反射机制，根据算法名字调用不同算法
					//先要区分是否是外部算法,执行时无区别，但是外部算法需要存储在数据库中，以方便删除
					String algorithm_name = algorithm.split(":")[1];
					algorithmIn = new AlgorithmIn();
					ArrayList<String> items = algorithmIn.find(algorithm_name, findAttribute(subattributes));
					System.out.println(items.toString());
				}	
			}
		}
		
		//获取转换对象
		returnTable =  new ReturnTable();
		returnTable.retranfer(readTable.jsonTable, path);		
	}
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Process process = new Process();
		process.process( " showing table01.headers ","g:/Winners.csv");
	}

}
