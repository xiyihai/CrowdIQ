package schedules;

import services.Interface.TaskProcessService;

public class TaskDeadline {
	private boolean isRunning = false;
	
	//spring依赖注入的时候应该是根据后面的 uvs来的，包括setter/getter方法都是uvs
	//不然前面是是String，不见得在 getString()/setString()
	private TaskProcessService taskProcessService;
	
	public TaskProcessService getTaskProcessService() {
		return taskProcessService;
	}

	public void setTaskProcessService(TaskProcessService taskProcessService) {
		this.taskProcessService = taskProcessService;
	}

	protected void execute() {
		if (!isRunning) {
			isRunning=true;
			taskProcessService.findDeadlineTask();
			taskProcessService.findTakenDeadlineTask();
			isRunning=false;
			
		}
	}
}
