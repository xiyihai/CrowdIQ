package ryr0231.method;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ryr0231.tool.ProbaseAPI;
import ryr0231.util.ColumnS;
import ryr0231.util.MyCountDown;
import ryr0231.util.MyCountDownOne;
import ryr0231.util.MyThread;
import ryr0231.util.MyThreadOne;

public class SemanticRecover{
	public static HashMap<Integer, Double> EntityColumnsHashMap = new HashMap<Integer, Double>(); // 实体列HashMap
	public static ArrayList<ArrayList<String>> TableHeaderLIST = new ArrayList<ArrayList<String>>(); // 表头列表
	public static ArrayList<ArrayList<Double>> TableHeaderValueLIST = new ArrayList<ArrayList<Double>>(); // 表格标题值列表
	public static ArrayList<int[][]> graghs = new ArrayList<int[][]>(); // 图形
	public static ArrayList<ArrayList<Integer>> indexgraphs = new ArrayList<ArrayList<Integer>>(); // 图形索引

	public ArrayList<ArrayList<String>> ResourceTable;
	public static ArrayList<ArrayList<String>> sheetHeader = new ArrayList<ArrayList<String>>(); // 只是用来存放表中第一行的内容，相当于表头
	private static final double ALPHA = 0.85;
	private static final double DISTANCE = 0.0000001;

	// 1.调用了handle方法
	public void handle(ArrayList<ArrayList<String>> ResourceTable,
			PrintWriter pw) throws IOException {

		TableHeaderLIST = GetColumnHeader(ResourceTable, pw);

		System.out.println(TableHeaderValueLIST);
		System.out.println(TableHeaderLIST);
		pw.println(TableHeaderValueLIST);
		pw.println(TableHeaderLIST);

		// TableHeaderLIST
		// ={{"athlete"},{"country"},{"award"},{"sport event"},{"olympics"}};

		// size=3，表示列数
		int size = TableHeaderLIST.size();
		System.out.println("表头长度" + TableHeaderLIST.size());
		pw.println("表头长度" + TableHeaderLIST.size());
		if (size == 0) {
			pw.println("语义恢复失败");

			// 如果表中只有一列内容的话，那么这一列就是主键
		} else if (size == 1) {

			HashMap<Integer, Double> hashMap = new HashMap<Integer, Double>();
			int index = Integer.parseInt(TableHeaderLIST.get(0).get(0));

			hashMap.put(index, 0.5);
			EntityColumnsHashMap = hashMap;
			pw.println("第" + TableHeaderLIST.get(0).get(0) + "是主键");

			// 表中内容大于2列的，寻找主键
		} else {

			// 调用getSubjectColumn方法
			// TableHeaderLIST=
			// [[0, country, nation, developed country, economic power, key
			// supplier country, economy, leading aerospace country],
			// [1, language, laguages, interface language, linguistic guide,
			// human language, multi language, international character],
			// [2, city, global city, world city, big city, large city,
			// financial center, city lamp]]
			// ResourceTable.size()=3 ResourceTable=[[China, Japan, Britain],
			// [Chinese, Japanese, English], [BeiJing, Tokyo, London]]
			// 获取主键的方法：getSubjectColumn
			System.out.println(TableHeaderLIST);
			long start = System.currentTimeMillis();
			System.out.println("开始寻找实体列的时间" + start);
			pw.println("开始寻找实体列的时间" + start);
			getSubjectColumn(TableHeaderLIST, ResourceTable.size(), pw);
			long end = System.currentTimeMillis();
			System.out.println("开始寻找实体列的时间" + end);
			pw.println("开始寻找实体列的时间" + end);

			System.out.println("发现实体列的时间是" + (end - start) + "毫秒");
			pw.println("发现实体列的时间是" + (end - start) + "毫秒");
			// getSubjectColumn(TableHeaderLIST,5,pw);
			// System.out.println(TableHeaderLIST);
		}

		graghs.clear();
		indexgraphs.clear();
		TableHeaderLIST.clear();
		TableHeaderValueLIST.clear();
		SemanticRecover.sheetHeader.clear();
	}

