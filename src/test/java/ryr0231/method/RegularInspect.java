package ryr0231.method;

import java.util.ArrayList;

/** 
 * @author polaris  
 * @version 创建时间：2015-12-14 下午5:56:24 
 * 定期检验
 */
public class RegularInspect {
	
	//执行RemovalPartContent方法
	//iArrayList为原来表中一列的内容
	//对每一个表中内容进行：去除字符串中的大、小、中括号，以及下划线换成空格、分号拆开。
	public static ArrayList<String> RemovalPartContent(ArrayList<String> iArrayList) {
		ArrayList<String> arrayList = new ArrayList<String>();
			
		for (String iString : iArrayList) {
				
			// 该正则表达式去除了字符串中的小括号，中括号，大括号。
			iString = iString.replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", "");
				
			// 将下划线换成空格；
			iString = iString.replaceAll("_", " ");
			String[] spiltStrings;
				
			// 此处将字符串中以分号为单位，分割，各自为实体。
			if (!iString.contains(";|,")) {
				if (!iString.isEmpty()) {
					while (iString.charAt(0) == ' ') {
						iString = iString.replace(" ", "");
					}
					if (!arrayList.contains(iArrayList)) {
						arrayList.add(iString);
					}
				}
			} else {
				spiltStrings = iString.split(";|,");

				for (String isString : spiltStrings) {
					// 反正存在重复的情况。
					if (!isString.isEmpty()) {
						if (!arrayList.contains(iArrayList)) {
							while (isString.charAt(0) == ' ') {
								isString = isString.replaceFirst(" ", "");
							}
							arrayList.add(isString);
						}
					}
				}
			}

		}
		return arrayList;
	}

		
		
	//执行RegularInspect类的ListISData方法
	//判断是否是数字
	public static boolean ListISData(ArrayList<String> iArrayList) {
		int count_num = 0;
		int total = iArrayList.size();
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
	
	
	//执行RegularInspect类的ListISDate方法
	//判断是否是日期
	public static boolean ListISDate(ArrayList<String> iArrayList) {
		int count_num = 0;
		int total = iArrayList.size();
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

	
	//执行RegularInspect类的ListIsSex方法
	//判断是否是性别
	public static boolean ListIsSex(ArrayList<String> iArrayList) {
		int count_num = 0;
		int total = iArrayList.size();
		double possible_num;
		for (String iString : iArrayList) {
			if (iString.equals("F") || iString.equals("M")) {
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

	
	//执行RegularInspect类的ListISWeb方法
	//判断是否是网址
	//判断是否是邮箱
	public static boolean ListISWeb(ArrayList<String> iArrayList) {
		int count_num = 0;
		int total = iArrayList.size();
		double possible_num;
		for (String iString : iArrayList) {
			if (iString.contains("www.")) {
				count_num++;
			}

		}
		possible_num = count_num * 1.0 / total;
		if (possible_num > 0.5) {
			return true;
		} else {
			return false;
		}
	}

	
	// 判断是否为邮箱
	public static boolean ListISMail(ArrayList<String> iArrayList) {
		int count_num = 0;
		int total = iArrayList.size();
		double possible_num;
		for (String iString : iArrayList) {
			if (iString
					.matches("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
				count_num++;
			}

		}
		possible_num = count_num * 1.0 / total;
		if (possible_num > 0.5) {
			return true;
		} else {
			return false;
		}
	}

	
	//执行RegularInspect类的ListIsLength方法
	//判断内容的字符串长度
	public static boolean ListIsLength(ArrayList<String> iArrayList) {
		int count_num = 0;
		int total = iArrayList.size();
		double possible_num;
		for (String iString : iArrayList) {
			if (iString.length() > 50) {
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

	
	// 判断是否是日期，或者年份如1990-12-12,1990-1991、1990/12/13,1990/12/13-1990/12/12等类型
	public static boolean isDate(String str) {
		if (str.matches("\\d{4}-*\\d*-*\\d*")) {
			return true;
		} else if (str.matches("\\d{4}/*\\d*/*\\d*-*\\d{4}/*\\d*/*\\d*")) {
			return true;
		} else {
			return false;
		}

	}

	
	//判断是否是数字
	public  static boolean isNum(String str) {
		return str.matches("[-+]?([0-9]+)(([.]([0-9]+))?|([.]([0-9]+))?)\\D*");
	}


}
