package Quality_Inspection;

import java.util.ArrayList;

import SQL_Process.ReadTable;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//这里检测可以利用jsontable_show来检测，因为数据不需要改变，所以可用show。其余的地方操作由于会有改动，所以要用data

public class Inspection {

	public ReadTable readtable;
	//输入应该是二维数组，即一张表
	public boolean hasHeader(){
		return true;
	}
		
	//检查每一列数据规范性,输入是json中data，以列输入
	//返回是一个数组，展示的时候转换成这种形式rows[1][2], String: rows[2][1]
	public ArrayList<String> dataformat(){
		return null;
	}
	
	//检查缺失数据，输入是headers，data行输入 检查
	//返回同上，就用rows表示，headers, String: headers[2] , rows[1][2] 
	public ArrayList<String> datamiss(JSONObject jsontable_show){
		ArrayList<String> result = new ArrayList<>();
		JSONArray headers = jsontable_show.getJSONArray("headers");
		for (int i = 0; i < headers.size(); i++) {
			if (headers.getString(i).equals("")) {
				result.add("headers["+i+"]");
			}
		}
		
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
	
	//检查冗余行，方案没确定
	//返回值rows[2]
	public ArrayList<String> redundancyRow(){
		return null;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Inspection inspection = new Inspection();
		inspection.readtable =  new ReadTable();
		inspection.readtable.tranfer("src/test/resources/Winners.csv");
		inspection.readtable.showJSONTable();
		System.out.println((inspection.datamiss(inspection.readtable.jsonTable_show)).toString());
	}

}
