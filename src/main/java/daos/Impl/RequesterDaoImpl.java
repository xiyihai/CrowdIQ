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

}
