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
	private final String fileStemmedCorpus = Constants.STEMMED_CORPUS_FILE; // file name of the stemmed corpus
	private final String directoryStemmedCorpus = Constants.STEM_DOCS_DIR; // directory of the stemmed corpus
	private final String directoryStemmedParsedCorpus = Constants.STEM_PARSED_DIR;
	private final String directoryRawCorpus = Constants.RAW_CORPUS_DIR; // directory of the raw corpus
	private final String directoryParsedCorpus = Constants.PARSED_CORPUS_DIR; // directory of the parsed corpus
	
	// Results storing
	private List<Query> ResultTask1BM25 = null; // query with updated results of BM25
	private List<Query> ResultTask1tfIdf = null; // query with updated results of tf-idf
	private List<Query> ResultTask1SQL = null; // query with updated results of SQL
	private List<Query> ResultTask1Lucene = null; // query with updated results of Lucene
	
	
	Scanner in;
	
	@SuppressWarnings("unchecked")
	public Index(int nGram) throws IOException {
		
		in = new Scanner(System.in);
		System.out.println("Please wait initializing...");
		//Parse and index raw corpus
		this.parseAndGenerateIndex(nGram);
		// Inverted index and document length after removing stop words
		this.indexAndDocumentLength = Indexers.getInvertedIndexAndDocumentLength(nGram, this.directoryPathBase, true);
		this.invertedIndexStop = this.indexAndDocumentLength.get(0);
		this.documentLengthStop = this.indexAndDocumentLength.get(1);
		// Inverted index and document length of stemmed corpus
		StemDocumentHandlers.generateStemmedDocuments(fileStemmedCorpus, directoryStemmedCorpus);
		Parser.parseAllFiles(3, directoryStemmedCorpus, directoryStemmedParsedCorpus);
		this.indexAndDocumentLength = Indexers.getInvertedIndexAndDocumentLength(nGram, directoryStemmedParsedCorpus, false);
		this.invertedIndexStem = this.indexAndDocumentLength.get(0);
		this.documentLengthStem = this.indexAndDocumentLength.get(1);
	}
	
	@SuppressWarnings("unchecked")
	private void parseAndGenerateIndex(int nGram) throws IOException {

		// Parse the raw corpus
		Parser.parseAllFiles(3, directoryRawCorpus, directoryParsedCorpus);
		//Basic Inverted Index and document length
		this.indexAndDocumentLength = Indexers.getInvertedIndexAndDocumentLength(nGram, this.directoryPathBase, false);
		this.invertedIndexBase = this.indexAndDocumentLength.get(0);
		this.documentLengthBase = this.indexAndDocumentLength.get(1);
	}
	
	
	public void choosePhase() {
		
		while(true) {
			System.out.println("Choose the phase"
					+ "\n1. Phase 1: Indexing and Retrieval"
					+ "\n2. Phase 2: Displaying Results"
					+ "\n3. Phase 3: Evaluation"
					+ "\n0. Exit ");
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
				System.out.println("Invalid Input. Try again");
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
		
		
	}
	
	private void phase1() {
		
		while(true) {
			System.out.println("Choose the task of Phase 1"
					+ "\n1. Task 1: Create inverted Indexer. Perform BM25, tf-idf, Smoothed Query Likelihood or Lucene baseline runs"
					+ "\n2. Task 2: Pseudo relevance feedback using one of the given runs" //TODO choose run
					+ "\n3. Task 3: Baseline runs after stopping and Stemming" //TODO choose run 
					+ "\n9. Go back to the previous menu."
					+ "\n0. Exit "); 
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
	
	@SuppressWarnings("unchecked")
	private void phase1Task3() {
		while(true) {
			System.out.println("Choose one of the following:"
					+ "\n1. Index documents using stop words"
					+ "\n2. Index Stemmed documents"
					+ "\n3. 3 Runs on stopped and stemmed index"); //TODO choose 3 runs
			switch(in.nextInt()) {
			
			case 1:
				// Index documents using stop words
				try {
					this.indexAndDocumentLength = Indexers.getInvertedIndexAndDocumentLength(1, directoryParsedCorpus, true);  //nGram is 1
					this.invertedIndexStop = this.indexAndDocumentLength.get(0);
					this.documentLengthStop = this.indexAndDocumentLength.get(1);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				break;
			case 2:
				// Parse and index stemmed documents
				try {
					Parser.parseAllFiles(3, directoryStemmedCorpus, directoryStemmedParsedCorpus);
					this.indexAndDocumentLength = Indexers.getInvertedIndexAndDocumentLength(1, directoryStemmedParsedCorpus, false); //nGram is 1
					this.invertedIndexStem = this.indexAndDocumentLength.get(0);
					this.documentLengthStem = this.indexAndDocumentLength.get(1);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				break;
			case 3:
				//TODO choose 3 runs for stopped and stemmed documents
				this.phase1Task3Runs();
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
	
	private void phase1Task3Runs() {
		
		//TODO choose 3 runs and generate results
	}
	
	private void phase1Task1() {
		
		while(true) {
			System.out.println("Choose one of the following:"
					+ "\n1. Parse the documents and create basic inverted index using the raw corpus (Note:- You can skip this option for the Runs)"
					+ "\n2. Run BM25, tf-idf, Smoothed QueryLiklihood and Lucene retrieval model"
					+ "\n9. Go back to the previous menu."
					+ "\n0. Exit");
			switch(in.nextInt()) {
			
			case 1: 
				// Parse and index raw corpus
				try {
					this.parseAndGenerateIndex(1); //nGram
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				break;
			case 2:
				// 4 Baseline runs
				this.phase1Task1BaselineRuns();
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
	
	private void phase1Task1BaselineRuns() {
		
		while(true) {
			System.out.println("Choose one of the following options:"
					+ "\n1. BM25 retrieval model"
					+ "\n2. tf-idf retrieval model"
					+ "\n3. Smoothed Query likelihood model"
					+ "\n4. Lucene retrieval model"
					+ "\n9. Go back to the previous menu."
					+ "\n0. Exit.");
			switch(in.nextInt()) {
			
			case 1:
				// BM25 retrieval model
				this.ResultTask1BM25 = BM25Models.executeBM25ModelOnSystem(queryList, invertedIndexBase, relevanceInfoList, documentLengthBase);
				break;
			case 2:
				// tf-idf retrieval mode
				this.ResultTask1tfIdf = TfIdf.executeTfIdfOnSystem(queryList, invertedIndexBase, documentLengthBase);
				break;
			case 3:
				// Smoothed Query likelihood model
				try {
					this.ResultTask1SQL = QueryLikelihoodModel.executeSQLOnSystem(queryList, relevanceInfoList, invertedIndexBase, documentLengthBase);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				break;
			case 4:
				// Lucene retrieval model
				//TODO call lucene model
				break;
			case 9:
				// Go back to the previous menu
				return;
			case 0:
				// Exit
				System.exit(0);
			default:
				System.out.println("Invalid Input. Try Again");
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
