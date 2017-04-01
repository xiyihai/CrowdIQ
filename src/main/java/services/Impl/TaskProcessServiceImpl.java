package services.Impl;

import java.sql.Timestamp;
import java.util.ArrayList;

import daos.Interface.RTaskDao;
import daos.Interface.RequesterTaskDao;
import daos.Interface.WTaskDao;
import domains.RTask;
import domains.RequesterTask;
import domains.WTask;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import services.Interface.TaskProcessService;
import vos.RequesterTaskVos;
import vos.WorkerTaskVos;

public class TaskProcessServiceImpl implements TaskProcessService {

	private RTaskDao rtaskDao;
	private WTaskDao wtaskDao;
	private RequesterTaskDao requestertaskDao;
	
	
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
		taskUI.put("receiveAnswers", null);
		taskUI.put("finalAnswers", null);
		
		//这里是要返回给前端查看的所有参数
		JSONObject taskVos = new JSONObject();
		taskVos.put("content", taskUI);
		taskVos.put("deadline", null);
		//下面这些参数待计算？？？？？？？？？？？？？？？？
		taskVos.put("each_reward", null);
		taskVos.put("hastaken_number", 0);
		taskVos.put("hasanswer_number", 0);
		taskVos.put("state", 0);
		taskVos.put("difficult_degree", null);
		taskVos.put("worker_number", null);
		taskVos.put("predict_cost", null);
		taskVos.put("haspaid_cost", 0);
		
		return taskVos.toString();
	}

	@Override
	public boolean commitTask(String userID, String taskString) {
		// TODO Auto-generated method stub
		//将这个taskString解析出元素，直接存入RTask数据库生成任务ID，然后再存入R——T数据库
		//为了利用数据库生成唯一ID，只能再将内容判断一遍来获取id值，只能假设content不可能重复
		
		JSONObject UI = JSONObject.fromObject(taskString);
		rtaskDao.save(new RTask((String)UI.get("content"), (Timestamp)UI.get("deadline"),
				(Double)UI.get("each_reward"),(Integer)UI.get("hastaken_number"),
				(Integer)UI.get("hasanswer_number"),(Integer)UI.get("state"),
				(Double)UI.get("difficult_degree"), (Integer)UI.get("worker_number"),
				(Double)UI.get("predict_cost"), (Double)UI.get("haspaid_cost")));
		
		Integer task_id = rtaskDao.getIDbyContent((String)UI.get("content")).get(0).getTask_id();
		
		RequesterTask requestertask = new RequesterTask(Integer.valueOf(userID), task_id);
		requestertaskDao.save(requestertask);
		return false;
	}

	@Override
	public boolean publishTask(String userID, String taskID) {
		// TODO Auto-generated method stub
		RTask rTask = rtaskDao.get(RTask.class, Integer.valueOf(taskID));
		rTask.setState(1);
		rtaskDao.save(rTask);
		//?????????????发布的任务可能还要进行额外的操作
		return true;
	}

	@Override
	public boolean pauseTask(String userID, String taskID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteTask(String userID, String taskID, String flag) {
		// TODO Auto-generated method stub
		//这里需要判断任务状态才能删除
		if (flag.equals("worker")) {
			wtaskDao.delete(wtaskDao.getByWidTid(userID, taskID).get(0));
		}else if (flag.equals("requester")) {
			rtaskDao.delete(rtaskDao.get(RTask.class, taskID));
			requestertaskDao.delete(requestertaskDao.getBy2ID(userID, taskID).get(0));
		}
		
		return false;
	}

	@Override
	public String showTask(String userID, String taskID, String flag) {
		// TODO Auto-generated method stub
		String result = null;
		
		if (flag.equals("worker")) {
			result = wtaskDao.getByWidTid(userID, taskID).get(0).getContent();
		}else if (flag.equals("requester")) {
			//这里应该获取数据库中所有参数，最终作为一个JSONObject返回给用户，删选工作交给前端
			RTask rTask = rtaskDao.get(RTask.class, taskID);
			result = JSONObject.fromObject(rTask).toString();
		}
		return result;
	}

	@Override
	public boolean takeTask(String userID, String taskID) {
		// TODO Auto-generated method stub
		//先要根据taskID，找到对应雇主的task、从中构造出工人task
		//然后将对应字段存入worker-task表数据库
		
		RTask rtaskObject = rtaskDao.get(RTask.class, taskID);
		//从这个String中获取工人任务需要的信息成分,本质删除不要的，填入工人特有的
		String rtaskContent =  rtaskObject.getContent();
		JSONObject jsonObject = JSONObject.fromObject(rtaskContent);
		jsonObject.remove("receiveAnswers");
		jsonObject.remove("finalAnswers");
		jsonObject.put("submitAnswer", null);
		
		Timestamp deadline = rtaskObject.getDeadline();
		Integer state = 0;
		Double each_reward =rtaskObject.getEach_reward();
		
		wtaskDao.save(new WTask(Integer.valueOf(userID), Integer.valueOf(taskID), jsonObject.toString(), 
				deadline, state , each_reward));

		//有工人收录，则对应雇主任务中已收录工人数参数也要变化
		rtaskObject.setHastaken_number(rtaskObject.getHastaken_number()+1);
		rtaskDao.save(rtaskObject);
		//？？？？？？？？？？？？？？？？？需要解决若收录工人数已满则怎么处理
		
		return true;
	}

	@Override
	public boolean finishTask(String userID, String taskID, String answers) {
		// TODO Auto-generated method stub
		WTask wTask = wtaskDao.getByWidTid(userID, taskID).get(0);
		JSONObject wtaskContent = JSONObject.fromObject(wTask.getContent());
		wtaskContent.put("submitAnswer", answers);
		wTask.setContent(wtaskContent.toString());
		//修改任务详情，任务状态
		wTask.setState(2);
		wtaskDao.save(wTask);
		
		//需要修改雇主对应task字段,修改任务详情，已收到工人数，任务已花费
		RTask rTask = rtaskDao.get(RTask.class, taskID);
		JSONObject rtask = JSONObject.fromObject(rTask.getContent());
		JSONArray hasReceivedAnswers = rtask.getJSONArray("receiveAnswers");
		//这个answers本质是一个JSONArray,相当于ArrayList<String>,所以不用修改直接加入即可
		hasReceivedAnswers.add(answers);
		rtask.replace("receiveAnswers", hasReceivedAnswers);
		rTask.setContent(rtask.toString());
		
		rTask.setHasanswer_number(rTask.getHasanswer_number()+1);
		rTask.setHaspaid_cost(rTask.getHaspaid_cost()+rTask.getEach_reward());
		
		rtaskDao.save(rTask);
		//???????????????这里还需要考虑雇主任务已经收集满了，需要决策等后续任务，雇主任务状态修改等
		
		return true;
	}

	@Override
	public boolean addAnswerTask(String taskID, String answer) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
