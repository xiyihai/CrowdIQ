package RecommendTask;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import vos.RequesterTaskInfo;
import vos.WorkerInfo;

public class RecommendTaskImpl implements RecommendTask {

	private int num;
	private int j=0;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String[] getRecommendTask(Integer worker_number, Integer times, List<WorkerInfo> workerInfos, RequesterTaskInfo requesterInfos){
		// TODO Auto-generated method stub
		ArrayList messageid=new ArrayList();
		ArrayList messagequality=new ArrayList();
		//ArrayList messagecost=new ArrayList();
		ArrayList wm=new ArrayList();
		for(int i=0;i<workerInfos.size();i++){
			messageid.add(workerInfos.get(i).getWorker_id());
			wm.add(workerInfos.get(i).getQuality());
			//messagecost.add(workerInfos.get(i).getAver_reward());
			}
		//for(int j=0;j<wm.size();j++){
			messagequality=ReadWm(wm);
//			System.out.println(messagequality);
		//}
		/**
		 *������id����������һ��String���͵������У����ں�������	
		 */
			String[][] quaId=new String[messageid.size()][2];
			double sum = 0;
			double average;//ƽ������
			
			/**
			 * ������id����������һ����ά������
			 */
			for(int i=0;i<quaId.length;i++){
					quaId[i][0]=messageid.get(i).toString();
					quaId[i][1]=messagequality.get(i).toString();
					sum=sum+Double.parseDouble(quaId[i][1]);
			}
			
			average=sum/messageid.size();//���˵�ƽ������
			//System.out.println(average);
			
			/**
			 * ����֮��Ĺ���������id��Ϣ
			 */
			String[][] sortMessage=new String[messageid.size()][2];
			sortMessage=bubbleSort(quaId);
			
		    num=worker_number;//��ȡ��Ҫ�Ĺ�����Ŀ
		    
		    try{
		    /**
		     * * �Ƽ��Ĺ������
		     * */
		    String[] worker=new String[num];
		    
		   /* //�Ƽ��Ĺ��˸�������ƽ̨�Ĺ��˸���ʱ��������ȫ���Ƽ���ȥ
		    if(num>sortMessage.length){
		    	for(int m=0;m<sortMessage.length;m++){
		    		worker[m]=sortMessage[m][0];
                }
		    	worker=(String[]) change(worker);
		    }
		    
		    else{*/
		    	int l=0;
		        //int j=0;
		        while(j<num){
		        	int index = -1;//��¼����ƽ�������ĵ�һ���±�
		        	//����
		    	    /*System.out.println("!");
		    	    for(int m=0;m<sortMessage.length;m++){
				    	//System.out.println(sortMessage[m][0]);
				    	System.out.println(sortMessage[m][1]);
	                }*/
	                //System.out.println(index);
		    	    for(int i=0;i<sortMessage.length-j;i++){
		    	    	if(Double.parseDouble(sortMessage[i][1])>=average){
		    	    		index=i;
		    		        break;
		    		        }
		    	    	}
		    		/**
		    		 * ƽ��ֵ����4λС��
		    		 */
		    	     DecimalFormat    df   = new DecimalFormat("######0.0000"); 
		    	     average=Double.parseDouble(df.format(average));
			    	 //System.out.println(average);
		    	     
		    	     /*
		    	      * ��������С��ƽ��ֵ
		    	      */
		    	     if(index==-1){
			    	    	break;
			    	    }
		    	    //�޸�ƽ������ֵ
		    	    if(j==0){
		    	    	average=(average*(sortMessage.length)-Double.parseDouble(sortMessage[index][1]))/(sortMessage.length-1);//�޸�ƽ������
		    	    	}
		    	    else{
		    	    	average=(average*(sortMessage.length-j)-Double.parseDouble(sortMessage[index][1]))/(sortMessage.length-1-j);//�޸�ƽ������
		    	    	}
		    	    
		    	    /**
		    	     * * ��ȡ����id
		            */
		    	    
		    	    worker[j]=sortMessage[index][0];
		    	    //ÿ�ν�ȡ���Ĺ���id�Լ������滻�ɺ�����id�Լ�����
                    for(int m=index;m<sortMessage.length-1;m++){
                    	sortMessage[m][0]=sortMessage[m+1][0];
                	    sortMessage[m][1]=sortMessage[m+1][1];
                	    }
                    sortMessage[sortMessage.length-1-j][0]="0";
            	    sortMessage[sortMessage.length-1-j][1]="0";
		    	    //System.out.println(worker[j]);
		    	    j++;//����ȡ���Ĺ���id��Ŀ
		    	    }
		        //}
		        
		        /*
		         * �δѡ���Ĺ���id
		         */
		        int v=0;
		        for(;j<num;j++){
		        	worker[j]=sortMessage[v][0];
		        	v++;
		        }
		    return worker;
		    }catch (Exception e){
		    	System.out.println("�����Ƽ�ʧ��");
		    	}
		    return null;
}
	/**
	 * 
	 * ȥ�������пյ��ַ���
	 * 
	 */
	
