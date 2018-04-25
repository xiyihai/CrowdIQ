package actions.extend.task.worker;


import com.mchange.v2.reflect.ReflectUtils;

import actions.base.TaskProcessBaseAction;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class FinishTaskAction extends TaskProcessBaseAction {

	//用于接受前端工人答案
	private String answers;
	//用于接受前端任务ID号
	private String taskID;
	
	private String userID;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getAnswers() {
		return answers;
	}
	public void setAnswers(String answers) {
		this.answers = answers;
	}
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	
	public String execute(){
		if (JSONArray.fromObject(answers).size() == 0) {
			return ERROR;
		}
		if (taskProcessService.finishTask(userID, taskID, answers)) {
			return SUCCESS;	
		}else {
			return ERROR;
		}
		
	}
	
}
