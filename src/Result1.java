/**
 * Implements Result
 *
 */
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
  
        this.docID = docID;
        this.score = score;
        this.queryID = queryID;
        this.literal = "Q0";
        this.systemName = model_name;
        this.model_name=SystemName ;
    }


    /* (non-Javadoc)
     * @see Result#Score()
     */
    @Override
    public double Score() {
        return this.score;
    }

    /* (non-Javadoc)
     * @see Result#changeScore(double)
     */
    @Override
    public void changeScore(double sc) {
        this.score=this.score+sc;
    }

	/* (non-Javadoc)
	 * @see Result#queryID()
	 */
	@Override
	public int queryID() {
		
		return this.queryID;
	}

	/* (non-Javadoc)
	 * @see Result#literal()
	 */
	@Override
	public String literal() {
		
		return this.literal;
	}

	/* (non-Javadoc)
	 * @see Result#docID()
	 */
	@Override
	public String docID() {
		
		return this.docID;
	}

	/* (non-Javadoc)
	 * @see Result#systemName()
	 */
	@Override
	public String systemName() {
		
		return this.systemName;
	}

    /* (non-Javadoc)
     * @see Result#modelName()
     */
    @Override
    public String modelName() {
        return this.model_name;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
		
		return (this.queryID() + " " + this.literal() + " " + this.docID() + " " + this.modelName()+"_"+this.Score() + " "
		+ this.rank() + " " +  this.systemName());
	}


	/* (non-Javadoc)
	 * @see Result#precision()
	 */
	@Override
	public double precision() {
		
		return this.precision;
	}


	/* (non-Javadoc)
	 * @see Result#recall()
	 */
	@Override
	public double recall() {
		
		return this.recall;
	}


	/* (non-Javadoc)
	 * @see Result#changePrecision(double)
	 */
	@Override
	public void changePrecision(double newPrecision) {
		
		this.precision = newPrecision;
	}


	/* (non-Javadoc)
	 * @see Result#changeRecall(double)
	 */
	@Override
	public void changeRecall(double newRecall) {
		
		this.recall = newRecall;
	}


	/* (non-Javadoc)
	 * @see Result#rank()
	 */
	@Override
	public int rank() {
		
		return this.rank;
	}


	/* (non-Javadoc)
	 * @see Result#changeRank(int)
	 */
	@Override
	public void changeRank(int newRank) {
		
		this.rank = newRank;
	}

	/* (non-Javadoc)
	 * @see Result#ApplyLog()
	 */
	@Override
	public void ApplyLog() {
		this.score= Math.log(this.score);
	}

	/* (non-Javadoc)
	 * @see Result#snippet()
	 */
	@Override
	public String snippet() {
		
		return this.snippet;
	}


	/* (non-Javadoc)
	 * @see Result#addSnippet(java.lang.String)
	 */
	@Override
	public void addSnippet(String newSnippet) {
		
		this.snippet = newSnippet;
	}


	/* (non-Javadoc)
	 * @see Result#changeModelName(java.lang.String)
	 */
	@Override
	public void changeModelName(String newModelName) {
		
		this.model_name = newModelName;
	}
}
