public class Result1 implements Result {

    private String docID;
    private double score;
    private int queryID;
    private String literal;
    private String systemName;
    private double precision;
    private double recall;
    private int rank;
    private String snippet;
    private String model_name;

    /**
     * @param docID
     * @param score
     * @param queryID
	 *  @param SystemName
     */
    Result1(String docID,double score, int queryID,String SystemName,String model_name) {
    	//TODO take systemName from the user
        this.docID = docID;
        this.score = score;
        this.queryID = queryID;
        this.literal = "Q0";
        this.systemName = SystemName;
        this.model_name=model_name;
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
	public String systemName() {
		
		return this.systemName;
	}

    @Override
    public String modelName() {
        return this.model_name;
    }

    @Override
	public String toString() {
		
		return (this.queryID() + " " + this.literal() + " " + this.docID() + " " + this.model_name+"_"+this.Score() + " "
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

	@Override
	public String snippet() {
		
		return this.snippet;
	}


	@Override
	public void addSnippet(String newSnippet) {
		
		this.snippet = newSnippet;
	}


	@Override
	public void changeModelName(String newModelName) {
		
		this.model_name = newModelName;
	}
}
