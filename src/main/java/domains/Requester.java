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
@Table(name="requester_info")

@Cacheable
public class Requester {

	@Id
	@Column(name="requester_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer requester_id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="password")
	private String password;
	
	@Column(name="account")
	private Double account;
	
	@Column(name="email")
	private String email;

	public Requester(String name, String password, Double account, String email) {
		super();
		this.name = name;
		this.password = password;
		this.account = account;
		this.email = email;
	}

	public Integer getRequester_id() {
		return requester_id;
	}

	public void setRequester_id(Integer requester_id) {
		this.requester_id = requester_id;
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

	public Requester() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
