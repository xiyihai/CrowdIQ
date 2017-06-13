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
@Table(name="testtask_info")
@Cacheable
public class TestTask {


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="test_id")
	private Integer test_id;
	
	@Column(name="content")
	private String content;

	@Column(name="answer")
	private String answer;

	public Integer getTest_id() {
		return test_id;
	}

	public void setTest_id(Integer test_id) {
		this.test_id = test_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public TestTask(String content, String answer) {
		super();
		this.content = content;
		this.answer = answer;
	}

	public TestTask() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
