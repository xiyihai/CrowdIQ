package actions.extend.task;

import actions.base.TaskProcessBaseAction;

public class DeleteTaskAction extends TaskProcessBaseAction {

	//用于接受前端需要的ID号
		private String taskID;

		public String getTaskID() {
			return taskID;
		}

		public void setTaskID(String taskID) {
			this.taskID = taskID;
		}

		public String execute() {
			taskProcessService.deleteTask(taskID);
			return SUCCESS;
		}
}
