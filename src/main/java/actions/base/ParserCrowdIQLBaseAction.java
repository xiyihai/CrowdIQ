package actions.base;

import com.opensymphony.xwork2.ActionSupport;

import services.Interface.ParserCrowdIQLService;
import services.Interface.ReadTableService;

public class ParserCrowdIQLBaseAction extends ActionSupport{
	
	protected ParserCrowdIQLService parserService;
	protected ReadTableService readService;

	public void setParserService(ParserCrowdIQLService parserService) {
		this.parserService = parserService;
	}

	public void setReadService(ReadTableService readService) {
		this.readService = readService;
	}
	
	
}
