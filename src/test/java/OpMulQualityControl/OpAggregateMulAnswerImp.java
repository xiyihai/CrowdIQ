package OpMulQualityControl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.json.JSONArray;

public class OpAggregateMulAnswerImp implements OpAggregateMulAnswer{
    
	private ArrayList<String> valueWm;//���պ������յĹ���ģ�;���洢��һ��ArrayList<String>������
	private ArrayList<ArrayList<String>> valueAggFinalAnswer;//���պ������յĹ��˻ش�����Ĵ𰸷ֲ����洢��һ��ArrayList<String>������
	private ArrayList<String> valuePro; //���պ������յ�topK��������ʣ��洢��һ��ArrayList<String>������
	private ArrayList<String> reWm;//���غ������صĸ��º�Ĺ���ģ�;��󣬴洢��һ��ArrayList<String>������
	private ArrayList<String> reFinalAnswer;//���غ������ص����մ𰸣��洢��һ��ArrayList<String>������

	
	/**
     * �����Ϲ���
     */
	

	public void AggresionMulAnswer(ArrayList<ArrayList<String>> answer,ArrayList<String> wm,ArrayList<String> pro){
		/**
		 * ���뺯��
		 * �����������
		 */
		this.valueWm = wm;
		this.valueAggFinalAnswer = answer;
		this.valuePro = pro;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	int l;
	@SuppressWarnings("unchecked")
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
						   //System.out.println(fill[k]);
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
	     ArrayList finalAnswer=new ArrayList();
	     ArrayList<ArrayList<String>> definalAnswer=new ArrayList<ArrayList<String>>();
	     ArrayList<ArrayList<String>> definalAnswertest=new ArrayList<ArrayList<String>>();
	     for(int i=0;i<(topAnswer.length+fillAnswer.length);i++){
			 if(i<protopa.length){
				 finalAnswer.add(topAnswer[i]);//top-k�е�ѡ�����finalAnswer��
			 }
			 else{
				 finalAnswer.add(fillAnswer[i-protopa.length]);//���������չ��ѡ�����finalAnswer�� 
			 }
	     }
	     
	     //System.out.println(finalAnswer+"test");
		 CombinationAnswer com=new CombinationAnswer();
		 definalAnswertest=com.FinalAnswer(finalAnswer);
		 //System.out.println(definalAnswertest+"test");
		 //����
		 //System.out.println(definalAnswer.size());
		 /*for(int v=0;v<definalAnswer.size();v++){
			 System.out.println(definalAnswer.get(v).toString()); 
		 }*/
		 
		 
		/**
		 * �����������
		 */
		 priorProb=Pro(protopa,fillAnswer);//����ÿ���𰸵�������ʣ����������չ��ѡ������������
	
		/**
		 * ����4λС��
		 */
		 DecimalFormat    df   = new DecimalFormat("######0.0000000000"); 
		 //System.out.println(priorProb.length);
		 for(int i=0;i<priorProb.length;i++){
			 priorProb[i]=Double.parseDouble(df.format(priorProb[i]));
		    //����
		    //System.out.println(priorProb[i]);
			 }
		     //System.out.println();
		  
		 
		/**
		 * ��ȡ�Ż���Ĵ�������
		 */
		 definalAnswer=GetFinalAnswer(definalAnswertest,answer,priorProb,similary,fillAnswer);//��������Ĵ����
		 double[] depriorProb=new double[definalAnswer.size()];//�洢����������������ֵ
		 
		 //��ȡ��������Ĵ���ϵ��������
		 //System.out.println(definalAnswer);
		 //System.out.println(prior);
		 for(int i=0;i<prior.size();i++){
			 depriorProb[i]=Double.parseDouble(prior.get(i).toString());
			 //����
			 //System.out.println(depriorProb[i]);
		 }
		 //����
		 //System.out.println(definalAnswer);
		 
		/**
		 * ����ÿ�����˵�׼ȷ��
		 */
		 wmlist=ReadWm(wm,definalAnswer.size());//��ȡ���˵������Լ���չ��Ĺ���ģ�;���
		 //����
		 //System.out.println(wmlist);
		     
		/**
		 * ���������ʣ����д𰸾���
		 */
		 double[] posteriorProb=new double[definalAnswer.size()];
		 posteriorProb=AggAnswer(answer,fillAnswer,definalAnswer,wmlist,similary,depriorProb);
		   //����
		 /*for(int i=0;i<posteriorProb.length;i++){
		   System.out.println(posteriorProb[i]);
			 }*/
		 
		 definalAnswertest.clear();//�ͷ�ȫ�ֱ���
		 prior.clear();
		 
		 /**
		  * ��ģ�����еı�����ֵ
		  * ����������еõ���Ӧ�Ĳ���ֵ
		  */
		 InputArgute inputArgute=new InputArgute();
		 inputArgute.n=finalAnswer.size();
		 inputArgute.m=answer.size();
		 inputArgute.finalAnswer=finalAnswer;
		 inputArgute.refinalAnswer=definalAnswer;
		 inputArgute.posteriorProb=posteriorProb;
		 inputArgute.wanswer=answer;
		 inputArgute.wmlist=wmlist;
		 
		     
		 return  inputArgute;
		 	
	}
	
