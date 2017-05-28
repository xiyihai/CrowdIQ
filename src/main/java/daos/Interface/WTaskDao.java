package daos.Interface;

import java.util.List;

import domains.WTask;

public interface WTaskDao extends BaseDao<WTask> {

	//根据工人id，任务id查找
	List<WTask> getByWidTid(String userID, String taskID);
	
	//根据任务ID查找
	List<WTask> getByTid(String taskID);
	
	//根据工人id查看
	List<WTask> getByWid(String userID);
	
	List<WTask> getByWidState(String userID, Integer state);
}
