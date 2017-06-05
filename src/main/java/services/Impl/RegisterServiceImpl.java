package services.Impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.hibernate.loader.plan.exec.process.spi.ReturnReader;

import FunctionsSupport.CaculateAccuracy;
import FunctionsSupport.MailSender;
import TestQuestion.CalculateTest;
import TestQuestion.CalculateTestImp;
import daos.Interface.RAlgorithmDao;
import daos.Interface.RTableDao;
import daos.Interface.RTableListDao;
import daos.Interface.RTaskDao;
import daos.Interface.RequesterDao;
import daos.Interface.RequesterTaskDao;
import daos.Interface.TestTaskDao;
import daos.Interface.WTaskDao;
import daos.Interface.WorkerDao;
import daos.Interface.WorkerRTaskDao;
import daos.Interface.WorkerTestTaskDao;
import domains.RAlgorithm;
import domains.RTable;
import domains.RTableList;
import domains.RTask;
import domains.Requester;
import domains.RequesterTask;
import domains.TestTask;
import domains.WTask;
import domains.Worker;
import domains.WorkerRTask;
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
	private RequesterTaskDao requestertaskDao;
	private RTaskDao rtaskDao;
	private RTableDao rtableDao;
	private RAlgorithmDao ralgorithmDao;
	private RTableListDao rtableListDao;
	private WTaskDao wtaskDao;
	private WorkerRTaskDao workerRTaskDao;
	
	//导入计算初始质量举证的类
	private CalculateTest calculateTest = new CalculateTestImp();
	
	public WorkerRTaskDao getWorkerRTaskDao() {
		return workerRTaskDao;
	}

	public void setWorkerRTaskDao(WorkerRTaskDao workerRTaskDao) {
		this.workerRTaskDao = workerRTaskDao;
	}

	public WTaskDao getWtaskDao() {
		return wtaskDao;
	}

	public void setWtaskDao(WTaskDao wtaskDao) {
		this.wtaskDao = wtaskDao;
	}

	public RTableListDao getRtableListDao() {
		return rtableListDao;
	}

	public void setRtableListDao(RTableListDao rtableListDao) {
		this.rtableListDao = rtableListDao;
	}

	public RAlgorithmDao getRalgorithmDao() {
		return ralgorithmDao;
	}

	public void setRalgorithmDao(RAlgorithmDao ralgorithmDao) {
		this.ralgorithmDao = ralgorithmDao;
	}

	public RTableDao getRtableDao() {
		return rtableDao;
	}

	public void setRtableDao(RTableDao rtableDao) {
		this.rtableDao = rtableDao;
	}

	public RTaskDao getRtaskDao() {
		return rtaskDao;
	}

	public void setRtaskDao(RTaskDao rtaskDao) {
		this.rtaskDao = rtaskDao;
	}

	public RequesterTaskDao getRequestertaskDao() {
		return requestertaskDao;
	}

	public void setRequestertaskDao(RequesterTaskDao requestertaskDao) {
		this.requestertaskDao = requestertaskDao;
	}

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
			Worker worker = new Worker(name, password, account, email, null, 0, 0.0, 0.0, 0.0, 0);
			workerDao.save(worker);
			//只可能返回一个
			Worker worker2 = workerDao.getByEmail(email).get(0);
			String id = worker2.getWorker_id().toString();
			
			//注册好了还需要写入对应工人的测试数据,这里加入所有测试数据，待用户自己选择做
			List<TestTask> testtasks = testtaskDao.findAll(TestTask.class);
			for(int i=0;i<testtasks.size();i++){
				WorkerTestTask workerTestTask = new WorkerTestTask(Integer.valueOf(id), testtasks.get(i).getTest_id(), new Integer(0), "", 0);
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
		
		//需要判断工人做题是否正确,并写入数据库
		if (answer.equals(truth)) {
			task.setIscorrect(1);
		}else {
			task.setIscorrect(0);
		}
		workertesttaskDao.update(task);
		
		
		
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
		
		String userid = "";
		if (flag.equals("worker")) {
			Worker worker = workerDao.getByEmail(email).get(0);
			String realpass = worker.getPassword();
			if (realpass.equals(password)) {
				userid = worker.getWorker_id().toString();		
			}
		}else {
			if (flag.equals("requester")) {
				Requester requester = requesterDao.getByEmail(email).get(0);
				if (requester.getPassword().equals(password)) {
					userid = requester.getRequester_id().toString();
				}
			}
		}
		
		if (userid.equals("")) {
			return null;
		}else {
			return userid;
		}
	}


	@Override
	public String getLoginInfo(String userID, String flag) {
		// TODO Auto-generated method stub
		JSONObject showinformation = new JSONObject();
		
		if (flag.equals("worker")) {
				Worker worker = workerDao.get(Worker.class, userID);
				//下面分装信息个人信息
				JSONObject personInfo = JSONObject.fromObject(worker);
				CaculateAccuracy caculateAccuracy = new CaculateAccuracy();
				personInfo.replace("quality", caculateAccuracy.getAccuracy(worker.getQuality()));
				personInfo.remove("total_tasks");
				showinformation.put("personInfo", personInfo);
				
				//封装任务信息
				int state2=0;
				int state0=0;
				int state4=0;
				int rtask_number=0;
				List<WTask> wTasks2 = wtaskDao.getByWidState(userID, 2);
				state2 = wTasks2.size();
				List<WTask> wTasks0 = wtaskDao.getByWidState(userID, 0);
				state0 = wTasks0.size();
				List<WTask> wTasks4 = wtaskDao.getByWidState(userID, 4);
				state4 = wTasks4.size();
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
				Date now=new Date();
				String deadline=dateFormat.format(now); 
				
				List<WorkerRTask> workerRTasks = workerRTaskDao.findByWidDeadline(userID, deadline);
				rtask_number = workerRTasks.size();
				
				JSONObject taskInfo = new JSONObject();
				taskInfo.put("state0", state0);
				taskInfo.put("state2", state2);
				taskInfo.put("state4", state4);
				taskInfo.put("rtask_number", rtask_number);
				
				showinformation.put("taskInfo", taskInfo);
				
			}
		
			if (flag.equals("requester")) {
				
					Requester requester = requesterDao.get(Requester.class, userID);
					//下面分装信息个人信息
					showinformation.put("personInfo", JSONObject.fromObject(requester));
					//分装任务信息,思路： 找对其对应的所有任务ID，再根据这些ID统计对应的任务状态信息
					int state2=0;
					int state0=0;
					int state1=0;		
					List<RequesterTask> requesterTasks = requestertaskDao.getByRID(userID);
					for (int i = 0; i < requesterTasks.size(); i++) {
						Integer taskID = requesterTasks.get(i).getTask_id();
						RTask rTask = rtaskDao.get(RTask.class, taskID);
						if (rTask.getState()==0) {
							state0++;
						}
						if (rTask.getState()==1) {
							state1++;
						}
						if (rTask.getState()==2) {
							state2++;
						}
					}
					List<RTable> rTables = rtableDao.findAllByRid(userID);
					int table_number = rTables.size();
					JSONObject taskInfo = new JSONObject();
					taskInfo.put("state0", state0);
					taskInfo.put("state1", state1);
					taskInfo.put("state2", state2);
					taskInfo.put("table_number", table_number);
					
					showinformation.put("taskInfo", taskInfo);
					
					//下面封装算法及数据源
					List<RAlgorithm> rAlgorithms = ralgorithmDao.findByID(userID);
					List<RTableList> rTableLists = rtableListDao.findByID(userID);
					JSONObject datasource = new JSONObject();
					
					JSONArray ralgorithm = new JSONArray();
					for (int i = 0; i < rAlgorithms.size(); i++) {
						ralgorithm.add(rAlgorithms.get(i).getAlgorithm_name());
					}
					JSONArray rtablelist = new JSONArray();
					for (int i = 0; i < rTableLists.size(); i++) {
						rtablelist.add(rTableLists.get(i).getTablelist());
					}
					datasource.put("algorithms", ralgorithm);
					datasource.put("tablelists", rtablelist);
					
					showinformation.put("datasource", datasource);		
				}
			return showinformation.toString();
		}
	
	
	@Override
	public String getAllTestTask(String userID) {
		// TODO Auto-generated method stub
		List<WorkerTestTask> testTasks = workertesttaskDao.findByWid(userID);
		return JSONArray.fromObject(testTasks).toString();
	}

	@Override
	public String showDoneTestTask(String userID, String taskID) {
		// TODO Auto-generated method stub
		TestTask testTask = testtaskDao.get(TestTask.class, taskID);
		//这个返回的数据中包含 content，answer两部分
		return JSONObject.fromObject(testTask).toString();
	}


	

}
