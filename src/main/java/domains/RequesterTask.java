package domains;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="requester_task")
public class RequesterTask {

	@Column(name="requester_id")
	private Integer requester_id;
	
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
	
}
