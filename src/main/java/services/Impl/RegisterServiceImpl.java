package services.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;


import FunctionsSupport.MailSender;
import TestQuestion.CalculateTest;
import TestQuestion.CalculateTestImp;
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

	//导入计算初始质量举证的类
	private CalculateTest calculateTest = new CalculateTestImp();
	
	public RequesterDao getRequesterDao() {
		return requesterDao;
	}

	public void setRequesterDao(RequesterDao requesterDao) {
		this.requesterDao = requesterDao;
	}

	public WorkerDao getWorkerDao() {
		return workerDao;
	}

	public void setWorkerDao(WorkerDao workerDao) {
		this.workerDao = workerDao;
	}

	public TestTaskDao getTesttaskDao() {
		return testtaskDao;
	}

	public void setTesttaskDao(TestTaskDao testtaskDao) {
		this.testtaskDao = testtaskDao;
	}

	public WorkerTestTaskDao getWorkertesttaskDao() {
		return workertesttaskDao;
	}

	public void setWorkertesttaskDao(WorkerTestTaskDao workertesttaskDao) {
		this.workertesttaskDao = workertesttaskDao;
	}

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
			Worker worker = new Worker(name, password, account, email, null, 0);
			workerDao.save(worker);
			//只可能返回一个
			Worker worker2 = workerDao.getByEmail(email).get(0);
			String id = worker2.getWorker_id().toString();
			
			//注册好了还需要写入对应工人的测试数据,这里加入所有测试数据，待用户自己选择做
			List<TestTask> testtasks = testtaskDao.findAll(TestTask.class);
			for(int i=0;i<testtasks.size();i++){
				WorkerTestTask workerTestTask = new WorkerTestTask(Integer.valueOf(id), testtasks.get(i).getTest_id(), new Integer(0), "");
				workertesttaskDao.save(workerTestTask);
			}
		}
		return true;
	}

	@Override
	public String getTestTask(String userID, String taskID) {
		// TODO Auto-generated method stub
		TestTask testTask = testtaskDao.get(TestTask.class, Integer.valueOf(taskID));
		String content = testTask.getContent();
		return content;
	}

	@Override
	public boolean finishTestTask(String userID, String taskID, String answer) {
		// TODO Auto-generated method stub
		WorkerTestTask task = workertesttaskDao.findByWidTid(userID, taskID).get(0);
		task.setState(1);
		task.setWorker_answer(answer);
		workertesttaskDao.update(task);
		
		//工人每做一题就要更新质量矩阵，若是做满10题更改工人level
		TestTask testtask = testtaskDao.get(TestTask.class, taskID);
		//这个truth是C B D答案，是个JSONArray数组，但目前数组长度都是1
		JSONArray truthArray = JSONArray.fromObject(testtask.getAnswer());
		String truth = truthArray.getString(0);
		//这个由JSONArray变换来的， 目前假设长度为1
		JSONArray wanswerArray = JSONArray.fromObject(task.getWorker_answer());
		String wanswer = wanswerArray.getString(0);
		//还得获取候选答案长度，这里假定都是选择题
		JSONObject content = JSONObject.fromObject(testtask.getContent());
		JSONArray candidateItems = content.getJSONArray("candidateItems");
		JSONArray items = candidateItems.getJSONArray(0);
		int length = items.size();
		//以：分割
		String result = wanswer+":"+truth+":"+length;
		//需要计算工人初步质量举证
		ArrayList<String> results = new ArrayList<>();
		results.add(result);
		
		Worker worker = workerDao.get(Worker.class, userID);
		//需要将正确答案和工人答案打包然后发给对应计算模块

		calculateTest = new CalculateTestImp();
		worker.setQuality(calculateTest.CaculateTestQuestion(results));
		workerDao.update(worker);
		
		//需要判断该用户的测试题完成的是否足够多,若完成且工人目前level为0，则修改工人level为1
		if (worker.getLevel()==0) {
			List<WorkerTestTask> tasks = workertesttaskDao.findTaskbyState(userID, 1);
			if (tasks.size()>=10) {
				//需要更改工人的level
				worker.setLevel(1);
				workerDao.update(worker);
			}
		}	
		return true;
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
