package services.Interface;

public interface TaskProcessService {
	
	//雇主
	//前端确定UI之后, 根据前端传过来的信息, 生成初步的task展示表返回给用户。
	//前端传过来的是一个JOSNObject，返回给用户也是JSONObject,，不过里面东西更加集全，包括价格信息等
	//转成java类是出于task中有很多重要信息，java类更加方便处理
	String buildTask(String taskString);
	
	//雇主确定修改task展示表之后，这里taskString和build部分可能不同，正式将用户ID，任务ID存入数据库，任务作为整个JSONObject字符串存入
	boolean commitTask(String userID, String taskString);
	
	//雇主点击发布任务，根据任务ID，索引到对应任务发布，需要更改任务状态
	boolean publishTask(String userID, String taskID);
	
	//雇主针对发布中的任务暂停
	boolean pauseTask(String userID, String taskID);
	
	//删除任务，需要区分雇主和工人。雇主必须是暂停之后才能删除，工人则都可以
	boolean deleteTask(String userID, String taskID, String flag);
	
	//雇主或工人（这里要做好区分工作）查看某task，返回task的整个内容。
	String showTask(String userID, String taskID, String flag);
	
	//工人确定要收录该任务，将工人ID，任务ID，写入数据库
	boolean takeTask(String userID, String taskID);
	
	//工人做好该任务，需要将答案写入工人task的属性中，然后根据ID号找到雇主taskID号，修改雇主任务
	boolean finishTask(String userID, String taskID, String answers);
	
	//工人提交了任务之后，对应雇主任务也需要修改。此函数放在finishTask里面，同时需要判断雇主任务是否已满，
	//若满则还需要更改雇主ID详情表的中任务状态，并且调用决策模块得到答案，在写入数据库
	boolean addAnswerTask(String taskID, String answer);
	
}
