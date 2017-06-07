package daos.Interface;

import java.util.List;

import domains.Requester;

public interface RequesterDao extends BaseDao<Requester> {

	//根据邮箱返回对应的雇主
	List<Requester> getByEmail(String email);
	
	List<Requester> getByRID(String userID);
}
