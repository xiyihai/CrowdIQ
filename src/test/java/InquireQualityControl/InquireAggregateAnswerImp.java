package InquireQualityControl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.json.JSONArray;

public class InquireAggregateAnswerImp implements InquireAggregateAnswer{
    
	private ArrayList<String> valueWm;//���պ������յĹ���ģ�;���洢��һ��ArrayList<String>������
	private ArrayList<String> valueAggFinalAnswer;//���պ������յĹ��˻ش�����Ĵ𰸷ֲ����洢��һ��ArrayList<String>������
	private ArrayList<String> valuePro; //���պ������յ�topK��������ʣ��洢��һ��ArrayList<String>������
	private ArrayList<String> reWm;//���غ������صĸ��º�Ĺ���ģ�;��󣬴洢��һ��ArrayList<String>������
	private ArrayList<String> reFinalAnswer;//���غ������ص����մ𰸣��洢��һ��ArrayList<String>������

	
	/**
     * �����Ϲ���
     */
	

	public void AggresionAnswer(ArrayList<String> answer,ArrayList<String> wm,ArrayList<String> pro){
		/**
		 * ���뺯��
		 * �����������
		 */
		this.valueWm = wm;
		this.valueAggFinalAnswer = answer;
		this.valuePro = pro;
	}
	private InputArgute AggreAnswer(){
		
		/**
		 * ��ȡ�������
		 */
		ArrayList<String> answer =new ArrayList<String>();
		ArrayList<String> wm =new ArrayList<String>();
		ArrayList<String> pro =new ArrayList<String>();
		answer=this.valueAggFinalAnswer;
		wm=this.valueWm;
		pro=this.valuePro;
		
		/**
		 * �������ж�ȡ���˻ش�Ĵ����
		 */
		Object[] arr =answer.toArray();
	    String[] wanswer=new String[arr.length];//�洢���˻ش�Ĵ����
	     for(int i=0;i<arr.length;i++){
	    	 wanswer[i]=arr[i].toString();
	    	 //����
	    	 //System.out.println(wanswer[i]);
	     }
	     
		ArrayList<Object> wmlist = new ArrayList<Object>();//���ڽ��չ��˵��������Լ���չ��Ĺ���ģ�;���
		int j=0;//���ƹ��˴���Ϊ��յĸ���
		
		
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
	     
	    //������ʹ�һ��
	     double sum=0;
	     for(int i=0;i<protopa.length;i++){
	    	 sum=sum+protopa[i];
	     }
	     for(int i=0;i<protopa.length;i++){
	    	 protopa[i]=protopa[i]/sum;
	     }
		
	     String [] fill = new String[wanswer.length];//���е����������ݣ�δ�������ƶ�ƥ��ģ�,wanswer�������˵�id�Լ����˵Ĵ�
		 String[] fillAnswer = null;//�����չ��ѡ�����������������ƶ�ƥ��֮��Ľ����
		 ArrayList<Object> list = new ArrayList<Object>();//���ڽ����������չ��ѡ������Լ�ѡ��ÿ��ѡ���������
		 /**
			 * ��ȡ��յ����ݴ洢��fill��
			 */
			//top-k��ѡ����ѡ�������A��B��C��D......
			 String[]  topAnswer=new String[protopa.length];//top-k��ѡ����ķֲ����
			 double[] priorProb;//�𰸵��������

			 /**
			  * �����������
			  */
			 if(protopa.length==0){
				//��ȡ��յ����ݴ洢��fill��
				 for(int i=0;i<wanswer.length;i++){
					   fill[j]=wanswer[i];//����������л�ȡ��յĲ���
					   //����
					   //System.out.println(fill[j]);
					   j++;
				}
			 }
			 /**
			  * ѡ�������ͻ������+ѡ�������
			  */
			 else{
			 topAnswer[0]="A";
			 for(int i=1;i<topAnswer.length;i++){
				 topAnswer[i]=String.valueOf((char) (topAnswer[i-1].charAt(0)+1));//top-k�е�ѡ�����finalAnswer��
				 //����
				 //System.out.println(topAnswer[i]);
				 }
			 //��ȡ��յ����ݴ洢��fill��
			 List<String> toplist=Arrays.asList(topAnswer);
			 for(int i=0;i<wanswer.length;i++){
				if(!toplist.contains(wanswer[i])){
				   fill[j]=wanswer[i];//����������л�ȡ��յĲ���
				   //����
				   //System.out.println(fill[j]);
				   j++;
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
		
		/**
		 * ����4λС��
		 */
	     DecimalFormat    df   = new DecimalFormat("######0.0000"); 
	     for(int i=0;i<priorProb.length;i++){
	    	 
	    	 priorProb[i]=Double.parseDouble(df.format(priorProb[i]));
	    	//����
	    	//System.out.println(priorProb[i]+"�������");
	     }
	     //System.out.println();
		
	     /**
		    * �������Ҫ���������ʵĴ𰸴����finalAnswer��,����top-k�е�ѡ���Լ���չ���������ѡ��
		    */
		     String[]  finalAnswer=new String[protopa.length+fillAnswer.length];//������Ҫ���������ʵĴ����
			 for(int i=0;i<finalAnswer.length;i++){
				 if(i<protopa.length){
					 finalAnswer[i]=topAnswer[i];//top-k�е�ѡ�����finalAnswer��
				 }
				 else{
					 finalAnswer[i]=fillAnswer[i-protopa.length];//���������չ��ѡ�����finalAnswer�� 
				 }
				 //����
				 //System.out.println(finalAnswer[i]+"��");
			 }

		 /**
			 * ����ÿ�����˵�׼ȷ��
			 */
		     wmlist=ReadWm(wm,priorProb.length);//��ȡ���˵������Լ���չ��Ĺ���ģ�;���
		     //����
		     //System.out.println(wmlist);
		     
		     /**
		      * ���������ʣ����д𰸾���
		      */
		     double[] posteriorProb=new double[finalAnswer.length];
		     posteriorProb=AggAnswerInquire(wanswer.length,wanswer,finalAnswer,wmlist,priorProb);
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
		     inputArgute.m=wanswer.length;
		     inputArgute.finalAnswer=finalAnswer;
		     inputArgute.posteriorProb=posteriorProb;
		     inputArgute.wanswer=wanswer;
		     inputArgute.wmlist=wmlist;
		     
		     return  inputArgute;
		 	
	}
	
	
	/**
	 * 
	 * ����Inquire�Ļ�������ģ�͵Ĵ𰸾ۺ��㷨���������ʣ����д𰸾ۺϾ���
	 * 
	 */
	//����ȫ�ֱ������ڵݹ鷽����ʹ��
	private int z=0;//���Ƶݹ���ڵĲ���������������𰸶�����֮�������ݹ鷽��
	private int l=0;//���z���õ����˵���������
	private double[] AggAnswerInquire(int lenght,String[] wanswer,String[] finalAnswer,ArrayList<Object> wmlist,double[] priorProb){
		
		/** 
	      * ����Inquire�Ĵ𰸾ۺ��㷨���д�����
	      */
		 double[]  conditionProb=new double[finalAnswer.length];//�洢�����и��ʵ�ֵ
		 double[]  proProb=new double[finalAnswer.length];//�洢��ĸ����͹�ʽ�еĸ������ӵĸ���ֵ
		 double[] posteriorProb=new double[conditionProb.length];//�洢���д𰸵ĺ�����ʵ�ֵ
		 
		 //�ݹ麯���ĳ��ڣ������������д𰸺������ݹ麯��
		 if(z==lenght){
			 z=0;//�ͷ�ȫ�ֱ���
             l=0;
	    	 return priorProb;	
		 }
	     else{
	    	 for(int i=0;i<conditionProb.length;i++){
	    		 conditionProb[i]=1;
	    		 proProb[i]=1;
				 
				 /**
				  * ��������е���������
				  */
				 //1)���˻ش�Ĵ�����ʵ����ͬ
				 if(finalAnswer[i].equals(wanswer[z])){
					 conditionProb[i]=conditionProb[i]*Double.parseDouble(wmlist.get(z+l).toString())*priorProb[i];//���˴�����ʵ����ͬʱ
					 }
				 
				 //2)���˻ش�Ĵ�����ʵ�𰸲���ͬ
				 else { 
					 conditionProb[i]=conditionProb[i]*(1-Double.parseDouble(wmlist.get(z+l).toString()))*(priorProb[i]/(1-priorProb[i]));
					 }
				 } 
	    	 
	    	 double pProb = 0;//�洢��ʽ�з�ĸ��ֵ
	    	 
	    	 /**
	    	  * ����������
	    	  * */
	    	 for(int i=0;i<conditionProb.length;i++){
	    		 pProb=pProb+conditionProb[i];
	    		 }
	    	 for(int i=0;i<conditionProb.length;i++){
	    		 posteriorProb[i]=conditionProb[i]/pProb;
	    		 }
	    	 
	    	 /**
	    	  * * ����6λС��
	    	  * */
	    	 DecimalFormat    df1   = new DecimalFormat("######0.000000"); 
		     for(int i=0;i<posteriorProb.length;i++){
		    	 
		    	 //����
		    	 //System.out.println(posteriorProb[i]);
		    	 posteriorProb[i]=Double.parseDouble(df1.format(posteriorProb[i]));
		     }
		    
		    	 z++;
		    	 l++;
		    	 return AggAnswerInquire(lenght,wanswer,finalAnswer,wmlist,posteriorProb);//�ݹ���ú���֪�����й��˴𰸶�������
		    	 }
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
     * ��������ַ��������ƶȺ�������ʾ�����ַ����Ƿ�Ϊ��ͬ�ı�ʶ��
     * 
     */
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

		InquireAggregateAnswerImp.threshold = threshold;

	}

	
	private float getThreshold() {

		return threshold;

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
		String[] wanswer=inputWm.wanswer;
		
		// TODO Auto-generated method stub
		ArrayList<String> wlist= new ArrayList<String>();//���ظ��º�Ĺ���ģ�;���
		
		/**
		 * ���¹���ģ�;���
		 */

	     for(int k=0;k<wanswer.length;k++){
	    	 int index=0;
			 for(int z = 0;z<finalAnswer.length;z++){         
			        if(finalAnswer[z].equals(wanswer[k])){             
			        	index=z;//���ҹ��˴�������ѡ���е�λ�ã��Ӷ��ɻ�ù��˻ش������𰸵��������        
			         }   
			     }
			 double[][] wm=(double[][]) wmlist.get((k*2+1));//��ȡ����ģ�;���
			 //���ݹ��˻ش�Ĵ�Ϊindex,���¹���ģ�;�����index�е�����ֵ
			 for(int k2=0;k2<wm[1].length;k2++){
				 if(k2<posteriorProb.length){
					 wm[index][k2]=wm[index][k2]+posteriorProb[k2];//�𰸸�������ԭ����ģ�;����Сʱ
					 }
				 else{
					 wm[index][k2]=wm[index][k2]+0;//�𰸸���С��ԭ����ģ�;����Сʱ
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
		return ProValueWm(wlist);
		}
	
    
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

