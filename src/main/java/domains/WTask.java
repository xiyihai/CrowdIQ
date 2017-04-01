package domains;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="worker_task")
public class WTask {

	@Column(name="worker_id")
	private Integer worker_id;
	
	@Column(name="task_id")
	private Integer task_id;
	
	@Column(name="content")
	private String content;

	@Column(name="deadline")
	private Timestamp deadline;
	
	@Column(name="state")
	private Integer state;

	@Column(name="each_reward")
	private Double each_reward;

	public Integer getWorker_id() {
		return worker_id;
	}

	public void setWorker_id(Integer worker_id) {
		this.worker_id = worker_id;
	}

	public Integer getTask_id() {
		return task_id;
	}

	public void setTask_id(Integer task_id) {
		this.task_id = task_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getDeadline() {
		return deadline;
	}

	public void setDeadline(Timestamp deadline) {
		this.deadline = deadline;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Double getEach_reward() {
		return each_reward;
	}

	public void setEach_reward(Double each_reward) {
		this.each_reward = each_reward;
	}

	public WTask(Integer worker_id, Integer task_id, String content, Timestamp deadline, Integer state,
			Double each_reward) {
		super();
		this.worker_id = worker_id;
		this.task_id = task_id;
		this.content = content;
		this.deadline = deadline;
		this.state = state;
		this.each_reward = each_reward;
	}
	
	
	
}
