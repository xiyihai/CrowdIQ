package services.Impl;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import CDASQualityControl.CDASAggregateAnswer;
import CDASQualityControl.CDASAggregateAnswerImp;
import CaculateParams.CaculateParameter;
import CaculateParams.CaculateParameterImpl;
import CaculateParams.CaculateSalary;
import CaculateParams.CaculateSalaryImpl;
import FunctionsSupport.TranferAnswer;
import InquireQualityControl.InquireAggregateAnswer;
import InquireQualityControl.InquireAggregateAnswerImp;
import QuestionControl.AggregateAnswer;
import QuestionControl.AggregateAnswerImp;
import MultiQualityControl.AggregateMulAnswerImp;
import MultiQualityControl.MulAggregateAnswer;
import OpMulQualityControl.OpAggregateMulAnswer;
import OpMulQualityControl.OpAggregateMulAnswerImp;
import RecommendTask.FirstCountNumRecommend;
import RecommendTask.RecommendTask;
import SigMulQualityControl.AggregateMulSigAnswer;
import SigMulQualityControl.AggregateMulSigAnswerImp;
import daos.Interface.AnswerCompareDao;
import daos.Interface.MulAnswerCompareDao;
import daos.Interface.RTaskDao;
import daos.Interface.RequesterDao;
import daos.Interface.RequesterTaskDao;
import daos.Interface.WTaskDao;
import daos.Interface.WorkerDao;
import daos.Interface.WorkerRTaskDao;
import domains.AnswerCompare;
import domains.MulAnswerCompare;
import domains.RTask;
import domains.Requester;
import domains.RequesterTask;
import domains.WTask;
import domains.Worker;
import domains.WorkerRTask;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import services.Interface.ParserCrowdIQLService;
import services.Interface.TaskProcessService;
import vos.LastestM;
import vos.RequesterTaskInfo;
import vos.WorkerInfo;

public class TaskProcessServiceImpl implements TaskProcessService {

	private RTaskDao rtaskDao;
	private WTaskDao wtaskDao;
	private RequesterTaskDao requestertaskDao;
	private WorkerDao workerDao;
	private RequesterDao requesterDao;
	private WorkerRTaskDao workerRTaskDao;
	private AnswerCompareDao answerCompareDao;
	private MulAnswerCompareDao mulAnswerCompareDao;
	
	private ParserCrowdIQLService parserCrowdIQLService;
	
	private DecimalFormat dFormat = new DecimalFormat("#######0.00");
	
	public ParserCrowdIQLService getParserCrowdIQLService() {
		return parserCrowdIQLService;
	}

	public void setParserCrowdIQLService(ParserCrowdIQLService parserCrowdIQLService) {
		this.parserCrowdIQLService = parserCrowdIQLService;
	}

	
	public AnswerCompareDao getAnswerCompareDao() {
		return answerCompareDao;
	}

	public void setAnswerCompareDao(AnswerCompareDao answerCompareDao) {
		this.answerCompareDao = answerCompareDao;
	}

	public MulAnswerCompareDao getMulAnswerCompareDao() {
		return mulAnswerCompareDao;
	}

	public void setMulAnswerCompareDao(MulAnswerCompareDao mulAnswerCompareDao) {
		this.mulAnswerCompareDao = mulAnswerCompareDao;
	}

	public WorkerRTaskDao getWorkerRTaskDao() {
		return workerRTaskDao;
	}

	public void setWorkerRTaskDao(WorkerRTaskDao workerRTaskDao) {
		this.workerRTaskDao = workerRTaskDao;
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

	public RTaskDao getRtaskDao() {
		return rtaskDao;
	}

	public void setRtaskDao(RTaskDao rtaskDao) {
		this.rtaskDao = rtaskDao;
	}

	public WTaskDao getWtaskDao() {
		return wtaskDao;
	}

	public void setWtaskDao(WTaskDao wtaskDao) {
		this.wtaskDao = wtaskDao;
	}

	public RequesterTaskDao getRequestertaskDao() {
		return requestertaskDao;
	}

	public void setRequestertaskDao(RequesterTaskDao requestertaskDao) {
		this.requestertaskDao = requestertaskDao;
	}

	@Override
	public String buildTask(String taskString) {
		// TODO Auto-generated method stub
		//需要根据前端的数据，计算对应的难度系数等参数
		JSONObject taskUI = JSONObject.fromObject(taskString);
		//把UI中内容取出来，组成RTask	, 直接加入两个新的元素即可
		taskUI.put("receiveAnswers", new ArrayList<>());
		taskUI.put("finalAnswers", new ArrayList<>());
		taskUI.put("receiveWorkerID", new ArrayList<>());
		
		//这里是要返回给前端查看的所有参数
		JSONObject taskVos = new JSONObject();
		taskVos.put("content", taskUI);
		
		//用来增加三天
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
		Date now=new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DATE, 3);
		
		String deadline=dateFormat.format(calendar.getTime()); 
		taskVos.put("deadline", deadline);
		//下面这些参数待计算
		
		//实现接口 ： CaculateParameter
		//实现类： CaculateParameterImpl
		//里面函数包括： 主要是难度系数，其余两个和难度系数成正比
		//Double getDiffDegree(Integer[] selects, Integer[] showings, Double top_k)
		//Double getEachReward(Integer[] selects, Integer[] showings, Double top_k)
		//Integer getWorkNumber(String[] qualities)
		
		CaculateParameter caculateParameter = new CaculateParameterImpl();
		Double di = caculateParameter.getDiffDegree(parserCrowdIQLService.getTargetsBlank(), 
				parserCrowdIQLService.getShowingsBlank(), parserCrowdIQLService.getTop_kPerc());
		Double each_reward = caculateParameter.getEachReward(parserCrowdIQLService.getTargetsBlank(), 
				parserCrowdIQLService.getShowingsBlank(), parserCrowdIQLService.getTop_kPerc());
		//传入所有工人的质量矩阵
		List<Worker> workers = workerDao.getByLevel(1);
	
		String[] qualities = new String[workers.size()];
		for (int i = 0; i < qualities.length; i++) {
			qualities[i] = workers.get(i).getQuality();
		}
		//这里除以2，后续需要改善
		int workNumber = caculateParameter.getWorkNumber(qualities)/2;
//		System.out.println(di);
//		System.out.println(each_reward);
//		System.out.println(workNumber);
//		double di = 0.3;
//		double each_reward = 0.05;
//		int workNumber = 12;
		
		taskVos.put("each_reward", dFormat.format(each_reward));
		taskVos.put("hastaken_number", 0);
		taskVos.put("hasanswer_number", 0);
		taskVos.put("state", 0);
		taskVos.put("difficult_degree", dFormat.format(di));
		taskVos.put("worker_number", workNumber);
		taskVos.put("predict_cost", dFormat.format(workNumber*each_reward));
		taskVos.put("haspaid_cost", 0);
		
		return taskVos.toString();
	}

