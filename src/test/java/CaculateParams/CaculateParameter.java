package CaculateParams;

public interface CaculateParameter {
	double getDiffDegree(Integer[] selects,Integer[] showing,Double top_k);
	double getEachReward(Integer[] selects,Integer[] showing,Double top_k);
	int getWorkNumber(String[] workerc);
}
