package daos.Interface;

import java.util.List;

import domains.RTask;

public interface RTaskDao extends BaseDao<RTask> {

	//对于刚存入数据库，生成唯一ID的任务需要再次取出来得到ID值
	List<RTask> getIDbyContent(String content);
}
