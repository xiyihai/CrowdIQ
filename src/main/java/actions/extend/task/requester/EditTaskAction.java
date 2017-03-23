package actions.extend.task.requester;

import actions.base.TaskProcessBaseAction;

public class EditTaskAction extends TaskProcessBaseAction {

		//用于接受前端需要发布任务的ID号
		private String taskID;
		
		//用于返回待修改的真个task数据
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

		public String execute() {
			taskJSON = taskProcessService.showTask(taskID);
			return SUCCESS;
		}
}
