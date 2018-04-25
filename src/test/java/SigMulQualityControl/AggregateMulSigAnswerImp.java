package SigMulQualityControl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import SigMulQualityControl.AggregateMulSigAnswer;
import net.sf.json.JSONArray;

public class AggregateMulSigAnswerImp implements AggregateMulSigAnswer{
    
	private ArrayList<String> valueWm;//���պ������յĹ���ģ�;���洢��һ��ArrayList<String>������
	private ArrayList<ArrayList<String>> valueAggFinalAnswer;//���պ������յĹ��˻ش�����Ĵ𰸷ֲ����洢��һ��ArrayList<String>������
	private ArrayList<String> valuePro; //���պ������յ�topK��������ʣ��洢��һ��ArrayList<String>������
	private ArrayList<String> reWm;//���غ������صĸ��º�Ĺ���ģ�;��󣬴洢��һ��ArrayList<String>������
	private ArrayList<String> reFinalAnswer;//���غ������ص����մ𰸣��洢��һ��ArrayList<String>������

	
	/**
     * �����Ϲ���
     */
	

	public void AggresionMulSigAnswer(ArrayList<ArrayList<String>> answer,ArrayList<String> wm,ArrayList<String> pro){
		/**
		 * ���뺯��
		 * �����������
		 */
		this.valueWm = wm;
		this.valueAggFinalAnswer = answer;
		this.valuePro = pro;
	}
	@SuppressWarnings({ })
	private InputArgute AggreAnswer(){
		
		/**
		 * ��ȡ�������
		 */
		ArrayList<ArrayList<String>> answer =new ArrayList<ArrayList<String>>();
		ArrayList<String> wm =new ArrayList<String>();
		ArrayList<String> pro =new ArrayList<String>();
		answer=this.valueAggFinalAnswer;
		wm=this.valueWm;
		pro=this.valuePro;
		
		double[] priorProb;//�𰸵��������
	     
	     
		String [] fill = new String[answer.size()];//���е����������ݣ�δ�������ƶ�ƥ��ģ�,wanswer�������˵�id�Լ����˵Ĵ�
		String[] fillAnswer = null;//�����չ��ѡ�����������������ƶ�ƥ��֮��Ľ����
		ArrayList<Object> list = new ArrayList<Object>();//���ڽ����������չ��ѡ������Լ�ѡ��ÿ��ѡ���������
		ArrayList<Object> wmlist = new ArrayList<Object>();//���ڽ��չ��˵��������Լ���չ��Ĺ���ģ�;���
		/**
		 * �������л�ȡtopK�ĸ���ֵ
		 * 
		 */
		Object[] top =pro.toArray();
	    double[] protopa=new double[top.length];//top k�ĸ��ʷֲ����
	    String[] pro1=new String[top.length];
	     for(int i=0;i<top.length;i++){
	    	 pro1[i]=(String) top[i];
	    	 
	    	 if(pro1[i].length()<=2){
	    		 protopa[i]=(double)1/(double)(top.length);
	    	 }
	    	 else{
	    		 protopa[i]=Double.parseDouble(pro1[i].substring(2,pro1[i].length()));
	    	 }
	    	 //����
	    	 //System.out.println(protopa[i]);
	     }

	     /**
	      * ���˴𰸼�׼ȷ��Ԥ����
	      */
	     
		/**
		 * 
		 * ��ȡ��յ����ݴ洢��fill��
		 * 
		 */
		//top-k��ѡ����ѡ�������A��B��C��D......
		 String[]  topAnswer=new String[protopa.length];//top-k��ѡ����ķֲ����
		 
		 int k=0;
		 for(int i=0;i<answer.size();i++){
			 for(int m=0;m<answer.get(i).size();m++){
				 if(protopa.length==0){
					 //��ȡ��յ����ݴ洢��fill��
					 fill[k]=answer.get(i).get(m).toString();
					 k++;
				 }
				 else{
					 topAnswer[0]="A";
					 for(int i1=1;i1<topAnswer.length;i1++){
						 topAnswer[i1]=String.valueOf((char) (topAnswer[i1-1].charAt(0)+1));//top-k�е�ѡ�����finalAnswer��
						 //����
						 //System.out.println(topAnswer[i]);
					 }
					 
					 //��ȡ��յ����ݴ洢��fill��
					 List<String> toplist=Arrays.asList(topAnswer);
					 if(!toplist.contains(answer.get(i).get(m).toString())){
						   fill[k]=answer.get(i).get(m).toString();//����������л�ȡ��յĲ���
						   //����
						   //System.out.println(fill[j]);
						   k++;
						}
				 }
			 }
		 }
		
		/**
		 * ��ȡ�������չ����ѡ����������Լ�ѡ��ÿ����չ��ѡ���������
		 */
		int[] h = null;//�洢ѡ���������չ����ÿ��ѡ���������
		fill=(String[]) change(fill);//�˳��ո�
		int[] h1=new int[fill.length];
		list=exChose(fill,h1);
		h=(int[]) list.get(1);//ѡ��ÿ����չ��ѡ���������
		//����
		/*for(int i=0;i<h.length;i++){
			System.out.println(h[i]);
		}*/
		fillAnswer=(String[]) list.get(0);//��ȡ�����չ��ѡ����
		//����
		/*for(int i=0;i<fillAnswer.length;i++){
			System.out.println(fillAnswer[i]);	
		}*/
		
		/**
		 * �����������
		 */
		 priorProb=Pro(protopa,h);//����ÿ���𰸵�������ʣ����������չ��ѡ������������
		 //�����������
		 /*for(int i=0;i<priorProb.length;i++){
			 System.out.println(priorProb[i]+"&&");
		 }*/
		
		/**
		 * ������չ������������ѡ����֮������ƶ�
		 */
		double[][] similary = new double[fillAnswer.length][fillAnswer.length];//�������չ��ѡ��������ƶ�
		for(int i=0;i<fillAnswer.length;i++){
			for(int l=i+1;l<fillAnswer.length;l++){
				similary[i][l]=getDistance(fillAnswer[i],fillAnswer[l]);
				similary[l][i]=similary[i][l];//A��B�����ƶ���B��A�����ƶ���ͬ
			}
			similary[i][i]=1;//��������ƶ�Ϊ1
		}
		//����
		/*for(int i=0;i<similary.length;i++){
			for(int j1=0;j1<similary[0].length;j1++){
				System.out.println(similary[i][j1]);
			}
			System.out.println();
		}*/
			
	   /**
	    * �������Ҫ���������ʵĴ𰸴����finalAnswer��,����top-k�е�ѡ���Լ���չ���������ѡ��
	    */
	     String[] finalAnswer=new String[topAnswer.length+fillAnswer.length];
	     //ArrayList<ArrayList<String>> definalAnswer=new ArrayList<ArrayList<String>>();
	     for(int i=0;i<(topAnswer.length+fillAnswer.length);i++){
			 if(i<protopa.length){
				 finalAnswer[i]=topAnswer[i];//top-k�е�ѡ�����finalAnswer��
			 }
			 else{
				 finalAnswer[i]=fillAnswer[i-protopa.length];//���������չ��ѡ�����finalAnswer�� 
			 }
	     }
		 
			
		/**
		 * ����4λС��
		 */
		 DecimalFormat    df   = new DecimalFormat("######0.0000"); 
		 //System.out.println(priorProb.length);
		 for(int i=0;i<priorProb.length;i++){
			 priorProb[i]=Double.parseDouble(df.format(priorProb[i]));
		    //����
		    //System.out.println(priorProb[i]+"##");
			 }
		     //System.out.println();
		         
		/**
		 * ����ÿ�����˵�׼ȷ��
		 */
		 wmlist=ReadWm(wm,finalAnswer.length);//��ȡ���˵������Լ���չ��Ĺ���ģ�;���
		 //����
		 //System.out.println(wmlist);
		 ArrayList<String> testanswer=new ArrayList<String>();
		 testanswer=getSingleAnswer(answer,wmlist,finalAnswer,priorProb);
		 String[] workanswer=new String[testanswer.size()];
		 
		 for(int i=0;i<testanswer.size();i++){
			 workanswer[i]=testanswer.get(i).toString();
			 //System.out.println(workanswer[i]+"%%");
		 }
		 //System.out.println(lastwm);
		
		     
		/**
		 * ���������ʣ����д𰸾���
		 */
		 double[] posteriorProb=new double[finalAnswer.length];
		 posteriorProb=AggAnswer(topAnswer,workanswer,fillAnswer,finalAnswer,lastwm,similary,priorProb);
		 //����
		 /*for(int i=0;i<posteriorProb.length;i++){
		   System.out.println(posteriorProb[i]);
			 }*/
		     
		 /**
		  * ��ģ�����еı�����ֵ
		  * ����������еõ���Ӧ�Ĳ���ֵ
		  */
		 
		 InputArgute inputArgute=new InputArgute();
	     inputArgute.n=finalAnswer.length;
	     inputArgute.m=workanswer.length;
	     inputArgute.finalAnswer=finalAnswer;
	     inputArgute.posteriorProb=posteriorProb;
	     inputArgute.wanswer=answer;
	     inputArgute.wmlist=wmlist;
		 
		     
		 return  inputArgute;
		 	
	}
	
