package daos.Interface;

import java.util.List;

import domains.RTableList;

public interface RTableListDao extends BaseDao<RTableList> {

	List<RTableList> findByIDName(String userID, String tablelist);
	
	List<RTableList> findByID(String userID);
	
}
