package services.Impl;

import java.util.ArrayList;
import java.util.HashMap;

import csy4367.impl.HeaderDetecter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import services.Interface.InspectionService;

public class InspectionServiceImpl implements InspectionService {

	private boolean flag_header;
	
	@Override
	public String inspect(JSONObject jsontable_show) {
		
		HashMap<String, ArrayList<String>> problems = new HashMap<>();
		problems.put("dataformat", dataformat(jsontable_show));
		problems.put("datamiss", datamiss(jsontable_show));
		problems.put("redundancyRow", redundancyRow(jsontable_show));
		//可能是[headers], 或者[headers[2],headers[4]]
		problems.put("headersmiss", headermiss(jsontable_show));
		return JSONObject.fromObject(problems).toString();
	}

	@Override
	public boolean hasHeader(ArrayList<String[]> readTable) {
		// TODO Auto-generated method stub
		//这里借助readTable的ArrayList<String[]> 来判断是否存在表头
		
		HeaderDetecter headerDetecter = new HeaderDetecter();
		flag_header = headerDetecter.hasHeader(readTable, "E:\\Probase\\CrowdIQ\\ConceptAndAttribute.txt");
		return flag_header;
	}

	//检查每一列数据规范性,输入是json中data，以列输入
	//返回是一个数组，展示的时候转换成这种形式rows[1][2], String: rows[2][1]
	private ArrayList<String> dataformat(JSONObject jsontable_show){
		ArrayList<String> result = new ArrayList<>();
		return result;
	}
		
	//检查缺失数据，输入是data行输入 检查
	//返回同上，就用rows表示， String: rows[2][2] , rows[1][2] 
	private ArrayList<String> datamiss(JSONObject jsontable_show){
		ArrayList<String> result = new ArrayList<>();
		
		JSONArray rows = jsontable_show.getJSONArray("rows");
		for (int i = 0; i < rows.size(); i++) {
			JSONArray rows1 = rows.getJSONArray(i);
			for (int j = 0; j < rows1.size(); j++) {
				if (rows1.getString(j).equals("")) {
					result.add("rows["+i+"]"+"["+j+"]");
				}
			}
		}
		return result;
	}
		
	private ArrayList<String> headermiss(JSONObject jsontable_show){
		ArrayList<String> result = new ArrayList<>();
		
		//只有存在表头时才要检测表头中的空缺数据
		if (jsontable_show.has("headers")) {
			JSONArray headers = jsontable_show.getJSONArray("headers");
			for (int i = 0; i < headers.size(); i++) {
				if (headers.getString(i).equals("")) {
					result.add("headers["+i+"]");
				}
			}	
		}else {
			result.add("headers");
		}
		return result;
	}
	
	//检查冗余行，方案没确定
	//返回值rows[2]
	private ArrayList<String> redundancyRow(JSONObject jsontable_show){
		ArrayList<String> result = new ArrayList<>();
		return result;
	}
}
