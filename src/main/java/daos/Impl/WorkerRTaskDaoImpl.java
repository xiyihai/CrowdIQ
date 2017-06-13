package daos.Impl;

import java.util.List;

import daos.Interface.WorkerRTaskDao;
import domains.WorkerRTask;

public class WorkerRTaskDaoImpl extends BaseDaoImpl<WorkerRTask> implements WorkerRTaskDao {

	@Override
	public List<WorkerRTask> findByWidDeadline(String userID, String deadline) {
		// TODO Auto-generated method stub
		return findBySql("select * from worker_recommendtask where worker_id = "+Integer.valueOf(userID)+" and date_format(taken_deadline,'%Y-%m-%d %H:%i') >= '"+deadline+"'", WorkerRTask.class);
	}

	@Override
	public List<WorkerRTask> findByDeadline(String deadline) {
		// TODO Auto-generated method stub
		return findBySql("select * from worker_recommendtask where date_format(taken_deadline,'%Y-%m-%d %H:%i') <= '"+deadline+"'", WorkerRTask.class);
	}

	@Override
	public List<WorkerRTask> findByTid(String taskID) {
		// TODO Auto-generated method stub
		return find("from WorkerRTask as w where w.task_id = ?0", Integer.valueOf(taskID));
	}

	@Override
	public List<WorkerRTask> findByTidWid(String userID, String taskID) {
		// TODO Auto-generated method stub
		return find("from WorkerRTask as w where w.task_id = ?0 and w.worker_id = ?1", Integer.valueOf(taskID), Integer.valueOf(userID));
	}

}
