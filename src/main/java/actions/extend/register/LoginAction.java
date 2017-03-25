package actions.extend.register;

import actions.base.RegisterBaseAction;

public class LoginAction extends RegisterBaseAction {

	private String userID;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String execute(){
		if (registerService.login(userID)) {
			return SUCCESS;
		}else {
			return ERROR;
		}
	}
}
