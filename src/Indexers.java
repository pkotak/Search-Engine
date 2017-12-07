import utilities.Constants;
import utilities.FileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Helper class for Indexer
 * 
 * @author Gaurav Gandhi
 *
 */
public class Indexers {

	/**
	 * @param nGram (unigram, bigram etc) a word gram
	 * @param directoryPath path where the documents to be indexed are stored
	 * @return An inverted index
	 * @throws IOException 
	 * 
	 */
	public static HashMap<String, List<Posting>> getInvertedIndex(int nGram, String directoryPath) throws IOException {
		
		return new Indexer(nGram, directoryPath).generateIndex();
	}
	

	/**
	 * @return list of stop words from the file
	 * @throws IOException
	 */
	private static List<String> getStopWordListFromFile() throws IOException {

		FileHandler r;
		List<String> stopWordList = null;
		r = new FileHandler(Constants.COMMON_WORDS_FILE, 1);

		stopWordList = new ArrayList<String>();
		String currentLine;
		while((currentLine = r.readLine()) != null) {

			stopWordList.add(currentLine);
		}

		return stopWordList;
	}
		
	
	/**
	 * @param nGram (unigram, bigram etc) a word gram
	 * @param directoryPath path where the documents to be indexed are stored
	 * @param removeStopWords indicating whether the indexer should remove stop words
	 * @return A list of an inverted index and the length of each document in the given corpus
	 * @throws IOException 
	 */
	public static List<HashMap> getInvertedIndexAndDocumentLength(int nGram, String directoryPath, boolean removeStopWords) throws IOException {
		
		@SuppressWarnings("rawtypes")
		List<HashMap> indexerData = new ArrayList<HashMap>();
		Indexer i = new Indexer(nGram, directoryPath);
		HashMap<String, List<Posting>> invertedIndex = getInvertedIndex(nGram, directoryPath);
		HashMap<String, Integer> documentLength = i.getWordCountOfDocuments();
		if(removeStopWords)
			invertedIndexDocumentLengthWithStopWords(invertedIndex, documentLength);
		indexerData.add(invertedIndex);
		indexerData.add(documentLength);
		
		return indexerData;
	}
	
	private static void invertedIndexDocumentLengthWithStopWords(HashMap<String, List<Posting>> invertedIndex, HashMap<String, Integer> documentLength) throws IOException {
		
		List<String> stopWordList = getStopWordListFromFile();
		invertedIndex.entrySet().stream().filter(x -> stopWordList.stream().anyMatch(stop -> stop.equals(x.getKey())))
		.forEach(term -> {
			List<Posting> postingList = term.getValue();
			postingList.stream().forEach(posting -> {
				int length = documentLength.get(posting.docID());
				documentLength.put(posting.docID(), (length - posting.termFrequency()));
			});
		});
		invertedIndex.keySet().removeAll(stopWordList);
	}
}
