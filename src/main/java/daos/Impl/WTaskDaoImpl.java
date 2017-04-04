package daos.Impl;

import java.util.List;

import daos.Interface.WTaskDao;
import domains.WTask;

public class WTaskDaoImpl extends BaseDaoImpl<WTask> implements WTaskDao {

	@Override
	public List<WTask> getByWidTid(String userID, String taskID) {
		// TODO Auto-generated method stub
		return find("from WTask w where w.worker_id = ?0 and w.task_id = ?1", userID, taskID);
	}

	@Override
	public List<WTask> getByTid(String taskID) {
		// TODO Auto-generated method stub
		return find("from WTask w where w.task_id = ?0", taskID);
	}

}