package daos.Interface;

import java.util.List;

import domains.RTable;

public interface RTableDao extends BaseDao<RTable> {

	//查找雇主上传的所有表格，返回所有表名
	List<RTable> findAllByRid(String userID);
}
