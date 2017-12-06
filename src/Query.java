import java.util.List;

/**
 * Represents a query used for retrieving results from the reteiving module(s) of the system</br></br>
 * <b>queryID:</b> represents the query identifier</br>
 * <b>query:</b> represents the query</br>
 * <b>
 * @author Gaurav Gandhi
 *
 */
public interface Query {

	int queryID();
	
	String query();
	
	List<RelevanceInfo> listOfRelevantDocuments();
	
	List<Result> resultList();
	
	void putResultList(List<Result> resultList);
}