	// 获取实体的方法，调用了GetColumnHeader方法
	// 获取实体的方法：GetColumnHeader
	protected ArrayList<ArrayList<String>> GetColumnHeader(
			ArrayList<ArrayList<String>> ResourceData, PrintWriter pw) {

		long start = System.currentTimeMillis();

		// 执行MyThread类中的MyCountDown方法，赋值0过去
		MyCountDown c = new MyCountDown(0);

		int i = -1;

		// ResourceData = [[第一列],[第二列]，[第三列]]，对每一列分别进行遍历
		for (ArrayList<String> iArrayList : ResourceData) {
			i++; // 列标志，第0、1、2、3列

			// 执行RemovalPartContent方法
			// iArrayList为原来表中一列的内容
			// 对每一个表中内容进行：去除字符串中的大、小、中括号，以及下划线换成空格、分号拆开。
			// 返回来的NewArraylist存放每一列处理过后的内容
			ArrayList<String> NewArraylist = RegularInspect
					.RemovalPartContent(iArrayList);

			// if是单独的
			// 执行ListISData方法
			// 判断内容是否是数字
			if (RegularInspect.ListISData(NewArraylist)) {
				ArrayList<String> iList = new ArrayList<String>();
				ArrayList<Double> iList1 = new ArrayList<Double>();
				iList.add(Integer.toString(i));
				iList1.add(i * 1.0);
				iList.add("number");// 名次
				iList1.add(0.125);
				iList.add("time");// 次数、
				iList1.add(0.125);
				iList.add("quantity"); // 多少
				iList1.add(0.125);
				iList.add("square mile"); // 面积
				iList1.add(0.125);
				iList.add("year");//
				iList1.add(0.125);
				iList.add("volume"); // 体积
				iList1.add(0.125);
				iList.add("length"); // 长度
				iList1.add(0.125);
				iList.add("height"); // 高度
				iList1.add(0.125);
				pw.println("第" + i + "列的表头");
				pw.println("number");
				pw.println("time");
				pw.println("year");
				pw.println("quantity");
				pw.println("square mile");
				pw.println("length");
				pw.println("height");

				// MyThread类的MyCountDown类
				// iList = [0, number, time, quantity, square mile, year,
				// volume, length, height]
				c.arrayLists.add(iList);

				// iList1= [[0.0, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125,
				// 0.125, 0.125]]
				TableHeaderValueLIST.add(iList1);

				// 执行RegularInspect类的ListISDate方法
				// 判断是否是日期
			} else if (RegularInspect.ListISDate(NewArraylist)) {
				ArrayList<String> iList = new ArrayList<String>();
				ArrayList<Double> iList1 = new ArrayList<Double>();
				iList.add(Integer.toString(i));
				iList1.add(i * 1.0);
				iList.add("date");
				iList1.add(0.2);
				iList.add("time");
				iList1.add(0.2);
				iList.add("period");
				iList1.add(0.2);
				iList.add("birthday");
				iList1.add(0.2);
				iList.add("year");
				iList1.add(0.2);
				pw.println("第" + i + "列的表头");
				pw.println("date");
				pw.println("time");
				pw.println("period");
				pw.println("birthDate");
				pw.println("year");

				c.arrayLists.add(iList);

				TableHeaderValueLIST.add(iList1);

				// 执行RegularInspect类的ListIsLength方法
				// 判断内容的字符串长度，长度大于50的话执行这个
			} else if (RegularInspect.ListIsLength(NewArraylist)) {
				ArrayList<String> iList = new ArrayList<String>();
				ArrayList<Double> iList1 = new ArrayList<Double>();
				iList.add(Integer.toString(i));
				iList1.add(i * 1.0);
				iList.add("description");
				iList1.add(0.2);
				iList.add("state");
				iList1.add(0.2);
				iList.add("content");
				iList1.add(0.2);
				iList.add("interpretation");
				iList1.add(0.2);
				iList.add("claim");
				iList1.add(0.2);
				pw.println("第" + i + "列的表头");
				pw.println("description");
				pw.println("state");
				pw.println("content");
				pw.println("interpretation");
				pw.println("claim");

				c.arrayLists.add(iList);

				TableHeaderValueLIST.add(iList1);

				// 执行RegularInspect类的ListISWeb方法
				// 判断是否是网址
			} else if (RegularInspect.ListISWeb(NewArraylist)) {
				ArrayList<String> iList = new ArrayList<String>();
				ArrayList<Double> iList1 = new ArrayList<Double>();
				iList.add(Integer.toString(i));
				iList1.add(i * 1.0);
				iList.add("URL");
				iList1.add(0.25);
				iList.add("website");
				iList1.add(0.25);
				iList.add("Web site");
				iList1.add(0.25);
				iList.add("Web address");
				iList1.add(0.25);
				pw.println("第" + i + "列的表头");
				pw.println("website");
				pw.println("URL");

				c.arrayLists.add(iList);

				TableHeaderValueLIST.add(iList1);

				// 执行RegularInspect类的ListIsSex方法
				// 判断是否是性别
			} else if (RegularInspect.ListIsSex(NewArraylist)) {
				ArrayList<String> iList = new ArrayList<String>();
				ArrayList<Double> iList1 = new ArrayList<Double>();
				iList.add(Integer.toString(i));
				iList1.add(i * 1.0);
				iList.add("sex");
				iList1.add(0.5);
				iList.add("gender");
				iList1.add(0.5);
				pw.println("第" + i + "列的表头");
				pw.println("sex");
				pw.println("gender");

				c.arrayLists.add(iList);

				TableHeaderValueLIST.add(iList1);

				// 执行RegularInspect.ListISWeb
				// 判断是否是邮箱
			} else if (RegularInspect.ListISWeb(NewArraylist)) {
				ArrayList<String> iList = new ArrayList<String>();
				ArrayList<Double> iList1 = new ArrayList<Double>();
				iList.add(Integer.toString(i));
				iList1.add(i * 1.0);
				iList.add("email address");
				iList1.add(0.5);
				iList.add("mailing address");
				iList1.add(0.5);
				pw.println("第" + i + "列的表头");
				pw.println("email address");
				pw.println("mailing address");

				c.arrayLists.add(iList);

				TableHeaderValueLIST.add(iList1);

			} else {

				// 上面的都不符合则执行下面
				// 执行MyThread类的MyCountDown的countAdd方法
				c.countAdd();

				// NewArraylist为一列内容，i为列标示
				// 执行MyThread方法
				MyThread myThread = new MyThread(NewArraylist, i, c, 0, pw);

				// 获取实体的方法
				// 执行MyTread的run方法
				myThread.run();
			}

		}

		while (true) { // 等待所有子线程执行完
			if (!c.hasNext())
				break;
		}

		long end = System.currentTimeMillis();
		pw.println("生成表头的时间是" + (end - start) + "毫秒");
		return c.arrayLists;

	}

