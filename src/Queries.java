import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utilities.FileHandler;

public class Queries {

	public static List<Query> readQueriesFromFile(String filePath) throws IOException {
		
		List<Query> relInfo = new ArrayList<Query>();
		FileHandler f = new FileHandler(filePath, 1);
		StringBuilder fileContent = new StringBuilder();
		String currentLine;
		while((currentLine = f.readLine()) != null) {
			
			fileContent.append(currentLine);
		}
		String[] splitByDoc = fileContent.toString().split("</DOC>");
		for(String s : splitByDoc) {
			int queryID = Integer.parseInt(s.split("</DOCNO>")[0].replace("<DOC>","")
					.replace("<DOCNO>", "").trim());
			String query = s.split("</DOCNO>")[1].replace("</DOC>", "");
			while(query.charAt(0) == ' ')
				query = query.substring(1);
			//TODO removing special characters
			relInfo.add(new Query1(queryID, query
					.replaceAll("(?=[]\\[+&|!(){}^\"~*?:\\\\-])", "\\\\")
					.replaceAll("/", "\\\\/")));
		}	
		return relInfo;
	}
}
