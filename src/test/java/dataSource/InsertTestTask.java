package dataSource;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import domains.TestTask;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import vos.TestTaskVos;

public class InsertTestTask {

	//往数据库插入测试任务数据
	public static void insertDB(){
		Configuration conf=new Configuration().configure("hibernate.cfg.xml");
		//这一步需要显示，可能是版本问题,之前不需要写这句话
		conf.addAnnotatedClass(domains.TestTask.class);
		StandardServiceRegistry serviceRegistry=new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
		SessionFactory sf=conf.buildSessionFactory(serviceRegistry);
		
		Session session=sf.openSession();
		Transaction tx=session.beginTransaction();
		
		//构建测试任务
		TestTaskVos taskVos = new TestTaskVos();
		ArrayList<String> sqlTargets = new ArrayList<>();
		sqlTargets.add("header[2]");
		taskVos.setSqlTargets(sqlTargets);
		taskVos.setQuestionDescribe("recover the missing header");
		ArrayList<String> showing_content = new ArrayList<>();
		showing_content.add("columns-2:[\"China\",\"Japan\",\"America\",\"England\"]");
		taskVos.setShowing_contents(showing_content);
		
		ArrayList<ArrayList<String>> candidateItems = new ArrayList<>();
		ArrayList<String> itmes = new ArrayList<>();
		itmes.add("Country");
		itmes.add("Language");
		itmes.add("Nation");
		candidateItems.add(itmes);
		taskVos.setCandidateItems(candidateItems);	

		JSONArray answer = new JSONArray();
		answer.add("Country");
		
		session.save(new TestTask(JSONObject.fromObject(taskVos).toString(), answer.toString()));
		
		tx.commit();
		session.close();
		sf.close();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		insertDB();
	}

}
