package ryr0231.impl;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import ryr0231.method.SemanticRecover;

public class headerrecover implements AlgorithmInterface {

	@Override
	public ArrayList<String> process(String[] data,int top_k) {
		// TODO Auto-generated method stub
		//这里data长度肯定为1，且里面是一个一维数组
		//返回的字符串也是个一维数组
		JSONArray jsonArray = JSONArray.fromObject(data[0]);
		ArrayList<String> column_data = (ArrayList<String>) JSONArray.toList(jsonArray);
		ArrayList<String> result = new SemanticRecover().GetColumnHeaderNew(column_data, top_k);
		result.remove(0);		
		return result;
	}
}
