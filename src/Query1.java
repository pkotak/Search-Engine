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
	private List<Query> queryList;
	
	public Query1(int queryID, String query) {
		
		this.queryID = queryID;
		this.query = query;
		queryList = new ArrayList<Query>();
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
	public void readQueryFile(String filePath) {
		
		try {
			FileHandler fc = new FileHandler(filePath, 1);
			StringBuilder fileContent = new StringBuilder();
			String currentLine;
			while((currentLine = fc.readLine()) != null) {
				
				fileContent.append(currentLine);
			}
			String[] splitByDoc = fileContent.toString().split("</DOC>");
			
			for(String s : splitByDoc) {
				
				int queryID = Integer.parseInt(s.split("</DOCNO>")[0].replace("<DOC>","")
	                    .replace("<DOCNO>", "").trim());
				 String query = s.split("</DOCNO>")[1];
				 queryList.add(new Query1(queryID, query));
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}	
	}

	@Override
	public Stream<Query> stream() {
		
		return this.queryList.stream();
	}
}
