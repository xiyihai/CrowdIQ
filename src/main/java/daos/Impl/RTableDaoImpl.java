package daos.Impl;

import java.util.List;

import daos.Interface.RTableDao;
import domains.RTable;

public class RTableDaoImpl extends BaseDaoImpl<RTable> implements RTableDao {

	@Override
	public List<RTable> findAllByRid(String userID) {
		// TODO Auto-generated method stub
		return find("from RTable as r where r.requester_id = ?0", userID);
	}

}
