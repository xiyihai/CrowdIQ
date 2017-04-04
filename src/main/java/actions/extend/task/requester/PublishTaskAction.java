package actions.extend.task.requester;

import actions.base.TaskProcessBaseAction;

public class PublishTaskAction extends TaskProcessBaseAction {

	//用于接受前端需要发布任务的ID号
	private String taskID;
	private String userID;

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

	public String execute() {
		if (taskProcessService.publishTask(userID, taskID)) {
			return SUCCESS;		
		}else {
			return ERROR;
		}
	}
}

