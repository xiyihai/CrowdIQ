package daos.Interface;

import java.util.List;

import domains.WorkerTestTask;

public interface WorkerTestTaskDao extends BaseDao<WorkerTestTask> {

	//根据工人id，找到对应的测试任务
	List<WorkerTestTask> findByWid(String userID);
	
	//根据工人id，任务id找到对应的字段
	List<WorkerTestTask> findByWidTid(String userID, String taskID);
	
	//查找state的字段,即已经完成的测试任务
	List<WorkerTestTask> findTaskbyState(String userID, int state);
}
