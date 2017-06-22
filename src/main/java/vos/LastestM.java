package vos;

public class LastestM implements Comparable<LastestM>{

	private Integer taskID;
	private Integer workerID;
	private String flag;
	private Long time;
	public Integer getTaskID() {
		return taskID;
	}
	public void setTaskID(Integer taskID) {
		this.taskID = taskID;
	}
	public Integer getWorkerID() {
		return workerID;
	}
	public void setWorkerID(Integer workerID) {
		this.workerID = workerID;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	
	
	public LastestM(Integer taskID, Integer workerID, String flag, Long time) {
		super();
		this.taskID = taskID;
		this.workerID = workerID;
		this.flag = flag;
		this.time = time;
	}
	@Override
	public int compareTo(LastestM o) {
		// TODO Auto-generated method stub
		if (this.getTime() < o.getTime()) {
			return -1;
		}else {
			return 1;	
		}
	}
	
	
	
	
	
}
