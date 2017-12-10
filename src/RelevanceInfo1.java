
/**
 * Implements RelevanceInfo
 */
public class RelevanceInfo1 implements RelevanceInfo {

	private int queryID;
	private String literal;
	private String documentID;
	private int systemName;
	
	
	/**
	 * @param queryID
	 * @param documentID
	 */
	public RelevanceInfo1(int queryID, String documentID) {
		
		this.queryID = queryID;
		this.literal = "Q0";
		this.documentID = documentID;
		this.systemName = 1;
	}
	
	
	/* (non-Javadoc)
	 * @see RelevanceInfo#queryId()
	 */
	@Override
	public int queryId() {
		
		return this.queryID;
	}

	/* (non-Javadoc)
	 * @see RelevanceInfo#literal()
	 */
	@Override
	public String literal() {
		
		return this.literal;
	}

	/* (non-Javadoc)
	 * @see RelevanceInfo#documentID()
	 */
	@Override
	public String documentID() {
		
		return this.documentID;
	}

	/* (non-Javadoc)
	 * @see RelevanceInfo#systemName()
	 */
	@Override
	public int systemName() {
		
		return this.systemName;
	}





}
