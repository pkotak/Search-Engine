import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import utilities.FileHandler;

/** 
 * 
 * Generates a map of inverted index and document length of the corpus
 */
public class Indexer {
	
	private HashMap<String, List<Posting>> invertedIndex;
	private int ngram;
	private String directoryPath;
	private HashMap<String, Integer> documentWordTotal;
	
	
	/**
	 * @param nGram word n-gram 
	 * @param directoryPath path of the file to index
	 * @Effects creates an Indexer object
	 */
	public Indexer(int nGram, String directoryPath) {
		
		this.ngram = nGram;
		this.directoryPath = directoryPath;
		this.invertedIndex = new HashMap<String, List<Posting>>();
		this.documentWordTotal = new HashMap<String, Integer>();
	}

	/**
	 * @return inverted Index
	 */
	public HashMap<String, List<Posting>> generateIndex() {
		
		try {
			Files.list(Paths.get(directoryPath))
			.filter(pa -> pa.toString().endsWith(".txt"))
			.forEach(p ->{
				this.readFile(p);
			});
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return this.invertedIndex;
	}
	
	/**
	 * @return Length of each document
	 */
	public HashMap<String, Integer> getWordCountOfDocuments() {
		
		return this.documentWordTotal;
	}
	
	/**
	 * @param path path of a file
	 * @Effects reads the file and creates tokens in the form of term and Posting
	 * for the inverted index
	 */
	private void readFile(Path path) {
		
		try {
			FileHandler reader = new FileHandler(path.toString(), 1);
			String currentLine;
			
			while((currentLine = reader.readLine()) != null) {
				
				//System.out.println(file);
				String doc = path.toString().substring(path.toString().lastIndexOf(File.separator) + 1);
				//System.out.println(file.substring(0, file.indexOf(".")));
				doc = doc.substring(0, doc.indexOf('.'));
				generateDocumentLength(doc, currentLine.split(" ").length);
				//this.documentWordTotal.put(doc, currentLine.split(" ").length);
				//System.out.println(doc);
				
				String docID = doc;
				this.generateTerms(docID, currentLine);
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * @param doc document ID
	 * @param lineLength length of a line
	 * @Effects adds the given length to the map of document length
	 */
	private void generateDocumentLength(String doc, int lineLength) {
		
		if(this.documentWordTotal.containsKey(doc)) {
			int length = this.documentWordTotal.get(doc);
			this.documentWordTotal.put(doc, length + lineLength);
		}
		else
			this.documentWordTotal.put(doc, lineLength);	
	}
	
	/**
	 * @param docID document ID
	 * @param currentLine a line of the document
	 * @Effects creates terms for the inverted index (which will act as a key)
	 */
	private void generateTerms(String docID, String currentLine) {
		
		while(!currentLine.isEmpty()) {
			
			
			String term = this.nGramText(currentLine);
			if(term.isEmpty()) {
				break;
			}
			currentLine = this.removeText(currentLine);
			this.addToIndex(term, docID);
		}
		
	}
	
	/**
	 * @param currentLine a string
	 * @return removes the first word of the given string
	 * 		if only one word is present in the given String 
	 * 		then an empty string is returned
	 */
	private String removeText(String currentLine) {
		
		try {

			while(currentLine.charAt(0) == ' ')
				currentLine = currentLine.substring(1);
			if(!currentLine.contains(" "))
				currentLine = "";
			else {
				currentLine = currentLine.substring(currentLine.indexOf(" ") + 1);
				while(currentLine.charAt(0) == ' ')
					currentLine = currentLine.substring(1);
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return currentLine;
	}
	
	
	/**
	 * @param currentLine any String of words
	 * @return a term for the inverted index in the form of n-gram
	 * @see
	 * 	<b>nGram:</b> Refer constructor of the Indexer
	 */
	private String nGramText(String currentLine) {
		
		int len = 0;
		StringBuilder st = new StringBuilder();
		try {
			while(len < this.ngram) {
				
				while(currentLine.charAt(0) == ' ')
					currentLine = currentLine.substring(1);
				if(!currentLine.contains(" ") && len < (this.ngram - 1)) {
					st = new StringBuilder();
					break;
				}
				st.append(currentLine.split(" ")[0]);
				if(len < (this.ngram - 1))
					st.append(" ");
				currentLine = currentLine.substring(currentLine.indexOf(" ") + 1);
				len++;
			}
		}catch(Exception e) {
			st = null;
			e.printStackTrace();
		}
		try {
			return st.toString();
		}catch(NullPointerException ne) {
			return "";
		}
		
	}
	
	/**
	 * @param term term for the inverted index (key)
	 * @param docID document id
	 * @Where the term is present in the given docID
	 * @Effects adds the Posting corresponding to the given term in the inverted index
	 */
	private void addToIndex(String term, String docID) {
		
		
		if(this.invertedIndex.containsKey(term)) {
			
			List<Posting> termPosting = this.invertedIndex.get(term);
			
			if(termPosting.stream().anyMatch(x -> x.docID().equals(docID))) {
				
				Posting p = termPosting.stream().filter(x -> x.docID().equals(docID)).findFirst().get();
				
				p.changeTermFrequency(p.termFrequency() + 1);
			}
			else 
				this.invertedIndex.get(term).add(new Posting1(docID, 1, new ArrayList<Integer>(Arrays.asList(0))));
		}
		else {
			this.invertedIndex.put(term, new ArrayList<Posting>());
			this.invertedIndex.get(term).add(new Posting1(docID, 1, new ArrayList<Integer>(Arrays.asList(0))));
		}
	}
	
	/*public static void main(String[] args) throws IOException {
		
		Indexer r = new Indexer(1, Constants.PARSED_CORPUS_DIR);
		HashMap<String, List<Posting>> a = r.generateIndex();
		FileHandler tr = new FileHandler(Constants.INDEX_FILE, 0);
		for(Entry<String, List<Posting>> record: a.entrySet()) {
			
			tr.addText(record.getKey() + " => ");
			
			for(Posting p : record.getValue()) {
				
				tr.addText("[" + p.docID() + ", " + p.termFrequency() + "], " );
			}
			tr.addText("\n");
		}
		tr.closeConnection();
		tr = new FileHandler(Constants.DOCUMENT_LENGTH_FILE, 0);
		for(Entry<String, Integer> record : r.getWordCountOfDocuments().entrySet()) {
			
			tr.addText(record.getKey() + " => " + record.getValue());

			tr.addText("\n");
		}
		tr.closeConnection();
	}*/
}
