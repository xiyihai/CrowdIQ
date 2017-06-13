package CaculateParams;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.hibernate.loader.custom.Return;

import com.sun.org.apache.bcel.internal.generic.RETURN;

import net.sf.json.JSONArray;

public class CaculateParameterImpl implements CaculateParameter{

	/**
	 * 计算困难度
	 */
	private double dif;
	public double getDiffDegree(Integer[] selects, Integer[] showing, Double top_k) {
		// TODO Auto-generated method stub
		double dif_topk;
		//int local1=1;
		// 划分空格数量等级
		int level0=0,level1=0;
		//level0：select的数量对于任务难度的影响；level1：select中空格的个数对于任务难度的影响；
		int sum=0;
		int lens=selects.length;
		if(lens==0){
			level0=0;
		}else if(lens==1){
			level0=1;
		}else if(lens==2){
			level0=2;
		}else if(lens<=5){
			level0=5;
		}else 
			level0=15;
		
		for(int i=0;i<selects.length;i++){
			sum=sum+selects[i];
			}
		if(sum<5){
			level1=1;
			}
		else if(sum<15){
			level1=5;	
			}
		else if(sum<50){
			level1=10;
		}else if(sum<100){
			level1=25;
		}else{
			level1=50;
		}
		dif_topk=1*(1-top_k);
		dif=0.4*level0/23+0.4*level1/91+0.2*dif_topk;
		return dif;
	}

	@Override
	public double getEachReward(Integer[] selects, Integer[] showing, Double top_k) {

		/**
		 * 计算任务单位工资
		 */
		double punit=1,dfloat=0.0,wbase=0.0;
		if(dif>=0&&dif<=0.5){
			dfloat=0.6;
		}else if(dif>0.5&&dif<=0.8){
			dfloat=1;
		}else if(dif>0.8&&dif<=1.0){
			dfloat=1.2;
		}else{
			System.out.println("the HIT got something wrong");
		}
		wbase=punit*dfloat;
		return wbase;
	}

	@Override
	public int getWorkNumber(String[] wm) {
        double[] quality=new double[wm.length];
		double u=0.65,average=0,c = 0;//定义质量系数，以及工人的平均质量
		
		/**
		 * 计算工人的平均质量
		 */
        for(int i=0;i<quality.length;i++){
        	quality[i]=ReadWm(wm[i]);
//        	System.out.println(quality[i]);
        	c=c+quality[i];
        }
        average=c/quality.length;//工人的平均质量
        
		/**
		 * 计算工人个数
		 */
		int n=0;
		n=getOptimizeWokerNum(average,u);
		return n;
	}
	//优化工人个数
		@SuppressWarnings("unused")
		private int getOptimizeWokerNum(double c,double u){
			int e=0;
			if(c==1){
				 e=1;
			}else if(c==0){
				e=0;
			}else{
			 e=(int) (2*((0-Math.log(1-c))/(4*(u-0.5)*(u-0.5)))+1);
			}
			return e;
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
	   
	   /**
		 * 
		 * 根据工人id从数据库中读取工人模型，获得工人准确率
		 * 
		 */
		private double ReadWm(String wm) {
			// TODO Auto-generated method stub
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

	
}
