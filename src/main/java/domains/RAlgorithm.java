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
@Table(name="requester_algorithm")

@Cacheable
public class RAlgorithm {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="requester_id")
	private Integer requester_id;
	
	@Column(name="algorithm_name")
	private String algorithm_name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRequester_id() {
		return requester_id;
	}

	public void setRequester_id(Integer requester_id) {
		this.requester_id = requester_id;
	}

	public String getAlgorithm_name() {
		return algorithm_name;
	}

	public void setAlgorithm_name(String algorithm_name) {
		this.algorithm_name = algorithm_name;
	}

	public RAlgorithm(Integer requester_id, String algorithm_name) {
		super();
		this.requester_id = requester_id;
		this.algorithm_name = algorithm_name;
	}

	public RAlgorithm() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
