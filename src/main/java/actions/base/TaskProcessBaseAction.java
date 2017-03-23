package actions.base;

import com.opensymphony.xwork2.ActionSupport;

import services.Interface.TaskProcessService;

public class TaskProcessBaseAction extends ActionSupport {
	
	protected TaskProcessService taskProcessService;

	public void setTaskProcessService(TaskProcessService taskProcessService) {
		this.taskProcessService = taskProcessService;
	}
	
}
