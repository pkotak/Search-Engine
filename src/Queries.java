import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utilities.Constants;
import utilities.FileHandler;


/**
 * Helper class for Query object
 */
public class Queries {

	/**
	 * @param filePath
	 * @return a list of queries by reading query text file
	 * @throws IOException
	 */
	public static List<Query> readQueriesFromFile(String filePath) throws IOException {
		
		List<Query> relInfo = new ArrayList<Query>();
		FileHandler f = new FileHandler(filePath, 1);
		StringBuilder fileContent = new StringBuilder();
		String currentLine;
		while((currentLine = f.readLine()) != null) {
			
			fileContent.append(currentLine + " ");
		}
		fileContent.substring(0, (fileContent.length() - 2));
		List<RelevanceInfo> relevanceList = RelevanceInfos.readRelevanceInfoFromFile(Constants.RELEVANCE_FILE);
		String[] splitByDoc = fileContent.toString().split("</DOC>");
		for(String s : splitByDoc) {
			try {
				int queryID = Integer.parseInt(s.split("</DOCNO>")[0].replace("<DOC>","")
						.replace("<DOCNO>", "").trim());
				String query = s.split("</DOCNO>")[1];
				while(query.charAt(0) == ' ')
					query = query.substring(1);
				//TODO removing special characters
				query = Parser.handlePunctuation(new StringBuilder(query)).toString();
				query = Parser.performCaseFolding(new StringBuilder(query)).toString();
				if(query.charAt(query.length() - 1) == ' ')
					query = query.substring(0, query.length() - 1);
				//System.out.println(query);
				relInfo.add(new Query1(queryID, query, RelevanceInfos.getRelevanceInfoByQueryID(queryID, relevanceList)));
			}catch(NumberFormatException nfe) {
				
				//nfe.printStackTrace();
			}
		}	
		return relInfo;
	}
	
	/**
	 * @param filePath
	 * @return list of queries by reading the stemmed query text file
	 * @throws IOException
	 */
	public static List<Query> readQueriesFromStemmedQueryFile(String filePath) throws IOException {
		
		FileHandler reader = new FileHandler(filePath, 1);
		String currentLine;
		int queryID = 0;
		List<Query> queryList = new ArrayList<Query>();
		while((currentLine = reader.readLine()) != null) {
			
			queryList.add(new Query1(queryID++, currentLine, null));
		}
		return queryList;
		
	}
	
	public static void main(String[] args) throws IOException {
		
		//System.out.println(q.toString());
		//System.out.println(q.size());
	}
}
