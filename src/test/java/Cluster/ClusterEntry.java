package Cluster;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import SQL_Process.ReadTable;
import net.sf.json.JSONArray;


public class ClusterEntry {
	
	public static List<Point> clusterBegin(String[][] data, int size){
		List<Point> centerList = new ArrayList<Point>();
		data = goodData(data);
		ClusterUtil.beginCluster(data, size);
		Map<Point, ArrayList<Point>> cluster = Cluster.clusterResult;
		Iterator iter = cluster.entrySet().iterator();
		int count = 0;
		while (iter.hasNext()) {// 只遍历一次,速度快
			Map.Entry e = (Map.Entry) iter.next();
			Point point = (Point) e.getKey();
			centerList.add((Point)e.getKey());
			ArrayList<String> center = point.getData();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("ID", String.valueOf(count + 1));
			for (int i = 0; i < center.size(); i++) {
				String temp = center.get(i);
//				System.out.println(temp);
				map.put(String.valueOf(i + 1), temp);
			}
		}
		return centerList;
	}
	
	public static void main(String args[]) throws FileNotFoundException, IOException{
		
		ReadTable table = new ReadTable();
		table.tranfer("src/test/resources/Winners.csv");
		ArrayList<String[]> odata = table.getReadList();
		JSONArray results = JSONArray.fromObject(odata);
		System.out.println(results.toString());
			
		//以 " 出现的个数即可判断，个数/2就是 showing内容单元格数
			String content = results.toString();
			int count = 0;
			Pattern pattern = Pattern.compile("\"");
			Matcher matcher = pattern.matcher(content);
			while (matcher.find()) {
				count++;
			}
			System.out.println(count/2);
		}
	
	
	// 对源数据进行预处理，去除空白列和空白行
		public static String[][] goodData(String[][] data) {
			data = deleteEmptyRow(data);
			data = deleteEmptyColumn(data);
			int row_length = usefulRow(data);
			int column_length = usefulColumn(data);
			String a[][] = new String[row_length][column_length];
			for (int i = 0; i < row_length; i++) {
				for (int j = 0; j < column_length; j++) {
					a[i][j] = data[i][j];
				}
			}
			return a;
		}

		// 获得有效列宽
		public static int usefulColumn(String a[][]) {
			int i = 0;
			for (; i < a[0].length; i++) {
				if (a[0][i].equalsIgnoreCase("")) {
					break;
				}
			}
			return i;
		}

		// 获得有效行宽
		public static int usefulRow(String a[][]) {
			int length = 0;
			for (int i = 0; i < a.length; i++) {
				int temp = 0;
				for (int j = 0; j < a[0].length; j++) {
					if (!a[i][j].equalsIgnoreCase("")) {
						break;
					}
					temp++;
				}
				if (!(temp == a[0].length)) {
					length++;
				}
			}
			return length;
		}

		// 后移空白行
		public static String[][] deleteEmptyRow(String a[][]) {
			for (int i = 0; i < a.length - 1; i++) {
				int temp = 0;
				for (int j = 0; j < a[0].length; j++) {
					if (a[i][j].equalsIgnoreCase("")) {
						temp++;
					}
				}
				if (temp == a[0].length) {
					String arr[] = new String[a[0].length];
					for (int k = 0; k < a[0].length; k++) {
						arr[k] = a[i][k];
					}
					for (int c = 0; c < a[0].length; c++) {
						a[i][c] = a[i + 1][c];
					}
					for (int d = 0; d < a[0].length; d++) {
						a[i + 1][d] = arr[d];
					}
				}
			}
			return a;
		}

		// 后移空白列
		public static String[][] deleteEmptyColumn(String a[][]) {
			for (int i = 0; i < a[0].length - 1; i++) {
				if (a[0][i].equalsIgnoreCase("")) {
					String temp = a[0][i];
					a[0][i] = a[0][i + 1];
					a[0][i + 1] = temp;
				}
			}
			return a;
		}

}
