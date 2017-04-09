package daos.Interface;

import java.util.List;

import domains.RTable;

public interface RTableDao extends BaseDao<RTable> {

	//查找雇主上传的所有表格，返回所有表名
	List<RTable> findAllByRid(String userID);
	
	//根据雇主ID和表名，查看是否由此表
	List<RTable> findByIDName(String userID, String tablename);
}
