import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import utilities.Constants;

/**
 * @author Gaurav Gandhi
 *
 */
public class Index {

	private HashMap<String, List<Posting>> invertedIndexBase; // basic inverted index 
	private HashMap<String, Integer> documentLengthBase; // basic document lengths
	private final String directoryPathBase = Constants.PARSED_CORPUS_DIR; // parsed  documents directory (for use in invertedIndexBase and invertedIndexStop
	private HashMap<String, List<Posting>> invertedIndexStop; // inverted index after removing stop words
	private HashMap<String, List<Posting>> documentLengthStop; // document length after removing stop words 
	private HashMap<String, List<Posting>> invertedIndexStem; // inverted index using stemmed docs
	private HashMap<String, List<Posting>> documentLengthStem; // document length of stemmed docs
	private String directoryPathStem; //TODO  path of parsed stemmed documents 
	private List<HashMap> indexAndDocumentLength; // store inverted index and document length
	private final String fileRelevanceInfo = Constants.RELEVANCE_FILE; // path of relevance info file
	private final List<RelevanceInfo> relevanceInfoList = RelevanceInfos.readRelevanceInfoFromFile(fileRelevanceInfo); // list of relevant docs for each query
	private final String fileQuery = Constants.QUERY_FILE; // path of query file
	private final List<Query> queryList = Queries.readQueriesFromFile(fileQuery); // list of queries to be executed
	Scanner in;
	
	@SuppressWarnings("unchecked")
	public Index(int nGram) throws IOException {
		
		in = new Scanner(System.in);
		this.indexAndDocumentLength = Indexers.getInvertedIndexAndDocumentLength(nGram, this.directoryPathBase, false, false);
		this.invertedIndexBase = this.indexAndDocumentLength.get(0);
		this.documentLengthBase = this.indexAndDocumentLength.get(1);
		this.indexAndDocumentLength = Indexers.getInvertedIndexAndDocumentLength(nGram, this.directoryPathBase, true, false);
		this.invertedIndexStop = this.indexAndDocumentLength.get(0);
		this.documentLengthStop = this.indexAndDocumentLength.get(1);
		//TODO inverted index and document length for stemmed corpus
	}
	
	public void choosePhase() {
		System.out.println("Choose phase");
		while(true) {
			switch(in.nextInt()) {
			
			case 1: 
				// Phase 1
				this.phase1();
				break;
			case 2:
				//Phase 2
				this.phase2();
				break;
			case 3:
				//Phase 3
				this.phase3();
				break;
			default:
				System.out.println("Invalid Input");
				break;
			case 0:
				// Exit
				System.exit(0);
				break;
			}
		}
	}
	
	private void phase2() {
		
		//TODO snippet generation. Choose run
	}
	
	private void phase3() {
		
		//TODO evaluation.
	}
	
	private void phase1() {
		System.out.println("Inside phase 1");
		while(true) {
			switch(in.nextInt()) {
			
			case 1:
				// Perform Phase 1 Task 1
				this.phase1Task1();
				break;
			case 2:
				// Perform Phase 1 Task 2
				this.phase1Task2();
				break;
			case 3:
				// Perform Phase 1 Task 3
				this.phase1Task3();
				break;
			default:
				System.out.println("Invalid Input");
				break;
			case 9: 
				//GO back to previous menu
				return;
			case 0:
				// Exit
				System.exit(0);
				break;
			}
		}
	}
	
	private void phase1Task3() {
		while(true) {
			switch(in.nextInt()) {
			
			case 1:
				// Index documents using stop words
				break;
			case 2:
				// Parse and index stemmed documents
				break;
			case 3:
				//TODO choose 3 runs for stopped and stemmed documents
				break;
			default:
				System.out.println("Invalid Input");
			case 9: 
				//GO back to previous menu
				this.choosePhase();
				break;
			case 0:
				// Exit
				System.exit(0);
				break;
			}
		}
	}
	
	private void phase1Task1() {
		System.out.println("Inside Phase 1 Task 2");
		while(true) {
			switch(in.nextInt()) {
			
			case 1: 
				// Parse and Index the documents
				break;
			case 2:
				// 4 Baseline runs
				break;
			default:
				System.out.println("Invalid Input");
			case 9: 
				//GO back to previous menu
				return;
			case 0:
				// Exit
				System.exit(0);
				break;
			}
		}
	}
	
	private void phase1Task2Runs() {
		
		while(true) {
			switch(in.nextInt()) {
			
			case 1:
				//BM25 Run
				break;
			case 2:
				//tf-idf Run
				break;
			case 3:
				//Smoothed Query Likelihood
				break;
			case 4:
				//Lucene
				break;
			default:
				System.out.println("Invalid Input");
				break;
			case 9: 
				//GO back to previous menu
				this.choosePhase();
				break;
			case 0:
				// Exit
				System.exit(0);
				break;
			}
		}
	}
	
	private void phase1Task2() {
		
		//TODO Execute Pseudo Relevance feedback, choose run. 
	}
	
	public static void main(String[] args) throws IOException {
		
		Index i = new Index(1); // 1 is the nGram
		i.choosePhase();
	}
	
	
}