	// GetColumnHeaderNew是根据单列表内容，来进行表头恢复

	public ArrayList<String> GetColumnHeaderNew(
			ArrayList<String> ResourceDataOne, int top_k) {

		long start = System.currentTimeMillis();

		// 执行MyThread类中的MyCountDown方法，赋值0过去
		MyCountDownOne c = new MyCountDownOne(0);

		// ResourceData = [[第一列],[第二列]，[第三列]]，对每一列分别进行遍历
		// 列标志，第0、1、2、3列

		// 执行RemovalPartContent方法
		// iArrayList为原来表中一列的内容
		// 对每一个表中内容进行：去除字符串中的大、小、中括号，以及下划线换成空格、分号拆开。
		// 返回来的NewArraylist存放每一列处理过后的内容
		ArrayList<String> NewArraylist = RegularInspect
				.RemovalPartContent(ResourceDataOne);

		// if是单独的
		// 执行ListISData方法
		// 判断内容是否是数字
		if (RegularInspect.ListISData(NewArraylist)) {
			ArrayList<String> iList = new ArrayList<String>();
			ArrayList<Double> iList1 = new ArrayList<Double>();

			iList.add("number");// 名次
			iList1.add(0.125);
			iList.add("time");// 次数、
			iList1.add(0.125);
			iList.add("quantity"); // 多少
			iList1.add(0.125);
			iList.add("square mile"); // 面积
			iList1.add(0.125);
			iList.add("year");//
			iList1.add(0.125);
			iList.add("volume"); // 体积
			iList1.add(0.125);
			iList.add("length"); // 长度
			iList1.add(0.125);
			iList.add("height"); // 高度
			iList1.add(0.125);

			// MyThread类的MyCountDown类
			// iList = [0, number, time, quantity, square mile, year,
			// volume, length, height]
			c.arrayLists = iList;

			// iList1= [[0.0, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125,
			// 0.125, 0.125]]
			TableHeaderValueLIST.add(iList1);

			// 执行RegularInspect类的ListISDate方法
			// 判断是否是日期
		} else if (RegularInspect.ListISDate(NewArraylist)) {
			ArrayList<String> iList = new ArrayList<String>();
			ArrayList<Double> iList1 = new ArrayList<Double>();

			iList.add("date");
			iList1.add(0.2);
			iList.add("time");
			iList1.add(0.2);
			iList.add("period");
			iList1.add(0.2);
			iList.add("birthday");
			iList1.add(0.2);
			iList.add("year");
			iList1.add(0.2);

			c.arrayLists = iList;

			// 执行RegularInspect类的ListIsLength方法
			// 判断内容的字符串长度，长度大于50的话执行这个
		} else if (RegularInspect.ListIsLength(NewArraylist)) {
			ArrayList<String> iList = new ArrayList<String>();
			ArrayList<Double> iList1 = new ArrayList<Double>();

			iList.add("description");
			iList1.add(0.2);
			iList.add("state");
			iList1.add(0.2);
			iList.add("content");
			iList1.add(0.2);
			iList.add("interpretation");
			iList1.add(0.2);
			iList.add("claim");
			iList1.add(0.2);

			c.arrayLists = iList;

			// 执行RegularInspect类的ListISWeb方法
			// 判断是否是网址
		} else if (RegularInspect.ListISWeb(NewArraylist)) {
			ArrayList<String> iList = new ArrayList<String>();
			ArrayList<Double> iList1 = new ArrayList<Double>();

			iList.add("URL");
			iList1.add(0.25);
			iList.add("website");
			iList1.add(0.25);
			iList.add("Web site");
			iList1.add(0.25);
			iList.add("Web address");
			iList1.add(0.25);

			c.arrayLists = iList;

			// 执行RegularInspect类的ListIsSex方法
			// 判断是否是性别
		} else if (RegularInspect.ListIsSex(NewArraylist)) {
			ArrayList<String> iList = new ArrayList<String>();
			ArrayList<Double> iList1 = new ArrayList<Double>();

			iList.add("sex");
			iList1.add(0.5);
			iList.add("gender");
			iList1.add(0.5);

			c.arrayLists = iList;

			TableHeaderValueLIST.add(iList1);

			// 执行RegularInspect.ListISWeb
			// 判断是否是邮箱
		} else if (RegularInspect.ListISWeb(NewArraylist)) {
			ArrayList<String> iList = new ArrayList<String>();
			ArrayList<Double> iList1 = new ArrayList<Double>();
			iList.add("email address,0.5");
			iList1.add(0.5);

			iList.add("mailing address,0.5");
			iList1.add(0.5);

			c.arrayLists = iList;

		} else {

			// 上面的都不符合则执行下面
			// 执行MyThread类的MyCountDown的countAdd方法
			c.countAdd();

			// NewArraylist为一列内容，i为列标示，第二个参数
			// 执行MyThread方法
			MyThreadOne myThreadone = new MyThreadOne(NewArraylist, 1, c, 0,top_k);
			myThreadone.run();

			// 获取实体的方法
			// 执行MyTread的run方法 myThread.run();
		}

		while (true) { // 等待所有子线程执行完
			if (!c.hasNext())
				break;
		}

		long end = System.currentTimeMillis();

		return c.arrayLists;

	}

