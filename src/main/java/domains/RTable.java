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
@Table(name="requester_table")
@Cacheable
public class RTable {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="requester_id")
	private Integer requester_id;
	
	@Column(name="table_name")
	private String table_name;
	
	@Column(name="available")
	private Integer available;
	
	@Column(name="jsontable")
	private String jsontable;
	
	public String getJsontable() {
		return jsontable;
	}

	public void setJsontable(String jsontable) {
		this.jsontable = jsontable;
	}

	public Integer getAvailable() {
		return available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}

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

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public RTable(Integer requester_id, String table_name, Integer available, String jsontable) {
		super();
		this.requester_id = requester_id;
		this.table_name = table_name;
		this.available = available;
		this.jsontable = jsontable;
	}

	public RTable() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
