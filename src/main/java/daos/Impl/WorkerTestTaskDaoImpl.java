package daos.Impl;

import java.util.List;

import daos.Interface.WorkerTestTaskDao;
import domains.WorkerTestTask;

public class WorkerTestTaskDaoImpl extends BaseDaoImpl<WorkerTestTask> implements WorkerTestTaskDao {

	@Override
	public List<WorkerTestTask> findByWid(String userID) {
		// TODO Auto-generated method stub
		return find("from WorkerTestTask as w where w.worker_id = ?0", userID);
	}

}
