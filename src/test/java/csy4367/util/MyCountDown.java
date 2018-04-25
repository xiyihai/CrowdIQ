package csy4367.util;

import java.util.ArrayList;

/** 
 * @author polaris  
 * @version ����ʱ�䣺2015-12-14 ����5:54:00 
 *  
 */
public class MyCountDown {
	private int count;  
	public ArrayList<ArrayList<String>> arrayLists;
	protected ArrayList<String> lostColumnindex;
		
	//ִ����MyCountDown��������ֵ������count = 0
	public MyCountDown(int count){  
		this.count = count;  
		this.arrayLists=new ArrayList<ArrayList<String>>();
		lostColumnindex=new ArrayList<String>();
	} 

	public synchronized void addlostindex(String iString){  
		lostColumnindex.add(iString); 
	}  
		
	public synchronized void countDown(){  
		count--;  
	} 
		
	//ִ��countAdd����
	//count��1��countĬ��Ϊ0
	public synchronized void countAdd(){  
		count++;        //1��2��3 
	} 
		
	public synchronized boolean hasNext(){  
		return (count > 0);  
	}  
		
	public int getCount() {  
		return count;  
	}  
		
	public void setCount(int count) {  
		this.count = count;  
	}  

}