	private Object[] change(String[] strings){    
		ArrayList<String> tmp = new ArrayList<String>();      
		for(String str:strings){             
			if(str!=null && str.length()!=0){  
				tmp.add(str);            
				}        
			}        
		strings = tmp.toArray(new String[0]); 
		return strings;
		} 
	
	/**
	 * �øĽ���ð�����򷨶Թ��˵�������������
	 */
	/** 
	     * �Ľ����ð�������㷨��ʵ�֣� 
	     *  
	     * @param list 
	     *            ����������� 
	     * @author csc 
	     */  
	 private static String[][] bubbleSort(String[][] list) {  
		 boolean isSorted = true;
		 for (int i = 1; i < list.length && isSorted; i++) {
			 isSorted = false;  
		     //System.out.print(i + " ");   
		     for (int j = 0; j < list.length-i; j++) {
		    	 if (Double.parseDouble(list[j][1]) > Double.parseDouble(list[j+1][1])) {
		    		 double temp = Double.parseDouble(list[j][1]);  
		    		 int id=Integer.parseInt(list[j][0]);
		    		 
		             list[j][1]= list[j + 1][1];  
		             list[j + 1][1] = Double.toString(temp); 
		             
		             list[j][0]= list[j + 1][0];  
		             list[j + 1][0] =Integer.toString(id);
		             
		             isSorted = true;  
		             }
		    	 }
		     }
		 return list;
		 }  

	
	 /**
    *
    * ���˵�׼ȷ�Լ���
    * 
    */
   
	private double WokerAccuracy(double[][] wm){
   	
   	double s=0;//��¼����ѡ��Ĵ�����ʵ����ͬ���ۻ�����ֵ
   	double sum=0;//��¼���˻ش�����������ۻ�����ֵ
   	double wa=0;//���˵�׼ȷ��
   	for(int i=0;i<wm.length;i++){
   		for(int j=0;j<wm.length;j++){
   			sum=sum+wm[i][j];
   			if(i==j){
   				s=s+wm[i][j];
   			}	
   		}
   	}
   	wa=s/sum;//����׼ȷ�ʵļ���
   	DecimalFormat    df   = new DecimalFormat("######0.00"); 
   	wa=Double.parseDouble(df.format(wa));
   	return wa;	
   }
   
   /**
	 * 
	 * ���ݹ���id�����ݿ��ж�ȡ����ģ�ͣ���ù���׼ȷ��
	 * 
	 */
	private ArrayList ReadWm(ArrayList<String> wm) {
		// TODO Auto-generated method stub
		  ArrayList<Object> list = new ArrayList<Object>();
		  JSONArray jsonArray = new JSONArray();//����json����������
		  Object w =new double[wm.toArray().length][wm.toArray().length];//
		  Object[] a =wm.toArray();
		  String[] workmodel=new String[a.length];//��������ת��Ϊ�ַ����Ĺ���ģ�;���
		  for(int i=0;i<a.length;i++){
			  workmodel[i]=a[i].toString();
			  jsonArray.add(workmodel[i]);//������ģ�ʹ���json������
			  }
		  
		  /**
		   * ��json�����еõ�����ģ�;��󣬸��ݸþ�����㹤�˵�׼ȷ��
		   */
		  for(int z=0;z<workmodel.length;z++){
			  w= jsonArray.get(z);
			  
			  //����
			  //System.out.println(w);
		      int n = 0;
		      for(int i=0;i<w.toString().length();i++){
		    	  if(w.toString().charAt(i)==(']'))
				  n++;//�����жϹ���ģ�͵Ĵ�С
		    	  }
		      double[][] m=new double[n-1][n-1];//���չ���ģ�;���
		      double qua=0;//���˵�׼ȷ��
		      JSONArray arr=JSONArray.fromObject(w);
		      
		      //����
		      //System.out.println(arr);
		      /**
		       * ��json�����ж�ȡ�����ݿ��еĹ���ģ�;���
		       */
		     
		      int j=0;
		      for(Object o:arr){
		    	  JSONArray a1=(JSONArray) o;
		    	  for(int i=0;i<a1.size();i++){
		    		  m[j][i]=Double.parseDouble((String) a1.get(i));//��ȡ���ݿ��й���ģ��
		    		  }
		    	  j++;
		    	  }
		      qua=WokerAccuracy(m);//���ü��㹤�������������õ�����׼ȷ�� 
		      list.add(qua);
		  }
		 return  list;
		  }

@Override
public Timestamp getTakenDeadline(Integer worker_number, Integer times, List<WorkerInfo> workerInfos,
		RequesterTaskInfo requesterInfos) {
	// TODO Auto-generated method stub
	Date date =new Date();
	Timestamp time=new Timestamp(date.getTime()+1000*60*20);
	return time;
}
}
