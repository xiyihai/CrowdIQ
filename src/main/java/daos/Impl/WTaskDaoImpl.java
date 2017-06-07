package daos.Impl;

import java.util.List;

import daos.Interface.WTaskDao;
import domains.WTask;

public class WTaskDaoImpl extends BaseDaoImpl<WTask> implements WTaskDao {

	@Override
	public List<WTask> getByWidTid(String userID, String taskID) {
		// TODO Auto-generated method stub
		return find("from WTask w where w.worker_id = ?0 and w.task_id = ?1", Integer.valueOf(userID), Integer.valueOf(taskID));
	}

	@Override
	public List<WTask> getByTid(String taskID) {
		// TODO Auto-generated method stub
		return find("from WTask w where w.task_id = ?0", Integer.valueOf(taskID));
	}

	@Override
	public List<WTask> getByWid(String userID) {
		// TODO Auto-generated method stub
		return find("from WTask w where w.worker_id = ?0", Integer.valueOf(userID));
	}

	@Override
	public List<WTask> getByWidState(String userID, Integer state) {
		// TODO Auto-generated method stub
		return find("from WTask w where w.worker_id = ?0 and w.state = ?1", Integer.valueOf(userID), state);
	}

}
