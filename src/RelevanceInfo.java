

/**
 * Represents the document which is relevant to the given query
 */
public interface RelevanceInfo {

	/**
	 * @return queryID
	 */
	int queryId();
	
	/**
	 * @return literal
	 */
	String literal();
	
	/**
	 * @return document ID
	 */
	String documentID();
	
	/**
	 * @return name of the system
	 */
	int systemName();
	
}
