package Cluster;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;



public class ClusterUtil {
	public static String data[][];
	public static ArrayList<String> core;

	public static void beginCluster(String[][] original, int size) {

		data = new String[original.length + 1][original[0].length];
		for (int i = 0, j = 0; i < data.length; i++) {
			if (i == 0) {
				for (int k = 0; k < data[0].length; k++) {
					data[i][k] = String.valueOf(k + 1);
				}
			} else {
				for (int k = 0; k < data[0].length; k++) {
					data[i][k] = original[j][k];
				}
				j++;
			}
		}
		data = original;
		ClusterUtil test = new ClusterUtil();
		Cluster cluster = new Cluster();
		int length = 0;
		for (int i = 1; i < data[0].length; i++) {
			if (!data[0][i].equals("")) {
				length++;
			}
		}
		String title[] = new String[length];
		for (int i = 1; i < data[0].length; i++) {
			if (!data[0][i].equals("")) {
				title[i - 1] = data[0][i];
			}
		}
		ArrayList<ArrayList<ArrayList<String>>> disMatrx = test.getDisMatrix();
//		System.out.println("Discernibility Matrix");
//		System.out.print("\t");
		/*for (int i = 1; i < data.length - 1; i++) {
			// System.out.print(data[i][0]+"\t");
		}*/
		System.out.println("");
/*		
		for (int j = 2; j < data.length; j++) {
			// System.out.print(data[j][0]+"\t");
			ArrayList<ArrayList<String>> row = disMatrx.get(j - 2);
			for (int k = 0; k < row.size(); k++) {
				ArrayList<String> single = row.get(k);
				for (int m = 0; m < single.size(); m++) {
					// System.out.print(single.get(m)+",");
				}
				// System.out.print("\t");
			}
			// System.out.println("");
			
		}
*/
		// System.out.println("----------------------------------------------------------");
		test.reductMatric(disMatrx);
		WordsCount count = new WordsCount();
		int cluster_size = size;
		if (core != null && core.size() > 0) {
/*			count.wordCount(data, core, cluster_size);
			if (WordsCount.topWords.size() >= cluster_size) {
				cluster.cluster_size = cluster_size;
				test.getClusterData();
				cluster.startCluster();
				System.out.println("Keywords clustering");
			} else {
				Kmeans kmeans = new Kmeans();
				kmeans.cluster_size = cluster_size;
				kmeans.getKeansData();
				kmeans.startCluster();
				System.out.println("Kmeans clustering");
			}*/
			Kmeans kmeans = new Kmeans();
			kmeans.cluster_size = cluster_size;
			kmeans.getKeansData();
			kmeans.startCluster();
//			System.out.println("Kmeans clustering");
		} 
		else {
			Kmeans kmeans = new Kmeans();
			kmeans.cluster_size = cluster_size;
			kmeans.getKeansData();
			kmeans.startCluster();
//			System.out.println("Kmeans clustering");
		}
	}

	// 得到聚类中心和待聚类点
	public void getClusterData() {
		Map<String, Integer> wordCount = new HashMap<String, Integer>();
		ArrayList<String> topWords = WordsCount.topWords;
		ClusterUtil test = new ClusterUtil();
		for (int i = 1; i < data.length; i++) {
			String content = test.unionContent(data, i);
			int sum = 0;
			int number[] = new int[topWords.size()];
			for (int j = 0; j < topWords.size(); j++) {
				String target = topWords.get(j);
				int temp = test.stringFind(content, target);
				number[j] = temp;
			}
			sum = test.arraySum(number);
			if (sum > 0) {
				wordCount.put(String.valueOf(i), sum);
			}
		}
		wordCount = MapSort.sortMapByValue(wordCount);
		int top_k = Cluster.cluster_size;
		Cluster.clusterCenter = new Point[top_k];
		Cluster.clusterPoint = new Point[data.length - top_k - 1];
		int exist[] = new int[top_k];
		Iterator iterator = wordCount.entrySet().iterator();
		int number = 0;
		while ((number < top_k) && iterator.hasNext()) {
			Map.Entry entry = (Entry) iterator.next();
			String key = (String) entry.getKey();
			int row = Integer.valueOf(key);
			exist[number] = row;
			ArrayList<String> temp_list = new ArrayList<String>();
			for (int i = 0; i < data[0].length; i++) {
				temp_list.add(data[row][i]);
			}
			Point point = new Point();
			point.setData(temp_list);
			Cluster.clusterCenter[number] = point;
			number++;
		}
		for(int i=0;i<top_k;i++)
		{
			if(exist[i] == 0)
			{
				for(int j=1;j<data.length;j++)
				{
					if(!test.elemExist(exist, j))
					{
						exist[i] = j;
						for(int m=0;m<top_k;m++)
						{
							if(Cluster.clusterCenter[m] == null)
							{
								
								ArrayList<String> point_list = new ArrayList<String>();
								for (int n = 0; n < data[0].length; n++) {
									point_list.add(data[j][n]);
								}
								Point temp_point = new Point();
								temp_point.setData(point_list);
								Cluster.clusterCenter[m] = temp_point;
							}
						}
						break;
					}
				}
			}
		}
		for (int i = 1, j = 0; i < data.length; i++) {
			if (!test.elemExist(exist, i)) {
				ArrayList<String> temp_list = new ArrayList<String>();
				for (int k = 0; k < data[0].length; k++) {
					temp_list.add(data[i][k]);
				}
				Point point = new Point();
				point.setData(temp_list);
				Cluster.clusterPoint[j] = point;
				j++;
			}
		}
	}

