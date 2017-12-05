import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import utilities.FileHandler;

/**
 * @author Gaurav Gandhi
 *
 */
public class RelevanceInfo1 implements RelevanceInfo {

	private int queryID;
	private String literal;
	private String documentID;
	private int systemName;
	
	
	public RelevanceInfo1(int queryID, String documentID) {
		
		this.queryID = queryID;
		this.literal = "Q0";
		this.documentID = documentID;
		this.systemName = 1;
	}
	
	
	@Override
	public int queryId() {
		
		return this.queryID;
	}

	@Override
	public String literal() {
		
		return this.literal;
	}

	@Override
	public String documentID() {
		
		return this.documentID;
	}

	@Override
	public int systemName() {
		
		return this.systemName;
	}





}
