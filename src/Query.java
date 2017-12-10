import java.util.List;

/**
 * Represents a query used for retrieving results from the reteiving module(s) of the system</br></br>
 * <b>queryID:</b> represents the query identifier</br>
 * <b>query:</b> represents the query</br>
 * <b>

 */
public interface Query {

	/**
	 * @return ID of the given query
	 */
	int queryID();
	
	/**
	 * @return given query
	 */
	String query();
	
	/**
	 * @return list of relevant documents to the corresponding query
	 */
	List<RelevanceInfo> listOfRelevantDocuments();
	
	/**
	 * @return list of results to the corresponding query after running one of the retrieval models
	 */
	List<Result> resultList();
	
	/**
	 * @param resultList
	 * @Effects adds results to the result list of the corresponding query
	 */
	void putResultList(List<Result> resultList);
}
