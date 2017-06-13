package dataSource;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import SQL_Process.ReadTable;
import domains.RTable;

public class InsertTable {
	//往数据库插入table数据
		public static void insertDB(){
			Configuration conf=new Configuration().configure("hibernate.cfg.xml");
			//这一步需要显示，可能是版本问题,之前不需要写这句话
			conf.addAnnotatedClass(domains.RTable.class);
			StandardServiceRegistry serviceRegistry=new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
			SessionFactory sf=conf.buildSessionFactory(serviceRegistry);
			
			Session session=sf.openSession();
			Transaction tx=session.beginTransaction();
			
			ReadTable readTable = new ReadTable();
			readTable.tranfer("g:\\person.csv");
			
			System.out.println(readTable.jsonTable.toString());
			RTable rTable = new RTable(200000, "200000.person", 0, readTable.jsonTable.toString());
			
			session.save(rTable);
			
			tx.commit();
			session.close();
			sf.close();
		}
		
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			insertDB();
		}
}
