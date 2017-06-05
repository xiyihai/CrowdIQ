package services.Interface;

public interface RegisterService {

	//雇主和工人注册信息，程序要去区分对待，员工的还要额外往数据库加入  员工ID，测试题任务ID
	boolean register(String information);

	//前端工人请求做测试任务，后端根据ID返回对应json数据
	String getTestTask(String userID, String taskID);
	
	//后端Service存入数据库 工人ID，测试任务ID，完成状态，测试答案。
	//同时需要判断表中测试问题是否完成大于10题，若大于则更新计算工人质量矩阵
	boolean finishTestTask(String userID, String taskID, String answer);
	
	//需要区分雇主和工人
	String login(String informationJSON);
	
	String getLoginInfo(String userID, String flag);
	
	//返回所有该工人的测试任务ID号
	String getAllTestTask(String userID);
	
	//返回测试任务详情
	String showDoneTestTask(String userID, String taskID);
}
