package actions.extend.task;

import actions.base.TaskProcessBaseAction;

public class DeleteTaskAction extends TaskProcessBaseAction {

	//用于接受前端需要的ID号
		private String taskID;
		private String userID;
		private String flag;
		
		public String getFlag() {
			return flag;
		}

		public void setFlag(String flag) {
			this.flag = flag;
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
			if (taskProcessService.deleteTask(userID, taskID, flag)) {
				return SUCCESS;
			}else {
				return ERROR;
			}
		}
}
