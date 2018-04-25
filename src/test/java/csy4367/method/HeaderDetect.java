package csy4367.method;

import java.io.IOException;
import java.util.ArrayList;

import csy4367.util.MyCountDown;


public class HeaderDetect {
	public static ArrayList<ArrayList<String>> TableHeaderLIST = new ArrayList<ArrayList<String>>(); // 表头列表
	public static ArrayList<ArrayList<Double>> TableHeaderValueLIST = new ArrayList<ArrayList<Double>>(); // 表格标题值列表
	
	public ArrayList<ArrayList<String>> ResourceTable;
	double result=0;
	// 1.调用了handle方法
	public double handle(ArrayList<ArrayList<String>> ResourceTable) throws IOException {					
		TableHeaderLIST = GetColumnHeader(ResourceTable);
		int size = TableHeaderLIST.size();
		if (size == 0) {
			return 0;
		} 
		return result/TableHeaderLIST.size();
	}

	protected ArrayList<ArrayList<String>> GetColumnHeader(
			ArrayList<ArrayList<String>> ResourceData) {

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
			
			String header=NewArraylist.get(0).toString();
			
			if(RegularInspect.ListISDate(NewArraylist)>0.3)
			{
				if (!RegularInspect.isDate(header)){
					ArrayList<String> iList = new ArrayList<String>();
					ArrayList<Double> iList1 = new ArrayList<Double>();
					
					iList1.add(i * 1.0);
					iList.add("date");
					
					result = result + RegularInspect.ListISDate(NewArraylist);
					
				c.arrayLists.add(iList);

				TableHeaderValueLIST.add(iList1);
				}
				// 执行RegularInspect类的ListIsLength方法
				// 判断内容的字符串长度，长度大于50的话执行这个
			} else if (RegularInspect.ListIsLength(NewArraylist)>0.3) {
				if(!RegularInspect.IsLength(header)){
				ArrayList<String> iList = new ArrayList<String>();
				ArrayList<Double> iList1 = new ArrayList<Double>();
				
				iList1.add(i * 1.0);
				iList.add("description");
				result = result + RegularInspect.ListIsLength(NewArraylist);
				
				c.arrayLists.add(iList);

				TableHeaderValueLIST.add(iList1);
				}
				// 执行RegularInspect类的ListISWeb方法
				// 判断是否是网址
			} else if (RegularInspect.ListISWeb(NewArraylist)>0.6) {
				if(!RegularInspect.ISMail(header)){
				ArrayList<String> iList = new ArrayList<String>();
				ArrayList<Double> iList1 = new ArrayList<Double>();
				
				iList1.add(i * 1.0);
				iList.add("URL");
				result = result + RegularInspect.ListISWeb(NewArraylist);
				
				c.arrayLists.add(iList);

				TableHeaderValueLIST.add(iList1);
				}
				// 执行RegularInspect类的ListIsSex方法
				// 判断是否是性别
			} else if (RegularInspect.ListIsSex(NewArraylist)>0.6) {
				if(!RegularInspect.IsSex(header)){
				ArrayList<String> iList = new ArrayList<String>();
				ArrayList<Double> iList1 = new ArrayList<Double>();
				
				iList1.add(i * 1.0);
				iList.add("sex");
			
				result = result + RegularInspect.ListIsSex(NewArraylist);
				
				c.arrayLists.add(iList);

				TableHeaderValueLIST.add(iList1);
				}
				// 执行RegularInspect.ListISWeb
				// 判断是否是邮箱
			} else if (RegularInspect.ListISMail(NewArraylist)>0.6) {
				if(!RegularInspect.ISMail(header)){
				ArrayList<String> iList = new ArrayList<String>();
				ArrayList<Double> iList1 = new ArrayList<Double>();	
				iList1.add(i * 1.0);
				iList.add("email address");				
				result = result + RegularInspect.ListISMail(NewArraylist);			
				c.arrayLists.add(iList);

				TableHeaderValueLIST.add(iList1);
				}
			} else if(RegularInspect.ListISData(NewArraylist)>0.9){
				if (!RegularInspect.isNum(header)) {
					ArrayList<String> iList = new ArrayList<String>();
					ArrayList<Double> iList1 = new ArrayList<Double>();			
					iList1.add(i * 1.0);
					iList.add("number");// 名次			
					result = result + RegularInspect.ListISData(NewArraylist);
					c.arrayLists.add(iList);
					TableHeaderValueLIST.add(iList1);
				}		
			}
		}
		return c.arrayLists;

	}

}
