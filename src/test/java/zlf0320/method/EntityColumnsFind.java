package zlf0320.method;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/*根据函数依赖关系，进行规范化得到实体列
 *输入：表格内容[[第一列内容],[第二列内容],[第三列内容],[第三列内容]]
 *输出：“实体列为第*列”；
 **/
public class EntityColumnsFind {

	/*
	 * handle为整个论文的处理过程，目前只有根据表格内容和函数依赖来探测表格的实体列 输入表格中的表格内容 输出为实体列
	 */
	public void handle(ArrayList<ArrayList<String>> ResourceTable,
			PrintWriter pw) throws IOException {

		getSubjectColumn(ResourceTable, ResourceTable.size(), pw);

	}

	/*
	 * getSubjectColumn根据表格内容和函数依赖来探测表格的实体列 输入表格中的表格内容 输出为实体列
	 */
	protected int getSubjectColumn(ArrayList<ArrayList<String>> arrayLists,
			int length, PrintWriter pw) throws IOException {

		// diagram为二维数组，存储的列与列之间的函数依赖值
		// int[][] diagramOriginal = weightGraphOri(arrayLists, pw);
		int[][] diagram = weightGraph(arrayLists, pw);

		/*
		 * pw.println("原始的函数依赖关系："); System.out.println("原始的函数依赖关系："); for (int
		 * i = 0; i < diagramOriginal.length; i++) { for (int j = 0; j <
		 * diagramOriginal.length; j++) { pw.print(diagramOriginal[i][j] + " ");
		 * System.out.print(diagramOriginal[i][j] + " ");
		 * 
		 * } pw.println(); System.out.println(); }
		 */
		pw.flush();

		// 输出diagram的内容
		pw.println("经过处理的函数依赖关系：");
		System.out.println("经过处理的函数依赖关系：");
		for (int i = 0; i < diagram.length; i++) {
			for (int j = 0; j < diagram.length; j++) {
				pw.print(diagram[i][j] + " ");
				System.out.print(diagram[i][j] + " ");

			}
			pw.println();
			System.out.println();
		}
		pw.flush();
		int[][] DM = dependencyMatrix(diagram);
		int[][] DG = directedGraph(DM);
		int[][] OG = optimalGraph(DG);
		int[][] DC = dependcyClosureGraph(DM, OG);
		// 输出DG
		pw.println("DG矩阵为：");
		System.out.println("DG矩阵为：");
		for (int i = 0; i < DG.length; i++) {

			for (int j = 0; j < DG.length; j++) {
				pw.print(DG[i][j] + " ");
				System.out.print(DG[i][j] + " ");
			}
			pw.println();
			System.out.println();

		}

		pw.println("OG矩阵为：");
		System.out.println("OG矩阵为：");
		for (int i = 0; i < OG.length; i++) {

			for (int j = 0; j < OG.length; j++) {
				pw.print(OG[i][j] + " ");
				System.out.print(OG[i][j] + " ");
			}
			pw.println();
			System.out.println();

		}

		// 输出DC
		pw.println("DC矩阵为：");
		System.out.println("DC矩阵为：");
		for (int i = 0; i < DC.length; i++) {

			for (int j = 0; j < DC.length; j++) {
				pw.print(DC[i][j] + " ");
				System.out.print(DC[i][j] + " ");
			}
			pw.println();
			System.out.println();

		}
		// 根据DC判断主键
		pw.println("根据DC实体列判断：");
		System.out.println("根据DC实体列判断：");
		for (int i = 0; i < DC.length; i++) {

			for (int j = 0; j < DC.length; j++) {
				if (DC[i][j] == 1 && i != j) {

					pw.print("主键为" + i + "列；");
					System.out.print("主键为" + i + "列；");
				}

			}
			pw.println();
			System.out.println();

		}

		return 0;

	}

