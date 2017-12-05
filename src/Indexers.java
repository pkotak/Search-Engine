import java.util.HashMap;
import java.util.List;

public class Indexers {

	public static HashMap<String, List<Posting>> getInvertedIndex(int nGram, String directoryPath) {
		
		return new Indexer(nGram, directoryPath).generateIndex();
	}
}
