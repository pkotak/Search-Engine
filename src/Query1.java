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
	private List<RelevanceInfo> listOfRelevantDocuments;
	private List<Result> resultList;
	
	public Query1(int queryID, String query, List<RelevanceInfo> listOfRelevantDocuments) {
		
		this.queryID = queryID;
		this.query = query;
		this.listOfRelevantDocuments = listOfRelevantDocuments;
	}
	
	
	@Override
	public int queryID() {
		
		return this.queryID;
	}

	@Override
	public String query() {
		
		return this.query;
	}


	@Override
	public List<RelevanceInfo> listOfRelevantDocuments() {
		
		return this.listOfRelevantDocuments;
	}


	@Override
	public List<Result> resultList() {
		
		return this.resultList;
	}


	@Override
	public void putResultList(List<Result> resultList) {
		
		this.resultList = resultList;
	}


}
