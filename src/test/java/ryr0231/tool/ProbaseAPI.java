package ryr0231.tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.struts2.ServletActionContext;
import ryr0231.method.BufferedRandomAccessFile;
import ryr0231.method.Match;
import ryr0231.util.AddressIndex;
import ryr0231.util.EntityFrequencyScore;
import ryr0231.util.NameScorePair;

public class ProbaseAPI {
	static double icontent = Math.log(0.0000001);
	FileReader rdFileReader;
	BufferedReader BR;
	String path = ServletActionContext.getRequest().getRealPath("/WEB-INF/probase");
	
	// 执行GetOneClassSetByInstanceSet方法
	// 得到一个类的实例集，得到一个相关概念，用来处理实体的
	// iArrayList为一列内容, topN=7, exactMatch=false
	public ArrayList<NameScorePair> GetOneClassSetByInstanceSet(
			ArrayList<String> iArrayList, int top_k, Boolean exactMatch)
			throws NumberFormatException, IOException {

		HashMap<String, EntityFrequencyScore> hashMap = new HashMap<String, EntityFrequencyScore>();
		ArrayList<AddressIndex> arrayList = new ArrayList<AddressIndex>(); // arrayList[entiy,start_index,end_index]

		// 概念和实体索引.txt
		
		FileReader rdFileReader = new FileReader(path+"/ConceptAndEntity_Index.txt");

		this.BR = new BufferedReader(rdFileReader);
		String rowtext = "";
		byte[] indexbyte;
		String[] SpiltIndex = null;
		String removeString = null;
		int count = iArrayList.size(); // count为这一列内容的个数

		// 对ConceptAndEntity_Index.txt索引内容中每一行进行遍历
		while ((rowtext = BR.readLine()) != null) {

			// 比如：iArrayList = [Chinese, Japanese, English]
			// 对一列内容进行遍历
			for (String iString : iArrayList) {

				// indexbyte为要恢复的字节
				// 比如：Chinese = [67, 104, 105, 110, 101, 115, 101]
				indexbyte = iString.getBytes();

				if (indexbyte.length >= 2) { // 表格中的内容的长度>=2

					// 在索引中对前两个字符进行匹配
					if (rowtext.contains(Byte.toString(indexbyte[0]))
							&& rowtext.contains(Byte.toString(indexbyte[1]))) {
						SpiltIndex = rowtext.split("\\t");

						// 索引中的第一个数字等于第一个字节码，索引中的第二个数字等于第二个字节码
						if (indexbyte[0] == Integer.parseInt(SpiltIndex[0])
								&& indexbyte[1] == Integer
										.parseInt(SpiltIndex[1])) {
							AddressIndex AI = new AddressIndex();
							AI.entity = iString; // 表中内容
							AI.start_index = Integer.parseInt(SpiltIndex[2]);
							AI.end_index = Integer.parseInt(SpiltIndex[3]);

							arrayList.add(AI);
							removeString = iString;
							break;
						}
					}
				}
			}

			if (removeString != null) {
				iArrayList.remove(removeString);
				removeString = null;
			}
		}

		BR.close();
		rdFileReader.close();
		long pointer;
		String aString;
		String[] spilttextStrings;

		// 执行BufferedRandomAccessFile方法
		// BufferedRandomAccessFile：缓冲随机存取文件
		// 传入的参数：概念实体源，只读r
		// 返回的file中buf=[65536]数组，默认为[0,0,0,.....]
		BufferedRandomAccessFile file = new BufferedRandomAccessFile(
				path+"/ConceptAndEntity.txt", "r");

		// arrayList = [entity,start_index,end_index]
		for (AddressIndex AI : arrayList) {
			aString = AI.entity; // 得到表中内容

			// 调用seek方法
			// 调用了本地方法，buf不用管
			file.seek(AI.start_index);

			// 调用readLine1方法
			while ((rowtext = file.readLine1()) != null) { // 概念实体源的行内容不为空
				// System.out.println(rowtext); //factor Chinese demand 3 387647
				// factor Chinese demand 3 387647
				// 中间有个空格
				// factor Christian Zionism 2 387647
				pointer = file.getFilePointer(); // 422500670、422500671、422500705、422500706、422500739、422500740、422500797、422500798、422500850、422500851、422500880

				if (pointer <= AI.end_index) {
					if (rowtext.contains(aString)) { // 如果概念实体行内容包含实体

						spilttextStrings = rowtext.split("\\t"); // 将概念实体行内容变为字符数组

						// 调用Match.match的算法方法
						// 最大公共子序列，比较表中内容和取出的实体源的第二个内容
						if (Match.match(aString, spilttextStrings[1])) {
							// 如果hashMap中包含rowtext的第一个元素，执行if
							if (hashMap.containsKey(spilttextStrings[0])) {

								EntityFrequencyScore EFS = new EntityFrequencyScore();
								EFS = hashMap.get(spilttextStrings[0]);

								// score=等于上次score*
								EFS.score = EFS.score
										* ((Integer
												.parseInt(spilttextStrings[2]
														.trim())
												* 100000
												/ Integer
														.parseInt(spilttextStrings[3]
																.trim()) / 100000.0));

								EFS.frequency = EFS.frequency - 1;

								hashMap.put(spilttextStrings[0], EFS);
							} else {
								EntityFrequencyScore EFS = new EntityFrequencyScore();

								EFS.score = Integer
										.parseInt(spilttextStrings[3])
										* (Integer.parseInt(spilttextStrings[2]
												.trim())
												* 100000
												/ Integer
														.parseInt(spilttextStrings[3]
																.trim()) / 100000.0);

								EFS.frequency = count - 1; // count为这一列内容的个数

								hashMap.put(spilttextStrings[0], EFS);

							}
						}
					}
				} else {
					break;
				}
			}
		}

		BR.close();
		file.close();

		// 调用了dataSort方法
		ArrayList<NameScorePair> Sort_arrayList = dataSort(hashMap, top_k);
		return Sort_arrayList; // 返回的conceptList[maxconcept,maxscore]
	}

