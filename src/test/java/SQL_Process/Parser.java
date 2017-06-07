package SQL_Process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// 基本思路： 预处理，分离语句块

public class Parser {

	// 预处理：
	//1）消除SQL语句前后的空白，将其中的连续空白字符（包括空格，TAB和回车换行）替换成单个空格
	//2）将sql语句全变成小写形式（或大写形式）
	//3）在SQL语句的尾后加上结束符号“END”
	public String preprocess(String sql){
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
	public Map<String, Object> getElements (String sql){
		
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
			//对算法操作
			HashMap<String, String[]> algorithm_map = new HashMap<>();
			String[] algor = using.split(" and ");
			for(int i=0;i<algor.length;i++){
				String algorithm;
				if (algor[i].startsWith("outer algorithm")) {
					algorithm = "outer-algorithm:"+segment(regex("algorithm"), algor[i]);	
				}else {
					algorithm = "inter-algorithm:"+segment(regex("algorithm"), algor[i]);			
				}
				algorithm_map.put(algorithm, segment(regex("on"), algor[i]+" END").split(","));
			}
			map.put("using", algorithm_map);
		}else {
			map.put("using", null);
		}		
		return map;
	}
	
	//输出测试用
	public void test(String sql){
		Map<String, Object> map = getElements(sql);
		Iterator<Map.Entry<String, Object>> iter = map.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry<String, Object> entry= iter.next();
			System.out.print(entry.getKey()+":::");
			if (entry.getValue() instanceof String[]) {
				String[] items = (String[])entry.getValue();
				for(int i=0;i<items.length;i++){
					System.out.print(items[i]+" ");
				}
				System.out.println("");
			}else if (entry.getValue() instanceof Map) {
				Map<String,String[]> algorithm_map = (Map<String, String[]>) entry.getValue();
				Iterator<Map.Entry<String, String[]>> iter_algor = algorithm_map.entrySet().iterator();
				while(iter_algor.hasNext()){
					Map.Entry<String, String[]> entry_algor= iter_algor.next();
					System.out.print(entry_algor.getKey()+"=>");
					String[] itemss = entry_algor.getValue();
					for(int i=0;i<itemss.length;i++){
						System.out.print(itemss[i]+" ");
					}
					System.out.println("");
				}
			}else {
				System.out.println(entry.getValue());
			}
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stubJ
		Parser parser = new Parser();
		String initsql = "   select table.header[2],table.entity_column showing table.tablename,table.headers,table.rows[0],table.rows[2] using Algorithm(zlf.impl.entity_find) on table.rows";
			
		parser.test(initsql);
	}

}
