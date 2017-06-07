package daos.Impl;

import java.util.List;

import daos.Interface.RTaskDao;
import domains.RTask;
import domains.RequesterTask;

public class RTaskDaoImpl extends BaseDaoImpl<RTask> implements RTaskDao {

	@Override
	public List<RTask> getIDbyContent(String content) {
		// TODO Auto-generated method stub
		return find("from RTask as r where r.content = ?0", content);
	}

	@Override
	public List<RTask> showAllTaskByState(Integer state) {
		// TODO Auto-generated method stub
		return find("from RTask as t where t.state = ?0", state);
	}

	@Override
	public List<RTask> showTaskByTablename(String tablename) {
		// TODO Auto-generated method stub
		return find("from RTask as t where t.table_name = ?0", tablename);
	}

	@Override
	public List<RTask> findDeadlineTask(String deadline) {
		// TODO Auto-generated method stub
		return findBySql("select * from rtask_info where date_format(deadline,'%Y-%m-%d %H:%i') <= '"+deadline+"'", RTask.class);
	}

	@Override
	public List<RTask> getBytaskID(String taskID) {
		// TODO Auto-generated method stub
		return find("from RTask as t where t.task_id = ?0", Integer.valueOf(taskID));
	}
}
