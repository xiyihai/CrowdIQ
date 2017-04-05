package domains.key;

import java.io.Serializable;

public class RequesterTaskKey implements Serializable {

	private Integer requester_id;
	private Integer task_id;
	public RequesterTaskKey() {
		super();
		// TODO Auto-generated constructor stub
	}
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((requester_id == null) ? 0 : requester_id.hashCode());
		result = prime * result + ((task_id == null) ? 0 : task_id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequesterTaskKey other = (RequesterTaskKey) obj;
		if (requester_id == null) {
			if (other.requester_id != null)
				return false;
		} else if (!requester_id.equals(other.requester_id))
			return false;
		if (task_id == null) {
			if (other.task_id != null)
				return false;
		} else if (!task_id.equals(other.task_id))
			return false;
		return true;
	}
	
	
}
