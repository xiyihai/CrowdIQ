package services.Interface;

import java.util.ArrayList;

import net.sf.json.JSONObject;

public interface InspectionService {

	//检查表中存在的问题，返回值为JSONObject.toString()
	//missing headers: [headers[2],headers[4]],data format: [rows[2][3]]
	String inspect(JSONObject readtable_show);
	
	//检查是否存在表头，供给表转换时使用，判断的是单纯地ArrayList数据，不是json数据
	boolean hasHeader(ArrayList<String[]> readTable);
}
