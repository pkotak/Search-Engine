import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import utilities.FileHandler;

public class Indexer {
	
	private HashMap<String, List<Posting>> invertedIndex;
	private int ngram;
	private String directoryPath;
	private HashMap<String, Integer> documentWordTotal;
	
	
	public Indexer(int nGram, String directoryPath) {
		
		this.ngram = nGram;
		this.directoryPath = directoryPath;
		this.invertedIndex = new HashMap<String, List<Posting>>();
		this.documentWordTotal = new HashMap<String, Integer>();
	}

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
	
	public HashMap<String, Integer> getWordCountOfDocuments() {
		
		return this.documentWordTotal;
	}
	
	/*
	 * Reads each file and adds/updates the inverted index
	 */
	private void readFile(Path path) {
		
		try {
			FileHandler reader = new FileHandler(path.toString(), 1);
			String currentLine;
			
			while((currentLine = reader.readLine()) != null) {
				
				//System.out.println(file);
				String doc = path.toString().substring(path.toString().lastIndexOf("\\") + 1);
				//System.out.println(file.substring(0, file.indexOf(".")));
				doc = doc.substring(0, doc.indexOf('.'));
				this.documentWordTotal.put(doc, currentLine.split(" ").length);
				System.out.println(doc);
				
				String docID = doc;
				this.generateTerms(docID, currentLine);
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	/*
	 * Creates terms based on the word n-gram
	 */
	private void generateTerms(String docID, String currentLine) {
		
		boolean breakFlag = false;
		while(!currentLine.isEmpty()) {
			
			
			String term = this.nGramText(currentLine);
			if(term.isEmpty()) {
				break;
			}
			currentLine = this.removeText(currentLine);
			this.addToIndex(term, docID);
		}
		
	}
	
	/*
	 * Fetches new text of the current document for next index generation
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
	
	
	
	/*
	 * Generates the terms based on n-gram
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
	
	/*
	 * Adds the terms and the document where it exists in the inverted index
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
	
	public static void main(String[] args) throws IOException {
		
		Indexer r = new Indexer(1, "C:\\Study\\IR-Project\\Documents\\ParsedDocuments");
		HashMap<String, List<Posting>> a = r.generateIndex();
		FileHandler tr = new FileHandler("index.txt", 0);
		for(Entry<String, List<Posting>> record: a.entrySet()) {
			
			tr.addText(record.getKey() + " => ");
			
			for(Posting p : record.getValue()) {
				
				tr.addText("[" + p.docID() + ", " + p.termFrequency() + "], " );
			}
			tr.addText("\n");
		}
		tr.closeConnection();
	}
}
