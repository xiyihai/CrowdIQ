package daos.Impl;

import java.util.List;

import daos.Interface.RTableDao;
import domains.RTable;

public class RTableDaoImpl extends BaseDaoImpl<RTable> implements RTableDao {

	@Override
	public List<RTable> findAllByRid(String userID) {
		// TODO Auto-generated method stub
		return find("from RTable as r where r.requester_id = ?0", Integer.valueOf(userID));
	}

	@Override
	public List<RTable> findByIDName(String userID, String tablename) {
		// TODO Auto-generated method stub
		return find("from RTable as r where r.requester_id = ?0 and r.table_name = ?1", Integer.valueOf(userID), tablename);
	}

}