	/**
	 * ��ȡ���Ż�˳��
	 */
	@SuppressWarnings({"rawtypes" })
	private ArrayList prior=new ArrayList();//���������������
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ArrayList<ArrayList<String>> GetFinalAnswer(ArrayList<ArrayList<String>> definalAnswertest,ArrayList<ArrayList<String>> answer,double[] pro,double[][] similary,String[] fillAnswer){
		ArrayList prop=new ArrayList();//�洢�������֪ʶ
		 for(int m=0;m<pro.length;m++){
			 prop.add(pro[m]);
		 }
		 //test
		 //System.out.println(prop.size());
		 //System.out.println(definalAnswertest+"test");
		 //System.out.println(answer+"test1");

		 int j=0;//�����ж����Ƿ��������Ҫ��
		 int length=definalAnswertest.size();
		 //System.out.println(definalAnswertest.size()+"%%");

		 ArrayList<ArrayList<String>> definalAnswer = new ArrayList<ArrayList<String>>();
			 for(int i=0;i<definalAnswertest.size();i++){
				 if(answer.contains(definalAnswertest.get(i))){
					 definalAnswer.add((ArrayList<String>) definalAnswertest.get(i).clone());
					 prior.add(prop.get(i).toString());
					 definalAnswertest.remove(i);
					 prop.remove(i);
					 j++;
					 }
				 }
			 //System.out.println(definalAnswertest);
			 if(fillAnswer.length>0){
				 for(int i=0;i<definalAnswertest.size();i++){
					 for(int k=0;k<answer.size();k++){
						 if(!definalAnswertest.get(i).equals((answer.get(k)))&&definalAnswertest.get(i).size()==answer.get(k).size()){//����ѡ��𰸸�����ͬ
							 int answerindex1=0;
					         int wanswerindex1=0;
					         for(int z = 0;z<fillAnswer.length;z++){
					        	 if(definalAnswertest.get(i).size()==1){
					        		 answerindex1=-1;
					    		     break;
					    		     }
					    	     else if(definalAnswertest.get(i).get(definalAnswertest.get(i).size()-1).toString().equals(fillAnswer[z])){
					    		     answerindex1=z;//���ҹ��˴�������ѡ���е�λ�ã��Ӷ��ɻ�ù��˻ش������𰸵��������        
					                 break;
					                 } 
					             else{
					                 answerindex1=-1;
					                 }
					        	 }
					         for(int z = 0;z<fillAnswer.length;z++){
					        	 if(answer.get(k).size()==1){
					        		 wanswerindex1=-1;
					    		     break;
					    	         }
					        	 else if(answer.get(k).get(answer.get(k).size()-1).toString().equals(fillAnswer[z])){
					    		     wanswerindex1=z;//���ҹ��˴�������ѡ���е�λ�ã��Ӷ��ɻ�ù��˻ش������𰸵��������        
					                 break;
					                 }
					             else{
					                 wanswerindex1=-1;
					                 }
					    	     }
					         if(answerindex1==-1||wanswerindex1==-1||similary[answerindex1][wanswerindex1]<0.8){//��ղ������ƶ�С����ֵ
					    	     break;
					    	     }
					         else{//��ղ������ƶȴ�������ֵ
						         int n=0;
						         for(int z = 0;z<definalAnswertest.get(i).size()-1;z++){
						        	 if(definalAnswertest.get(i).get(z).toString().equals(answer.get(k).get(z).toString())){
						        		 n=n+1;//���ҹ��˴�������ѡ���е�λ�ã��Ӷ��ɻ�ù��˻ش������𰸵��������
						        		 }
						        	 }
						         if(n==definalAnswertest.get(i).size()-1){//ǰ׺������ȫƥ��
						        	 definalAnswer.add((ArrayList<String>) definalAnswertest.get(i).clone());
						        	 prior.add(prop.get(i).toString());
						    	     definalAnswertest.remove(i);
							         prop.remove(i);
							         j++;
							         }
						         }
					         }
						 }
					 }
				 }
			 //while(j<length){
			 int index=0;
			 for(int i=1;i<prop.size();i++){
				 if(Double.parseDouble(prop.get(i).toString())> Double.parseDouble(prop.get(index).toString())){
					 index=i;
					 }
				 }
			 definalAnswer.add((ArrayList<String>) definalAnswertest.get(index).clone());
			 prior.add(prop.get(index).toString());
			 definalAnswertest.remove(index);
			 prop.remove(index);
			 //j++;
			 //}
		return definalAnswer;
		
	}
	
