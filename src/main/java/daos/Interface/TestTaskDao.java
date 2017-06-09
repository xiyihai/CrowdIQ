package daos.Interface;

import java.util.List;

import domains.TestTask;
import domains.Worker;

public interface TestTaskDao extends BaseDao<TestTask> {


	List<TestTask> getByID(String taskID);
}
