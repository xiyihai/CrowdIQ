package domains;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="answer_comparsion")

@Cacheable
public class AnswerCompare {
	@Id
	@Column(name="task_id")
	private Integer task_id;
	
	@Column(name="truth_answer")
	private String truth_answer;
	
	@Column(name="cdas")
	private String cdas;
	
	@Column(name="inquire")
	private String inquire;
	
	@Column(name="truth_time")
	private String truth_time;
	
	@Column(name="cdas_time")
	private String cdas_time;
	
	@Column(name="inquire_time")
	private String inquire_time;
	
	public Integer getTask_id() {
		return task_id;
	}

	public void setTask_id(Integer task_id) {
		this.task_id = task_id;
	}

	public String getTruth_answer() {
		return truth_answer;
	}

	public void setTruth_answer(String truth_answer) {
		this.truth_answer = truth_answer;
	}

	public String getCdas() {
		return cdas;
	}

	public void setCdas(String cdas) {
		this.cdas = cdas;
	}

	public String getInquire() {
		return inquire;
	}

	public void setInquire(String inquire) {
		this.inquire = inquire;
	}

	public String getTruth_time() {
		return truth_time;
	}

	public void setTruth_time(String truth_time) {
		this.truth_time = truth_time;
	}

	public String getCdas_time() {
		return cdas_time;
	}

	public void setCdas_time(String cdas_time) {
		this.cdas_time = cdas_time;
	}

	public String getInquire_time() {
		return inquire_time;
	}

	public void setInquire_time(String inquire_time) {
		this.inquire_time = inquire_time;
	}

	public AnswerCompare(Integer task_id, String truth_answer, String cdas, String inquire, String truth_time,
			String cdas_time, String inquire_time) {
		super();
		this.task_id = task_id;
		this.truth_answer = truth_answer;
		this.cdas = cdas;
		this.inquire = inquire;
		this.truth_time = truth_time;
		this.cdas_time = cdas_time;
		this.inquire_time = inquire_time;
	}


	
}
