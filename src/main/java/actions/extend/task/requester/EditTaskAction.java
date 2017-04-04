package actions.extend.task.requester;

import actions.base.TaskProcessBaseAction;

public class EditTaskAction extends TaskProcessBaseAction {

		//用于接受前端需要任务的ID号
		private String taskID;
		
		//用于返回待修改的整个task数据
		private String taskJSON;
		private String userID;

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
			taskJSON = taskProcessService.editTask(userID, taskID);
			if(taskJSON!=null){
				return SUCCESS;	
			}else {
				return ERROR;
			}
			
		}
}
