package daos.Interface;

import java.util.List;

import domains.WTask;

public interface WTaskDao extends BaseDao<WTask> {

	//根据工人id，任务id查找
	List<WTask> getByWidTid(String userID, String taskID);
}
