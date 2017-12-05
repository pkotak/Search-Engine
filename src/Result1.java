public class Result1 implements Result {

    private String doc_id;
    private double score;
    private int queryID;
    private String literal;
    private int systemName;

    Result1(String doc_id,double score, int queryID){
        this.doc_id=doc_id;
        this.score=score;
        this.queryID = queryID;
        this.literal = "Q0";
        this.systemName = 1;
    }


    @Override
    public double Score() {
        return this.score=score;
    }

    @Override
    public void changeScore(double sc) {
        this.score=this.score+sc;
    }

	@Override
	public int queryID() {
		
		return this.queryID;
	}

	@Override
	public int literal() {
		
		return this.literal();
	}

	@Override
	public String docID() {
		
		return this.doc_id;
	}

	@Override
	public int systemName() {
		
		return this.systemName;
	}
}