	/**
     * ���˴𰸼�׼ȷ��Ԥ����
     */
	
	private ArrayList<Double> lastwm=new ArrayList<Double>();
	@SuppressWarnings("unused")
	private ArrayList<String> getSingleAnswer(ArrayList<ArrayList<String>> answer,ArrayList<Object> wm,String[] finalAnswer,double[]priobPro){
		ArrayList<Double> newwm=new ArrayList<Double>();
		ArrayList<String> newanswer=new ArrayList<String>();

		
		//�������
		for(int k=0;k<answer.size();k++){
			
			/**
			 * ��¼ÿ������ѡ��Ĵ𰸼��𰸵��������
			 */
			double[] pro=new double[answer.get(k).size()];
			String[] ans=new String[answer.get(k).size()];
			
			for(int i=0;i<answer.get(k).size();i++){
				for(int j=0;j<finalAnswer.length;j++){
					if(finalAnswer[j].equals(answer.get(k).get(i))){
						pro[i]=priobPro[j];
						//System.out.println(pro[i]+"%$");
						//ans[i]=(String) finalAnswer.get(j);
						newanswer.add((String) finalAnswer[j]);
						break;
					}
				}
			}
			
			/**
			 * Ԥ����ÿ�����˵Ķ���𰸹���
			 */
			double answerone=1;
			for(int i=1;i<pro.length;i++){
				answerone=answerone*pro[i]/pro[0];
			}
			double wmone=Math.pow((Double.parseDouble(wm.get(k*2).toString())/answerone), ((double)1/answer.get(k).size()));
			newwm.add(wmone);
			for(int i=1;i<pro.length;i++){
				newwm.add(wmone*pro[i]/pro[0]);
			}
		}
		
		/**
		 * ��0-�������ֵӳ�䵽0-1��
		 */
		for(int i=0;i<newwm.size();i++){
			//System.out.println(newwm.get(i)+"**");
			double w=1-Math.pow(Math.E, (-1*Double.parseDouble(newwm.get(i).toString())));
			lastwm.add(w);
		}
		
		//System.out.println(lastwm+"%%");
		//System.out.println(newanswer+"!!");
		
		return newanswer;
		
	}

	
	/**
	 * PS���Լ��������ʣ����ñ�Ҷ˹��ʽ���д𰸾ۺϾ���
	 */
	private double[] AggAnswer(String[] topAnswer,String[] wanswer,String[] fillAnswer,String[] finalAnswer,ArrayList<Double> wmlist,double[][] similary,double[] priorProb){
		/**
	      * 
	      * ���ݱ�Ҷ˹��ʽ���д�����
	      * 
	      */
		 
		 double[]  conditionProb=new double[finalAnswer.length];//�洢��Ҷ˹��ʽ�������������ʵ�ֵ
		 List<String> filllist=Arrays.asList(fillAnswer);
		 for(int i=0;i<conditionProb.length;i++){
			 conditionProb[i]=1;
			 for(int k=0;k<wanswer.length;k++){
				 //�жϹ��˻ش��ѡ������һ�����Ӷ��ɵõ��������
				 int index=i;
				 for(int z = 0;z<finalAnswer.length;z++){         
				        if(finalAnswer[z].equals(wanswer[k])){             
				        	index=z;//���ҹ��˴�������ѡ���е�λ�ã��Ӷ��ɻ�ù��˻ش������𰸵��������        
				         }   
				     } 
				 //����
				 //System.out.println(index);
				 //����
				 //System.out.println(wanswer.length);
				 
				 /**
				  *
				  * ���ݱ�Ҷ˹��ʽ����������
				  * 
				  */
				 
				 /**
				  * ���㱴Ҷ˹��ʽ�����е���������
				  */
				 //1)���˻ش�Ĵ�����ʵ����ͬ
				 if(finalAnswer[i].equals(wanswer[k])){
					
					 conditionProb[i]=conditionProb[i]*Double.parseDouble(wmlist.get(k).toString());//���˴�����ʵ����ͬʱ
					 //����
					 //System.out.println(1);
					 }
				 
				 //2)��ʵ��Ϊѡ�����е�һ�������˻ش�Ĵ�����ʵ�𰸲�ͬ���ҹ��˻ش�Ĵ�Ҳ�������������չ��ѡ����
				 else if(!finalAnswer[i].equals(wanswer[k]) && !filllist.contains(wanswer[k]) && !filllist.contains(finalAnswer[i])){
				    
					 conditionProb[i]=conditionProb[i]*(1-Double.parseDouble(wmlist.get(k).toString()))*priorProb[i]*(1/(1-priorProb[index]));
					 //����
					 //System.out.println(2);
					 }
				 
				 //3)��ʵ��Ϊѡ�����е�һ�������ǹ��˻ش�Ĵ������������չ��ѡ����
				 else if(filllist.contains(wanswer[k]) && !filllist.contains(finalAnswer[i])){
					 
					 int f=0;
					 double sim=0;
					 double P=0;
					 for(int m=0;m<fillAnswer.length;m++){
						 //���˻ش�Ĵ������лش�������Ĵ𰸵����ƶ�С��ĳһ��ֵ
						 if(m!=(index-topAnswer.length) && similary[m][index-topAnswer.length]<0.8){
							 f++;
							 }
						 
						 //���˻ش�Ĵ���ĳЩ�˻ش�������Ĵ𰸵����ƶȴ���ĳһ��ֵ
						 else if(similary[m][index-topAnswer.length]>=0.8){
							 sim=sim+1-similary[m][index-topAnswer.length];//�����빤�˻ش�Ĵ�֮�����ƶȴ���ĳһ��ֵ�������
							 P=P+priorProb[m+topAnswer.length];//���й��˻ش�Ĵ�֮�����ƶȴ���ĳһ��ֵ�Ĵ𰸵��������
							 }
						 }
					 
					 /**
					  * �ж�������������е����֣���ת����ͬ�ļ��㹫ʽ
					  */
					 //���㹤�˻ش�Ĵ������лش�������Ĵ𰸵����ƶ�С��ĳһ��ֵ
					 if(f==(fillAnswer.length-1)){
						 conditionProb[i]=conditionProb[i]*(1-Double.parseDouble(wmlist.get(k).toString()))*priorProb[i]*(1/(1-priorProb[index])); 
						 //����
						 //System.out.println(3);	 
						 }
					//���㹤�˻ش�Ĵ���ĳЩ�˻ش�������Ĵ𰸵����ƶȴ���ĳһ��ֵ
					 else{
						 conditionProb[i]=conditionProb[i]*((1-Double.parseDouble(wmlist.get(k).toString()))*(1-sim))*(priorProb[i]/(1-P));
						 //����
						 //System.out.println(4);
						 }
					 }
				 
				 
				 //5)��ʵ�����������չ����ѡ�����һ���������˻ش�Ĵ𰸲�Ϊ�������չ����ѡ����
				 else if(filllist.contains(finalAnswer[i])&& !filllist.contains(wanswer[k])){
					 
					 conditionProb[i]=conditionProb[i]*(1-Double.parseDouble(wmlist.get(k).toString()))*priorProb[i]*(1/(1-priorProb[index]));
					 //���� 
					 //System.out.println(5);
					 }
				 
				 //6)��ʵ�����������չ����ѡ�����һ�������˻ش�Ĵ�������ʵ�𰸲�ͬ���������չ����ѡ�����е�һ��,������ʵ�����ƶ�С��ĳһ��ֵ
				 else if(filllist.contains(finalAnswer[i])&& !finalAnswer[i].equals(wanswer[k]) &&filllist.contains(wanswer[k])&&similary[i-topAnswer.length][index-topAnswer.length]<0.8){
					 int f=0;
					 double sim=0;
					 double P=0;
					 for(int m=0;m<fillAnswer.length;m++){
						 //���˻ش�Ĵ������лش�������Ĵ𰸵����ƶ�С��ĳһ��ֵ
						 if(m!=(index-topAnswer.length) && similary[m][index-topAnswer.length]<0.8){
							 f++;
							 }
						 //���˻ش�Ĵ���ĳЩ�˻ش�������Ĵ𰸵����ƶȴ���ĳһ��ֵ
						 else if(similary[m][index-topAnswer.length]>=0.8){
							 sim=sim+1-similary[m][index-topAnswer.length];//�����빤�˻ش�Ĵ�֮�����ƶȴ���ĳһ��ֵ�������
						     P=P+priorProb[m+topAnswer.length];//���й��˻ش�Ĵ�֮�����ƶȴ���ĳһ��ֵ�Ĵ𰸵��������
						     }
						 }
					 /**
					  * �ж�������������е����֣���ת����ͬ�ļ��㹫ʽ
					  */
					 //���㹤�˻ش�Ĵ������лش�������Ĵ𰸵����ƶ�С��ĳһ��ֵ
					 if(f==(fillAnswer.length-1)){
						 conditionProb[i]=conditionProb[i]*(1-Double.parseDouble(wmlist.get(k).toString()))*(priorProb[i]/(1-priorProb[index])); 
						 //����
						 //System.out.println(6);
					     }
					 //���㹤�˻ش�Ĵ���ĳЩ�˻ش�������Ĵ𰸵����ƶȴ���ĳһ��ֵ
					 else{
						  conditionProb[i]=conditionProb[i]*((1-Double.parseDouble(wmlist.get(k).toString()))*(1-sim))*(priorProb[i]/(1-P)); 
						  //����
						  //System.out.println(7);
					      }
					 }
				 
				 //8)�������Ǵ�Ϊ�������չ��ѡ�����е�һ�������˻ش�Ĵ�����ʵ�𰸲�ͬ�������������չ�Ĵ𰸣��ҹ��˻ش�Ĵ�����ʵ�𰸵����ƶȴ���ĳһ��ֵ
				 else if(filllist.contains(finalAnswer[i])&& !finalAnswer[i].equals(wanswer[k]) && filllist.contains(wanswer[k]) && similary[i-topAnswer.length][index-topAnswer.length]>=0.8){
					 double sim=Double.parseDouble(wmlist.get(k).toString())*similary[i-topAnswer.length][index-topAnswer.length];
					 double dissim=(1-Double.parseDouble(wmlist.get(k).toString()))*(1-similary[i-topAnswer.length][index-topAnswer.length]);
					 conditionProb[i]=conditionProb[i]*(sim+dissim);
					 //����
					 //System.out.println(8);
					 }
				 }
			 //����
			 //System.out.println(conditionProb[i]);
			 }
		 //System.out.println();
		 double pProb = 0;//�洢��Ҷ˹��ʽ��ĸ��ֵ
		 double[] posteriorProb=new double[conditionProb.length];//�洢���д𰸵ĺ�����ʵ�ֵ
		 
		 /**
		  * ���㱴Ҷ˹��ʽ�ķ�ĸ
		  */
		 //��Ҷ˹��ʽ��ĸ�ļ������
		 for(int i=0;i<conditionProb.length;i++){
			 pProb=pProb+conditionProb[i]*priorProb[i];
			 }
		 //����
		 //System.out.println(pProb);
		 
		 /**
		  * ��Ҷ˹��ʽ���ս��
		  */
		 //���ݱ�Ҷ˹��ʽ���м���
		 for(int i=0;i<conditionProb.length;i++){
			 posteriorProb[i]=conditionProb[i]*priorProb[i]/pProb;
		 }
		 
		 /**
			 * ����6λС��
			 */
		     DecimalFormat    df1   = new DecimalFormat("######0.000000"); 
		     for(int i=0;i<posteriorProb.length;i++){
		    	 
		    	 //����
		    	 //System.out.println(posteriorProb[i]);
		    	 posteriorProb[i]=Double.parseDouble(df1.format(posteriorProb[i]));
		     }          
		 return posteriorProb;	
	}
	
	
	/**
	 * 
	 * ��ȡtop K�ĸ���ֵ
	 * 
	 */
	