	// 调用getSubjectColumn方法，获取主键的方法
	// arrayLists=[
	// [[0, country, nation, developed country, economic power, key supplier
	// country, economy, leading aerospace country],
	// [1, language, laguages, interface language, linguistic guide, human
	// language, multi language, international character],
	// [2, city, global city, world city, big city, large city, financial
	// center, city lamp]]
	// length=这个表里面有多少列
	// 获取主键的方法：getSubjectColumn
	protected int getSubjectColumn(ArrayList<ArrayList<String>> arrayLists,
			int length, PrintWriter pw) throws IOException {

		// 调用CombinationData方法
		// 返回来的ilist = first, firstvalue, second, secondvalue
		// ilist = first:第0列的查出来的7个实体,firstvalue:第0列查出来的7个实体的可能度
		// second:第1列的查出来的7个实体,secondvalue:第1列查出来的7个实体的可能度
		// 如果有三列：返回ilist = [com.bj.util.ColumnS@379104ed,
		// com.bj.util.ColumnS@11da801b, com.bj.util.ColumnS@201075c5]
		ArrayList<ColumnS> ilist = CombinationData(arrayLists);

		// 调用weightGraph方法
		// ilist=first,firstvalue,second,secondvalue
		// length=这个表里面有多少列
		// ilist = [com.bj.util.ColumnS@379104ed, com.bj.util.ColumnS@11da801b,
		// com.bj.util.ColumnS@201075c5]
		// 返回权重图：diagram = [[0, 211, 0], [0, 0, 0], [31, 21, 0]]
		int[][] diagram = weightGraph(ilist, length);

		// 打印数组
		System.out.println("概念属性矩阵图：");
		pw.println("概念属性矩阵图：");
		for (int i = 0; i < diagram.length; i++) {
			for (int j = 0; j < diagram.length; j++) {
				pw.print(diagram[i][j] + " ");
				System.out.print(diagram[i][j] + " ");
			}
			pw.println();
			System.out.println();
		}

		PageRankyou(diagram, ALPHA, pw);

		return 0;

	}

