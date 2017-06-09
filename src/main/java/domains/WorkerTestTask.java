package domains;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="worker_testtask")
@IdClass(domains.key.WorkerTestTaskKey.class)
@Cacheable
public class WorkerTestTask {

	@Id
	@Column(name="worker_id")
	private Integer worker_id;
	
	@Id
	@Column(name="testtask_id")
	private Integer testtask_id;
	
	@Column(name="state")
	private Integer state;
	
	@Column(name="worker_answer")
	private String worker_answer;

	@Column(name="iscorrect")
	private Integer iscorrect;
	
	public WorkerTestTask(Integer worker_id, Integer testtask_id, Integer state, String worker_answer, Integer iscorrect) {
		super();
		this.worker_id = worker_id;
		this.testtask_id = testtask_id;
		this.state = state;
		this.worker_answer = worker_answer;
		this.iscorrect = iscorrect;
	}

	public Integer getWorker_id() {
		return worker_id;
	}

	public void setWorker_id(Integer worker_id) {
		this.worker_id = worker_id;
	}

	public Integer getTesttask_id() {
		return testtask_id;
	}

	public void setTesttask_id(Integer testtask_id) {
		this.testtask_id = testtask_id;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getWorker_answer() {
		return worker_answer;
	}

	public void setWorker_answer(String worker_answer) {
		this.worker_answer = worker_answer;
	}

	public Integer getIscorrect() {
		return iscorrect;
	}

	public void setIscorrect(Integer iscorrect) {
		this.iscorrect = iscorrect;
	}

	public WorkerTestTask() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
