/**
 * Represents a result which is a document for the given query after performing any retrieval run
 *
 */
public interface Result {

	/**
	 * @return ID of a query corresponding to the given result
	 */
	int queryID();
	
	/**
	 * @return rank amongst the list of results for a query
	 */
	int rank();
	
	/**
	 * @return literal of the results
	 */
	String literal();
	
    /**
     * @return document identifier
     */
    String docID();
    
    /**
     * @return score of the document given by a retrieval model
     */
    double Score();
    
    /**
     * @return Parsing techniques done on a document
     */
    String systemName();

    /**
     * @return retrieval model
     */
    String modelName();
    
    /**
     * @return precision of the document (Evaluation)
     */
    double precision();
    
    /**
     * @return recall of the document (Evaluation)
     */
    double recall();
    
    /**
     * @return Snippet of the result (Snippet generation)
     */
    String snippet();
    
    /**
     * @param sc
     */
    void changeScore(double sc);
    
    /**
     * @param newPrecision
     */
    void changePrecision(double newPrecision);
    
    /**
     * @param newRecall
     */
    void changeRecall(double newRecall);
    
    /**
     * @param newRank
     */
    void changeRank(int newRank);

    /**
     * Applies log a the score
     */
    void ApplyLog();
    
    /**
     * @param newSnippet
     */
    void addSnippet(String newSnippet);
    
    /**
     * @param newModelName
     */
    void changeModelName(String newModelName);
     

}

