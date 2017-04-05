package domains.key;

import java.io.Serializable;

public class WTaskKey implements Serializable{

	private Integer worker_id;
	private Integer task_id;
	
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
	public WTaskKey() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((task_id == null) ? 0 : task_id.hashCode());
		result = prime * result + ((worker_id == null) ? 0 : worker_id.hashCode());
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
		WTaskKey other = (WTaskKey) obj;
		if (task_id == null) {
			if (other.task_id != null)
				return false;
		} else if (!task_id.equals(other.task_id))
			return false;
		if (worker_id == null) {
			if (other.worker_id != null)
				return false;
		} else if (!worker_id.equals(other.worker_id))
			return false;
		return true;
	}
	
	
	
}
