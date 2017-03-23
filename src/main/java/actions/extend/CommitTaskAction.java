package actions.extend;

import actions.base.TaskProcessBaseAction;

public class CommitTaskAction extends TaskProcessBaseAction {
	
	//用于接受前端传过来的task的json数据，若用户修改过，则和之前的build部分不同
	private String taskJSON;
	
	public String getTaskJSON() {
		return taskJSON;
	}

	public void setTaskJSON(String taskJSON) {
		this.taskJSON = taskJSON;
	}

	public String execute(){
		taskProcessService.commitTask(taskJSON);
		return SUCCESS;
	}
}
