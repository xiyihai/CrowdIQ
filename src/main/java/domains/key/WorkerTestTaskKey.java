package domains.key;

import java.io.Serializable;

public class WorkerTestTaskKey implements Serializable {

	private Integer worker_id;
	private Integer testtask_id;
	public WorkerTestTaskKey() {
		super();
		// TODO Auto-generated constructor stub
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((testtask_id == null) ? 0 : testtask_id.hashCode());
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
		WorkerTestTaskKey other = (WorkerTestTaskKey) obj;
		if (testtask_id == null) {
			if (other.testtask_id != null)
				return false;
		} else if (!testtask_id.equals(other.testtask_id))
			return false;
		if (worker_id == null) {
			if (other.worker_id != null)
				return false;
		} else if (!worker_id.equals(other.worker_id))
			return false;
		return true;
	} 
	
	
}
