package actions.extend.register;

import actions.base.RegisterBaseAction;

public class FinishTestTaskAction extends RegisterBaseAction {

	//用于接受前端数据，任务ID，工人答案
	private String taskID;
	private String userID;
	private String answer;
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public String execute() {
		if (registerService.finishTestTask(userID, taskID, answer)) {
			return SUCCESS;	
		}else {
			return ERROR;
		}
		
	}
	
}
