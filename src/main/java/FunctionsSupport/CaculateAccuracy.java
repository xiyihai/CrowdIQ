package FunctionsSupport;


import java.text.DecimalFormat;
import java.util.ArrayList;

import net.sf.json.JSONArray;

public class CaculateAccuracy {

	public double getAccuracy(String wm) {
		// TODO Auto-generated method stub
		if (wm == null) {
			return 0;
		}
		  ArrayList<Object> list = new ArrayList<Object>();
		  JSONArray jsonArray = new JSONArray();//定义json，接收数组
		  Object w ;
		  jsonArray.add(wm);//将工人模型存入json数组中
		  
		  /**
		   * 从json数组中得到工人模型矩阵，根据该矩阵计算工人的准确性
		   */
		  w= jsonArray.get(0);
		  int n = 0;
		  for(int i=0;i<w.toString().length();i++){
			  if(w.toString().charAt(i)==(']'))
				  n++;//用来判断工人模型的大小
			  }
		  double[][] m=new double[n-1][n-1];//接收工人模型矩阵
		  double qua=0;//工人的准确性
		  JSONArray arr=JSONArray.fromObject(w);
		  /**
		   * 从json数组中读取出数据库中的工人模型矩阵
		   */
		  int j=0;
		  for(Object o:arr){
			  JSONArray a1=(JSONArray) o;
			  for(int i=0;i<a1.size();i++){
				  m[j][i]=Double.parseDouble((String) a1.get(i));//读取数据库中工人模型
				  }
			  j++;
			  }
		  qua=WokerAccuracy(m);//调用计算工人质量方法，得到工人准确性 
		  return  qua;
	}
	 /**
    *
    * 工人的准确性计算
    * 
    */
   
	private double WokerAccuracy(double[][] wm){
   	
   	double s=0;//记录工人选择的答案与真实答案相同的累积贡献值
   	double sum=0;//记录工人回答所有问题的累积贡献值
   	double wa=0;//工人的准确率
   	for(int i=0;i<wm.length;i++){
   		for(int j=0;j<wm.length;j++){
   			sum=sum+wm[i][j];
   			if(i==j){
   				s=s+wm[i][j];
   			}	
   		}
   	}
   	wa=s/sum;//工人准确率的计算
   	DecimalFormat    df   = new DecimalFormat("######0.00"); 
   	wa=Double.parseDouble(df.format(wa));
   	return wa;	
   }
   
  
}
