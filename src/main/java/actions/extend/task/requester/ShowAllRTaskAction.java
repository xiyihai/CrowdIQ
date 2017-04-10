package actions.extend.task.requester;

import actions.base.TaskProcessBaseAction;

public class ShowAllRTaskAction extends TaskProcessBaseAction {

	private String userID;
	//用来返回雇主所有任务的简短信息
	private String tasksInfo;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getTasksInfo() {
		return tasksInfo;
	}

	public void setTasksInfo(String tasksInfo) {
		this.tasksInfo = tasksInfo;
	}
	
	public String execute(){
		tasksInfo = taskProcessService.showAllRTask(userID);
		return SUCCESS;
	}
	
}
