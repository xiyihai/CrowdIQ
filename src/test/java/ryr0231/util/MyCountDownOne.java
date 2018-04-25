package ryr0231.util;

import java.util.ArrayList;

/**
 * @author polaris
 * @version 创建时间：2015-12-14 下午5:54:00
 * 
 */
public class MyCountDownOne {
	private int count;
	public ArrayList<String> arrayLists;
	protected ArrayList<String> lostColumnindex;

	// 执行了MyCountDown方法，赋值过来的count = 0
	public MyCountDownOne(int count) {
		this.count = count;
		this.arrayLists = new ArrayList<String>();
		lostColumnindex = new ArrayList<String>();
	}

	public synchronized void addlostindex(String iString) {
		lostColumnindex.add(iString);
	}

	public synchronized void countDown() {
		count--;
	}

	// 执行countAdd方法
	// count加1，count默认为0
	public synchronized void countAdd() {
		count++; // 1、2、3
	}

	public synchronized boolean hasNext() {
		return (count > 0);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