	@SuppressWarnings("unused")
	private double[] Topkpro(String protoa){
		JSONArray jsonArray = new JSONArray();//����json����������
		jsonArray.add(protoa);
		Object w;
		w= jsonArray.get(0);
		//����
		//System.out.println(w);
		/**
		 * �ж�top k��k��ֵΪ����
		 */
		int n = 0;
		for(int i=0;i<w.toString().length();i++){
			if(w.toString().charAt(i)==(',')){
				n++;//����topK������k�ĸ���
				}
			}
		double[] protopa=new double[n+1];//�������top K����ֵ������
		JSONArray arr=JSONArray.fromObject(w);
		for(int i=0;i<arr.size();i++){
			protopa[i]=Double.parseDouble((String) arr.get(i));//��ȡtopK����ֵ
			}  
	     for(int i=0;i<protopa.length;i++){
	        //����
	    	//System.out.println(protopa[i]);
	     }
		 
		return protopa;	
		}
	
	/**
	 * 
	 * �������չѡ�񲢷���ѡ��ÿ��ѡ���������
	 * 
	 */
	
	private  int z1=0;//h���±�
	@SuppressWarnings({ "unused" })
	private ArrayList<Object> exChose(String[] fill,int[] h){
		//System.out.println(z1);
		/*for(int i=0;i<fill.length;i++){
			System.out.println(fill[i]);

		}*/
		/*for(int i=0;i<h.length;i++){
			System.out.println(h[i]);

		}*/
		ArrayList<Object> list = new ArrayList<Object>();
		int [] newData = new int[h.length+1]; //�洢ѡ���������չ����ÿ��ѡ���������
		if(fill.length==0){
	    	newData[0]=0;
		}
		else{
		    newData = Arrays.copyOfRange(h, 0, fill.length); //ȥ��0ֵ
		}
		list.add(fill);
		list.add(newData);
		int[] h1= new int[fill.length];//�洢ѡ���������չ����ÿ��ѡ���������
		float sim;//���ڴ洢�����������֮������ƶ�
		String[] fillAnswer = new String[fill.length];//�����չ��ѡ�����������������ƶ�ƥ��֮��Ľ����   
		int i=z1;
		
		/**
		 * �����ݹ���������
		 */
		//��������ϵ�ѡ�����ֻ��һ����ʱ�򷵻ؽ��
		if(fill.length==1){
			if(i==0){
				newData[0]=1;//�ж�������Ƿ�ֻ��һ������
			}
			fillAnswer[0]=fill[0];
			z1=0;
			return list;
		}
		//û�й���ѡ���������лش�ʱ���ؽ��
		if(fill.length==0){
			z1=0;
			return list;
		}
		//�ж������һ����յ����ݺ󷵻ؽ��
		if(i==fill.length){
			z1=0;
			return list;
		}
			
		
		/**
		 * ���µݹ�����еĲ���
		 */
		for(int l=0;l<z1+1;l++){
			fillAnswer[l]=fill[l];
		}
		for(int l=0;l<z1+1;l++){
			h1[l]=h[l];
		}
		int z=0;//sim���±�
		int z2=z1;//fillanswer���±�
		h[z1]=1;
		//h[z1+1]=1;

		for(int j=i+1;j<fill.length;j++){
			sim=getDistance(fill[i],fill[j]);//�����������֮������ƶ�
			if(sim==1){
				fillAnswer[z1]=fill[i];
				h[z1]=h[z1]+1;
			}
			else{
				z2++;
				fillAnswer[z1]=fill[i];
				fillAnswer[z2]=fill[j];
			}
		}
		z1++;
		fillAnswer=(String[]) change(fillAnswer);//�������пյ��ַ���ȥ��
		return exChose(fillAnswer,h);//�ݹ����
		
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
	 * 
	 * ����д𰸵����ƶȼ���(Jaro-Winkler Distance)
	 * 
	 */
	
	private static float threshold = 0.7f;

	private int[] matches(String s1, String s2) {
		String max, min;
		if (s1.length() > s2.length()) {

			max = s1;//�����ַ����нϳ����ַ���
			min = s2;//�����ַ����н϶̵��ַ���

		} else {

			max = s2;
			min = s1;

		}
		int range = Math.max(max.length() / 2 - 1, 0);//ƥ�䴰�ڼ��㹫ʽ
		int[] matchIndexes = new int[min.length()];
		Arrays.fill(matchIndexes, -1);
		boolean[] matchFlags = new boolean[max.length()];
		int matches = 0;
		//��ƥ�䴰����ƥ�������ַ��Ƿ���ͬ
		for (int mi = 0; mi < min.length(); mi++) {

			char c1 = min.charAt(mi);//��ȡmin�е�mi���ַ�
			//ѭ������������ƥ��ķ�Χ��ƥ�䴰�ڷ�Χ��
			for (int xi = Math.max(mi - range, 0), xn = Math.min(
					mi + range + 1, max.length()); xi < xn; xi++) {

				if (!matchFlags[xi] && c1 == max.charAt(xi)) {

					matchIndexes[mi] = xi;
					matchFlags[xi] = true;//��ʾ�����ַ�ƥ��
					matches++;//�����ַ�����ͬ�ַ��ĸ���
					break;

				}

			}

		}
		char[] ms1 = new char[matches];
		char[] ms2 = new char[matches];
		for (int i = 0, si = 0; i < min.length(); i++) {

			if (matchIndexes[i] != -1) {
				ms1[si] = min.charAt(i);//�洢���ַ���ƥ����ַ�
				si++;

			}

		}
		for (int i = 0, si = 0; i < max.length(); i++) {

			if (matchFlags[i]) {

				ms2[si] = max.charAt(i);//�洢���ַ���ƥ����ַ�
				si++;

			}

		}
		int transpositions = 0;
		for (int mi = 0; mi < ms1.length; mi++) {

			if (ms1[mi] != ms2[mi]) {

				transpositions++;//��λ����Ŀ

			}

		}
		int prefix = 0;
		for (int mi = 0; mi < min.length(); mi++) {

			if (s1.charAt(mi) == s2.charAt(mi)) {

				prefix++;//�����ַ���������ͬ�ַ��ĸ���

			} else {

				break;

			}

		}
		return new int[] { matches, transpositions / 2, prefix, max.length() };

		
	}
	
	
    /**
     * 
     * ��������ַ��������ƶȺ�������ʾ�����ַ����Ƿ�Ϊ��ͬ�ı�ʶ��
     * 
     */
	
	private float getDistance(String s1, String s2) {

		int[] mtp = matches(s1, s2);
		float m = (float) mtp[0];//�����ַ�����ͬ�ַ��ĸ���
		if (m == 0) {

			return 0f;

		}
		float j = ((m / s1.length() + m / s2.length() + (m - mtp[1]) / m)) / 3;
		float jw = j < getThreshold() ? j : j + Math.min(0.1f, 1f / mtp[3])
				* mtp[2] * (1 - j);
		
		return jw;

	}

	
	private  void setThreshold(float threshold) {

		AggregateMulSigAnswerImp.threshold = threshold;

	}

	
	private float getThreshold() {

		return threshold;

	}
	
	
	/**
	 * 
	 * ����������ʲ����ش������������ֵ
	 * 
	 */
	
	private double[] Pro(double[] protopa,int h[])  {
    
    	int k=protopa.length;//k��ѡ����ĸ�����wnumber�������չ�������ĸ���
    	double sum=0;//ѡ����ĳ�ʼ������ʺ�
    	int m1 = 0;//������¼�ش�����������
    	int number;//�����ж��Ƿ��й���ѡ���������лش�
    	if(h[0]==0){
    		number=0;//�����û���˻ش�
    	}
    	else{
    		number=h.length;
    	}
    	double priorProb[]=new double[k+number+1];//�洢�𰸵��������
    	for(int j=0;j<protopa.length;j++){
    		
    		sum=sum+protopa[j];
    		
    	}
    	for(int z=0;z<h.length;z++){
    		
    		m1=m1+h[z];
    		
    	}
    	if(m1==0){
			 priorProb[k]=(float)number/(float)(k+number);//�����û���˻ش����������Ϊ0
		 }
    	for(int i=0;i<k+number;i++) {//������ʼ������
    		
    		if(i<k){
    		     priorProb[i]=(float) ( ((float)k/(double)(k+number))*(protopa[i]/(float)sum));//ѡ������������

    		}
    		else{

    			 priorProb[i]=(((float)number/(float)(k+number)))*((float)h[i-k]/(float)m1);//�������������	
    		}
    	}
    	
    	//������������˻ش���ȥ����������е�0ֵ
    	if(number!=0){
    		priorProb = Arrays.copyOfRange(priorProb, 0,k+number); //ȥ��0ֵ
    	}

    	return priorProb;//�����������	
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
		return wa;	
    }

    
    /**
     * 
     * DealData�ӿڵ�ʵ��
     * 
     */
    
    /**
	 * 
	 * ���ݹ���id�����ݿ��ж�ȡ����ģ�ͣ���ù���׼ȷ��
	 * 
	 */
	private ArrayList<Object> ReadWm(ArrayList<String> wm,int num) {
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
		      double Q=0;//���˵�׼ȷ��
		      JSONArray arr=JSONArray.fromObject(w);
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
		      Q=WokerAccuracy(m);//���ü��㹤�������������õ�����׼ȷ�� 
		      /**
  	           * �޸Ĺ���ģ�;���Ĵ�С
  	           */
		      int length;//�޸ĺ�Ĺ���ģ�;��������ֵ
		      if(m.length>num){
		    	  length=m.length;//ԭ��������ֵ���ں�ѡ�𰸵Ĵ�Сʱ���¾�������ֵ��ԭ��������ֵ��ͬ
		    	  }
		      else{
		    	  length=num;//ԭ��������ֵС�ں�ѡ�𰸵Ĵ�Сʱ���¾��������ֵ��չΪ��ѡ�𰸸���
		    	  }
		      double[][] m1=new double[length][length];//���չ���ģ�;���
		      for(int r=0;r<length;r++){
		    	  for(int t=0;t<length;t++){
		    		  if((r<m.length)&&(t<m.length)){
		    			  m1[r][t]=m[r][t];
		    			  }
		    		  else{
		    			  m1[r][t]=0;
		    			  }
		    		  }
		    	  }
		      /**
		       * * ����4λС��
			   */
		      DecimalFormat    df   = new DecimalFormat("######0.0000"); 
		      Q=Double.parseDouble(df.format(Q));
		      list.add(Q);//������׼ȷ�Դ���list
		      //System.out.println(Q);
		      list.add(m1);//���޸ĺ�Ĺ���ģ�;������list
		      }
		  
		  return list;
		  }
	