	// 修改后的输入输出实体列探测
	public ArrayList<String> getSubjectColumnNew(
			ArrayList<ArrayList<String>> arrayLists) throws IOException {
		ArrayList<String> result = new ArrayList<String>();

		// diagram为二维数组，存储的列与列之间的函数依赖值
		// int[][] diagramOriginal = weightGraphOri(arrayLists, pw);
		int[][] diagram = weightGraphNew(arrayLists);

		int[][] DM = dependencyMatrix(diagram);
		int[][] DG = directedGraph(DM);
		int[][] OG = optimalGraph(DG);
		int[][] DC = dependcyClosureGraph(DM, OG);
		
		// 根据DC判断主键

		
		for (int i = 0; i < DC.length; i++) {

			for (int j = 0; j < DC.length; j++) {
				if (DC[i][j] == 1 && i != j) {
					if (!result.contains(String.valueOf(i))) {
						result.add(String.valueOf(i));		
					}
				}

			}
		}

		return result;

	}

	/*
	 * 计算表格列与列之间的依赖权重图 输入表格中的表格内容 输出为表格内各个列之间的函数依赖关系
	 */

	protected int[][] weightGraphOri(ArrayList<ArrayList<String>> resourceData,
			PrintWriter pw) throws IOException {
		int size = resourceData.size();// 函数依赖的权重计算
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		pw.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("表格的总列数:" + size);
		pw.println("表格的总列数:" + size);

		ArrayList<String> arrayList = null;

		int[][] diagramyuan = new int[size][size];
		String[][] numa = new String[size][]; // 存储表格内容

		for (int i = 0; i < size; i++) {
			arrayList = resourceData.get(i);
			int sizea = arrayList.size();

			String[] num = new String[sizea];

			for (int j = 0; j < arrayList.size(); j++) {
				num[j] = (String) arrayList.get(j);
				System.out.print("(" + j + ") " + num[j]);
				pw.print("(" + j + ") " + num[j]);
			}

			numa[i] = num;
			System.out.println();
			pw.println();
			pw.flush();
		}
		// 原始函数依赖矩阵（包含所有的可能性）

		for (int i = 0; i < size; i++) {

			for (int j = 0; j < size; j++) {

				// 第i列依赖第j列
				double a = yilai(numa[j], numa[i], pw);
				diagramyuan[j][i] = (int) (a * 100);

			}

		}
		System.out.println("原始函数依赖矩阵：");
		pw.println("原始函数依赖矩阵：");

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {

				System.out.print(diagramyuan[i][j] + " ");
				pw.print(diagramyuan[i][j] + " ");

			}
			System.out.println();
			pw.println();

		}

