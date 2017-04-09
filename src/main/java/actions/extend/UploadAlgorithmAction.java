package actions.extend;

import actions.base.ParserCrowdIQLBaseAction;

public class UploadAlgorithmAction extends ParserCrowdIQLBaseAction {

	private String userID;
	private String algorithmname;
	
	public String getAlgorithmname() {
		return algorithmname;
	}

	public void setAlgorithmname(String algorithmname) {
		this.algorithmname = algorithmname;
	}

	public String getUserID() {
		return userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}


	public String execute(){
		if (parserService.uploadAlgorithm(userID, algorithmname)) {
			return SUCCESS;
		}else {
			return ERROR;
		}
	}
}
