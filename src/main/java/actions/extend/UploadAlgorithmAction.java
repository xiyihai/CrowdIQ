package actions.extend;

import actions.base.ParserCrowdIQLBaseAction;

public class UploadAlgorithmAction extends ParserCrowdIQLBaseAction {

	
	public String execute(){
		if (parserService.uploadAlgorithm()) {
			return SUCCESS;
		}else {
			return ERROR;
		}
	}
}
