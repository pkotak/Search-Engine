import utilities.Constants;
import utilities.FileHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Indexers {

	public static HashMap<String, List<Posting>> getInvertedIndex(int nGram, String directoryPath) {

		return new Indexer(nGram, directoryPath).generateIndex();
	}

	public static HashMap<String, List<Posting>> getStoppedInvertedIndex(int nGram, String directoryPath) throws IOException {
		HashMap<String, List<Posting>> invertedIndex = getInvertedIndex(nGram,directoryPath);
		FileHandler r = new FileHandler(Constants.COMMON_WORDS_FILE, 1);
		String currentLine;
		while((currentLine = r.readLine()) != null) {
			if(invertedIndex.get(currentLine) != null){
				invertedIndex.remove(currentLine);
			}
		}
		return invertedIndex;
	}
}
