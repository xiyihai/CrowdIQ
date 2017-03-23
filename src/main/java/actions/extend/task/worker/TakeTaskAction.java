package actions.extend.task.worker;

import actions.base.TaskProcessBaseAction;

public class TakeTaskAction extends TaskProcessBaseAction {

	//用于接受前端需要的ID号
			private String taskID;

			public String getTaskID() {
				return taskID;
			}

			public void setTaskID(String taskID) {
				this.taskID = taskID;
			}

			public String execute() {
				taskProcessService.takeTask(taskID);
				return SUCCESS;
			}
}
