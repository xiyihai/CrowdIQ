package daos.Impl;

import java.util.List;

import daos.Interface.RAlgorithmDao;
import domains.RAlgorithm;

public class RAlgorithmDaoImpl extends BaseDaoImpl<RAlgorithm> implements RAlgorithmDao {

	@Override
	public List<RAlgorithm> findByIDAlgorithm(String userID, String algorithm_name) {
		// TODO Auto-generated method stub
		return find("from RAlgorithm as r where r.requester_id = ?0 and algorithm_name = ?1", Integer.valueOf(userID), algorithm_name);
	}

	@Override
	public List<RAlgorithm> findByID(String userID) {
		// TODO Auto-generated method stub
	return find("from RAlgorithm as r where r.requester_id = ?0", Integer.valueOf(userID));
	}

}
