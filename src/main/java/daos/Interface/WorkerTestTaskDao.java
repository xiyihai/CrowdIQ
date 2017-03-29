package daos.Interface;

import java.util.List;

import domains.WorkerTestTask;

public interface WorkerTestTaskDao extends BaseDao<WorkerTestTask> {

	//根据工人id，找到对应的测试任务
	List<WorkerTestTask> findByWid(String userID);
}
