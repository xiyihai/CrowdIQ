package actions.extend.task.worker;

import actions.base.TaskProcessBaseAction;

public class ShowAllTaskAction extends TaskProcessBaseAction {

	//用于展示所有目前数据库中可做的任务,及其相应信息
	private String taskInfo;

	public String getTaskInfo() {
		return taskInfo;
	}

	public void setTaskInfo(String taskInfo) {
		this.taskInfo = taskInfo;
	}
	
	public String execute(){
		taskInfo = taskProcessService.showAllAvailableTask();
		return SUCCESS;
	}
	
}
