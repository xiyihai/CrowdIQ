package domains;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="requester_table")
public class RTable {

	@Column(name="id")
	private Integer id;
	
	@Column(name="requester_id")
	private Integer requester_id;
	
	@Column(name="table_name")
	private String table_name;
	
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

	public RTable(Integer requester_id, String table_name) {
		super();
		this.requester_id = requester_id;
		this.table_name = table_name;
	}
	
	
}
