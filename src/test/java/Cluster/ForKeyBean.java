package Cluster;

public class ForKeyBean {
	
	private String primaryKey;
    private String foreignKey;
    private String schemaA;
    private String schemaB;
	private String pathA;
	private String pathB;
	private float priScore;
	private int priIndex;
	private TableBean referenced_table;
	private TableBean dependent_table;
	private int lineA;
	private int lineB;
	private int forIndex;
	private float incluScore;
	private float forScore;
	private float score;
	public TableBean getreferenced_table() {
		return referenced_table;
	}
	public void setreferenced_table(TableBean referenced_table) {
		this.referenced_table = referenced_table;
	}
	public TableBean getdependent_table() {
		return dependent_table;
	}
	public void setdependent_table(TableBean tableB) {
		this.dependent_table = tableB;
	}
	
	public int getPriIndex() {
		return priIndex;
	}
	public void setPriIndex(int priIndex) {
		this.priIndex = priIndex;
	}
	public int getForIndex() {
		return forIndex;
	}
	public void setForIndex(int forIndex) {
		this.forIndex = forIndex;
	}
	
	public float getPriScore() {
		return priScore;
	}
	public void setPriScore(float priScore) {
		this.priScore = priScore;
	}
	public float getIncluScore() {
		return incluScore;
	}
	public void setIncluScore(float incluScore) {
		this.incluScore = incluScore;
	}
	public float getForScore() {
		return forScore;
	}
	public void setForScore(float forScore) {
		this.forScore = forScore;
	}
	
	public String getPathA() {
		return pathA;
	}
	public void setPathA(String pathA) {
		this.pathA = pathA;
	}
	public String getPathB() {
		return pathB;
	}
	public void setPathB(String pathB) {
		this.pathB = pathB;
	}
	public int getLineA() {
		return lineA;
	}
	public void setLineA(int lineA) {
		this.lineA = lineA;
	}
	public int getLineB() {
		return lineB;
	}
	public void setLineB(int lineB) {
		this.lineB = lineB;
	}
	
    public String getSchemaA() {
		return schemaA;
	}
	public void setSchemaA(String schemaA) {
		this.schemaA = schemaA;
	}
	public String getSchemaB() {
		return schemaB;
	}
	public void setSchemaB(String schemaB) {
		this.schemaB = schemaB;
	}
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getForeignKey() {
		return foreignKey;
	}
	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	
}
