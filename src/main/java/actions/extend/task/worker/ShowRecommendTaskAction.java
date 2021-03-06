package actions.extend.task.worker;

import actions.base.TaskProcessBaseAction;

public class ShowRecommendTaskAction extends TaskProcessBaseAction {

	private String userID;

	private String taskInfo;
	
	
	public String getTaskInfo() {
		return taskInfo;
	}

	public void setTaskInfo(String taskInfo) {
		this.taskInfo = taskInfo;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String execute(){
		taskInfo = taskProcessService.getRecommendTask(userID);
		return SUCCESS;
	}
}
