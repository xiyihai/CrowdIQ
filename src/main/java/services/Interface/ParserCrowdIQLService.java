package services.Interface;

import net.sf.json.JSONObject;

public interface ParserCrowdIQLService {

	//输入参数为 ReadTableService获取JSONTable
	//输出参数为解析出来的各个部分元素，以map存储，将map转换成jsonObject之后返回
	//这个类主要用于解析语句，生成UI模板，模板确定之后，建立task任务
	String parser(String sql, JSONObject jsonTable);
	
	//雇主上传外部算法,这里需要检查一下，是否按照要求上传，是否成功。并将用户id，算法id写入数据库
	boolean uploadAlgorithm();
}
