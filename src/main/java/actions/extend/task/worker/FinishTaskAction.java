package actions.extend.task.worker;

import actions.base.TaskProcessBaseAction;

public class FinishTaskAction extends TaskProcessBaseAction {

	//用于接受前端工人答案
	private String answers;
	//用于接受前端任务ID号
	private String taskID;
	public String getAnswers() {
		return answers;
	}
	public void setAnswers(String answers) {
		this.answers = answers;
	}
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	
	public String execute(){
		taskProcessService.finishTask(taskID, answers);
		return SUCCESS;
	}
	
}
