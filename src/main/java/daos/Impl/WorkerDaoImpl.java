package daos.Impl;

import java.util.List;

import daos.Interface.WorkerDao;
import domains.Worker;

public class WorkerDaoImpl extends BaseDaoImpl<Worker> implements WorkerDao {

	@Override
	public List<Worker> getByEmail(String email) {
		// TODO Auto-generated method stub
		return find("from Worker as w where w.email = ?0", email);
	}

	@Override
	public List<Worker> getByLevel(Integer level) {
		// TODO Auto-generated method stub
		return find("from Worker as w where w.level >= ?0", level);
	}

	@Override
	public List<Worker> getByWid(String userID) {
		// TODO Auto-generated method stub
		return find("from Worker as w where w.worker_id = ?0", Integer.valueOf(userID));
	}

}
