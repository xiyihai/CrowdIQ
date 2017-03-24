package services.Interface;

public interface RegisterService {

	//雇主和工人注册信息，程序要去区分对待，员工的还要额外往数据库加入  员工ID，测试题任务ID
	boolean register(String information);
	
	String getTestTask(String taskID);
}
