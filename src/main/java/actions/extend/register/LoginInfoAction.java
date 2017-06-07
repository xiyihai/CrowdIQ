package actions.extend.register;

import actions.base.RegisterBaseAction;

public class LoginInfoAction extends RegisterBaseAction {

	//接受前端数据
	private String userID;
	private String flag;
	
	private String showinformation;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getShowinformation() {
		return showinformation;
	}

	public void setShowinformation(String showinformation) {
		this.showinformation = showinformation;
	}
	
	public String execute(){		
		showinformation = registerService.getLoginInfo(userID, flag);
		return SUCCESS;
	}
	
}
