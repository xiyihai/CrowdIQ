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

@Entity
@Table(name="worker_info")
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
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
	
	public Worker(String name, String password, Double account, String email, String quality, Integer level) {
		super();
		this.name = name;
		this.password = password;
		this.account = account;
		this.email = email;
		this.quality = quality;
		this.level = level;
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
