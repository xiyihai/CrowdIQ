package actions.extend.task;

import actions.base.TaskProcessBaseAction;

public class ShowTaskAction extends TaskProcessBaseAction {
	//用于接受前端需要发布任务的ID号
	private String taskID;
	//用于返回待修改的整个task数据
	private String taskJSON;
	private String userID;
	//区分标志
	private String flag;

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
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

	public String execute() {
		taskJSON = taskProcessService.showTask(userID, taskID, flag);
		return SUCCESS;
	}
}
