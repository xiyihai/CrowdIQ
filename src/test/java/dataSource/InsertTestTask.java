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
		sqlTargets.add("table.quality");
		taskVos.setSqlTargets(sqlTargets);
		taskVos.setQuestionDescribe("good or bad");
		ArrayList<String> showing_content = new ArrayList<>();
		String showheader = "headers:[\"Year\",\"Laureates\",\"Nationality\",\"Achievement\"]";
		String showcolumns = "rows:[[\"\",\"George McGovern\",\"\",\"\"],[\"1994\",\"Dr Muhammad Yunus\",\"Bangladesh\",\"Founder of the Grameen Bank in Bangladesh, developed innovative small loan programs for the poor, providing millions of people access to more food and better nutrition.\"],[\"2006\",\"Edson Lobato,\",\"Brazil\",\"Pioneering work in soil science and policy implementation that opened the vast Cerrado region of Brazil to agricultural and food production.\"],[\"2009\",\"Gebisa Ejeta\",\"Ethiopia\",\"Developing Africa's first sorghum hybrids resistant to drought and the parasitic witchweed.\"],[\"2015\",\"Sir Fazle Hasan Abed\",\"BangladeshBangladesh\",\"Founder of BRAC, the world's largest NGO, which is recognized for substantial work on reducing poverty in Bangladesh and 10 other countries[4]\"],[\"2005\",\"Dr Modadugu Vijay Gupta\",\"India\",\"Development and dissemination of low-cost techniques for freshwater fish farming (using tilapia species) by the rural poor.\"],[\"\",\"Luiz In��cio Lula da Silva\",\"Brazil\",\"\"],[\"\",\"\",\"United Nations\",\"\"]]";
		
		showing_content.add(showheader);
		showing_content.add(showcolumns);
		
		taskVos.setShowing_contents(showing_content);
		
		ArrayList<ArrayList<String>> candidateItems = new ArrayList<>();
		ArrayList<String> itmes = new ArrayList<>();
		itmes.add("good");
		itmes.add("bad");
		
		candidateItems.add(itmes);
		taskVos.setCandidateItems(candidateItems);	

		JSONArray answer = new JSONArray();
		answer.add("bad");
		
		session.save(new TestTask(JSONObject.fromObject(taskVos).toString(), answer.toString()));
		
		tx.commit();
		session.close();
		sf.close();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 10; i++) {
			insertDB();	
		}
	}

}
