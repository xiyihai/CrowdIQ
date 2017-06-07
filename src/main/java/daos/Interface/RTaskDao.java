package daos.Interface;

import java.util.List;

import domains.RTask;
import domains.RequesterTask;

public interface RTaskDao extends BaseDao<RTask> {

	//对于刚存入数据库，生成唯一ID的任务需要再次取出来得到ID值
	List<RTask> getIDbyContent(String content);
	
	//根据状态展示所有任务
	List<RTask> showAllTaskByState(Integer state);
	
	//根据对应的表ID返回所有属于该表的任务
	List<RTask> showTaskByTablename(String tablename);
	
	//根据时间戳，找到超过过等于该时间的任务
	List<RTask> findDeadlineTask(String deadline);
	
	List<RTask> getBytaskID(String ID);
}
