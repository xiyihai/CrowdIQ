package daos.Interface;

import java.io.Serializable;
import java.util.List;

public interface BaseDao<T> {
	//根据id加载实体,Integer已经实现了Serializable
	T get(Class<T> entityClazz,Serializable id);
	//保存实体，新构造的实体存入数据库
	void save(T entity);
	//更新实体，先要从数据库获取该实体
	void update(T entity);
	//删除实体，先要从数据库获取该实体
	void delete(T entity);
	//根据id删除实体
	void deleteById(Class<T> entityClazz,Serializable id);
	//获取所有实体
	List<T> findAll(Class<T> entityClazz);
	//获取实体总数
	long findCount(Class<T> entityClazz);
	//根据hql语句查询
	List<T> find(String hql);
	//根据占位符的HQL语句查询实体
	List<T> find(String hql,Object... params);
	//根据sql语句查询
	List<T> findBySql(String sql,Class<T> entityClazz);
	//根据hql分页查询
	List<T> findByPage(String hql,int offset,int lenght,Object... params);
	
}
