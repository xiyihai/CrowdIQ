package services.Interface;

import java.util.ArrayList;

import net.sf.json.JSONObject;

public interface ReadTableService {

	//将原始数据其转换为jsontable
	boolean tranferJSONTable();
	
	//存放读取的表原始数据
	boolean readTable();
	
	//获取jsontable_show的形式
	JSONObject getJSONTable_show();
}
