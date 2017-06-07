package daos.Impl;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;

import daos.Interface.BaseDao;

public class BaseDaoImpl<T> implements BaseDao<T> {

	//依赖注入SessionFactory
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public SessionFactory getSessionFactory(){
		return this.sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T get(Class<T> entityClazz, Serializable id) {
		// TODO Auto-generated method stub
		return (T)getSessionFactory().getCurrentSession().load(entityClazz, id);
	}

	//save()函数的值是Serializable对象,就是主键id的值
	@Override
	public void save(T entity) {
		// TODO Auto-generated method stub
		getSessionFactory().getCurrentSession().save(entity);
	}

	@Override
	public void update(T entity) {
		// TODO Auto-generated method stub
		getSessionFactory().getCurrentSession().update(entity);
	}

	@Override
	public void delete(T entity) {
		// TODO Auto-generated method stub
		getSessionFactory().getCurrentSession().delete(entity);
	}

	@Override
	public void deleteById(Class<T> entityClazz, Serializable id) {
		// TODO Auto-generated method stub
		this.delete(get(entityClazz, id));
	}

	@Override
	public List<T> findAll(Class<T> entityClazz) {
		// TODO Auto-generated method stub
		return this.find("from "+entityClazz.getSimpleName()); //p是别名，别忘记空格
	}

	@Override
	public long findCount(Class<T> entityClazz) {
		// TODO Auto-generated method stub
		List<T> list=find("select count(*) from "+entityClazz.getSimpleName());
		if (list!=null&&list.size()==1) {
			return (Long)list.get(0);
		}
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	//根据HQL语句查询实体
	public List<T> find(String hql){
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		return (List<T>)query.setCacheable(true).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	//根据占位符的HQL语句查询实体
	public List<T> find(String hql,Object... params) {
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		for(int i=0;i<params.length;i++){
			query.setParameter(i+"",params[i]);//因为第一个参数需要String类型，这里把i变为String
		}
		return (List<T>)query.setCacheable(true).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findBySql(String sql,Class<T> entityClazz) {
		// TODO Auto-generated method stub
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(sql); 
		//这里是比hql多个方法，用来关联实体，这样又可以用实体的方法来操作
		query.addEntity(entityClazz);
		return (List<T>)query.setCacheable(true).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByPage(String hql,int offset, int length,Object... params) {
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		for(int i=0;i<params.length;i++){
			query.setParameter(i+"",params[i]);//因为第一个参数需要String类型，这里把i变为String
		}
		query.setFirstResult(offset);
		query.setMaxResults(length);
		return (List<T>)query.setCacheable(true).list();
	}
	
	
}