	protected void PageRankyou(int[][] diagram, double a, PrintWriter pw) {
		System.out.println("进入pagerank!");

		// 打印数组
		System.out.println("概念属性矩阵图:");
		pw.println("概念属性矩阵图：");
		for (int i = 0; i < diagram.length; i++) {
			for (int j = 0; j < diagram.length; j++) {
				pw.print(diagram[i][j] + " ");
				System.out.print(diagram[i][j] + " ");
			}
			pw.println();
			System.out.println();
		}
		pw.println();
		System.out.println();

		boolean flag = true;
		int[][] exch_gragh = new int[diagram.length][diagram.length];
		for (int i = 0; i < diagram.length; i++) {
			for (int j = 0; j < diagram.length; j++) {
				exch_gragh[i][j] = diagram[i][j];
			}

		}

		while (flag == true) {
			double[][] guiyimatrix = GuiyiMatrix(diagram, pw);
			pw.println("归一化的初始矩阵图S:");
			System.out.println("归一化的初始矩阵图S");
			for (int i = 0; i < guiyimatrix.length; i++) {
				for (int j = 0; j < guiyimatrix.length; j++) {
					pw.print(guiyimatrix[i][j] + " ");
					System.out.print(guiyimatrix[i][j] + " ");
				}
				pw.println();
				System.out.println();
			}
			// 初始化q1;
			List<Double> q1 = new ArrayList<Double>();

			for (int i = 0; i < guiyimatrix.length; i++) {
				q1.add(new Double(1.0));

			}
			System.out.println("初始的向量q为:");
			pw.println("初始的向量q为:");
			printVec(q1, pw);
			System.out.println("初始的矩阵G为:"); // G=as+(1-a)U/n
			printMatrix(getG(guiyimatrix, ALPHA, guiyimatrix.length, pw), pw);
			List<Double> pageRank = calPageRank(guiyimatrix, q1, ALPHA, pw);
			System.out.println("最终的收敛后的PageRank值为:");
			printVec(pageRank, pw);
			System.out.println();
			int subjectColumn = -1;
			double maxValue = -1;
			for (int i = 0; i < pageRank.size(); i++) {

				// nodeWeight = [2.1094827586206897, 1.0, 3.2]
				if (pageRank.get(i) > maxValue) {
					maxValue = pageRank.get(i); // maxValue = 3.2
					subjectColumn = i; // subjectColumn = 2
				}
			}

			int subjectindex = subjectColumn;
			System.out.println("我是实体列的标号：" + subjectindex);
			pw.println("我是实体列的标号：" + subjectindex);
			// splitset的初始化
			Set<Integer> splitset = new HashSet<>();
			splitset.add(subjectindex);
			// otherset的初始化
			Set<Integer> otherset = new HashSet<>();
			boolean other = false;
			for (int i = 0; i < diagram.length; i++) {
				for (int j = 0; j < diagram.length; j++) {
					if (diagram[i][j] != 0) {
						if (i != subjectindex) {
							otherset.add(i);
						}
						if (j != subjectindex) {
							otherset.add(j);
						}
					}

				}

			}
			// 分裂
			while (true) {
				boolean f = false;

				Set<Integer> shanchu = new HashSet<>();
				for (int n : otherset) {
					// 初始化指向n的集合
					Set<Integer> zhixiangN = new HashSet<>();

					// 初始化最强指向集合
					Set<Integer> Nzuiqiangzhi = new HashSet<>();
					int[] zuiqiang = new int[diagram.length - 1];
					for (int j = 0; j < diagram.length - 1; j++) {
						zuiqiang[j] = diagram[j][n];

					}
					int maxValue1 = Integer.MIN_VALUE;

					// 找出数组里面的最大值
					for (int i = 0; i < zuiqiang.length; i++) {
						maxValue1 = Math.max(zuiqiang[i], maxValue1);
					}
					// 最大值与数组里面值比较
					for (int i = 0; i < zuiqiang.length; i++) {
						if (maxValue1 == zuiqiang[i]) {
							Nzuiqiangzhi.add(i);
						}
					}

					for (int j = 0; j < diagram.length; j++) {
						if (diagram[n][j] != 0) {
							zhixiangN.add(j);

						}

					}

					Nzuiqiangzhi.retainAll(splitset);

					if (splitset.containsAll(zhixiangN)
							&& (Nzuiqiangzhi.size() > 0)) {
						splitset.add(n);
						shanchu.add(n);

						f = true;

					}

				}

				for (Integer o1 : shanchu) {
					if (otherset.contains(o1))
						otherset.remove(o1);

				}
				System.out.println("other:内" + otherset);
				System.out.println("splitset:内" + splitset);

				if (f == false || otherset.isEmpty()) {
					break;

				}

			}

			System.out.println("other:" + otherset);
			pw.println("other:" + otherset);
			System.out.println("splitset:" + splitset);
			pw.println("splitset:" + splitset);

			// 修正图(只能全部判断完了，才能删除)第一步标记

			for (int n : splitset) {
				for (int i = 0; i < exch_gragh.length; i++) {
					exch_gragh[n][i] = 0;
					exch_gragh[i][n] = 0;
				}

			}

			// 判断是否还需要分裂

			flag = false;
			for (int i = 0; i < exch_gragh.length; i++) {
				for (int j = 0; j < exch_gragh.length; j++) {
					if (exch_gragh[i][j] != 0) {
						flag = true;
					}
				}

			}
			System.out.println("分裂图:" + splitset + " 剩余图：" + otherset);
			pw.println("分裂图:" + splitset + " 剩余图：" + otherset);

			// 再次初始化

			for (int i = 0; i < diagram.length; i++) {
				for (int j = 0; j < diagram.length; j++) {
					diagram[i][j] = exch_gragh[i][j];
				}

			}

		}

	}

