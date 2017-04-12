package actions.extend.register;

import actions.base.RegisterBaseAction;

public class ShowDoneTestTaskAction extends RegisterBaseAction {
		//前端接受工人请求的ID
		private String taskID;
		private String userID;

		public String getUserID() {
			return userID;
		}

		public void setUserID(String userID) {
			this.userID = userID;
		}
		//后端返回的测试任务详情,测试任务答案的json数据
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
		
		public String execute(){
			taskJSON = registerService.showDoneTestTask(userID, taskID);
			return SUCCESS;
		}
}
