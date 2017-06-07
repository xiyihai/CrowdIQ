package daos.Impl;

import java.util.List;

import daos.Interface.RTableListDao;
import domains.RTableList;

public class RTableListDaoImpl extends BaseDaoImpl<RTableList> implements RTableListDao {

	@Override
	public List<RTableList> findByIDName(String userID, String tablelist) {
		// TODO Auto-generated method stub
		return find("from RTableList as r where r.requester_id = ?0 and r.tablelist = ?1", Integer.valueOf(userID), tablelist);
	}

	@Override
	public List<RTableList> findByID(String userID) {
		// TODO Auto-generated method stub
	return find("from RTableList as r where r.requester_id = ?0", Integer.valueOf(userID));
	}

}
