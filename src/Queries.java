import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utilities.Constants;
import utilities.FileHandler;

//TODO implementation to add new queries by the user

public class Queries {

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
				if(query.charAt(query.length() - 1) == ' ')
					query = query.substring(0, query.length() - 1);
				System.out.println(query);
				relInfo.add(new Query1(queryID, query, RelevanceInfos.getRelevanceInfoByQueryID(queryID, relevanceList)));
			}catch(NumberFormatException nfe) {
				
				//nfe.printStackTrace();
			}
		}	
		return relInfo;
	}
	
	public static void main(String[] args) throws IOException {
		
		List<Query> q = readQueriesFromFile(Constants.QUERY_FILE);
		
		System.out.println(q.toString());
		System.out.println(q.size());
	}
}
