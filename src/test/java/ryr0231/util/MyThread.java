package ryr0231.util;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import ryr0231.method.SemanticRecover;
import ryr0231.tool.ProbaseAPI;




//执行MyThread类
public class MyThread extends Thread {
	protected ArrayList<String> list = new ArrayList<String>(); 
	protected int  icolumn;
	private MyCountDown c; 
	int iclass; 
	PrintWriter pw;
	//执行MyThread方法
	//list为一列的内容，icolumn列标示，iclass刚开始默认为0
	public MyThread(ArrayList<String> list, int icolumn, MyCountDown c, int iclass,PrintWriter pw) { 
		this.list = list; 
		this.icolumn = icolumn;
		this.c = c;               //记录线程开的次数。
		this.iclass = iclass;             //0 标志是寻找表头， 1标志寻找主键
		this.pw = pw;
	} 
	
	//执行MyTread的run方法
	public void run() { 		
		
		ProbaseAPI probaseAPI = new ProbaseAPI();
		ArrayList<NameScorePair> arrayList = null;
		ArrayList<String> iArrayList = new ArrayList<String>();
		
		//list为一列的内容
		for(String iString : list){
			iArrayList.add(iString);
		}
		
		try {
			if(iclass == 0){	   //iclass=0 标志是寻找表头， 1标志寻找主键
				//System.out.println("我开始找表头了"); 
				
				//执行GetOneClassSetByInstanceSet方法
				//得到一个类的实例集,返回了7个
				arrayList = probaseAPI.GetOneClassSetByInstanceSet(list, 7, false);  
				
				
				//假如获取不到任何概念，则将实例以逗号拆分重新查库
				if(arrayList.size() <= 1){
					arrayList = secondintergrate(iArrayList);
				}
				
				//调用了outprint方法，打印出7个[maxconcept,maxscore]
				outprint(arrayList,pw);
			}
		} catch (NumberFormatException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		}	
	} 
	
	
   protected   ArrayList<NameScorePair> secondintergrate(ArrayList<String> ilist) throws NumberFormatException, IOException{
	   ArrayList<String> iArrayList=new ArrayList<String>();
	   String[] spiltStrings;
	   String regex="[-,]";
	   for(String iString :ilist){
		   spiltStrings=iString.split(regex);
		   for(String spstring:spiltStrings)
		   {
			   while(spstring.charAt(0) == ' ')
			    {
				   spstring = iString.replaceFirst(" ", "");
			    }
			   iArrayList.add(spstring);
		   }		   
	   }
	   ProbaseAPI probaseAPI=new ProbaseAPI();
	   ArrayList<NameScorePair> arrayList=null;		 	
	   arrayList=probaseAPI.GetOneClassSetByInstanceSet(iArrayList, 5, false);
	   return arrayList;
	}
	
   
   
	//调用了outprint方法，打印出7个[maxconcept,maxscore]
 	protected synchronized  void outprint(ArrayList<NameScorePair> arrayList,PrintWriter pw){
		String contentString = null;
		
		//icolumn表示列标示
		if(arrayList.size() == 0){
			contentString="第" + icolumn + "列没有具体概念";	
			pw.println(contentString); 
		} else { 
			ArrayList<String> newarrayList = new ArrayList<String>();
			ArrayList<Double> newarrayList1 = new ArrayList<Double>();
		 
			String columnstring = Integer.toString(icolumn);
			newarrayList.add(columnstring);
			newarrayList1.add(icolumn * 1.0);
		 
			contentString="第" + icolumn + "列相关概念：";		  
			pw.println(contentString);
	     
			for(NameScorePair nsp : arrayList){
				newarrayList.add(nsp.name);
				newarrayList1.add(nsp.score);
				pw.println(nsp.name + "\t" + nsp.score);
			} 
	    
			SemanticRecover.TableHeaderValueLIST.add(newarrayList1);
			c.arrayLists.add(newarrayList);
	   
		}
		c.countDown();
		
	}
 	
 	
 	
	//获得主键
 	//调用integration方法，获得主键
	protected synchronized  void integration(ArrayList<NameScorePair> arrayList){
		
		if(arrayList.size() == 0){
			
	    }else{ 
		  ArrayList<String> newarrayList = new ArrayList<String>();	
		  String columnstring = Integer.toString(icolumn);
		  newarrayList.add(columnstring);
		  
	   for (NameScorePair nsp :arrayList){
		   newarrayList.add(nsp.name);
	   } 
	   c.arrayLists.add(newarrayList);
	   
	  }
		c.countDown();
	}
}

