package services.Interface;

import net.sf.json.JSONObject;

public interface ParserCrowdIQLService {

	//输入参数为 ReadTableService获取JSONTable
	//输出参数为解析出来的各个部分元素，以map存储，将map转换成jsonObject之后返回
	//这个类主要用于解析语句，生成UI模板，模板确定之后，建立task任务
	String parser(String sql, String userID, String tablename, String path);
	
	//雇主上传外部算法,这里需要检查一下，是否按照要求上传，是否成功。并将用户id，算法id写入数据库
	boolean uploadAlgorithm(String userID, String algorithmname, String path);
	
	//这个函数用于在TaskProcessService中addAnswerTask函数中调用，将task中数据取出，写入json表,第三个参数是JSON表
	JSONObject fillContent(String subattributes,String value, JSONObject jsonTable);

	//这个函数用于将JSON转换到对应的二维表
	boolean returnTable(JSONObject jsonTable, String tableID, String path);
	
	//函数作用是找到属性对应的空格数，如 columns[2] 返回第三列总的单元格数
	//主要是为了select 目标服务的, showing目标需要根据结果来判断
	Integer getAttributeNumber(String attribute, JSONObject jsonTable);
	
	//函数用于返回 难度系数需要参考的数据,每次解析完一个crowdiql语句时，自动产生这些数据，方便给taskService中计算难度系数提供数据
//	select目标，每一个目标的总的空格数（单一字符串headers[3]，一维columns[2], 二维rows），这里详细计算出个数
//	Integer[] targets,每一个值就是表示目标的空格数
//
//	showing目标： 同select方式计算
//	Integer[] showings, 同上
//
//	top-k: 针对几个目标使用了top-k， using Algorithm(cc) on table.columns[2],table.columns[3] 表示针对两个目标使用
//	使用top-k的百分比： 4个target，2个用了top-k，则为0.5
	
	//这三个操作建立的前提是 解析crowdiql语句和确定任务难度系数是前后接着的，实际上也是如此。除非crowdiql解析两次，而任务难度系数没有计算
	Integer[] getTargetsBlank();
	Integer[] getShowingsBlank();
	Double getTop_kPerc();
	
}
