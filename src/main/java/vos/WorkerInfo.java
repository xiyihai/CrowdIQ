package vos;

public class WorkerInfo {
	
	//为了制作推荐任务用的类
	private Integer worker_id;
	private String quality;
	private Integer level;
	private Double aver_costtime;
	private Double aver_di;
	private Double aver_reward;
	
	public Integer getWorker_id() {
		return worker_id;
	}
	public void setWorker_id(Integer worker_id) {
		this.worker_id = worker_id;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Double getAver_costtime() {
		return aver_costtime;
	}
	public void setAver_costtime(Double aver_costtime) {
		this.aver_costtime = aver_costtime;
	}
	public Double getAver_di() {
		return aver_di;
	}
	public void setAver_di(Double aver_di) {
		this.aver_di = aver_di;
	}
	public Double getAver_reward() {
		return aver_reward;
	}
	public void setAver_reward(Double aver_reward) {
		this.aver_reward = aver_reward;
	}
	public WorkerInfo(Integer worker_id, String quality, Integer level, Double aver_costtime, Double aver_di,
			Double aver_reward) {
		super();
		this.worker_id = worker_id;
		this.quality = quality;
		this.level = level;
		this.aver_costtime = aver_costtime;
		this.aver_di = aver_di;
		this.aver_reward = aver_reward;
	}
	
}
