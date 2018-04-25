package zlf0320.impl;

import java.io.IOException;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import zlf0320.method.EntityColumnsFind;

public class entityfind implements AlgorithmInterface {

	@Override
	public ArrayList<String> process(String[] data, int top_k) {
		//data长度肯定为1，且这个字符串肯定是二维数组，且以列为标准单位
		//返回的字符串是个一维数组
		JSONArray jsonArray = JSONArray.fromObject(data[0]);
		
		
		ArrayList<ArrayList<String>> column_data = (ArrayList<ArrayList<String>>) JSONArray.toList(jsonArray);
		ArrayList<String> result = new ArrayList<>();
		try {
			result = new EntityColumnsFind().getSubjectColumnNew(column_data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
