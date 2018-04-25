package OpMulQualityControl;

import java.util.ArrayList;
import java.util.List;

public class InputArgute {
	public int n;//���������С
	public int m;
    public ArrayList finalAnswer=new ArrayList();//�洢���������صĴ𰸷ֲ������
    public ArrayList<ArrayList<String>> refinalAnswer=new ArrayList<ArrayList<String>>();
    public double[] posteriorProb=new double[n];//�洢���������صĺ�����ʵ�ֵ
    public ArrayList<ArrayList<String>> wanswer;//�洢���������صĹ��˻ش�����Ĵ𰸵����
    public List wmlist;//�洢���������صĻش�����Ĺ��˵�ģ�;���
}
