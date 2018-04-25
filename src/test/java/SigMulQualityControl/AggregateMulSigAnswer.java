package SigMulQualityControl;

import java.util.ArrayList;
import java.util.List;

public interface AggregateMulSigAnswer {
	//public void AggresionAnswer(ArrayList<String> answer,ArrayList<String> wm,ArrayList<String> pro);//���뺯�����������
	public ArrayList<String> WriteWm();//���غ��������ظ��º�Ĺ���ģ�;���
	public ArrayList<String> AggFinalAnswer();//���غ��������ػ�ô𰸵����ս��
	public void AggresionMulSigAnswer(ArrayList<ArrayList<String>> answer,ArrayList<String> wm,ArrayList<String> pro);

}
