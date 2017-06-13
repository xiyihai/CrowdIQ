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
@Table(name="requester_tablelist")
@Cacheable
public class RTableList {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="requester_id")
	private Integer requester_id;
	
	@Column(name="tablelist")
	private String tablelist;

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

	public String getTablelist() {
		return tablelist;
	}

	public void setTablelist(String tablelist) {
		this.tablelist = tablelist;
	}

	public RTableList(Integer requester_id, String tablelist) {
		super();
		this.requester_id = requester_id;
		this.tablelist = tablelist;
	}

	public RTableList() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
