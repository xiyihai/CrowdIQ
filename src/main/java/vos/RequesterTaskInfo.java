package vos;

import java.sql.Timestamp;

public class RequesterTaskInfo {
	//输入任务数组： 任务ID， 截止时间， each_reward， 已收录工人数，需要工人数， 任务难度系数
	//这个类是为了帮助计算推荐任务实现
	
	private Integer taskID;
	private Timestamp deadline;
	private Double each_reward;
	private Integer hastaken_number;
	private Integer worker_number;
	private Double difficult_degree;
	public Integer getTaskID() {
		return taskID;
	}
	public void setTaskID(Integer taskID) {
		this.taskID = taskID;
	}
	public Timestamp getDeadline() {
		return deadline;
	}
	public void setDeadline(Timestamp deadline) {
		this.deadline = deadline;
	}
	public Double getEach_reward() {
		return each_reward;
	}
	public void setEach_reward(Double each_reward) {
		this.each_reward = each_reward;
	}
	public Integer getHastaken_number() {
		return hastaken_number;
	}
	public void setHastaken_number(Integer hastaken_number) {
		this.hastaken_number = hastaken_number;
	}
	public Integer getWorker_number() {
		return worker_number;
	}
	public void setWorker_number(Integer worker_number) {
		this.worker_number = worker_number;
	}
	public Double getDifficult_degree() {
		return difficult_degree;
	}
	public void setDifficult_degree(Double difficult_degree) {
		this.difficult_degree = difficult_degree;
	}
	public RequesterTaskInfo(Integer taskID, Timestamp deadline, Double each_reward, Integer hastaken_number,
			Integer worker_number, Double difficult_degree) {
		super();
		this.taskID = taskID;
		this.deadline = deadline;
		this.each_reward = each_reward;
		this.hastaken_number = hastaken_number;
		this.worker_number = worker_number;
		this.difficult_degree = difficult_degree;
	}
	
}
