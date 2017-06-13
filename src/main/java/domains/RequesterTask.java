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
@Table(name="requester_task")
@IdClass(domains.key.RequesterTaskKey.class)
@Cacheable
public class RequesterTask {

	@Id
	@Column(name="requester_id")
	private Integer requester_id;
	
	@Id
	@Column(name="task_id")
	private Integer task_id;

	public Integer getRequester_id() {
		return requester_id;
	}

	public void setRequester_id(Integer requester_id) {
		this.requester_id = requester_id;
	}

	public Integer getTask_id() {
		return task_id;
	}

	public void setTask_id(Integer task_id) {
		this.task_id = task_id;
	}

	public RequesterTask(Integer requester_id, Integer task_id) {
		super();
		this.requester_id = requester_id;
		this.task_id = task_id;
	}

	public RequesterTask() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
