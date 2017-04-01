package daos.Impl;

import java.util.List;

import daos.Interface.RTaskDao;
import domains.RTask;

public class RTaskDaoImpl extends BaseDaoImpl<RTask> implements RTaskDao {

	@Override
	public List<RTask> getIDbyContent(String content) {
		// TODO Auto-generated method stub
		return find("from RTask as r where r.content = ?0", content);
	}

}