		return diagramyuan;

	}

	/*
	 * 计算表格列与列之间的依赖权重图 输入表格中的表格内容 输出为表格内各个列之间的函数依赖关系
	 */
	protected int[][] weightGraph(ArrayList<ArrayList<String>> resourceData,
			PrintWriter pw) throws IOException {
		int size = resourceData.size();// 函数依赖的权重计算
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		pw.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("表格的总列数:" + size);
		pw.println("表格的总列数:" + size);

		ArrayList<String> arrayList = null;
		int[][] diagram = new int[size][size]; // diagram=[[0, 0, 0], [0, 0, 0],
												// [0, 0, 0]]
		int[][] diagramyuan = new int[size][size];
		String[][] numa = new String[size][]; // 存储表格内容

		for (int i = 0; i < size; i++) {
			arrayList = resourceData.get(i);
			int sizea = arrayList.size();

			String[] num = new String[sizea];

			for (int j = 0; j < arrayList.size(); j++) {
				num[j] = (String) arrayList.get(j);
				System.out.print("(" + j + ") " + num[j]);
				pw.print("(" + j + ") " + num[j]);
			}

			numa[i] = num;
			System.out.println();
			pw.println();
			pw.flush();
		}
		// 原始函数依赖矩阵（包含所有的可能性）

		for (int i = 0; i < size; i++) {

			for (int j = 0; j < size; j++) {

				// 第i列依赖第j列
				double a = yilai(numa[j], numa[i], pw);
				diagramyuan[j][i] = (int) (a * 100);

			}

		}
		System.out.println("原始函数依赖矩阵：");
		pw.println("原始函数依赖矩阵：");

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {

				System.out.print(diagramyuan[i][j] + " ");
				pw.print(diagramyuan[i][j] + " ");

			}
			System.out.println();
			pw.println();

		}
		// 规则二

		for (int i = 0; i < size; i++) {// 根据函数依赖关系，每一列将依赖依赖关系最强的那一列。
			double max = 0.00;
			int tempj = 0;
			for (int j = 0; j < size; j++) {

				// 第i列依赖第j列
				if (i != j) {

					System.out.println("numa[j]: " + numa[j]);
					System.out.println("numa[i]: " + numa[i]);
					pw.println("numa[j]: " + numa[j]);
					pw.println("numa[i]: " + numa[i]);

					double a = yilai(numa[j], numa[i], pw);
					if (a > max) {
						max = a;
						tempj = j;

					}

				}

			}

			diagram[tempj][i] = (int) (max * 100);

		}

		// 规则三
		// 若相互依赖,且最小的小于0.9，则将 最小的舍去。
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int min = (diagram[i][j] < diagram[j][i]) ? (diagram[i][j])
						: (diagram[j][i]);
				if (min != 0 && min < 90) {
					if (diagram[i][j] < 90) {

						diagram[i][j] = 0;
					} else {
						diagram[j][i] = 0;
					}
				}

			}

		}

		/*
		 * System.out.println("0依赖2："+yilai(numa[2],numa[0]));
		 * System.out.println("2依赖0："+yilai(numa[0],numa[2]));
		 */
		return diagram;

	}

	/*
	 * 计算表格列与列之间的依赖权重图 输入表格中的表格内容 输出为表格内各个列之间的函数依赖关系（新的）
	 */
	protected int[][] weightGraphNew(ArrayList<ArrayList<String>> resourceData)
			throws IOException {
		int size = resourceData.size();// 函数依赖的权重计算
		

		ArrayList<String> arrayList = null;
		int[][] diagram = new int[size][size]; // diagram=[[0, 0, 0], [0, 0, 0],
												// [0, 0, 0]]
		int[][] diagramyuan = new int[size][size];
		String[][] numa = new String[size][]; // 存储表格内容

		for (int i = 0; i < size; i++) {
			arrayList = resourceData.get(i);
			int sizea = arrayList.size();

			String[] num = new String[sizea];

			for (int j = 0; j < arrayList.size(); j++) {
				num[j] = (String) arrayList.get(j);
				
			}

			numa[i] = num;
			

		}
		// 原始函数依赖矩阵（包含所有的可能性）

		for (int i = 0; i < size; i++) {

			for (int j = 0; j < size; j++) {

				// 第i列依赖第j列
				double a = yilaiNew(numa[j], numa[i]);
				diagramyuan[j][i] = (int) (a * 100);

			}

		}
	
		// 规则二

		for (int i = 0; i < size; i++) {// 根据函数依赖关系，每一列将依赖依赖关系最强的那一列。
			double max = 0.00;
			int tempj = 0;
			for (int j = 0; j < size; j++) {

				// 第i列依赖第j列
				if (i != j) {

					

					double a = yilaiNew(numa[j], numa[i]);
					if (a > max) {
						max = a;
						tempj = j;

					}

				}

			}

			diagram[tempj][i] = (int) (max * 100);

		}

		// 规则三
		// 若相互依赖,且最小的小于0.9，则将 最小的舍去。
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int min = (diagram[i][j] < diagram[j][i]) ? (diagram[i][j])
						: (diagram[j][i]);
				if (min != 0 && min < 90) {
					if (diagram[i][j] < 90) {

						diagram[i][j] = 0;
					} else {
						diagram[j][i] = 0;
					}
				}

			}

		}

		/*
		 * System.out.println("0依赖2："+yilai(numa[2],numa[0]));
		 * System.out.println("2依赖0："+yilai(numa[0],numa[2]));
		 */
		return diagram;

	}

	/*
	 * 计算两列之间的函数依赖值 下面函数是计算b依赖a的可能难度，采用的是统计方法。 输入表格中的两列内容 输出为后者依赖前者的pDF
	 * 绝对相等的函数依赖计算
	 */
	public static double yilai(String[] a, String[] b, PrintWriter pw) {
		if (ListISData(a) || ListISDate(a)) {
			return 0;

		}
		int sum = 0;
		double p;
		double len = a.length;
		for (int i = 0; i < a.length; i++) {// 记录首次出现的字符串的个数

			String h = a[i];
			boolean repeated = false;
			for (int j = 0; j < i; j++) {// 保证A是第一个出现的
				if (a[j] == h) {
					repeated = true;
					break;
				}
			}
			if (!repeated) {
				StringBuffer sb = new StringBuffer();// 存储某个a所对应的b的标号
				ArrayList<String> al = new ArrayList<String>();// 存储某个a所对应的b内容
				for (int j = 0; j < a.length; j++) {
					if (h == a[j]) {
						sb.append(", ").append(j);
						System.out.print(h + b[j]);
						pw.print(h + b[j]);
						al.add(b[j]);
					}

				}
				System.out.println();

				Map<String, Integer> map = new HashMap<String, Integer>();
				String[] array = (String[]) al.toArray(new String[al.size()]);// 转换成数组类型
				for (String str : array) { // 统计每个b的个数map(str, num)
					Integer num = map.get(str);
					num = null == num ? 1 : num + 1;
					map.put(str, num);
				}

				Set set = map.entrySet();
				Iterator<Entry> it = set.iterator();
				int max = 0;

				while (it.hasNext()) { // 遍历某个a对应的b的个数
					Entry<String, Integer> entry = (Entry<String, Integer>) it
							.next();
					if (max < entry.getValue()) {
						max = entry.getValue();
					}
				}
				sum += max;
			}
		}
		System.out.println(sum);
		pw.println("总共相同的两列记录数： " + sum);
		p = sum / len;

		System.out.println("依赖值： " + p);
		pw.println("依赖值： " + p);
		return (p);

	}

	public static double yilaiNew(String[] a, String[] b) {
		if (ListISData(a) || ListISDate(a)) {
			return 0;

		}
		int sum = 0;
		double p;
		double len = a.length;
		for (int i = 0; i < a.length; i++) {// 记录首次出现的字符串的个数

			String h = a[i];
			boolean repeated = false;
			for (int j = 0; j < i; j++) {// 保证A是第一个出现的
				if (a[j] == h) {
					repeated = true;
					break;
				}
			}
			if (!repeated) {
				StringBuffer sb = new StringBuffer();// 存储某个a所对应的b的标号
				ArrayList<String> al = new ArrayList<String>();// 存储某个a所对应的b内容
				for (int j = 0; j < a.length; j++) {
					if (h == a[j]) {
						sb.append(", ").append(j);
						

						al.add(b[j]);
					}

				}
				

				Map<String, Integer> map = new HashMap<String, Integer>();
				String[] array = (String[]) al.toArray(new String[al.size()]);// 转换成数组类型
				for (String str : array) { // 统计每个b的个数map(str, num)
					Integer num = map.get(str);
					num = null == num ? 1 : num + 1;
					map.put(str, num);
				}

				Set set = map.entrySet();
				Iterator<Entry> it = set.iterator();
				int max = 0;

				while (it.hasNext()) { // 遍历某个a对应的b的个数
					Entry<String, Integer> entry = (Entry<String, Integer>) it
							.next();
					if (max < entry.getValue()) {
						max = entry.getValue();
					}
				}
				sum += max;
			}
		}
		

		p = sum / len;

		

		return (p);

	}

	// 规则一

	// 判断某列否是数字
	public static boolean ListISData(String[] iArrayList) {
		int count_num = 0;
		int total = iArrayList.length;
		double possible_num;
		for (String iString : iArrayList) {
			if (isNum(iString)) {
				count_num++;
			}
		}
		possible_num = count_num * 1.0 / total;
		if (possible_num > 0.6) {
			return true;
		} else {
			return false;
		}
	}

	// 判断某列是否是日期
	public static boolean ListISDate(String[] iArrayList) {
		int count_num = 0;
		int total = iArrayList.length;
		double possible_num;
		for (String iString : iArrayList) {
			if (isDate(iString)) {
				count_num++;
			}
		}
		possible_num = count_num * 1.0 / total;
		if (possible_num > 0.6) {
			return true;
		} else {
			return false;
		}
	}

	// 判断判断字符串是否为数字
	public static boolean isNum(String str) {
		return str.matches("[-+]?([0-9]+)(([.]([0-9]+))?|([.]([0-9]+))?)\\D*");
	}

	// 判断判断字符串是否为日期
	public static boolean isDate(String str) {
		if (str.matches("\\d{4}-*\\d*-*\\d*")) {
			return true;
		} else if (str.matches("\\d{4}/*\\d*/*\\d*-*\\d{4}/*\\d*/*\\d*")) {
			return true;
		} else {
			return false;
		}

	}

