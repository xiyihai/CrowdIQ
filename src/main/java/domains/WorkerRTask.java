package domains;

import java.sql.Timestamp;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import domains.key.WTaskKey;

@Entity
@Table(name="worker_recommendtask")
@IdClass(WTaskKey.class)
@Cacheable
public class WorkerRTask {

	@Id
	@Column(name="worker_id")
	private Integer worker_id;
	
	@Id
	@Column(name="task_id")
	private Integer task_id;
	
	@Column(name="times")
	private Integer times;
	
	@Column(name="taken_deadline")
	private Timestamp taken_deadline;
	
	@Column(name="valid")
	private Integer valid;

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

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Timestamp getTaken_deadline() {
		return taken_deadline;
	}

	public void setTaken_deadline(Timestamp taken_deadline) {
		this.taken_deadline = taken_deadline;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public WorkerRTask(Integer worker_id, Integer task_id, Integer times, Timestamp taken_deadline, Integer valid) {
		super();
		this.worker_id = worker_id;
		this.task_id = task_id;
		this.times = times;
		this.taken_deadline = taken_deadline;
		this.valid = valid;
	}

	public WorkerRTask() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
