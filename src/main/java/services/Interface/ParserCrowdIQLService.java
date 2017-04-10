package services.Interface;

import net.sf.json.JSONObject;

public interface ParserCrowdIQLService {

	//输入参数为 ReadTableService获取JSONTable
	//输出参数为解析出来的各个部分元素，以map存储，将map转换成jsonObject之后返回
	//这个类主要用于解析语句，生成UI模板，模板确定之后，建立task任务
	String parser(String sql, String userID, String tablename);
	
	//雇主上传外部算法,这里需要检查一下，是否按照要求上传，是否成功。并将用户id，算法id写入数据库
	boolean uploadAlgorithm(String userID, String algorithmname);
	
	//这个函数用于在TaskProcessService中addAnswerTask函数中调用，将task中数据取出，写入json表,第三个参数是JSON表
	JSONObject fillContent(String subattributes,String value, JSONObject jsonTable);

	//这个函数用于将JSON转换到对应的二维表
	boolean returnTable(JSONObject jsonTable, String tableID);
}
