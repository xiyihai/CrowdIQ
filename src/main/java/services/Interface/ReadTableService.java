package services.Interface;

import java.util.ArrayList;

import net.sf.json.JSONObject;

public interface ReadTableService {

	//将原始数据其转换为jsontable
	boolean tranferJSONTable();
	
	//读取上传的的表原始数据,对上传上的表需要给出tableID，然后写入数据库
	boolean readUploadTable(String userID);
	
	//将用户ID，表格ID存入数据库,下次登录时读取
	boolean readDBTable(String userID, String tableID);
	
	//获取jsontable_show的形式
	JSONObject getJSONTable_show();
	
	//返回jsontable数据
	JSONObject getJsonTable();
	
	//获取二维表原始数据
	ArrayList<String[]> getReadList();

	//将用户ID，表格ID写入数据库
	boolean insertDB(String userID, String tableID);
	
	//根据雇主ID号，展示其所有上传过的表ID
	ArrayList<String> showAllTable(String userID);
	
	//雇主下载完成的表
	boolean downloadTable(String tableID, String userID);
}