	// 执行dataSort方法，寻找相关概念
	// hashMap中存储的是这一列的数据：<key=类型,value(score,frequency)>
	protected ArrayList<NameScorePair> dataSort(
			HashMap<String, EntityFrequencyScore> hashMap, Integer topN) {
		ArrayList<NameScorePair> conceptList = new ArrayList<NameScorePair>(); // [name,score]
		EntityFrequencyScore EFS;

		// double AddFactor = 1/2600000.0; //添加因子
		double AddFactor = 1 / 26.0;
		double iscore;
		double sum = 0;

		for (String ikey : hashMap.keySet()) {
			EFS = hashMap.get(ikey);
			iscore = EFS.score;
			int z = EFS.frequency;
			while (true) {
				if (z > 0) {
					iscore = iscore * AddFactor; // 这一列每一个的score乘以AddFactor
					z--;
				} else {
					break;
				}
			}
			EFS.score = iscore;
			sum += iscore;
		}

		for (String ikey : hashMap.keySet()) {
			EFS = hashMap.get(ikey);
			EFS.score = EFS.score / sum; // 每一个score除以总的
		}

		for (int i = 0; i < topN; i++) {
			double maxscore = 0;
			String maxconcept = null;
			for (String ikey : hashMap.keySet()) {
				EFS = hashMap.get(ikey);

				// 找出score成绩最大的
				if (EFS.score > maxscore) {
					maxconcept = ikey;
					maxscore = EFS.score;
				}
			}

			if (maxconcept == null) {
				return conceptList;
			}
			hashMap.remove(maxconcept);
			// maxconcept，maxscore
			NameScorePair nsp = new NameScorePair(maxconcept, maxscore);

			conceptList.add(nsp);
		}
		return conceptList;
	}

	// 调用getWeightbyCAndA方法
	// alist=[0, country, nation, developed country, economic power, key
	// supplier country, economy, leading aerospace country]
	// avalue=[0.0, 0.6331987568810509, 0.13217715444865652, 0.0203298143166161,
	// 0.01752395678288845, 0.012537703754781505, 0.011589882758745717,
	// 0.010923626399507396]
	// blist=[1, language, laguages, interface language, linguistic guide, human
	// language, multi language, international character]
	// bvalue=[1.0, 0.5432011549096452, 0.11659488742423016, 0.0809589559704078,
	// 0.059478264829369173, 0.059469937747397034, 0.05829744371211508,
	// 0.0404794779852039]
	public int getWeightbyCAndA(ArrayList<String> alist,
			ArrayList<Double> avalue, ArrayList<String> blist,
			ArrayList<Double> bvalue) throws IOException {
		double weight = 0;

		// 概念与属性.txt
		// FileReader fReader = new
		// FileReader("F:/coding/1/1/表语义恢复数据源/ConceptAndAttribute.txt");
		FileReader fReader = new FileReader(
				path+"/ConceptAttribute.txt");

		this.BR = new BufferedReader(fReader);
		String row;
		String[] spilts;
		boolean a = false;
		String temp = "";
		int tempindex = -1;

		// row中的内容：实体 属性 可能性 出现次数
		while ((row = BR.readLine()) != null) {

			// System.out.println(alist.size()); //8
			for (int i = 1; i < alist.size(); i++) {

				// System.out.println(alist.get(1)); //country,对每一个内容取出来和row进行比较
				if (row.contains(alist.get(i))) {
					a = true;
					temp = alist.get(i);
					tempindex = i;
					break; // 跳出for循环
				}
			}

			// System.out.println(alist.size()); //8
			for (int j = 1; j < blist.size(); j++) {

				// System.out.println(blist.get(1));
				// //language,对每一个内容取出来和row进行比较
				if (row.contains(blist.get(j)) && a == true) {
					spilts = row.split("\\t");
					if (spilts.length >= 3) {

						// 第0列和第1列的实体与属性的权重 = 211表示第0列为实体，第1列为属性
						// 第0列和第2列的实体与属性的权重 = -31表示第2列为实体，第0列为属性
						// 第1列和第2列的实体与属性的权重 = -21表示第2列为实体，第1列为属性
						// row中第1列实体等于blist.get(j) && row中第2列属性等于alist.get(i)
						// 前面的为属性，后面的为实体时，权重为负
						if (spilts[0].equals(blist.get(j))
								&& spilts[1].equals(temp)) {
							weight = weight - Double.parseDouble(spilts[2])
									* avalue.get(tempindex) * bvalue.get(j);
							break;
						}

						// row中第2列属性等于blist.get(j) && row中第1列实体等于alist.get(i)
						// 前面的为实体，后面的为属性时，权重为正
						if (spilts[1].equals(blist.get(j))
								&& spilts[0].equals(temp)) {
							weight = weight + Double.parseDouble(spilts[2])
									* avalue.get(tempindex) * bvalue.get(j);
							break;

						}
					}

				}
			}
			a = false;
		}

		BR.close();
		fReader.close();
		return (int) (weight * 1000000); // 返回权重
	}
}
