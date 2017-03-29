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

}
