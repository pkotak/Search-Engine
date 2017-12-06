public class Result1 implements Result {

    private String docID;
    private double score;
    private int queryID;
    private String literal;
    private int systemName;
    private double precision;
    private double recall;
    private int rank;

    /**
     * @param doc_id
     * @param score
     * @param queryID
     */
    Result1(String docID,double score, int queryID) {
        this.docID = docID;
        this.score = score;
        this.queryID = queryID;
        this.literal = "Q0";
        this.systemName = 1;
    }


    @Override
    public double Score() {
        return this.score;
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
	public String literal() {
		
		return this.literal;
	}

	@Override
	public String docID() {
		
		return this.docID;
	}

	@Override
	public int systemName() {
		
		return this.systemName;
	}
	
	@Override
	public String toString() {
		
		return (this.queryID() + " " + this.literal() + " " + this.docID() + " " + this.Score() + " " 
		+ this.rank() + " " +  this.systemName());
	}


	@Override
	public double precision() {
		
		return this.precision;
	}


	@Override
	public double recall() {
		
		return this.recall;
	}


	@Override
	public void changePrecision(double newPrecision) {
		
		this.precision = newPrecision;
	}


	@Override
	public void changeRecall(double newRecall) {
		
		this.recall = newRecall;
	}


	@Override
	public int rank() {
		
		return this.rank;
	}


	@Override
	public void changeRank(int newRank) {
		
		this.rank = newRank;
	}

	@Override
	public void ApplyLog() {
		this.score= Math.log(this.score);
	}
}
