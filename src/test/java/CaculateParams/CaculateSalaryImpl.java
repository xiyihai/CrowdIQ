package CaculateParams;

import java.text.DecimalFormat;
import java.util.ArrayList;

import net.sf.json.JSONArray;

@SuppressWarnings("unused")
public  class CaculateSalaryImpl implements CaculateSalary{
	
	public double getSalary(String wquality,Double tfloat_minute,Double wbase,Double di,Integer workerDone_number, Integer worker_number, Double restTime, Double lastTime){
		double wage=0.0,ui=0.0;
		double c=0.5;
		c=ReadWm(wquality);
		ui=0.5*(1+Math.pow(1-di, c));
		wage=wbase*ui;
		return wage;
		
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
		return wa;	
   }

   
   
   /**
    * 
    * DealData接口的实现
    * 
    */
   
   /**
	 * 
	 * 根据工人id从数据库中读取工人模型，获得工人准确率
	 * 
	 */
	private double ReadWm(String wm) {
		// TODO Auto-generated method stub
	      double Q=0;//工人的准确性

		  /**
		   * 从json数组中得到工人模型矩阵，根据该矩阵计算工人的准确性
		   */
		      int n = 0;
		      for(int i=0;i<wm.length();i++){
		    	  if(wm.charAt(i)==(']'))
				  n++;//用来判断工人模型的大小
		    	  }
		      double[][] m=new double[n-1][n-1];//接收工人模型矩阵
		      JSONArray arr=JSONArray.fromObject(wm);
		      //System.out.println(arr);
		      
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
			      Q=WokerAccuracy(m);//调用计算工人质量方法，得到工人准确性 
			 
			  return Q;
			  }
		
		}