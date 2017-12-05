import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utilities.FileHandler;

public class Queries {

	public static List<Query> readQueriesFromFile(String filePath) throws IOException {
		
		List<Query> relInfo = new ArrayList<Query>();
		FileHandler f = new FileHandler(filePath, 1);
		String currentLine;
		while((currentLine = f.readLine()) != null) {
			
			System.out.println(currentLine);
			String[] temp = currentLine.split(" ");
			relInfo.add(new Query1(Integer.parseInt(temp[0]), temp[2]));
		}
		
		return relInfo;
	}
}
