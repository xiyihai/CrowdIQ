package services.Interface;

public interface TaskProcessService {
	
	//雇主
	//前端确定UI之后, 根据前端传过来的信息, 生成初步的task展示表返回给用户。
	//前端传过来的是一个JOSNObject，返回给用户也是JSONObject,，不过里面东西更加集全，包括价格信息等
	//转成java类是出于task中有很多重要信息，java类更加方便处理
	String buildTask(String taskString);
	
	//雇主确定修改task展示表之后，这里taskString和build部分可能不同，正式将用户ID，任务ID存入数据库，任务作为整个JSONObject字符串存入
	boolean commitTask(String taskString);
	
	//雇主点击发布任务，根据任务ID，索引到对应任务发布，需要更改任务状态
	boolean publishTask(String taskID);
	
	//雇主针对发布中的任务暂停
	boolean pauseTask(String taskID);
	
	//删除任务，需要区分雇主和工人。必须是暂停之后才能删除
	boolean deleteTask(String taskID);
	
	//雇主或工人（这里要做好区分工作）查看某task，返回task的整个内容。
	String showTask(String taskID);
	
	//工人确定要收录该任务，将工人ID，任务ID，写入数据库
	boolean takeTask(String taskID);
	
	//工人做好该任务，需要将答案写入task的属性中
	boolean finishTask(String taskID, String answers);
}
