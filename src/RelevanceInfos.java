import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utilities.FileHandler;

public class RelevanceInfos {

	
	public static List<RelevanceInfo> readRelevanceInfoFromFile(String filePath) throws IOException {
		
		List<RelevanceInfo> relInfo = new ArrayList<RelevanceInfo>();
		FileHandler f = new FileHandler(filePath, 1);
		String currentLine;
		System.out.println(f.readLine());
		while((currentLine = f.readLine()) != null) {
			
			System.out.println(currentLine);
			String[] temp = currentLine.split(" ");
			relInfo.add(new RelevanceInfo1(Integer.parseInt(temp[0]), temp[2]));
		}
		
		return relInfo;
	}
	
	public static void main(String[] args) throws IOException {
		
		List<RelevanceInfo> a = readRelevanceInfoFromFile("C:\\Study\\IR-Project\\ProblemStatement\\cacm.rel.txt");
		a.stream().forEach(x -> {
			System.out.println(x.queryId() + " "  + x.documentID());
		});
	}
}
