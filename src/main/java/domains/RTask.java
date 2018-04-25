package domains;

import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="rtask_info")
@Cacheable
public class RTask {

	@Id
	@Column(name="task_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer task_id;
	
	@Column(name="content")
	private String content;

	@Column(name="deadline")
	private Timestamp deadline;
	
	@Column(name="each_reward")
	private Double each_reward;
	
	@Column(name="hastaken_number")
	private Integer hastaken_number;
	
	@Column(name="hasanswer_number")
	private Integer hasanswer_number;

	@Column(name="state")
	private Integer state;
	
	@Column(name="difficult_degree")
	private Double difficult_degree;
	
	@Column(name="worker_number")
	private Integer worker_number;
	
	@Column(name="predict_cost")
	private Double predict_cost;
	
	@Column(name="haspaid_cost")
	private Double haspaid_cost;

	@Column(name="table_name")
	private String table_name;
	
	@Column(name="begin_time")
	private Timestamp begin_time;
	
	@Column(name="type")
	private Integer type;
	
	
	public Timestamp getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(Timestamp begin_time) {
		this.begin_time = begin_time;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
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

	public Double getEach_reward() {
		return each_reward;
	}

	public void setEach_reward(Double each_reward) {
		this.each_reward = each_reward;
	}

	public Integer getHastaken_number() {
		return hastaken_number;
	}

	public void setHastaken_number(Integer hastaken_number) {
		this.hastaken_number = hastaken_number;
	}

	public Integer getHasanswer_number() {
		return hasanswer_number;
	}

	public void setHasanswer_number(Integer hasanswer_number) {
		this.hasanswer_number = hasanswer_number;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Double getDifficult_degree() {
		return difficult_degree;
	}

	public void setDifficult_degree(Double difficult_degree) {
		this.difficult_degree = difficult_degree;
	}

	public Integer getWorker_number() {
		return worker_number;
	}

	public void setWorker_number(Integer worker_number) {
		this.worker_number = worker_number;
	}

	public Double getPredict_cost() {
		return predict_cost;
	}

	public void setPredict_cost(Double predict_cost) {
		this.predict_cost = predict_cost;
	}

	public Double getHaspaid_cost() {
		return haspaid_cost;
	}

	public void setHaspaid_cost(Double haspaid_cost) {
		this.haspaid_cost = haspaid_cost;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public RTask(String content, String table_name, Timestamp begin_time, Timestamp deadline, Double each_reward, Integer hastaken_number,
			Integer hasanswer_number, Integer state, Double difficult_degree, Integer worker_number,
			Double predict_cost, Double haspaid_cost, Integer type) {
		super();
		this.content = content;
		this.table_name = table_name;
		this.begin_time = begin_time;
		this.deadline = deadline;
		this.each_reward = each_reward;
		this.hastaken_number = hastaken_number;
		this.hasanswer_number = hasanswer_number;
		this.state = state;
		this.difficult_degree = difficult_degree;
		this.worker_number = worker_number;
		this.predict_cost = predict_cost;
		this.haspaid_cost = haspaid_cost;
		this.type = type;
	}

	public RTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
