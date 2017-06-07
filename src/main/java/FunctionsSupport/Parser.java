package FunctionsSupport;

import java.security.AlgorithmParameterGeneratorSpi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Algorithm_Process.Algorithm;

public class Parser {
	// 预处理：
	//1）消除SQL语句前后的空白，将其中的连续空白字符（包括空格，TAB和回车换行）替换成单个空格
	//2）将sql语句全变成小写形式（或大写形式）
	//3）在SQL语句的尾后加上结束符号“END”
	private String preprocess(String sql){
		sql=sql.trim(); //去除首尾两端空格
		sql=sql.toLowerCase();
		sql=sql.replaceAll("\\s+"," "); //把空格，tab，换行，回车都换成一个空格字符，java字符串中都会看成由多个空格字符组成
		sql=sql+" END";
		return sql;
	}
	
	//分离语句块
	//利用正则表达式，() group 来找到对应的子语句
	//每一个子句子都可以分成start body end三部分组成,主要内容在body
	private String segment(String regex,String sql){
		Pattern pattern = Pattern.compile(regex);
		
		String start;
		String body = null;
		String end;
		
		for(int i=0;i<=sql.length();i++){
			String shortsql = sql.substring(0,i);
			Matcher matcher = pattern.matcher(shortsql);
			//使用while的原因: find会从上次成功匹配(部分匹配)结束的地方再次开始。这里没啥用,这里需要的是第一次匹配到就停止返回值
			while (matcher.find()) { 	
				start = matcher.group(1);
				body = matcher.group(2);
				end = matcher.group(3);
				return body;
			}
		}
		return body;
	}
	
	//制定相关正则表达式，用于抽取各个部分的内容
	private String regex(String sign){
		String regex = null;
		
		switch (sign) {
		case "select":
			regex="(select )(.+)( showing | using | END)";
			break;
		case "insert":
			regex="(insert )(.+)( showing | using | END)";
			break;
		case "delete":
			regex="(delete )(.+)( showing | using | END)";	
			break;
		case "update":
			regex="(update )(.+)( showing | using | END)";	
				break;
		case "showing":
			regex="(showing )(.+)( using | END)";
			break;
		case "using":
			regex="(using )(.+)( END)";
			break;
		case "algorithm":
			regex="(algorithm\\()(.+)(\\) on)";
			break;
		case "on":
			regex="( on )(.+)( END)";
			break;
		default:
			break;
		}
		return regex;
	}
		
	//将所有解析成完整的内容，以map形式保存
	public Map<String, Object> getElements(String sql){
		
		sql = preprocess(sql);
		
		Map<String, Object> map = new HashMap<>();
				
		String update = segment(regex("update"), sql);
		if (update!=null) {
			map.put("update", update.split(","));	
		}else {
			map.put("update", null);
		}
		
		String insert = segment(regex("insert"), sql);
		if (insert!=null) {
			map.put("insert", insert.split(","));
		}else {
			map.put("insert", null);
		}
		
		String select = segment(regex("select"), sql);
		if (select!=null) {
			map.put("select", select.split(","));
		}else{
			map.put("select", null);
		}
	
		String  delete = segment(regex("delete"), sql);
		if (delete!=null) {
			map.put("delete", delete.split(","));	
		}else{
			map.put("delete", null);
		}
		
		String showing = segment(regex("showing"),sql);
		if (showing!=null) {
			map.put("showing", showing.split(","));
		}else {
			map.put("showing", null);
		}
		
		//using里面仍是一个map<算法名字String，作用的属性[]>
		String using = segment(regex("using"),sql);
		if (using!=null) {
			//对算法操作,这里采用map存<算法名, 作用的属性>，这里作用的属性是二维数组存
			//二维数组是为了括号准备的即 [[columns,headers],tablename]：(table.columns,table.headers),table.tablename
			HashMap<String, ArrayList<ArrayList<String>>> algorithm_map = new HashMap<>();
			String[] algor = using.split(" and ");
			for(int i=0;i<algor.length;i++){
				String algorithm;
				if (algor[i].startsWith("outer algorithm")) {
					algorithm = "outer-algorithm:"+segment(regex("algorithm"), algor[i]);	
				}else {
					algorithm = "inter-algorithm:"+segment(regex("algorithm"), algor[i]);			
				}
				//用来存放属性
				ArrayList<ArrayList<String>> attributes = new ArrayList<>();
				//先全部按照逗号区分，然后利用括号来判断
				String[] elements = segment(regex("on"), algor[i]+" END").split(",");
				for (int j = 0; j < elements.length; j++) {
					ArrayList<String> attribute = new ArrayList<>();
					if (elements[j].startsWith("(")) {
						String element = elements[j].substring(1, elements[j].length());
						attribute.add(element);
						//如果出现一个 以左括号 开头的，则一直要加到 以右括号结尾为止
						while (!elements[++j].endsWith(")")) {
							attribute.add(elements[j]);
						}
						//最后加上以右括号结尾的元素
						attribute.add(elements[j].substring(0, elements[j].length()-1));
					}else{
						//由于右括号的元素已经不可能独自出现了，所以只可能是单独无括号的元素,直接放入即可
						attribute.add(elements[j]);
					}
					attributes.add(attribute);	
				}
				//这里将attribute打印输出，作为test
//				for (int j = 0; j < attributes.size(); j++) {
//					for (int j2 = 0; j2 < attributes.get(j).size(); j2++) {
//						System.out.print(attributes.get(j).get(j2)+" ");
//					}
//					System.out.println();
//				}
				
				algorithm_map.put(algorithm, attributes);
			}
			map.put("using", algorithm_map);
		}else {
			map.put("using", null);
		}		
		return map;
	}
//	
//	public static void main(String[] main){
//		Parser parser = new Parser();
//		Map<String, Object> elements = parser.getElements("using algorithm(ddd) on (table.columns,table.rows[1],"
//				+ "TableList[xyh.cc]),"
//				+ "table.tablename,table.columns[2][3]");
//		
//	}
	
}
