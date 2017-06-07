package daos.Interface;

import java.util.List;

import domains.Worker;

public interface WorkerDao extends BaseDao<Worker> {

	//根据邮箱返回对应的工人
	List<Worker> getByEmail(String email);

	//删选出需要等级大于某一等级的的工人
	List<Worker> getByLevel(Integer level);
	
	List<Worker> getByWid(String userID);
	
}
