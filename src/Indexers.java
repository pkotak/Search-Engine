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
	 * @param removeStopWords indicating whether the indexer should remove stop words
	 * @param stemWords indicating whether the indexer should stem the terms
	 * @return An inverted index
	 * @throws IOException 
	 * 
	 */
	public static HashMap<String, List<Posting>> getInvertedIndex(int nGram, String directoryPath, boolean removeStopWords, boolean stemTerms) throws IOException {
		
		HashMap<String, List<Posting>> invertedIndex = new Indexer(nGram, directoryPath).generateIndex();
		if(removeStopWords)
			removeStopWordsFromInvertedIndex(invertedIndex);
		if(stemTerms)
			stemInvertedIndex(invertedIndex);
		return invertedIndex;
			
	}
	
	/**
	 * <b>Do nothing for now (Use if you have to manually stem the document before indexing</b>
	 * @param invertedIndex
	 */
	private static void stemInvertedIndex(HashMap<String, List<Posting>> invertedIndex) {
		// TODO Auto-generated method stub
		//Do nothing for now (Use if you have to manually stem the document before indexing
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
	 * @param invertedIndex
	 * @Effects Removes stop word from the given invertedIndex
	 * @throws IOException
	 */
	private static void removeStopWordsFromInvertedIndex(HashMap<String, List<Posting>> invertedIndex) throws IOException {
		
		invertedIndex.keySet().removeAll(getStopWordListFromFile());
	}
	
	/**
	 * @param invertedIndex 
	 * @throws IOException 
	 */
	public static List<HashMap> getStemmedInvertedIndexAndDocumentLength(int nGram, String directoryPath, boolean removeStopWords) throws IOException {
		
		//TODO code for parsing the stemmed document
		List<HashMap> indexerData = new ArrayList<HashMap>();
		HashMap<String, List<Posting>> invertedIndex = getInvertedIndex(nGram, directoryPath, removeStopWords, false);
		Indexer i = new Indexer(nGram, directoryPath);
		indexerData.add(invertedIndex);
		indexerData.add(i.getWordCountOfDocuments());
		
		return indexerData;
		
	}
	
	
	/**
	 * @param nGram (unigram, bigram etc) a word gram
	 * @param directoryPath path where the documents to be indexed are stored
	 * @param removeStopWords indicating whether the indexer should remove stop words
	 * @param stemTerms indicating whetehr the indexer should step the terms
	 * @return A list of an inverted index and the length of each document in the given corpus
	 * @throws IOException 
	 */
	public static List<HashMap> getInvertedIndexAndDocumentLength(int nGram, String directoryPath, boolean removeStopWords, boolean stemTerms) throws IOException {
		
		List<HashMap> indexerData = new ArrayList<HashMap>();
		Indexer i = new Indexer(nGram, directoryPath);
		HashMap<String, List<Posting>> invertedIndex = getInvertedIndex(nGram, directoryPath, removeStopWords, stemTerms);
		indexerData.add(invertedIndex);
		indexerData.add(i.getWordCountOfDocuments());
		
		return indexerData;
	}
}
