package daos.Impl;

import java.util.List;

import daos.Interface.WorkerTestTaskDao;
import domains.WorkerTestTask;

public class WorkerTestTaskDaoImpl extends BaseDaoImpl<WorkerTestTask> implements WorkerTestTaskDao {

	@Override
	public List<WorkerTestTask> findByWid(String userID) {
		// TODO Auto-generated method stub
		return find("from WorkerTestTask as w where w.worker_id = ?0", Integer.valueOf(userID));
	}

	@Override
	public List<WorkerTestTask> findByWidTid(String userID, String taskID) {
		// TODO Auto-generated method stub
		return find("from WorkerTestTask as w where w.worker_id = ?0 and w.testtask_id = ?1", Integer.valueOf(userID), Integer.valueOf(taskID));
	}

	@Override
	public List<WorkerTestTask> findTaskbyState(String userID, int state) {
		// TODO Auto-generated method stub
		return find("from WorkerTestTask as w where w.worker_id = ?0 and w.state = ?1", Integer.valueOf(userID), state);
	}

}
