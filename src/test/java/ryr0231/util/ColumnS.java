package ryr0231.util;

import java.util.ArrayList;

/** 
 * @author polaris  
 * @version 创建时间：2015-12-14 下午5:51:41 
 *  
 */
public class ColumnS {

	public ArrayList<String> first;
	public ArrayList<Double> firstValue; 
	public ArrayList<String> second;
	public ArrayList<Double> secondValue;
	
	public ColumnS(ArrayList<String> first,ArrayList<Double> value1,ArrayList<String> second,ArrayList<Double> value2) {
		this.first = first;
		this.firstValue = value1;
		this.second = second;
		this.secondValue = value2;
	}
}
