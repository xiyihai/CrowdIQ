package daos.Interface;

import java.util.List;

import domains.RequesterTask;

public interface RequesterTaskDao extends BaseDao<RequesterTask> {

	//根据两个号找对对应内容
	List<RequesterTask> getBy2ID(String userID, String taskID);
	
	//根据雇主id，返回内容
	List<RequesterTask> getByRID(String userID);
	
	//根据雇主任务id，返回内容
	List<RequesterTask> getByTID(String taskID);
}
