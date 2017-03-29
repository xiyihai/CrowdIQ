package services.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import FunctionsSupport.MailSender;
import daos.Interface.RequesterDao;
import daos.Interface.TestTaskDao;
import daos.Interface.WorkerDao;
import daos.Interface.WorkerTestTaskDao;
import domains.Requester;
import domains.TestTask;
import domains.Worker;
import domains.WorkerTestTask;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import services.Interface.RegisterService;

public class RegisterServiceImpl implements RegisterService {

	private MailSender mailSender;
	//给接口，但spring配置中传入实体
	private RequesterDao requesterDao;
	private WorkerDao workerDao;
	private TestTaskDao testtaskDao;
	private WorkerTestTaskDao workertesttaskDao;
	
	@Override
	public boolean register(String information) {
		// TODO Auto-generated method stub
		JSONObject info = JSONObject.fromObject(information);
		
		//获取邮箱地址，并验证，这里无法验证暂不开发此功能
		String email = info.getString("email");
		mailSender = new MailSender();
		try {
			mailSender.sendMail(email);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//获取姓名，密码，充值的金额
		String name = info.getString("name");
		String password = info.getString("password");
		Double account = Double.valueOf(info.getString("account"));
		
		//区分雇主还是工人
		String flag = info.getString("flag");
		if (flag.equals("requester")) {
			//需要再生成ID号，6位数，工人1开头，雇主2开头，顺序利用数据库实现
			//将数据存入数据库，并返回用户ID
			Requester requester = new Requester(name, password, account, email);
			requesterDao.save(requester);
		}else if (flag.equals("worker")) {
			Worker worker = new Worker(name, password, account, email, "");
			workerDao.save(worker);
			//只可能返回一个
			Worker worker2 = workerDao.getByEmail(email).get(0);
			String id = worker2.getWorker_id().toString();
			
			//注册好了还需要写入对应工人的测试数据,这里加入所有测试数据，待用户自己选择做
			List<TestTask> testtasks = testtaskDao.findAll(TestTask.class);
			for(int i=0;i<testtasks.size();i++){
				WorkerTestTask workerTestTask = new WorkerTestTask(Integer.valueOf(id), testtasks.get(i).getId(), new Integer(0), "");
				workertesttaskDao.save(workerTestTask);
			}
		}
		return true;
	}

	@Override
	public String getTestTask(String userID, String taskID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean finishTestTask(String userID, String taskID, String answer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String login(String informationJSON) {
		JSONObject user = JSONObject.fromObject(informationJSON);
		String email = user.getString("email");
		String password = user.getString("password");
		String flag = user.getString("flag");
		
		String id = null;
		if (flag.equals("worker")) {
			Worker worker = workerDao.getByEmail(email).get(0);
			String realpass = worker.getPassword();
			if (realpass.equals(password)) {
				id = worker.getWorker_id().toString();
			}
		}else {
			if (flag.equals("requester")) {
				Requester requester = requesterDao.getByEmail(email).get(0);
				if (requester.getPassword().equals(password)) {
					id = requester.getRequester_id().toString();
				}
			}
		}
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public String getAllTestTask(String userID) {
		// TODO Auto-generated method stub
		List<WorkerTestTask> testTasks = workertesttaskDao.findByWid(userID);
		return JSONArray.fromObject(testTasks).toString();
	}

	

}
