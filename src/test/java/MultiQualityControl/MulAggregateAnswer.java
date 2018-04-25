package MultiQualityControl;

import java.util.ArrayList;

public interface MulAggregateAnswer {
	//public void AggresionAnswer(ArrayList<String> answer,ArrayList<String> wm,ArrayList<String> pro);//���뺯�����������
	public ArrayList<String> WriteWm();//���غ��������ظ��º�Ĺ���ģ�;���
	public ArrayList<String> AggFinalAnswer();//���غ��������ػ�ô𰸵����ս��
	public void AggresionMulAnswer(ArrayList<ArrayList<String>> answer,ArrayList<String> wm,ArrayList<String> pro);

}
