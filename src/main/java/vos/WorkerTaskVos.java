package vos;

public class WorkerTaskVos extends TaskVos {

	//工人的任务状态： finished, unfinished, expired
	private String taskState;
	
	//工人提交的答案
	private String submitAnswer;

	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	public String getSubmitAnswer() {
		return submitAnswer;
	}

	public void setSubmitAnswer(String submitAnswer) {
		this.submitAnswer = submitAnswer;
	}
	
	
}
