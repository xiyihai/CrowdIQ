package daos.Impl;

import java.util.List;

import daos.Interface.RequesterDao;
import domains.Requester;

public class RequesterDaoImpl extends BaseDaoImpl<Requester> implements RequesterDao {

	@Override
	public List<Requester> getByEmail(String email) {
		// TODO Auto-generated method stub
		return find("from Requester as r where r.email = ?0", email);
	}

	@Override
	public List<Requester> getByRID(String userID) {
		// TODO Auto-generated method stub
		return find("from Requester as r where r.requester_id = ?0", Integer.valueOf(userID));
	}

}
