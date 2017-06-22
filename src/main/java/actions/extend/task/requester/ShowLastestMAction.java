package actions.extend.task.requester;

import actions.base.TaskProcessBaseAction;

public class ShowLastestMAction extends TaskProcessBaseAction {
	
	private String userID;

	private String messages;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String execute(){
		messages = taskProcessService.getLastestMessage(userID);
		return SUCCESS;
	}
}