	// 判断某个元素在某个数组中是否存在
	public boolean elemExist(int array[], int elem) {
		boolean flag = false;
		for (int i = 0; i < array.length; i++) {
			if (array[i] == elem) {
				flag = true;
				break;
			}
		}
		if (flag)
			return true;
		else
			return false;
	}

	// 数组元素求和
	public int arraySum(int number[]) {
		int sum = 0;
		for (int i = 0; i < number.length; i++) {
			sum += number[i];
		}
		return sum;
	}

	// 将某行中的全部数据放到一个String中
	public String unionContent(String[][] data, int index) {
		String content = "";
		for (int i = 0; i < data[0].length; i++) {
			content += " " + data[index][i];
		}
		return content;
	}

	// 查找某个子串在整个String中出现的次数
	public int stringFind(String source, String target) {
		int number = 0;
		int i = 0;
		while ((i = source.indexOf(target, i)) != -1) {
			number++;
			i++;
		}
		return number;
	}

	// 得到约简
	public void reductMatric(ArrayList<ArrayList<ArrayList<String>>> matrix) {
		core = new ArrayList<String>();
		ArrayList<ArrayList<String>> reduct = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < matrix.size(); i++) {
			ArrayList<ArrayList<String>> temp = matrix.get(i);
			for (int j = 0; j < temp.size(); j++) {
				ArrayList<String> temp_1 = temp.get(j);
				if (temp_1.size() == 1 && !temp_1.get(0).equals("λ")) {
					core.add(temp_1.get(0));
				}
				if (temp_1.size() > 0 && !temp_1.get(0).equals("λ")) {
					reduct.add(temp_1);
				}
			}
		}
		core = removeDuplicate(core);
		result.add(core);
		for (int i = 0; i < reduct.size(); i++) {
			ArrayList<String> temp = reduct.get(i);
			if (temp.size() > 1) {
				boolean exit = false;
				for (int j = 0; j < temp.size(); j++) {
					boolean flag = false;
					String temp_1 = temp.get(j);
					for (int k = 0; k < core.size(); k++) {
						if (core.get(k).equals(temp_1)) {
							flag = true;
							break;
						}
					}
					if (flag) {
						exit = true;
						break;
					}
				}
				if (!exit) {
					result.add(temp);
				}
			}
		}
		result = removeDuplicate_1(result);
//		System.out.println("Core && Reduct");
//		System.out.print("Core = {");
//		for (int i = 0; i < core.size(); i++) {
//			System.out.print(core.get(i) + ", ");
//		}
//		System.out.println("}");
//		System.out.print("Reduct = {");
//		for (int j = 0; j < result.size(); j++) {
//			ArrayList<String> temp = result.get(j);
//			System.out.print("(");
//			for (int k = 0; k < temp.size(); k++) {
//				System.out.print(temp.get(k) + " V ");
//			}
//			System.out.print(") Λ ");
//		}
//		System.out.println("}");
	}

	// 去除ArrayList中的重复值
	public static ArrayList<String> removeDuplicate(ArrayList<String> list) {
		ArrayList<String> newlist = new ArrayList<String>();
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			String obj = (String) iter.next();
			if (!newlist.contains(obj)) {
				newlist.add(obj);
			}
		}
		return newlist;
	}

	// 去除ArrayList中的重复值
	public static ArrayList<ArrayList<String>> removeDuplicate_1(
			ArrayList<ArrayList<String>> list) {
		ArrayList<ArrayList<String>> newlist = new ArrayList<ArrayList<String>>();
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			ArrayList<String> obj = (ArrayList<String>) iter.next();
			if (!newlist.contains(obj)) {
				newlist.add(obj);
			}
		}
		return newlist;
	}

	// 得到属性的组合
	public ArrayList<String[]> fullCombination(String title[]) {
		ArrayList<String> member = new ArrayList<String>();
		ArrayList<String[]> combination = new ArrayList<String[]>();
		int nCnt = title.length;
		int nBit = (0xFFFFFFFF >>> (32 - nCnt));
		for (int i = 1; i <= nBit; i++) {
			for (int j = 0; j < nCnt; j++) {
				if ((i << (31 - j)) >> 31 == -1) {
					member.add(title[j]);
				}
			}
			String[] temp = new String[member.size()];
			for (int k = 0; k < member.size(); k++) {
				temp[k] = member.get(k);
			}
			combination.add(temp);
			member.clear();
		}
		return combination;
	}

	// 得到可区分矩阵
	public ArrayList<ArrayList<ArrayList<String>>> getDisMatrix() {
		ArrayList<ArrayList<ArrayList<String>>> result = new ArrayList<ArrayList<ArrayList<String>>>();
		ArrayList<ArrayList<String>> result_1 = new ArrayList<ArrayList<String>>();
		ArrayList<String> result_2 = new ArrayList<String>();
		int column = 0;
		for (int i = 1; i < data[0].length; i++) {
			if (!data[0][i].equals("")) {
				column++;
			}
		}
		for (int i = 2; i < data.length; i++) {
			for (int j = 1; j < i; j++) {
				if (data[i][column].equals(data[j][column])) {
					result_2.add("λ");
				} else {
					for (int k = 1; k < column; k++) {
						if (!data[i][k].equals(data[j][k])) {
							result_2.add(data[0][k]);
						}
					}
				}
				result_1.add(result_2);
				result_2 = new ArrayList<String>();
			}
			result.add(result_1);
			result_1 = new ArrayList<ArrayList<String>>();
		}
		return result;
	}
}

