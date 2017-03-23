package vos;

import java.util.ArrayList;

public class TaskVos {
	
	private String taskID;
	
	private String userID;
	private String tableID;
	
	//sql语句中select目标可能不止一个，这个目标存取还是用sql解析中的原始数据。 select columns[2],headers[2]
	private ArrayList<String> sqlTargets;
	
	//sql语句中showing rows[2],rows[3],columns 所以String内容可能是多维数组，这里都以字符串存储
	private ArrayList<String> showing_contents;
	
	//sql语句中根据算法提供的top-k， using headercover on columns[2],columns[3]
	//每一个String都是一维数组[county:0.12, nation:0.1] 
	private ArrayList<String> candidateItems;
	
	//这里answer是经过决策过后获取的答案， String对应sqlTarger，可能是一维或者二维数组
	private ArrayList<String> answers;
	
	//这是任务的标识符，表示当前任务的状态：待发布，发布中，未完成（超时间未完成），已完成（获取到正确答案）
	private String taskState;
	
	public ArrayList<String> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
	}

	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	//通过计算得到的该任务的难度系数
	private double difficultDegree;
	
	//该任务需要的工人数量， 先系统预估， 后经过雇主确认修改
	private int workerNumber;
	
	//每个HIT任务的花费
	private double eachCost;
	
	//该任务总的花费
	private double sumCost;
	
	//该任务截止时间
	private String time;

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getTableID() {
		return tableID;
	}

	public void setTableID(String tableID) {
		this.tableID = tableID;
	}

	public ArrayList<String> getSqlTargets() {
		return sqlTargets;
	}

	public void setSqlTargets(ArrayList<String> sqlTargets) {
		this.sqlTargets = sqlTargets;
	}

	public ArrayList<String> getShowing_contents() {
		return showing_contents;
	}

	public void setShowing_contents(ArrayList<String> showing_contents) {
		this.showing_contents = showing_contents;
	}

	public ArrayList<String> getCandidateItems() {
		return candidateItems;
	}

	public void setCandidateItems(ArrayList<String> candidateItems) {
		this.candidateItems = candidateItems;
	}

	public double getDifficultDegree() {
		return difficultDegree;
	}

	public void setDifficultDegree(double difficultDegree) {
		this.difficultDegree = difficultDegree;
	}

	public int getWorkerNumber() {
		return workerNumber;
	}

	public void setWorkerNumber(int workerNumber) {
		this.workerNumber = workerNumber;
	}

	public double getEachCost() {
		return eachCost;
	}

	public void setEachCost(double eachCost) {
		this.eachCost = eachCost;
	}

	public double getSumCost() {
		return sumCost;
	}

	public void setSumCost(double sumCost) {
		this.sumCost = sumCost;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	
	
}
