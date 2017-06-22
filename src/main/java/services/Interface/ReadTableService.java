package services.Interface;

import java.util.ArrayList;

import com.sun.xml.internal.ws.developer.StreamingAttachment;

import net.sf.json.JSONObject;

public interface ReadTableService {

	//将原始数据其转换为jsontable
	boolean tranferJSONTable(String tablename);
	
	//读取上传的的表原始数据,对上传上的表需要给出tableID，然后写入数据库
	boolean readUploadTable(String userID, String tablename, String path);
	
	//读取对应的表格，根据tbale_name,返回给前端jsontable数据
	String readDBTable(String userID, String tablename);
	
	//获取jsontable_show的形式
	JSONObject getJSONTable_show();
	
	//返回jsontable数据
	JSONObject getJsonTable();
	
	//获取二维表原始数据
	ArrayList<String[]> getReadList();
	
	//根据雇主ID号，展示其所有上传过的表名字
	String showAllTable(String userID);
	
	//雇主下载完成的表，这里需要把对应任务中finalanswer写入到最终的表格中
	boolean downloadTable(String tableID, String userID, String path);
	
	//雇主上传tablelist表集合，需要写入数据库
	boolean uploadTableList(String userID, String tablelist, String path);
	
	boolean deleteTable(String userID, String tablename, String path);
}
