package actions.extend.task.requester;

import actions.base.TaskProcessBaseAction;

public class CommitEditTaskAction extends TaskProcessBaseAction {
	//用于接受前端传过来的task的json数据，若用户修改过，则和之前的build部分不同,这里面需要包含tablename
		//这里taskID存在于taskJSON中
		private String taskJSON;
		private String userID;
		private String taskID;
	
		public String getTaskID() {
			return taskID;
		}

		public void setTaskID(String taskID) {
			this.taskID = taskID;
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

		public String execute(){
			if (taskProcessService.commitEditTask(userID, taskID, taskJSON)) {
				return SUCCESS;	
			}else {
				return ERROR;
			}
		}

}
