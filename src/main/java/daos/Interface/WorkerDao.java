package daos.Interface;

import java.util.List;

import domains.Worker;

public interface WorkerDao extends BaseDao<Worker> {

	//根据邮箱返回对应的工人
	List<Worker> getByEmail(String email);

}
