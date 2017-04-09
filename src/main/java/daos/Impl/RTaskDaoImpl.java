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
	public List<RTask> showTaskByTableID(String tableID) {
		// TODO Auto-generated method stub
		return find("from RTask as t where t.table_id = ?0", tableID);
	}
}