	/**
	 * 
	 * ���ݵõ��ĺ������ֵ���������ݿ��еĹ���ģ�͵Ĺ���
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public  ArrayList<String> WriteWm() {
		
		InputArgute inputWm=new InputArgute();//����ģ����Ķ���
		inputWm=AggreAnswer();//���ô��������õ�����ֵ
		
		/**
		 * ��ȡ�����и������Ե�ֵ
		 */
		String[] finalAnswer=inputWm.finalAnswer;
		double[] posteriorProb=inputWm.posteriorProb;
		List wmlist=inputWm.wmlist;
		ArrayList<ArrayList<String>> wanswer=inputWm.wanswer;
		
		// TODO Auto-generated method stub
		ArrayList<String> wlist= new ArrayList<String>();//���ظ��º�Ĺ���ģ�;���
		
		/*for(int i=0;i<finalAnswer.length;i++){
			System.out.println(finalAnswer[i]);

		}*/
		/**
		 * ���¹���ģ�;���
		 */

	     for(int k=0;k<wanswer.size();k++){
	    	 int index=0;
			 double[][] wm=(double[][]) wmlist.get((k*2+1));//��ȡ����ģ�;���

			 for(int z = 0;z<finalAnswer.length;z++){         
			        if(wanswer.get(k).contains(finalAnswer[z])){             
			        	index=z;//���ҹ��˴�������ѡ���е�λ�ã��Ӷ��ɻ�ù��˻ش������𰸵��������        
			         }   
			     
			 //���ݹ��˻ش�Ĵ�Ϊindex,���¹���ģ�;�����index�е�����ֵ
			 for(int k2=0;k2<wm[1].length;k2++){
				 if(k2<posteriorProb.length){
					 wm[index][k2]=wm[index][k2]+posteriorProb[k2];//�𰸸�������ԭ����ģ�;����Сʱ
					 }
				 else{
					 wm[index][k2]=wm[index][k2]+0;//�𰸸���С��ԭ����ģ�;����Сʱ
					 }
				 }
			 }
			 /**
				 * ����6λС��
				 */
			  DecimalFormat    df   = new DecimalFormat("######0.000000"); 
			  for(int i=0;i<wm.length;i++){
				  for(int j=0;j<wm[1].length;j++){
					  wm[i][j]=Double.parseDouble(df.format(wm[i][j]));
					  }
				  }
			  
			 /**
			  * �����º�Ĺ���ģ�;���ת�����ַ��������絽һ��ArrayList<String>������
			  */
	         int row = wm.length;// ��ȡ����row 
	         int col = wm[0].length; // ��ȡ����col 
	         String str="[[";//��Ź���ģ�;�����ַ���
	         String tempStr = null;
	         for (int i = 0; i < row;) {
	        	 int j = 0;
	        	 while(j<col){
	        		 tempStr = String.valueOf(wm[i][j]);
	        		 if(j<col-1){
	        			 str = str +'\"'+ tempStr +'\"'+ ",";
	        			 }
	        		 else{
	        			 str = str +'\"'+ tempStr +'\"';
	        			 }
	        		 j++;
	        		 }
	        	 if(i<row-1){
	        		 i++;
	        		 str=str+"],[";
	        		 }
	        	 else{
	        		 i++;
	        		 str=str+"]]";
	        		 }         
	        	 
	        	 }
	         wlist.add(str);
	         
	         //����
	         //System.out.println(wlist);
	         }
	    // System.out.println(wlist);
	    //finalAnswer.clear();
	    lastwm.clear();
		return ProValueWm(wlist);
		}
	
	/**
	 * ���ݺ�����ʵõ����յĴ�
	 */
	public ArrayList<String> AggFinalAnswer(){
		InputArgute inputFinalAnswer=new InputArgute();//����ģ����Ķ���
		inputFinalAnswer=AggreAnswer();//���ô��������õ�����ֵ
	
		/**
		 * ��ȡ�����е�����ֵ
		 */
		String[] finalAnswer=inputFinalAnswer.finalAnswer;
		double[] posteriorProb=new double[inputFinalAnswer.posteriorProb.length];
		posteriorProb=inputFinalAnswer.posteriorProb;
		
		 ArrayList<String> aggreAnswer=new ArrayList<String>();//���ص����մ�
	     int pos=0;//��¼���ش𰸵�λ��
	     //Ѱ�Һ�������е����ֵ��λ��
	     for(int i=1;i<posteriorProb.length;i++){
	    	 if(posteriorProb[pos]<posteriorProb[i]){
	    		 pos=i;
	    		 }
	    	 }
	     aggreAnswer.add(finalAnswer[pos]);
		 lastwm.clear();
		 return ProValueAggFinalAnswer(aggreAnswer);
		 }	
	
	/**
	 * ���ز���
	 * @param param
	 * @return
	 */
	private ArrayList<String> ProValueWm(ArrayList<String> param){
		this.reWm = param;
        return this.reWm;
        }
	private ArrayList<String> ProValueAggFinalAnswer(ArrayList<String> param){
        this.reFinalAnswer = param;
        return this.reFinalAnswer;
        }
	}