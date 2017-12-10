import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import utilities.FileHandler;

/**
 * Helper class for relevance Info
 * @author Gaurav Gandhi
 *
 */
public class RelevanceInfos {

	
	/**
	 * @param filePath
	 * @return list of relevant documents for all the queries by reading a text file
	 * @throws IOException
	 */
	public static List<RelevanceInfo> readRelevanceInfoFromFile(String filePath) throws IOException {
		
		List<RelevanceInfo> relInfo = new ArrayList<RelevanceInfo>();
		FileHandler f = new FileHandler(filePath, 1);
		String currentLine;
		//System.out.println(f.readLine());
		while((currentLine = f.readLine()) != null) {
			
			//System.out.println(currentLine);
			String[] temp = currentLine.split(" ");
			relInfo.add(new RelevanceInfo1(Integer.parseInt(temp[0]), temp[2]));
		}
		
		return relInfo;
	}
	
	/**
	 * @param queryID
	 * @param relevanceList
	 * @return returns a list of relevant documents for the given query by its id
	 */
	public static List<RelevanceInfo> getRelevanceInfoByQueryID(int queryID, List<RelevanceInfo> relevanceList) {
		
		return relevanceList.stream().filter(x -> x.queryId() == queryID).collect(Collectors.toCollection(ArrayList<RelevanceInfo>::new));
	}
	
	public static void main(String[] args) throws IOException {
		
		List<RelevanceInfo> a = readRelevanceInfoFromFile("/Users/hardikshah/CS6200-Project/ProblemStatement/cacm.rel.txt");
		a.stream().forEach(x -> {
			System.out.println(x.queryId() + " "  + x.documentID());
		});
	}
}