	/**
	 * PS���Լ��������ʣ����ñ�Ҷ˹��ʽ���д𰸾ۺϾ���
	 */
	private double[] AggAnswer(ArrayList<ArrayList<String>> wanswer,String[] fillAnswer,ArrayList<ArrayList<String>> finalAnswer,ArrayList<Object> wmlist,double[][] similary,double[] priorProb){
		
		/**
	      * 
	      * ���ݱ�Ҷ˹��ʽ���д�����
	      * 
	      */
		 
		 double[]  conditionProb=new double[finalAnswer.size()];//�洢��Ҷ˹��ʽ�������������ʵ�ֵ
		 List<String> filllist=Arrays.asList(fillAnswer);

		 //System.out.println(conditionProb.length);
		 for(int i=0;i<conditionProb.length;i++){
			 conditionProb[i]=1;
				 
			 //System.out.println(i);
			 //System.out.println(finalAnswer.size());
			 for(int k=0;k<wanswer.size();k++){
				 
				 
				/**
				 * �жϹ��˻ش��ѡ������һ�����Ӷ��ɵõ��������
				 */
				 int index=i;
				 for(int z = 0;z<finalAnswer.size();z++){         
				        if(finalAnswer.get(z).equals(wanswer.get(k))){             
				        	index=z;//���ҹ��˴�������ѡ���е�λ�ã��Ӷ��ɻ�ù��˻ش������𰸵��������        
				         }   
				     } 
				 
				 
				 /**
				  * ���㹤��k�����������д𰸼�����ƶ����
				  */
				 int answerindex=0;//��¼��ʵ��λ��
				 int wanswerindex=0;//��¼���˴�λ��
				 double sim=0;//�洢��ʵ�������ƴ𰸼�����ƶȺ�
				 double p=0;//�洢����ʵ�����ƵĴ𰸵�������ʺ�

				 for(int m=0;m<finalAnswer.size();m++){
					 //ֻ�лش�𰸸�����ͬ���п�������
					 if(finalAnswer.get(m).size()==wanswer.get(k).size()){
						 if(fillAnswer.length>0){
							 for(int z = 0;z<fillAnswer.length;z++){
								 if(finalAnswer.get(m).size()==1){
					        		 answerindex=-1;
					    		     break;
					    		     }
								 else if(finalAnswer.get(m).get(finalAnswer.get(m).size()-1).toString().equals(fillAnswer[z])){
									 answerindex=z;//���Ҵ�����д�����ݵ�λ��  
								     break;
								     } 
							     else{
						             answerindex=-1;//��������û����д�����ݣ���λ��Ϊ-1
						             }
								 }
						     for(int z = 0;z<fillAnswer.length;z++){
						    	 if(wanswer.get(k).size()==1){
					        		 wanswerindex=-1;
					    		     break;
					    		     }
						    	 else if(wanswer.get(k).get(wanswer.get(k).size()-1).toString().equals(fillAnswer[z])){             
				        	         wanswerindex=z;//���ҹ��˴�����д�����ݵ�λ��  
				        	         break;
				                     }
				                 else{
						             wanswerindex=-1;//�������˴���û����д�����ݣ���λ��Ϊ-1
						             }
						         }
						     if(wanswerindex==-1||answerindex==-1||similary[answerindex][wanswerindex]<0.8){
						    	 break;//������д���ݲ����ƣ��������˴�ѭ��
						    	 }
				             else if(similary[answerindex][wanswerindex]>=0.8){//��ղ������ƶȴ�����ֵ ���ж�ǰ׺�����Ƿ�����
				        	     int n1=0;
							     for(int z1 = 0;z1<finalAnswer.get(m).size()-1;z1++){
							    	 if(finalAnswer.get(m).get(z1).toString().equals(wanswer.get(k).get(z1).toString())){
							    		 n1=n1+1;//���ҹ��˴�������ѡ���е�λ�ã��Ӷ��ɻ�ù��˻ش������𰸵��������
							    		 }
							    	 }
							     if(n1==finalAnswer.get(m).size()-1){//ǰ׺������ȫƥ��
							    	 sim=sim+1-similary[answerindex][wanswerindex];//�����빤�˻ش�Ĵ�֮�����ƶȴ���ĳһ��ֵ�������
								     p=p+priorProb[m];//���й��˻ش�Ĵ�֮�����ƶȴ���ĳһ��ֵ�Ĵ𰸵��������
								     }
							     }
						     }
						 }
					 }
				 
				 
				 /**
				  *
				  * ���ݱ�Ҷ˹��ʽ����������
				  * 
				  */
				 
				 /**
				  * ���㱴Ҷ˹��ʽ�����е���������
				  */
				 //1)���˻ش�Ĵ�����ʵ����ͬ
				 if(finalAnswer.get(i).equals((wanswer.get(k)))){

					 conditionProb[i]=conditionProb[i]*Double.parseDouble(wmlist.get(k*2).toString());//���˴�����ʵ����ͬʱ
					 //����
					 //System.out.println(1);
					 }
				 else if(!finalAnswer.get(i).equals((wanswer.get(k)))&&finalAnswer.get(i).size()==wanswer.get(k).size()){//����ѡ��𰸸�����ͬ
					 int answerindex1=0;
					 int wanswerindex1=0;
					 if(fillAnswer.length>0){
						 for(int z = 0;z<fillAnswer.length;z++){
							 if(finalAnswer.get(i).size()==1){
				        		 answerindex=-1;
				    		     break;
				    		     }
							 else if(finalAnswer.get(i).get(finalAnswer.get(i).size()-1).toString().equals(fillAnswer[z])){
								 answerindex1=z;//���ҹ��˴�������ѡ���е�λ�ã��Ӷ��ɻ�ù��˻ش������𰸵��������        
					             break;
					             } 
					         else{
					             answerindex1=-1;
					             }
							 }
						 for(int z = 0;z<fillAnswer.length;z++){
							 if(wanswer.get(k).size()==1){
				        		 wanswerindex=-1;
				    		     break;
				    		     }
							 else if(wanswer.get(k).get(wanswer.get(k).size()-1).toString().equals(fillAnswer[z])){             
					             wanswerindex1=z;//���ҹ��˴�������ѡ���е�λ�ã��Ӷ��ɻ�ù��˻ش������𰸵��������        
					             break;
					             }
					         else{
					             wanswerindex1=-1;
					             }
					         }
					     if(answerindex1==-1||wanswerindex1==-1||similary[answerindex1][wanswerindex1]<0.8){//��ղ������ƶ�С����ֵ
					    	 conditionProb[i]=conditionProb[i]*(1-Double.parseDouble(wmlist.get(k*2).toString()))*priorProb[i]/(1-priorProb[index]);//���˴�����ʵ����ͬʱ
						     //System.out.println(2);
					    	 }
					     else{//��ղ������ƶȴ�������ֵ
					    	 int n=0;
					    	 for(int z = 0;z<finalAnswer.get(i).size()-1;z++){
					    		 if(finalAnswer.get(i).get(z).toString().equals(wanswer.get(k).get(z).toString())){             
						        	n=n+1;//���ҹ��˴�������ѡ���е�λ�ã��Ӷ��ɻ�ù��˻ش������𰸵��������
						        	}
					    		 }
					    	 if(n==finalAnswer.get(i).size()-1){//ǰ׺������ȫƥ��
					    		 double sim1=Double.parseDouble(wmlist.get(k*2).toString())*similary[answerindex1][wanswerindex1];
							     double dissim=(1-Double.parseDouble(wmlist.get(k*2).toString()))*(1-similary[answerindex1][wanswerindex1]);
							     conditionProb[i]=conditionProb[i]*(sim1+dissim);
							     //System.out.println(3);
							     }
					    	 else{//ǰ׺���ֲ���ȫƥ��
					    		 //System.out.println("##"+sim);
								 //System.out.println("@@"+p);
					    		 conditionProb[i]=conditionProb[i]*((1-Double.parseDouble(wmlist.get(k*2).toString()))*(1-sim))*(priorProb[i]/(1-p-priorProb[index]));
							 	 //System.out.println(4);
					    		 }
					    	 }
					     }
					 else{
			    		 conditionProb[i]=conditionProb[i]*((1-Double.parseDouble(wmlist.get(k*2).toString()))*(1-sim))*(priorProb[i]/(1-p-priorProb[index]));
			    		 }
					 }
				 else{//����ѡ��𰸸�������ͬ
					 //System.out.println("&&"+sim);
					 //System.out.println("**"+p);
					 conditionProb[i]=conditionProb[i]*((1-Double.parseDouble(wmlist.get(k*2).toString()))*(1-sim))*(priorProb[i]/(1-p-priorProb[index])); 
					 //����
					 //System.out.println(5);
				 }
			 }
		 }
		 //System.out.println();
		 //double pProb = 0;//�洢��Ҷ˹��ʽ��ĸ��ֵ
		 //double[] posteriorProb=new double[conditionProb.length];//�洢���д𰸵ĺ�����ʵ�ֵ
		 
		 /**
		  * ���㱴Ҷ˹��ʽ�ķ�ĸ
		  */
		 //��Ҷ˹��ʽ��ĸ�ļ������
		 /*for(int i=0;i<conditionProb.length;i++){
			 pProb=pProb+conditionProb[i]*priorProb[i];
			 //System.out.println(conditionProb[i]);
			 }*/
		 //����
		 //System.out.println(pProb);
		 
		 /**
		  * ��Ҷ˹��ʽ���ս��
		  */
		 //���ݱ�Ҷ˹��ʽ���м���
		 /*for(int i=0;i<conditionProb.length;i++){
			 posteriorProb[i]=conditionProb[i]*priorProb[i]/pProb;
	         System.out.println(posteriorProb[i]);
		 }*/
		 //System.out.println();

		 /**
			 * ����6λС��
			 */
		     DecimalFormat    df1   = new DecimalFormat("######0.0000000000"); 
		     for(int i=0;i<conditionProb.length;i++){
		    	 
		    	 conditionProb[i]=Double.parseDouble(df1.format(conditionProb[i]*priorProb[i]));
		    	//����
		    	//System.out.println(conditionProb[i]*priorProb[i]);
		     }   
		 return conditionProb;	
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

	
	@SuppressWarnings("unused")
	private  void setThreshold(float threshold) {

		OpAggregateMulAnswerImp.threshold = threshold;

	}

	
	private float getThreshold() {

		return threshold;

	}
	
	
	/**
	 * 
	 * ����������ʲ����ش������������ֵ
	 * 
	 */
	
	private double[] Pro(double[] protopa,String[] fillAnswer)  {
    
    	int k=protopa.length;//k��ѡ����ĸ�����wnumber�������չ�������ĸ���
    	int m=fillAnswer.length;
    	double sum=0;//ѡ����ĳ�ʼ������ʺ�
    	//int m1 = 0;//������¼�ش�����������
    	//int number;//�����ж��Ƿ��й���ѡ���������лش�
    	double priorProb[]=new double[(int) (Math.pow(2,(k+m))-1)];//�洢�𰸵��������
    	for(int j=0;j<protopa.length;j++){
    		
    		sum=sum+protopa[j];
    		
    	}
    	for(int i=0;i<(Math.pow(2,(k+m))-1);i++) {//������ʼ������
    		
    		if(i<k){
    		     priorProb[i]=(float) ( ((float)k/(double)((Math.pow(2,(k+m))-1)))*(protopa[i]/(float)sum));//ѡ������������
    		}
    		else{

    			 priorProb[i]=((float)1/(float)((Math.pow(2,(k+m))-1)));//�������������	
    		}
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
		      /*int length;//�޸ĺ�Ĺ���ģ�;��������ֵ
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
		    	  }*/
		      /**
		       * * ����4λС��
			   */
		      DecimalFormat    df   = new DecimalFormat("######0.0000"); 
		      Q=Double.parseDouble(df.format(Q));
		      list.add(Q);//������׼ȷ�Դ���list
		      //System.out.println(Q);
		      list.add(m);//���޸ĺ�Ĺ���ģ�;������list
		      }
		  
		  return list;
		  }
	
	/**
	 * 
	 * ���ݵõ��ĺ������ֵ���������ݿ��еĹ���ģ�͵Ĺ���
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public  ArrayList<String> WriteWm() {
		
		InputArgute inputWm=new InputArgute();//����ģ����Ķ���
		inputWm=AggreAnswer();//���ô��������õ�����ֵ
		
		/**
		 * ��ȡ�����и������Ե�ֵ
		 */
		ArrayList<ArrayList<String>> refinalAnswer=inputWm.refinalAnswer;
		ArrayList<String> answer=inputWm.finalAnswer;
		double[] posteriorProb=inputWm.posteriorProb;
		List wmlist=inputWm.wmlist;
		ArrayList<String> worker=this.valueWm;
		ArrayList<ArrayList<String>> wanswer=inputWm.wanswer;
		
		// TODO Auto-generated method stub
		ArrayList<String> wlist= new ArrayList<String>();//���ظ��º�Ĺ���ģ�;���
		
		//System.out.println(wanswer);
		//System.out.println(refinalAnswer);

		/**
		 * ��ȡҪ���µľ����С
		 */
		int max_len=0;//������󳤶�
		
		//��ȡfinalAnswer��λ��
		int max_posterior=0;//��¼���ش𰸵�λ��
	     //Ѱ�Һ�������е����ֵ��λ��
	     for(int i=1;i<posteriorProb.length;i++){
	    	 if(posteriorProb[max_posterior]<posteriorProb[i]){
	    		 max_posterior=i;
	    		 }
	    	 }
	    
	     if(max_len<max_posterior)
	    	 max_len=max_posterior+1;
	     //��ȡ���˻ش�𰸵�λ�����ֵ
	     for(int i=0;i<wanswer.size();i++){
	    	 for(int j=0;j<refinalAnswer.size();j++){
	    		 if(refinalAnswer.get(j).toString().equals(wanswer.get(i).toString())){
	    			 if(max_len<=j)
	    				 max_len=j+1;
	    			 break;
	    		 }
	    	 }
	    	 
	     }
	     //test
	     //System.out.println(max_len+"maxsize");
		/**
		 * ���¾����С
		 */
		  ArrayList<Object> list = new ArrayList<Object>();
		  JSONArray jsonArray = new JSONArray();//����json����������
		  Object w =new double[worker.toArray().length][worker.toArray().length];//
		  Object[] a =worker.toArray();
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
		      /**
	           * �޸Ĺ���ģ�;���Ĵ�С
	           */
		      int length;//�޸ĺ�Ĺ���ģ�;��������ֵ
		      if(m.length>max_len){
		    	  length=m.length;//ԭ��������ֵ���ں�ѡ�𰸵Ĵ�Сʱ���¾�������ֵ��ԭ��������ֵ��ͬ
		    	  }
		      else{
		    	  length=max_len;//ԭ��������ֵС�ں�ѡ�𰸵Ĵ�Сʱ���¾��������ֵ��չΪ��ѡ�𰸸���
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
		      
		      list.add(m1);//���޸ĺ�Ĺ���ģ�;������list
		      }
		
		/**
		 * ���¹���ģ�;���
		 */

	     for(int k=0;k<wanswer.size();k++){
	    	 //System.out.println(wanswer.get(k));
	    	 
			 double[][] wm=(double[][]) list.get((k));//��ȡ����ģ�;���
			 //System.out.println(wm.length+"����");

			 for(int h=0;h<refinalAnswer.size();h++){
	    		 if(refinalAnswer.get(h).toString().equals(wanswer.get(k).toString())){
	    			 wm[h][max_posterior]=wm[h][max_posterior]+1;
	    			 break;
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
	         //test
	         //System.out.println(str);
	         wlist.add(str);
	         
	         //����
	         //System.out.println(wlist);
	         }
	    // System.out.println(wlist);
	    refinalAnswer.clear();
	    /*ArrayList<Object> test=new ArrayList<Object>();
	    test=ReadWm(wlist,answer.size());//��ȡ���˵������Լ���չ��Ĺ���ģ�;���
	    System.out.println(test+"����");*/
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
		ArrayList<ArrayList<String>> finalAnswer=inputFinalAnswer.refinalAnswer;
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
	     //System.out.println(finalAnswer);
	     aggreAnswer.add(finalAnswer.get(pos).toString());
	     finalAnswer.clear();
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