	@Override
	public boolean commitTask(String userID, String taskString) {
		// TODO Auto-generated method stub
		//将这个taskString解析出元素，直接存入RTask数据库生成任务ID，然后再存入R——T数据库
		//为了利用数据库生成唯一ID，只能再将内容判断一遍来获取id值，只能假设content不可能重复
		
		JSONObject UI = JSONObject.fromObject(taskString);
		
		//用来增加三天
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			Date now=new Date();
	
		UI.put("begin_time", dateFormat.format(now));
		RTask rTask = new RTask(UI.get("content").toString(), UI.get("table_name").toString(), Timestamp.valueOf(UI.get("begin_time").toString())
				,Timestamp.valueOf(UI.get("deadline").toString()+":00"),Double.valueOf(UI.get("each_reward").toString()),Integer.valueOf(UI.get("hastaken_number").toString()),
				Integer.valueOf(UI.get("hasanswer_number").toString()),Integer.valueOf(UI.get("state").toString()),
				Double.valueOf(UI.get("difficult_degree").toString()), Integer.valueOf(UI.get("worker_number").toString()),
				Double.valueOf(UI.get("predict_cost").toString()), Double.valueOf(UI.get("haspaid_cost").toString()), Integer.valueOf(UI.get("type").toString()));		
		
		rtaskDao.save(rTask);
		Integer task_id = rtaskDao.getIDbyContent(UI.get("content").toString()).get(0).getTask_id();
		
		RequesterTask requestertask = new RequesterTask(Integer.valueOf(userID), task_id);
		requestertaskDao.save(requestertask);
		return true;
	}

