package actions.extend.task.requester;

import actions.base.TaskProcessBaseAction;

public class PublishTaskAction extends TaskProcessBaseAction {

	//用于接受前端需要发布任务的ID号
	private String taskID;

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public String execute() {
		taskProcessService.publishTask(taskID);
		return SUCCESS;
	}
}

