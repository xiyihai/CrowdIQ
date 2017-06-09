package domains;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javassist.bytecode.annotation.DoubleMemberValue;

@Entity
@Table(name="worker_info")
@Cacheable
public class Worker {

	@Id
	@Column(name="worker_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer worker_id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="password")
	private String password;
	
	@Column(name="account")
	private Double account;
	
	@Column(name="email")
	private String email;
	
	@Column(name="quality")
	private String quality;

	@Column(name="level")
	private Integer level;
	
	@Column(name="average_costtime")
	private Double average_costtime;
	
	@Column(name="average_di")
	private Double average_di;
	
	@Column(name="average_reward")
	private Double average_reward;
	
	@Column(name="total_tasks")
	private Integer total_tasks;
	
	
	public Double getAverage_di() {
		return average_di;
	}


	public void setAverage_di(Double average_di) {
		this.average_di = average_di;
	}


	public Double getAverage_reward() {
		return average_reward;
	}


	public void setAverage_reward(Double average_reward) {
		this.average_reward = average_reward;
	}


	public Integer getTotal_tasks() {
		return total_tasks;
	}


	public void setTotal_tasks(Integer total_tasks) {
		this.total_tasks = total_tasks;
	}


	public Double getAverage_costtime() {
		return average_costtime;
	}


	public void setAverage_costtime(Double average_costtime) {
		this.average_costtime = average_costtime;
	}

	public Worker(String name, String password, Double account, String email, String quality,
			Integer level, Double average_costtime, Double average_di, Double average_reward, Integer total_tasks) {
		super();
		this.name = name;
		this.password = password;
		this.account = account;
		this.email = email;
		this.quality = quality;
		this.level = level;
		this.average_costtime = average_costtime;
		this.average_di = average_di;
		this.average_reward = average_reward;
		this.total_tasks = total_tasks;
	}


	public Worker() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Integer getLevel() {
		return level;
	}	
	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getWorker_id() {
		return worker_id;
	}

	public void setWorker_id(Integer worker_id) {
		this.worker_id = worker_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Double getAccount() {
		return account;
	}

	public void setAccount(Double account) {
		this.account = account;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}
	
	
	
}
