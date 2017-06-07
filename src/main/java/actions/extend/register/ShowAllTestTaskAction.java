package actions.extend.register;

import actions.base.RegisterBaseAction;

public class ShowAllTestTaskAction extends RegisterBaseAction {

	//用于接受前端
	private String userID;
	
	//工人点击测试任务模块，用来返回其对应的worker-testtask表内容，以JSONArray[ JSONObject, JSONObject]形式
	//删减工作交给前端
	private String taskInfo;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getTaskInfo() {
		return taskInfo;
	}

	public void setTaskInfo(String taskInfo) {
		this.taskInfo = taskInfo;
	}
	
	public String execute(){
		taskInfo = registerService.getAllTestTask(userID);
		return SUCCESS;
	}
	
}
