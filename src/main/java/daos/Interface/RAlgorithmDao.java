package daos.Interface;

import java.util.List;

import domains.RAlgorithm;

public interface RAlgorithmDao extends BaseDao<RAlgorithm> {

	//通过userID，algortihm_name；来找到对应记录
	List<RAlgorithm> findByIDAlgorithm(String userID, String algorithm_name);
	
	List<RAlgorithm> findByID(String userID);
}
