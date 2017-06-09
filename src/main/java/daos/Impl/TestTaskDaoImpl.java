package daos.Impl;

import java.util.List;

import daos.Interface.TestTaskDao;
import domains.TestTask;

public class TestTaskDaoImpl extends BaseDaoImpl<TestTask> implements TestTaskDao {

	@Override
	public List<TestTask> getByID(String taskID) {
		// TODO Auto-generated method stub
		return find("from TestTask as t where t.test_id = ?0", Integer.valueOf(taskID));
	}


}
