import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import utilities.FileHandler;

/**
 * @author Gaurav Gandhi
 *
 */
public class Query1 implements Query {

	private int queryID;
	private String query;
	
	public Query1(int queryID, String query) {
		
		this.queryID = queryID;
		this.query = query;
	}
	
	
	@Override
	public int queryID() {
		
		return this.queryID;
	}

	@Override
	public String query() {
		
		return this.query;
	}


}
