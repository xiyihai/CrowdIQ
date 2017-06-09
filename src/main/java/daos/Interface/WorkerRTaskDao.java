package daos.Interface;

import java.util.List;

import domains.WorkerRTask;

public interface WorkerRTaskDao extends BaseDao<WorkerRTask> {

	//根据工人id和截止日期，找到可展示的任务id
	List<WorkerRTask> findByWidDeadline(String userID, String deadline);
	
	//根据截止时间，找到过期的taskID
	List<WorkerRTask> findByDeadline(String deadline);
	
	//根据任务ID
	List<WorkerRTask> findByTid(String taskID);
	
	//根据任务ID，工人ID
	List<WorkerRTask> findByTidWid(String userID, String taskID);
	
}