//	public static void main(String argv[]) throws Exception {
//		String[] a = { "1", "2", "3", "4", "5" };
//		String[] b = { "a", "b", "c", "d", "e" };
//		String result = "E:/实验/根据函数依赖找实体列/result_test/ab.txt";
//		PrintWriter pw = new PrintWriter(result);
//		double c = yilai(b, a, pw);
//		double d = yilai(a, b, pw);
//		System.out.println("a依赖b的可能度:" + c);
//		System.out.println("b依赖a的可能度:" + d);
//		pw.flush();
//		pw.close();
//	}

	/*
	 * 获得原始依赖矩阵 下面函数是计算b依赖a的可能难度，采用的是统计方法。 输入未处理的函数依赖矩阵n*n
	 * 输出为经过处理的依赖矩阵m*n,m为决定因子个数
	 * 怎么表示？输出还是n*n;只是不是主键的那一行全部为0；其余的有函数依赖关系的为1，没有的为0，键自己决定自己的为2；
	 */
	protected int[][] dependencyMatrix(int[][] diagram) throws IOException {
		StringBuffer dc = new StringBuffer();// 存储决定列的标号
		// 遍历初始矩阵
		int[][] DM = new int[diagram.length][diagram.length];
		for (int i = 0; i < DM.length; i++) {
			for (int j = 0; j < DM.length; j++) {
				DM[i][j] = 0;
			}

		}
		for (int i = 0; i < diagram.length; i++) {
			int temp = DM[i][i];

			for (int j = 0; j < diagram.length; j++) {
				if (diagram[i][j] != 0 && i != j) {
					DM[i][j] = 1;
					DM[i][i] = 2;
					temp = DM[i][i];

					dc.append(i).append(j);
				
				} else {
					DM[i][j] = 0;
					DM[i][i] = temp;
				}
				// 取其i值放到动态数组中

			}

			// System.out.print(diagram[i][j]+" ");
		}



		// 输出DM 初始依赖关系
	

		return DM;
	}

	/*
	 * 获得键与键之间的关系 输入函数依赖矩阵n*n,输出为经过处理的依赖矩阵m*m,m为决定因子个数
	 * 怎么表示？输出还是n*n;只是不是主键的那一行全部为0；其余的有函数依赖关系的为1，没有的为-1，非主键行均为0；
	 */

	protected int[][] directedGraph(int[][] DM) throws IOException {
		int[][] DG = new int[DM.length][DM.length];
		for (int i = 0; i < DG.length; i++) {
			for (int j = 0; j < DG.length; j++) {
				DG[i][j] = 0;
			}

		}

		for (int i = 0; i < DM.length; i++) {
			for (int j = 0; j < DM.length; j++) {
				if (DM[i][i] != 0) {
					int temp = DG[i][i];
					DG[i][i] = 1;
					temp = DG[i][i];
					if (i != j && DM[i][j] == 1) {
						DG[i][j] = 1;
					} else {
						DG[i][j] = -1;
						DG[i][i] = temp;
					}
				} else
					continue;
			}

		}
		// 修正DG
		for (int i = 0; i < DG.length; i++) {

			for (int j = 0; j < DG.length; j++) {
				if (i == j && DG[i][j] != 1) {
					for (int k = 0; k < DG.length; k++) {
						DG[k][i] = 0;
					}

				}
			}

		}
		// 输出DG
	
		return DG;
	}

	/*
	 * 获得键与键之间的优化，加入传递依赖之后 输入函数依赖矩阵n*n 输出为经过处理的依赖矩阵m*m,m为决定因子个数
	 * 怎么表示？输出还是n*n;将具有传递依赖的依赖也加入键键依赖；
	 */
	protected int[][] optimalGraph(int[][] DG) throws IOException {
		int[][] OG = new int[DG.length][DG.length];
		for (int i = 0; i < DG.length; i++) {
			for (int j = 0; j < DG.length; j++) {
				OG[i][j] = DG[i][j];
			}

		}

		for (int i = 0; i < OG.length; i++) {
			for (int j = 0; j < OG.length; j++) {
				for (int k = 0; k < OG.length; k++) {
					if (OG[i][j] == 1 && i != j) {
						if (OG[j][k] == 1 && j != k) {
							OG[i][k] = 1;

						}

					}

				}

			}

		}
		// System.out.println("优化健-健依赖的迭代次数"+count);
		return OG;
	}

	/*
	 * 获得DM的优化，加入传递依赖之后 输入函数依赖矩阵n*n 输出为经过处理的依赖矩阵m*m,m为决定因子个数 怎么表示？
	 */
	protected int[][] dependcyClosureGraph(int DM[][], int[][] OG)
			throws IOException {
		int[][] DC = new int[DM.length][DM.length];
		for (int i = 0; i < DM.length; i++) {
			for (int j = 0; j < DM.length; j++) {
				DC[i][j] = DM[i][j];
			}

		}

		for (int i = 0; i < DC.length; i++) {
			for (int j = 0; j < DC.length; j++) {
				for (int k = 0; k < DC.length; k++) {
					if (OG[i][j] == 1 && i != j) {
						if (DC[j][k] == 1 && j != k) {
							DC[i][k] = ((j + 1) * 10);

						}

					}

				}

			}

		}

		return DC;

	}
	
//	public static void main(String[] args) throws IOException{
//		ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
//		ArrayList<String> columns = new ArrayList<>();
//		columns.add("china");
//		columns.add("japan");
//		columns.add("england");
//		columns.add("america");
//		
//		ArrayList<String> columns2 = new ArrayList<>();
//		columns2.add("chinese");
//		columns2.add("japanese");
//		columns2.add("english");
//		columns2.add("american");
//		
//		ArrayList<String> columns3 = new ArrayList<>();
//		columns3.add("Novel");
//		columns3.add("Novel");
//		columns3.add("Historical novel");
//		columns3.add("Experimental novel");
//		
//		arrayLists.add(columns);
//		arrayLists.add(columns2);
//		arrayLists.add(columns3);
//		
//		
//		System.out.println(new EntityColumnsFind().getSubjectColumnNew(arrayLists));
//		
//	}

}
