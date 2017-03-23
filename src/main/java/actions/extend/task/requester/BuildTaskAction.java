package actions.extend.task.requester;

import actions.base.TaskProcessBaseAction;

public class BuildTaskAction extends TaskProcessBaseAction {

	//用于接受前端用户确定修改UI之后，传过来的jsonObject字符串
	private String uiJSON;
	
	//用于传给前端整个task的结构，
	private String taskJSON;
	
	
	public String getUiJSON() {
		return uiJSON;
	}

	public void setUiJSON(String uiJSON) {
		this.uiJSON = uiJSON;
	}

	public String getTaskJSON() {
		return taskJSON;
	}

	public void setTaskJSON(String taskJSON) {
		this.taskJSON = taskJSON;
	}

	public String execute(){
		taskJSON = taskProcessService.buildTask(uiJSON);
		return SUCCESS;
	}
}