	/**
	 * 归一化初始矩阵
	 * 
	 * @param diagram
	 *            根据概念属性关系得到的初始矩阵
	 * @return 归一化后的矩阵
	 */

	protected double[][] GuiyiMatrix(int[][] diagram, PrintWriter pw) {
		// 求列值和
		double[] SumJ = new double[diagram.length];
		double[][] Guiyimatrix = new double[diagram.length][diagram.length];
		for (int i = 0; i < diagram.length; i++) {
			for (int j = 0; j < diagram.length; j++) {
				SumJ[i] = SumJ[i] + diagram[j][i];

			}

		}
		// 归一化矩阵

		for (int i = 0; i < diagram.length; i++) {
			for (int j = 0; j < diagram.length; j++) {
				if (SumJ[j] > 0) {
					Guiyimatrix[i][j] = diagram[i][j] / SumJ[j];
				} else {
					Guiyimatrix[i][j] = 0;
				}

			}

		}
		return Guiyimatrix;

	}

	// 调用CombinationData方法，获取主键的方法
	// arrayLists=
	// [[0, country, nation, developed country, economic power, key supplier
	// country, economy, leading aerospace country],
	// [1, language, laguages, interface language, linguistic guide, human
	// language, multi language, international character],
	// [2, city, global city, world city, big city, large city, financial
	// center, city lamp]]
	protected ArrayList<ColumnS> CombinationData(
			ArrayList<ArrayList<String>> arrayLists) {
		ArrayList<ColumnS> ilist = new ArrayList<ColumnS>();

		// 对每两列进行讨论，比如：总共有三列，有第0列和第1列、第0列和第2列、第1列和第2列
		for (int i = 0; i < arrayLists.size(); i++) {
			for (int j = i + 1; j < arrayLists.size(); j++) {

				// arrayLists=
				// [0, country, nation, developed country, economic power, key
				// supplier country, economy, leading aerospace country],
				// [1, language, laguages, interface language, linguistic guide,
				// human language, multi language, international character],
				// [2, city, global city, world city, big city, large city,
				// financial center, city lamp]
				// TableHeaderValueLIST
				// [0.0, 0.6331987568810509, 0.13217715444865652,
				// 0.0203298143166161, 0.01752395678288845,
				// 0.012537703754781505, 0.011589882758745717,
				// 0.010923626399507396],
				// [1.0, 0.5432011549096452, 0.11659488742423016,
				// 0.0809589559704078, 0.059478264829369173,
				// 0.059469937747397034, 0.05829744371211508,
				// 0.0404794779852039],
				// [2.0, 0.20225515601839553, 0.10688435556501981,
				// 0.05083181079467454, 0.04273057091851422,
				// 0.03489410807739768, 0.021676940731911475,
				// 0.01985552428754862]]

				// columnS=first,firstvalue,second,secondvalue
				ColumnS columnS = new ColumnS(arrayLists.get(i),
						TableHeaderValueLIST.get(i), arrayLists.get(j),
						TableHeaderValueLIST.get(j));
				ilist.add(columnS);
			}
		}
		return ilist;
	}

