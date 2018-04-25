package domains;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mulanswer_comparsion")
@Cacheable
public class MulAnswerCompare {
	@Id
	@Column(name="task_id")
	private Integer task_id;
	
	@Column(name="truth_answer")
	private String truth_answer;
	
	@Column(name="sigmulti")
	private String sigmulti;
	
	@Column(name="opmulti")
	private String opmulti;

	@Column(name="truth_time")
	private String truth_time;
	
	@Column(name="sig_time")
	private String sig_time;
	
	@Column(name="op_time")
	private String op_time;
	
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

	public String getSigmulti() {
		return sigmulti;
	}

	public void setSigmulti(String sigmulti) {
		this.sigmulti = sigmulti;
	}

	public String getOpmulti() {
		return opmulti;
	}

	public void setOpmulti(String opmulti) {
		this.opmulti = opmulti;
	}

	public String getTruth_time() {
		return truth_time;
	}

	public void setTruth_time(String truth_time) {
		this.truth_time = truth_time;
	}

	public String getSig_time() {
		return sig_time;
	}

	public void setSig_time(String sig_time) {
		this.sig_time = sig_time;
	}

	public String getOp_time() {
		return op_time;
	}

	public void setOp_time(String op_time) {
		this.op_time = op_time;
	}

	public MulAnswerCompare(Integer task_id, String truth_answer, String sigmulti, String opmulti, String truth_time,
			String sig_time, String op_time) {
		super();
		this.task_id = task_id;
		this.truth_answer = truth_answer;
		this.sigmulti = sigmulti;
		this.opmulti = opmulti;
		this.truth_time = truth_time;
		this.sig_time = sig_time;
		this.op_time = op_time;
	}

	
	
}
