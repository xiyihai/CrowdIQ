package actions.extend;

import actions.base.ParserCrowdIQLBaseAction;

public class UploadAlgorithmAction extends ParserCrowdIQLBaseAction {

	private String userID;
	
	public String getUserID() {
		return userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}


	public String execute(){
		if (parserService.uploadAlgorithm(userID)) {
			return SUCCESS;
		}else {
			return ERROR;
		}
	}
}
