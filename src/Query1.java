
import java.util.List;

/**
 * Implements query interface
 */
public class Query1 implements Query {

	private int queryID;
	private String query;
	private List<RelevanceInfo> listOfRelevantDocuments;
	private List<Result> resultList;
	
	/**
	 * @param queryID
	 * @param query
	 * @param listOfRelevantDocuments
	 */
	public Query1(int queryID, String query, List<RelevanceInfo> listOfRelevantDocuments) {
		
		this.queryID = queryID;
		this.query = query;
		this.listOfRelevantDocuments = listOfRelevantDocuments;
	}
	
	
	/* (non-Javadoc)
	 * @see Query#queryID()
	 */
	@Override
	public int queryID() {
		
		return this.queryID;
	}

	/* (non-Javadoc)
	 * @see Query#query()
	 */
	@Override
	public String query() {
		
		return this.query;
	}


	/* (non-Javadoc)
	 * @see Query#listOfRelevantDocuments()
	 */
	@Override
	public List<RelevanceInfo> listOfRelevantDocuments() {
		
		return this.listOfRelevantDocuments;
	}


	/* (non-Javadoc)
	 * @see Query#resultList()
	 */
	@Override
	public List<Result> resultList() {
		
		return this.resultList;
	}


	/* (non-Javadoc)
	 * @see Query#putResultList(java.util.List)
	 */
	@Override
	public void putResultList(List<Result> resultList) {
		
		this.resultList = resultList;
	}


}
