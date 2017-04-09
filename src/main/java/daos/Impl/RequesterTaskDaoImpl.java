package daos.Impl;

import java.util.List;

import daos.Interface.RequesterTaskDao;
import domains.RequesterTask;

public class RequesterTaskDaoImpl extends BaseDaoImpl<RequesterTask> implements RequesterTaskDao {

	@Override
	public List<RequesterTask> getBy2ID(String userID, String taskID) {
		// TODO Auto-generated method stub
		return find("from RequesterTask as t where t.requester_id = ?0 and t.task_id = ?1", userID, taskID);
	}

	@Override
	public List<RequesterTask> getByRID(String userID) {
		// TODO Auto-generated method stub
		return find("from RequesterTask as t where t.requester_id = ?0", userID);
	}

}
