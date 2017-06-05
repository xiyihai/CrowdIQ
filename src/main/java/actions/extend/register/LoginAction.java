package actions.extend.register;

import actions.base.RegisterBaseAction;

public class LoginAction extends RegisterBaseAction {

	//用于接受前端的邮箱，密码，和flag表示类别
	private String informationJSON;

	//这个ID用于在前端显示
	private String userID;

	public String getInformationJSON() {
		return informationJSON;
	}

	public void setInformationJSON(String informationJSON) {
		this.informationJSON = informationJSON;
	}
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String execute(){
		userID = registerService.login(informationJSON);
		if (userID!=null) {
			return SUCCESS;
		}else {
			return ERROR;
		}
	}
}