	// 调用weightGraph方法
	// ilist=first,firstvalue,second,secondvalue
	// first:第0列的查出来的7个实体,firstvalue:第0列查出来的7个实体的可能度
	// second:第1列的查出来的7个实体,secondvalue:第1列查出来的7个实体的可能度
	// 如果表有3列：ilist=[com.bj.util.ColumnS@379104ed, com.bj.util.ColumnS@11da801b,
	// com.bj.util.ColumnS@201075c5]
	// length=这个表里面有多少列
	protected int[][] weightGraph(ArrayList<ColumnS> ilist, int length)
			throws IOException {
		int[][] diagram = new int[length][length]; // diagram=[[0, 0, 0], [0, 0,
													// 0], [0, 0, 0]]
		ProbaseAPI probaseAPI = new ProbaseAPI();
		int temp;
		int i, j = 0;

		// 如果表有3列：ilist=[[0列实体，0列可能度，1列实体，1列可能度]，[0列实体，0列可能度，2列实体，2列可能度]，[1列实体，1列可能度，2列实体，2列可能度]]
		// 对ilist进行遍历，一次遍历一个
		for (ColumnS c : ilist) {

			// 调用getWeightbyCAndA方法
			// c.first=[0, country, asian country, nation, market, economy, east
			// asian country, eastern country]
			// c.firstValue=[0.0, 0.40904965949079697, 0.12237449377290467,
			// 0.056309345855938664, 0.030371758335203034, 0.028367364143355653,
			// 0.021079871652709144, 0.015191706299217825]
			// c.second=[1, language, laguages, interface language, linguistic
			// guide, human language, multi language, international character]
			// c.secondValue=[1.0, 0.5432011549096452, 0.11659488742423016,
			// 0.0809589559704078, 0.059478264829369173, 0.059469937747397034,
			// 0.05829744371211508, 0.0404794779852039]
			// temp=返回权重,返回第0列和第1列的实体与属性的权重 = 211表示第0列为实体，第1列为属性
			// temp=返回权重,返回第0列和第2列的实体与属性的权重 = -31表示第2列为实体，第0列为属性
			// temp=返回权重,返回第1列和第2列的实体与属性的权重 = -21表示第2列为实体，第1列为属性
			temp = probaseAPI.getWeightbyCAndA(c.first, c.firstValue, c.second,
					c.secondValue);
			// 211、-31、-21

			i = Integer.parseInt(c.first.get(0)); // 0、0、1
			j = Integer.parseInt(c.second.get(0)); // 1、2、2

			if (temp > 0) {
				diagram[i][j] = temp;

			} else {
				diagram[j][i] = -temp;
			}
		}
		return diagram; // [[0, 211, 0], [0, 0, 0], [31, 21, 0]]
	}

	// 1037到

	/**
	 * 打印输出一个矩阵
	 * 
	 * @param m
	 */
	public void printMatrix(List<List<Double>> m, PrintWriter pw) {
		for (int i = 0; i < m.size(); i++) {
			for (int j = 0; j < m.get(i).size(); j++) {
				System.out.print(m.get(i).get(j) + ": ");
				pw.print(m.get(i).get(j) + ", ");
			}
			System.out.println();
			pw.println();
		}
	}

	/**
	 * 打印输出一个向量
	 * 
	 * @param v
	 */
	public void printVec(List<Double> v, PrintWriter pw) {
		for (int i = 0; i < v.size(); i++) {
			System.out.print(v.get(i) + ": ");
			pw.print(v.get(i) + ", ");

		}
		System.out.println();
		pw.println();
	}

	/**
	 * 获得一个初始的随机向量q
	 * 
	 * @param n
	 *            向量q的维数
	 * @return 一个随机的向量q，每一维是0-5之间的随机数
	 */
	public List<Double> getInitQ(int n) {
		Random random = new Random();
		List<Double> q = new ArrayList<Double>();
		for (int i = 0; i < n; i++) {
			q.add(new Double(5 * random.nextDouble()));
		}
		return q;
	}

	/**
	 * 计算两个向量的距离
	 * 
	 * @param q1
	 *            第一个向量
	 * @param q2
	 *            第二个向量
	 * @return 它们的距离
	 */
	public double calDistance(List<Double> q1, List<Double> q2) {
		double sum = 0;

		if (q1.size() != q2.size()) {
			return -1;
		}

		for (int i = 0; i < q1.size(); i++) {
			sum += Math.pow(q1.get(i).doubleValue() - q2.get(i).doubleValue(),
					2);
		}
		return Math.sqrt(sum);
	}

	/**
	 * 计算pagerank
	 * 
	 * @param q1
	 *            初始向量
	 * @param a
	 *            alpha的值
	 * @return pagerank的结果
	 */

	// PageRank的迭代过程
	public List<Double> calPageRank(double[][] guiyimatrix, List<Double> q1,
			double a, PrintWriter pw) {

		List<List<Double>> g = getG(guiyimatrix, a, guiyimatrix.length, pw);
		List<Double> q = null;
		int num = 0;
		while (true) {
			q = vectorMulMatrix(g, q1);
			num = num + 1;
			double dis = calDistance(q, q1);
			System.out.println("第" + num + "次的向量距离为：" + dis);
			pw.println("第" + num + "次的向量距离为：" + dis);
			if (dis <= DISTANCE) {
				System.out.println("q1:");
				pw.println("q1:");
				printVec(q1, pw);
				System.out.println("最终的q:");
				pw.println("最终的q:");
				printVec(q, pw);
				break;
			}
			System.out.println("q:");
			printVec(q, pw);
			q1 = q;
		}
		System.out.println("迭代次数：" + num);
		pw.println("迭代次数：" + num);
		return q;
	}

