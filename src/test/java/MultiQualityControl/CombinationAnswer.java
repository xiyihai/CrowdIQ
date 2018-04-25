package MultiQualityControl;

import java.util.ArrayList;
import java.util.List;

public class CombinationAnswer {
	
	private int num;//�����Ƽ��Ĺ��˵ĸ���
	int n=1;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList FinalAnswer(ArrayList answer){
		ArrayList combineAnswer =new ArrayList();//������¼��������
		while(n<=answer.size()){
			combineAnswer=recomAnswer(answer,n);
			n++;
		}
		return combineAnswer;
	}
	@SuppressWarnings("rawtypes")
	public ArrayList recomAnswer(ArrayList answer,int n){
		num=n;
		ArrayList combineWorker =new ArrayList();//������¼����������	
		
		/**
		 * �ó����д𰸵�������
		 **/
		//�������Ĵ���ϢΪ�գ��򷵻ؿ�ֵ
		if(answer==null||answer.size()==0){  
            return null ; 
        }  
        ArrayList list=new ArrayList();  
        combineWorker=combine(answer,0,n,list); 
		return combineWorker;
	}
	/**
	 * ��������������
	 * @param cs
	 * @param begin
	 * @param number
	 * @param list
	 * @return
	 * 
	 */
	//���ַ������е�begin���ַ���ʼ��ѡnumber���ַ�����list��  
	//private int k=0;//�ü�¼��ϵ�
	@SuppressWarnings({ "rawtypes" })
	static ArrayList<ArrayList> list1=new ArrayList<ArrayList>();
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public  ArrayList<ArrayList> combine(List cs,int begin,int number,ArrayList list){  
		if(number==0){  
        	if(list.size()==num){
        		list1.add((ArrayList) list.clone());
    		    //System.out.println(list1);

        	}
            return list1;  
        }  
        if(begin==cs.size()){ 
        	if(list.size()==num){
        		list1.add((ArrayList) list.clone());
        	}
            return list1;  
        }  
        list.add(cs.get(begin));  
        combine(cs,begin+1,number-1,list);  
        list.remove(cs.get(begin));  
        return combine(cs,begin+1,number,list);  
    } 
}