	@Override
	public boolean commitEditTask(String userID, String taskID, String taskString) {
		// TODO Auto-generated method stub
		//将这个taskString解析出元素，直接存入RTask数据库生成任务ID，然后再存入R——T数据库
		//为了利用数据库生成唯一ID，只能再将内容判断一遍来获取id值，只能假设content不可能重复	
		JSONObject UI = JSONObject.fromObject(taskString);
		RTask rTaskedit = rtaskDao.getBytaskID(taskID).get(0); 	
		try {
			rTaskedit.setContent(UI.get("content").toString());
			rTaskedit.setDeadline(Timestamp.valueOf(UI.get("deadline").toString()));
			rTaskedit.setWorker_number(Integer.valueOf(UI.get("worker_number").toString()));
			rTaskedit.setEach_reward(Double.valueOf(UI.get("each_reward").toString()));
			rTaskedit.setPredict_cost(Double.valueOf(dFormat.format(rTaskedit.getEach_reward()*rTaskedit.getWorker_number())));
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
		rtaskDao.update(rTaskedit);
		return true;
	}

	
	@Override
	public boolean publishTask(String userID, String taskID) {
		// TODO Auto-generated method stub
		//通过判断之前的状态来决定怎么处理，之前是待发布则不变，之前是暂停中则需要 先确定还需要多少人，
		//然后从总人数中去除已经收录的工人id
		RTask rTask = rtaskDao.getBytaskID(taskID).get(0);
		
			rTask.setState(1);
			rtaskDao.update(rTask);
		
			//这里需要调用推荐模块
			//输入工人数组： 工人id，工人质量矩阵， 工人等级(这里删选出从1开始)， 平均答题时间， 平均任务难度系数， 平均任务报酬
			//输入任务： 任务ID， 截止时间， each_reward， 已收录工人数，需要工人数， 任务难度系数
			List<Worker> workers = workerDao.getByLevel(1);
			JSONArray receiveWorkerID = JSONObject.fromObject(rTask.getContent()).getJSONArray("receiveWorkerID");
			//这一步用来删除已收到的工人id
			for (int i = 0; i < workers.size(); i++) {
				for (int j = 0; j < receiveWorkerID.size(); j++) {
					if (workers.get(i).getWorker_id() == Integer.valueOf(receiveWorkerID.getString(j))) {
						workers.remove(i);
					}
				}
			}
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
			Date now=new Date();
			String deadline=dateFormat.format(now); 
			
			List<WorkerInfo> workerInfos = new ArrayList<>();
			for (int i = 0; i < workers.size(); i++) {
				Worker worker = workers.get(i);
				//需要判断如果数据库中该工人短时间内收到推荐任务多于10个，或者 收录的任务不多于32，则暂停
				int wrtask_number = workerRTaskDao.findByWidDeadline(String.valueOf(worker.getWorker_id()), deadline, 1).size();
				int wtask_number = wtaskDao.getByWidState(String.valueOf(worker.getWorker_id()), 0).size();
				if (wrtask_number <= 8 && wtask_number <= 20) {
					WorkerInfo workerInfo = new WorkerInfo(worker.getWorker_id(), worker.getQuality(), worker.getLevel(), worker.getAverage_costtime(),
							worker.getAverage_di(), worker.getAverage_reward());
					workerInfos.add(workerInfo);
				}
			}
			//找到对应发布的任务，取出其中有用的信息，包装发给接口类
			RequesterTaskInfo requesterTaskInfo = new RequesterTaskInfo(rTask.getTask_id(), 
						rTask.getDeadline(), rTask.getEach_reward(), rTask.getHastaken_number(),
						rTask.getWorker_number(), rTask.getDifficult_degree());

			//任务推荐接口： RecommendTask
			//实现类： RecommendTaskImpl
			//接口方法(返回工人ID组成的数组)： String[] getRecommendTask(Integer worker_number, Integer times, List<WorkerInfo> workerInfos, RequesterTaskInfo requesterTaskInfo)
			//(返回本次轮回的截止日期) TimeStamp getTakenDeadline(Integer worker_number, Integer times, List<WorkerInfo> workerInfos, RequesterTaskInfo requesterTaskInfo);
			//这里面两个类，是自己封装的，方便取信息
			RecommendTask recommendTask = new FirstCountNumRecommend();
			
			String[] workerIDs = recommendTask.getRecommendTask(rTask.getWorker_number()-rTask.getHastaken_number(), 1, workerInfos, requesterTaskInfo);
			Timestamp taken_deadline = recommendTask.getTakenDeadline(rTask.getWorker_number()-rTask.getHastaken_number(), 1, 15, workerInfos, requesterTaskInfo);	
			
			//要写入数据库
			for(int i=0;i<workerIDs.length;i++){
				if (workerRTaskDao.findByTidWid(workerIDs[i], taskID).size()>0) {
					WorkerRTask workerRTask = workerRTaskDao.findByTidWid(workerIDs[i], taskID).get(0);
					workerRTask.setTaken_deadline(taken_deadline);
					workerRTask.setTimes(1);
					workerRTaskDao.update(workerRTask);
				}else {
					WorkerRTask workerRTask = new WorkerRTask(Integer.valueOf(workerIDs[i]), Integer.valueOf(taskID), 1, taken_deadline, 1);
					workerRTaskDao.save(workerRTask);		
				}
			}	
		return true;
	}

	@Override
	public boolean pauseTask(String userID, String taskID) {
		// TODO Auto-generated method stub
		//先判断对应工人任务是否有未做的（收录的）
		List<WTask> wTasks = wtaskDao.getByTid(taskID);
		for(int i=0;i<wTasks.size();i++){
			if (wTasks.get(i).getState()==0) {
				return false;
			}
		}
		//找对对应雇主任务，修改状态为暂停，可能还有后续变化（任务暂停之后怎么处理）
		RTask rTask = rtaskDao.getBytaskID(taskID).get(0);
		rTask.setState(3);
		rtaskDao.update(rTask);
		
		//暂停之后的任务怎么处理， 从商品架下架， 这里无需额外操作
		return true;
	}

	@Override
	public boolean deleteTask(String userID, String taskID, String flag) {
		// TODO Auto-generated method stub
		//这里需要判断任务状态才能删除
		if (flag.equals("worker")) {
			WTask wTask = wtaskDao.getByWidTid(userID, taskID).get(0);
			Integer state = wTask.getState();
			if (state==4||state==2) {
				wtaskDao.delete(wtaskDao.getByWidTid(userID, taskID).get(0));	
				return true;
			}else {
				return false;
			}
			
		}else if (flag.equals("requester")) {
			RTask rTask = rtaskDao.getBytaskID(taskID).get(0);
			Integer state = rTask.getState();
			if (state==0||state==2||state==4) {
				rtaskDao.delete(rtaskDao.getBytaskID(taskID).get(0));
				requestertaskDao.delete(requestertaskDao.getBy2ID(userID, taskID).get(0));
				return true;
			}else {
				return false;
			}
		}
		return false;
	}

	@Override
	public String showTask(String userID, String taskID, String flag) {
		// TODO Auto-generated method stub
		String result = null;
		
		if (flag.equals("worker")) {
			WTask wTask = wtaskDao.getByWidTid(userID, taskID).get(0);
			result = JSONObject.fromObject(wTask).toString();
			
		}else if (flag.equals("requester")) {
			//这里应该获取数据库中所有参数，最终作为一个JSONObject返回给用户，删选工作交给前端
			RTask rTask = rtaskDao.getBytaskID(taskID).get(0);
			JSONObject showtask = JSONObject.fromObject(rTask);
			showtask.replace("deadline", rTask.getDeadline().toString());
			result = showtask.toString();
		}
		return result;
	}

	@Override
	public boolean takeTask(String userID, String taskID) {
		// TODO Auto-generated method stub
		//先要判断该工人是否满足要求，即level是否为1
		//先要根据taskID，找到对应雇主的task、从中构造出工人task
		//然后将对应字段存入worker-task表数据库
		Worker worker = workerDao.getByWid(userID).get(0);
		if (worker.getLevel()==0) {
			return false;
		}else {
			
			RTask rtaskObject = rtaskDao.getBytaskID(taskID).get(0);
			//需要先判断该任务是否能被收录
			if (rtaskObject.getState()!=1) {
				return false;
			}else {
				//从这个String中获取工人任务需要的信息成分,本质删除不要的，填入工人特有的
				String rtaskContent =  rtaskObject.getContent();
				JSONObject jsonObject = JSONObject.fromObject(rtaskContent);
				jsonObject.remove("receiveAnswers");
				jsonObject.remove("receiveWorkerID");
				jsonObject.remove("finalAnswers");
				jsonObject.put("submitAnswer", new ArrayList<>());
				Timestamp deadline = rtaskObject.getDeadline();
				Integer state = 0;
				Double each_reward =rtaskObject.getEach_reward();
				Integer type = rtaskObject.getType();
				
				//获取当前时间
				Timestamp taken_time = new Timestamp(new Date().getTime());
				Double difficult_degree = rtaskObject.getDifficult_degree();
				
				wtaskDao.save(new WTask(Integer.valueOf(userID), Integer.valueOf(taskID), jsonObject.toString(), 
						deadline, state , each_reward, taken_time, difficult_degree, type));
				//有工人收录，则对应雇主任务中已收录工人数参数也要变化
				rtaskObject.setHastaken_number(rtaskObject.getHastaken_number()+1);
				
				//需要解决若收录工人数已满则怎么处理
				//看状态标志是否为5，表示是否还能被其他工人收录
				if (rtaskObject.getHastaken_number()==rtaskObject.getWorker_number()) {
					rtaskObject.setState(5);
				}
				rtaskDao.update(rtaskObject);
				//从任务推荐表中去除
				WorkerRTask wRTask = workerRTaskDao.findByTidWid(userID, taskID).get(0);
				wRTask.setValid(0);
				workerRTaskDao.update(wRTask);
				return true;
			}
		}
	}

	@Override
	public boolean finishTask(String userID, String taskID, String answers) {
		// TODO Auto-generated method stub
		
		//需要防止单人页面多次点击提交任务
		//若type为2，则这边answer为 Array array
		WTask wTask = wtaskDao.getByWidTid(userID, taskID).get(0);
		if(wTask.getState() == 2){
			return false;
		}
		
		JSONObject wtaskContent = JSONObject.fromObject(wTask.getContent());
		wtaskContent.put("submitAnswer", answers);
		wTask.setContent(wtaskContent.toString());
		//修改任务详情，任务状态
		wTask.setState(2);
		//获取当前时间
		Timestamp finish_time = new Timestamp(new Date().getTime());
		wTask.setFinish_time(finish_time);
		
		//这里计算工人工资，需要引入外部算法
		//基础工资
		Double wbase = wTask.getEach_reward();
		//工人质量矩阵
		String wquality = workerDao.getByWid(userID).get(0).getQuality();
		//任务难度系数
		Double di = rtaskDao.getBytaskID(taskID).get(0).getDifficult_degree();
		//回答问题所用的时间长度，以分钟为单位 , getTime单位为毫秒，毫秒转换成分钟
		Double tfloat_hour = (double)(finish_time.getTime() - wTask.getTaken_time().getTime()) / (1000*60*60);
		//获取对应雇主任务中信息
		RTask rTask = rtaskDao.getBytaskID(taskID).get(0);
		Integer workerDone_number = rTask.getHasanswer_number();
		Integer worker_number = rTask.getWorker_number();
		Timestamp deadline = rTask.getDeadline();
		Timestamp begin_time = rTask.getBegin_time();
		
		Double restTime_hour = (double) ((deadline.getTime() - finish_time.getTime()) / (1000*60*60));
		Double lastTime_hour = (double) ((deadline.getTime() - begin_time.getTime()) / (1000*60*60));
		//计算工人工资
		//接口：  CaculateSalary  实现类： CaculateSalaryImpl
		//接口中方法：  Double getSalary(String wquality, Double tfloat_minute, Double wbase, Double di， 
		//Integer workerDone_number, Integer worker_number, Double restTime, Double lastTime)
		CaculateSalary caculateSalary = new CaculateSalaryImpl();
		Double get_reward = Double.valueOf(dFormat.format((caculateSalary.getSalary(wquality, tfloat_hour, wbase, di, workerDone_number, worker_number,
				restTime_hour, lastTime_hour))));
		wTask.setGet_reward(Double.valueOf(get_reward));
		wtaskDao.update(wTask);
		//这里需要跟新工人表,计算平均答题时间，还有收录的报酬等参数
		Worker worker = workerDao.getByWid(userID).get(0);
		worker.setAverage_costtime(Double.valueOf(dFormat.format(((worker.getAverage_costtime()*worker.getTotal_tasks())+tfloat_hour)/
				(worker.getTotal_tasks()+1))));
		worker.setAverage_reward(Double.valueOf(dFormat.format(((worker.getAverage_reward()*worker.getTotal_tasks())+get_reward)/
				(worker.getTotal_tasks()+1))));
		worker.setAverage_di(Double.valueOf(dFormat.format(((worker.getAverage_di()*worker.getTotal_tasks())+di)/
				(worker.getTotal_tasks()+1))));
		worker.setTotal_tasks(worker.getTotal_tasks()+1);
		//能finish则等级至少为1
		int level;
		if (worker.getTotal_tasks()<200) {
			level = 1;
		}else {
			level = worker.getTotal_tasks()%100;	
		}
		worker.setLevel(level);
		worker.setAccount(Double.valueOf(dFormat.format(worker.getAccount()+get_reward)));
		
		workerDao.update(worker);
		
		//需要修改雇主对应task字段,修改任务详情，已收到工人数，任务已花费，记录工人id
		JSONObject rtask = JSONObject.fromObject(rTask.getContent());
		JSONArray hasReceivedAnswers = rtask.getJSONArray("receiveAnswers");
		//这个answers本质是一个JSONArray,相当于ArrayList<String>,所以不用修改直接加入即可
		hasReceivedAnswers.add(answers);
		rtask.replace("receiveAnswers", hasReceivedAnswers);
		JSONArray receiveWorkerID = rtask.getJSONArray("receiveWorkerID");
		receiveWorkerID.add(userID);
		rtask.replace("receiveWorkerID", receiveWorkerID);
		rTask.setContent(rtask.toString());
		
		rTask.setHasanswer_number(rTask.getHasanswer_number()+1);
		//这里需要获取上面工人浮动工资，再写入		
		rTask.setHaspaid_cost(Double.valueOf(dFormat.format(rTask.getHaspaid_cost()+get_reward)));
		rtaskDao.update(rTask);
		//需要将雇主账户扣钱
		Integer requester_id = requestertaskDao.getByTID(taskID).get(0).getRequester_id();
		Requester requester = requesterDao.getByRID(String.valueOf(requester_id)).get(0);
		requester.setAccount(Double.valueOf(dFormat.format(requester.getAccount()-get_reward)));
		requesterDao.update(requester);
		
		if (rTask.getHasanswer_number()==rTask.getWorker_number()) {
			
			AggregateAnswer aggregateAnswer = new AggregateAnswerImp();
			MulAggregateAnswer mulAggregateAnswer = new AggregateMulAnswerImp();
			
			//获取其他答案决策方法？
			CDASAggregateAnswer cdasAggregateAnswer = new CDASAggregateAnswerImp();
			InquireAggregateAnswer inquireAggregateAnswer = new InquireAggregateAnswerImp();
			
			AggregateMulSigAnswer aggregateMulSigAnswer = new AggregateMulSigAnswerImp();
			OpAggregateMulAnswer opAggregateMulAnswer = new OpAggregateMulAnswerImp();
			
			//这里还需要考虑雇主任务已经收集满了，需要决策等后续任务，雇主任务状态修改等
			rTask.setState(2);
			//决策函数按照每一空来决策，所以需要for循环
			//每一空决策输入： top_k(ArrayString), answers(ArrayString), workerQuality(ArrayString)
			//输出： quality(ArrayString), finalAnswer(String)
			
			//获取这个task的空数
			int length = rtask.getJSONArray("sqlTargets").size();
			JSONArray finalAnswers = rtask.getJSONArray("finalAnswers");
			for (int i = 0; i < length; i++) {
				//制作参数 top_k，只有放入答案整合函数才用到，答案转换格式不需要！！
				ArrayList<String> top_k = new ArrayList<>();
				JSONArray candidateItems = new JSONArray();
				if (rtask.containsKey("candidateItems")) {
					if ((rtask.getJSONArray("candidateItems").size()-1) >= i) {
						candidateItems = rtask.getJSONArray("candidateItems").getJSONArray(i);
						top_k = TranferAnswer.top_ktranfer(candidateItems);
					}
				}
				//制作工人质量矩阵
				ArrayList<String> worker_quality = new ArrayList<>();
				for (int j = 0; j < rTask.getWorker_number(); j++) {
					String workerID = receiveWorkerID.getString(j);
					Worker worker2 = workerDao.getByWid(workerID).get(0);
					String quality = worker2.getQuality();
					worker_quality.add(quality);
				}
				ArrayList<String> update_qualitys = new ArrayList<>(); 
				
				//需要区分type
				int type = rTask.getType();
				if (type == 1) {
					//制作参数answers
					
					ArrayList<String> worker_answer = new ArrayList<>();
					for (int j = 0; j < rTask.getWorker_number(); j++) {
						worker_answer.add(TranferAnswer.answertranfer(hasReceivedAnswers.getJSONArray(j).getString(i), candidateItems));
					}	
					long truth_begin_time = System.currentTimeMillis(); 
					aggregateAnswer.AggresionAnswer(worker_answer, worker_quality, top_k);
					String finalAnswer = aggregateAnswer.AggFinalAnswer().get(0);
					long truth_finish_time = System.currentTimeMillis();

					//用其他方法决策？存储A,B,C即可
					long cdas_begin_time = System.currentTimeMillis();
					cdasAggregateAnswer.AggresionAnswer(worker_answer, worker_quality, top_k);
					String cdas = cdasAggregateAnswer.AggFinalAnswer().get(0);
					long cdas_finish_time = System.currentTimeMillis();

					long inquire_begin_time = System.currentTimeMillis();
					inquireAggregateAnswer.AggresionAnswer(worker_answer, worker_quality, top_k);
					String inquire = inquireAggregateAnswer.AggFinalAnswer().get(0);
					long inquire_finish_time = System.currentTimeMillis();

					AnswerCompare answerCompare = new AnswerCompare(Integer.valueOf(taskID), finalAnswer, cdas, inquire, String.valueOf(truth_finish_time-truth_begin_time),
							String.valueOf(cdas_finish_time-cdas_begin_time), String.valueOf(inquire_finish_time-inquire_begin_time));
					answerCompareDao.save(answerCompare);
							
					//还需要转一下
					finalAnswer = TranferAnswer.resulttranfer(finalAnswer, candidateItems);

					update_qualitys = aggregateAnswer.WriteWm();
					//更新雇主任务最终答案
					finalAnswers.add(finalAnswer);
					//将所有决策出的答案都放入
					rtask.put("finalAnswers", finalAnswers);
					rTask.setContent(rtask.toString());	
					//更新数据库对应雇主字段
					rtaskDao.update(rTask);
					
				} else if (type == 2) {

					//制作参数answers
					ArrayList<ArrayList<String>> worker_answer = new ArrayList<>();
					for (int j = 0; j < rTask.getWorker_number(); j++) {
						
						//获取该多选题的候选答案个数，将其依次转换
						ArrayList<String> worker_tranfer_answer = new ArrayList();
						int items = hasReceivedAnswers.getJSONArray(j).getJSONArray(i).size();
						for (int k = 0; k < items; k++) {
							worker_tranfer_answer.add(TranferAnswer.answertranfer(hasReceivedAnswers.getJSONArray(j).getJSONArray(i).getString(k), candidateItems));
						}
						
						worker_answer.add(worker_tranfer_answer);
					}

					//调用多选题答案决策函数
					long truth_begin_time = System.currentTimeMillis();
					mulAggregateAnswer.AggresionMulAnswer(worker_answer, worker_quality, top_k);
					String answer = mulAggregateAnswer.AggFinalAnswer().get(0);
					long truth_finish_time = System.currentTimeMillis();

					
					//用其他方法决策？存储A,B,C即可
					long op_begin_time = System.currentTimeMillis();
					opAggregateMulAnswer.AggresionMulAnswer(worker_answer, worker_quality, top_k);
					String op = opAggregateMulAnswer.AggFinalAnswer().get(0);
					long op_finish_time = System.currentTimeMillis();

					
					long sig_begin_time = System.currentTimeMillis();
					aggregateMulSigAnswer.AggresionMulSigAnswer(worker_answer, worker_quality, top_k);
					String sig = aggregateMulSigAnswer.AggFinalAnswer().get(0);
					long sig_finish_time = System.currentTimeMillis();

					MulAnswerCompare mulAnswerCompare = new MulAnswerCompare(Integer.valueOf(taskID), answer, sig, op,
							String.valueOf(truth_finish_time-truth_begin_time),String.valueOf(sig_finish_time-sig_begin_time),String.valueOf(op_finish_time-op_begin_time));
					mulAnswerCompareDao.save(mulAnswerCompare);

					//将[a,b]转换成可处理的数组
					answer = answer.substring(1, answer.length()-1);
					ArrayList<String> init_finalAnswer = new ArrayList<>();
					
					for (int j = 0; j < answer.split(",").length; j++) {
						if (j == 0) {
							init_finalAnswer.add(answer.split(",")[j]);
						}else {
							int l = answer.split(",")[j].length();
							init_finalAnswer.add(answer.split(",")[j].substring(1, l));	
						}
					}
					
					JSONArray finalAnswer = new JSONArray(); 
					
					//还需要转一下
					for (int j = 0; j < init_finalAnswer.size(); j++) {
						finalAnswer.add(TranferAnswer.resulttranfer(init_finalAnswer.get(j),  candidateItems));
					}
					update_qualitys = mulAggregateAnswer.WriteWm();
					
					//更新雇主任务最终答案
					finalAnswers.add(finalAnswer);
						
					//将所有决策出的答案都放入
					rtask.put("finalAnswers", finalAnswers);
					rTask.setContent(rtask.toString());	
					//更新数据库对应雇主字段
					rtaskDao.update(rTask);
				}
				
				//更新工人质量矩阵
				for (int j = 0; j < update_qualitys.size(); j++) {
					String update_quality = update_qualitys.get(j);
					String workerID = receiveWorkerID.getString(j);
					Worker worker2 = workerDao.getByWid(workerID).get(0);
					worker2.setQuality(update_quality);
					workerDao.update(worker2);
				}
			}
		}
		return true;
	}

	@Override
	public String editTask(String userID, String taskID) {
		// TODO Auto-generated method stub
		String jsonTask = null;
		RTask rTask = rtaskDao.getBytaskID(taskID).get(0);
		if (rTask.getState()!=1) {
			jsonTask = showTask(userID, taskID, "requester");
		}
		return jsonTask;
	}

	@Override
	public String showAllRTask(String userID) {
		// TODO Auto-generated method stub
		JSONArray tasks = new JSONArray();
		
		List<RequesterTask> requesterTasks = requestertaskDao.getByRID(userID);
		for (int i = 0; i < requesterTasks.size(); i++) {
			Integer taskID = requesterTasks.get(i).getTask_id();
			
			RTask rTask = rtaskDao.getBytaskID(String.valueOf(taskID)).get(0);
			
			//需要展示的信息： 任务ID，任务状态，截止时间，已收录工人数，已收到工人答案数，任务需要的工人数,
			//任务截止时间，任务单个报酬，预计总花费，已支出金额，任务难度系数
			
			JSONObject task = new JSONObject();
			task.put("task_id", taskID);
			task.put("state", rTask.getState());
			task.put("deadline", rTask.getDeadline().toString());
			task.put("hastaken_number", rTask.getHastaken_number());
			task.put("hasanswer_number", rTask.getHasanswer_number());
			task.put("worker_number", rTask.getWorker_number());
			task.put("each_reward", rTask.getEach_reward());
			task.put("predict_cost", rTask.getPredict_cost());
			task.put("haspaid_cost", rTask.getHaspaid_cost());
			task.put("difficult_degree", rTask.getDifficult_degree());
			tasks.add(task);
		}
		return tasks.toString();
	}

	@Override
	public String getRecommendTask(String userID) {
		// TODO Auto-generated method stub
		//在任务推荐表中找数据
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
		Date now=new Date();
		String deadline=dateFormat.format(now); 
		
		List<WorkerRTask> workerRTasks = workerRTaskDao.findByWidDeadline(userID, deadline, 1);
		
		//将任务的简要信息给前端, 包括 taskid, 收录截止时间，基础工资，难度系数，任务截止时间 ，已收录工人数/需要的工人数
		JSONArray tasks = new JSONArray();
		for (int i = 0; i < workerRTasks.size(); i++) {
			JSONObject task = new JSONObject();
			WorkerRTask workerRTask = workerRTasks.get(i);
			Integer taskID = workerRTask.getTask_id();
			Timestamp taken_deadline = workerRTask.getTaken_deadline();
			
			//还得判断对应任务是未收录过的，先获取taskid，然后在wtask中找有没有
			if (wtaskDao.getByWidTid(userID, String.valueOf(taskID)).isEmpty()) {
				RTask rTask = rtaskDao.getBytaskID(String.valueOf(taskID)).get(0);
				Double wbase = rTask.getEach_reward();
				Double di = rTask.getDifficult_degree();
				Timestamp final_deadline = rTask.getDeadline();
				Integer taken_number = rTask.getHastaken_number();
				Integer worker_number = rTask.getWorker_number();
				
				task.put("taskID", taskID);
				task.put("taken_deadline", taken_deadline.toString());
				task.put("wbase", wbase);
				task.put("di", di);
				task.put("final_deadline", final_deadline.toString());
				task.put("work_state", String.valueOf(taken_number)+"/"+String.valueOf(worker_number));
				
				tasks.add(task);
			}
		}
		return tasks.toString();
	}

	@Override
	public String getTakenTask(String userID) {
		// TODO Auto-generated method stub
		JSONArray tasks = new JSONArray();
		List<WTask> wTasks = wtaskDao.getByWid(userID);	
		for (int i = 0; i < wTasks.size(); i++) {
			WTask wTask = wTasks.get(i);
			
			//需要展示的信息： 任务ID，任务状态，截止时间,每个HIT花费
			JSONObject task = new JSONObject();
			task.put("task_id", wTask.getTask_id());
			task.put("state", wTask.getState());
			task.put("deadline", wTask.getDeadline().toString());
			task.put("each_reward", wTask.getEach_reward());
			task.put("di", wTask.getDifficult_degree());
			task.put("taken_time", wTask.getTaken_time().toString());
			
			if (wTask.getState()==2) {
				task.put("finish_time", wTask.getFinish_time().toString());
				task.put("get_reward",wTask.getGet_reward());	
			}	
			tasks.add(task);
		}
		
		return tasks.toString();
	}

	@Override
	public void findDeadlineTask() {
		// TODO Auto-generated method stub
		//需要外部传入数据作为比较的方法如下。 外部数据直接插入则  直接构造 new Timestamp(new Date().getTime())即可按要求插入数据库
		//首先找到对应超期的雇主任务
		//时间格式  %Y-%m-%d %H:%i
	
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
		Date now=new Date();
		String deadline=dateFormat.format(now); 
		
		List<RTask> rTasks = rtaskDao.findDeadlineTask(deadline);	
		for (int i = 0; i < rTasks.size(); i++) {
			RTask rTask = rTasks.get(i);
			if (rTask.getState()!=4 && rTask.getState()!=2) {
				rTask.setState(4);
				rtaskDao.update(rTask);	
				
				//找到对应的工人任务表
				String taskID = String.valueOf(rTask.getTask_id());
				List<WTask> wTasks = wtaskDao.getByTid(taskID);
				for (int j = 0; j < wTasks.size(); j++) {
					WTask wTask = wTasks.get(j);
					if (wTask.getState()!=2) {
						wTask.setState(4);
						wtaskDao.update(wTask);	
					}
				}
				
				//找到对应任务推荐表
				List<WorkerRTask> wRTasks = workerRTaskDao.findByTid(taskID);
				for (int j = 0; j < wRTasks.size(); j++) {
					wRTasks.get(j).setValid(0);
					workerRTaskDao.update(wRTasks.get(j));
				}
			}
		}
	}

	@Override
	public void findTakenDeadlineTask() {
		// TODO Auto-generated method stub
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
		Date now=new Date();
		String deadline=dateFormat.format(now); 
		try {
			
			//建立hashmap来去重，为了不再重复处理同一个任务的推荐，times有用
			HashMap<String, String> map = new HashMap<>();
			
			List<WorkerRTask> workerRTasks = workerRTaskDao.findByDeadline(deadline, 1);
			
			for (int i = 0; i < workerRTasks.size(); i++) {
				
				Integer taskID = workerRTasks.get(i).getTask_id();
				Integer times = workerRTasks.get(i).getTimes();
				String key = taskID+":"+times;

				if (map.containsKey(key)) {
					workerRTasks.get(i).setValid(0);
					workerRTaskDao.update(workerRTasks.get(i));	
					continue;
				}else{
					map.put(key, key);
				}
				
				RTask rTask = rtaskDao.getBytaskID(String.valueOf(taskID)).get(0);
				Integer taken_number = rTask.getHastaken_number();
				Integer worker_number = rTask.getWorker_number();
				//若是不在发布中状态，也无需推荐
				if (taken_number < worker_number && rTask.getState() == 1 ) {
					//这里需要调用推荐模块
					//输入工人数组： 工人id，工人质量矩阵， 工人等级(从1开始，排除已经推荐过的工人)， 平均答题时间， 平均任务难度系数， 平均任务报酬
					//输入任务： 任务ID， 截止时间， each_reward， 已收录工人数，需要工人数， 任务难度系数
					//这里需要排除已经推荐过的工人
					
					List<Worker> workersAll = workerDao.getByLevel(1); //
					List<WorkerRTask> workerRTasks2 = workerRTaskDao.findByTid(String.valueOf(taskID));
					
					//利用集合相减去重
					List<Integer> worker_id_workersAll = new ArrayList<>();
					List<Integer> worker_id_workerRTask = new ArrayList<>();
					for (int j = 0; j < workersAll.size(); j++) {
						worker_id_workersAll.add(workersAll.get(j).getWorker_id());
					}
					for (int j = 0; j < workerRTasks2.size(); j++) {
						worker_id_workerRTask.add(workerRTasks2.get(j).getWorker_id());
					}
					worker_id_workersAll.removeAll(worker_id_workerRTask);
					
					List<WorkerInfo> workerInfos = new ArrayList<>();
					for (int j = 0; j < worker_id_workersAll.size(); j++) {
						Worker worker = workerDao.getByWid(String.valueOf(worker_id_workersAll.get(j))).get(0);
						//需要判断如果数据库中该工人短时间内收到推荐任务多于10个，或者 收录的任务不多于32，则暂停
						int wrtask_number = workerRTaskDao.findByWidDeadline(String.valueOf(worker.getWorker_id()), deadline, 1).size();
						int wtask_number = wtaskDao.getByWidState(String.valueOf(worker.getWorker_id()), 0).size();
						if (wrtask_number <= 8 && wtask_number <= 20) {
							WorkerInfo workerInfo = new WorkerInfo(worker.getWorker_id(), worker.getQuality(), worker.getLevel(), worker.getAverage_costtime(),
									worker.getAverage_di(), worker.getAverage_reward());
							workerInfos.add(workerInfo);
						}
					}
					//找到对应发布的任务，取出其中有用的信息，包装发给接口类
					RequesterTaskInfo requesterTaskInfo = new RequesterTaskInfo(rTask.getTask_id(), 
								rTask.getDeadline(), rTask.getEach_reward(), rTask.getHastaken_number(),
								rTask.getWorker_number(), rTask.getDifficult_degree());
				
					//任务推荐接口： RecommendTask
					//实现类： RecommendTaskIm
					//接口方法(返回工人ID组成的数组)： String[] getRecommendTask(Integer worker_number, Integer times, List<WorkerInfo> workerInfos, RequesterTaskInfo requesterTaskInfo)
					//(返回本次轮回的截止日期) TimeStamp getTakenDeadline(Integer worker_number, Integer times, List<WorkerInfo> workerInfos, RequesterTaskInfo requesterTaskInfo);
					//这里面两个类，是自己封装的，方便取信息
					RecommendTask recommendTask = new FirstCountNumRecommend();
					String[] workerIDs = recommendTask.getRecommendTask(worker_number-taken_number, times+1, workerInfos, requesterTaskInfo);
					Timestamp taken_deadline = recommendTask.getTakenDeadline(worker_number-taken_number, times+1, 15, workerInfos, requesterTaskInfo);
					
					workerRTasks.get(i).setValid(0);
					workerRTaskDao.update(workerRTasks.get(i));
					//要写入数据库
					for(int j=0;j<workerIDs.length;j++){
						WorkerRTask workerRTask = new WorkerRTask(Integer.valueOf(workerIDs[j]), Integer.valueOf(taskID), times+1, taken_deadline, 1);
						workerRTaskDao.save(workerRTask);
					}
				}else{
					//把过期的推荐任务从数据库删除
					workerRTasks.get(i).setValid(0);
					workerRTaskDao.update(workerRTasks.get(i));	
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}

	@Override
	public String getLastestMessage(String userID) {
		// TODO Auto-generated method stub
		List<RequesterTask> requesterTasks = requestertaskDao.getByRID(userID);
		
		JSONArray messages = new JSONArray();
		List<LastestM> lastestMs = new ArrayList<>();
		
		for (int i = 0; i < requesterTasks.size(); i++) {
			List<WTask> wTasks = wtaskDao.getByTid(String.valueOf(requesterTasks.get(i).getTask_id()));
			for (int j = 0; j < wTasks.size(); j++) {
				WTask wTask = wTasks.get(j);
				Timestamp nowtime = new Timestamp(new Date().getTime());
			
				Timestamp taken_time = wTask.getTaken_time();		
				LastestM lastestM = new LastestM(wTask.getTask_id(), wTask.getWorker_id(), "taken", (nowtime.getTime()-taken_time.getTime())/(1000));	
				lastestMs.add(lastestM);
				
				Timestamp finish_time = wTask.getFinish_time();
				if (finish_time!=null) {
					LastestM lastestM2 = new LastestM(wTask.getTask_id(), wTask.getWorker_id(), "finished", (nowtime.getTime()-finish_time.getTime())/(1000));	
					lastestMs.add(lastestM2);
				}
			}
		}
		//然后需要排序,取出前5名
		Collections.sort(lastestMs);
		int count = 0;
		for (int i = 0; i < lastestMs.size(); i++) {
			if (count<5) {
				JSONObject mJsonObject = new JSONObject();
				LastestM lastestM = lastestMs.get(i);
				mJsonObject.put("taskID", lastestM.getTaskID());
				mJsonObject.put("flag", lastestM.getFlag());
				mJsonObject.put("workerLevel", workerDao.getByWid(String.valueOf(lastestM.getWorkerID())).get(0).getLevel());
				mJsonObject.put("time", lastestM.getTime());
				messages.add(mJsonObject);
				count++;	
			}
		}

		return messages.toString();
	}

}
