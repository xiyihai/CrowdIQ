package actions.extend.task.worker;

import actions.base.TaskProcessBaseAction;

public class PreviewTaskAction extends TaskProcessBaseAction {

	private String userID;
	private String taskID;
	
	private String taskJSON;
	
	
	
	public String getTaskJSON() {
		return taskJSON;
	}

	public void setTaskJSON(String taskJSON) {
		this.taskJSON = taskJSON;
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String execute(){
		//这本是工人action，但这个预览工作查看的表是雇主task
		taskJSON = taskProcessService.showTask(userID, taskID, "requester");
		return SUCCESS;
	}
}