	/**
	 * 计算获得初始的G矩阵
	 * 
	 * @param a
	 *            为alpha的值，0.85
	 * @return 初始矩阵G
	 */
	public List<List<Double>> getG(double[][] guiyimatrix, double a,
			int length, PrintWriter pw) {

		int n = length;
		// 初始化的转移矩阵
		List<List<Double>> aS = numberMulMatrix(getS(guiyimatrix, pw), a);
		// U为全一矩阵
		List<List<Double>> nU = numberMulMatrix(getU(guiyimatrix, pw), (1 - a)
				/ n);
		List<List<Double>> g = addMatrix(aS, nU);
		return g;
	}

	/**
	 * 计算一个矩阵乘以一个向量
	 * 
	 * @param m
	 *            一个矩阵
	 * @param v
	 *            一个向量
	 * @return 返回一个新的向量
	 */
	public List<Double> vectorMulMatrix(List<List<Double>> m, List<Double> v) {
		if (m == null || v == null || m.size() <= 0
				|| m.get(0).size() != v.size()) {
			return null;
		}

		List<Double> list = new ArrayList<Double>();
		for (int i = 0; i < m.size(); i++) {
			double sum = 0;
			for (int j = 0; j < m.get(i).size(); j++) {
				double temp = m.get(i).get(j).doubleValue()
						* v.get(j).doubleValue();
				sum += temp;
			}
			list.add(sum);
		}

		return list;
	}

	/**
	 * 计算两个矩阵的和
	 * 
	 * @param list1
	 *            第一个矩阵
	 * @param list2
	 *            第二个矩阵
	 * @return 两个矩阵的和
	 */
	public List<List<Double>> addMatrix(List<List<Double>> list1,
			List<List<Double>> list2) {
		List<List<Double>> list = new ArrayList<List<Double>>();
		if (list1.size() != list2.size() || list1.size() <= 0
				|| list2.size() <= 0) {
			return null;
		}
		for (int i = 0; i < list1.size(); i++) {
			list.add(new ArrayList<Double>());
			for (int j = 0; j < list1.get(i).size(); j++) {
				double temp = list1.get(i).get(j).doubleValue()
						+ list2.get(i).get(j).doubleValue();
				list.get(i).add(new Double(temp));
			}
		}
		return list;
	}

	/**
	 * 计算一个数乘以矩阵
	 * 
	 * @param s
	 *            矩阵s
	 * @param a
	 *            double类型的数
	 * @return 一个新的矩阵
	 */
	public List<List<Double>> numberMulMatrix(List<List<Double>> s, double a) {
		List<List<Double>> list = new ArrayList<List<Double>>();

		for (int i = 0; i < s.size(); i++) {
			list.add(new ArrayList<Double>());
			for (int j = 0; j < s.get(i).size(); j++) {
				double temp = a * s.get(i).get(j).doubleValue();
				list.get(i).add(new Double(temp));
			}
		}
		return list;
	}

	/**
	 * 初始化S矩阵
	 * 
	 * @return S
	 */
	public List<List<Double>> getS(double[][] guiyimatrix, PrintWriter pw) {
		List<List<Double>> s = new ArrayList<List<Double>>();
		for (int i = 0; i < guiyimatrix.length; i++) {
			List<Double> row = new ArrayList<Double>();
			for (int j = 0; j < guiyimatrix.length; j++) {
				row.add(guiyimatrix[i][j]);

				pw.print(guiyimatrix[i][j] + " ");
				System.out.print(guiyimatrix[i][j] + " ");
			}
			pw.println();
			System.out.println();
			s.add(row);

		}

		return s;
	}

	/**
	 * 初始化U矩阵，全1
	 * 
	 * @return U
	 */
	public List<List<Double>> getU(double[][] guiyimatrix, PrintWriter pw) {
		List<List<Double>> s = new ArrayList<List<Double>>();
		for (int i = 0; i < guiyimatrix.length; i++) {
			List<Double> row = new ArrayList<Double>();
			for (int j = 0; j < guiyimatrix.length; j++) {
				row.add(new Double(1));
			}

			pw.println();
			System.out.println();
			s.add(row);

		}

		return s;

	}
}