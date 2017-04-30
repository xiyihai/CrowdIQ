package Cluster;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

public class ClusterImpl {

	//输出肯定是一个二维数组
	//返回值也是个二维数组
	public static String process(String target, double scale){
		JSONArray rows = JSONArray.fromObject(target);
		int size = (int)(scale * rows.size());
		String[][] data = new String[rows.size()][rows.getJSONArray(0).size()];
		
		for (int i = 0; i < rows.size(); i++) {
			JSONArray row = rows.getJSONArray(i);
			for (int j = 0; j < row.size(); j++) {
				data[i][j] = row.getString(j);
			}
		}
		
		ArrayList<ArrayList<String>> cluster_result = new ArrayList<>();
		List<Point> results = ClusterEntry.clusterBegin(data, size);
		for (int i = 0; i < results.size(); i++) {
			ArrayList<String> result = results.get(i).getData();
			cluster_result.add(result);
		}
		return JSONArray.fromObject(cluster_result).toString();
	}
}
