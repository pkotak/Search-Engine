import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import utilities.Constants;

/**
 * Main file for running this application
 * 
 */
public class Index {

	private HashMap<String, List<Posting>> invertedIndexBase; // basic inverted index 
	private HashMap<String, Integer> documentLengthBase; // basic document lengths
	private final String directoryPathBase = Constants.PARSED_CORPUS_DIR; // parsed  documents directory (for use in invertedIndexBase and invertedIndexStop
	private HashMap<String, List<Posting>> invertedIndexStop; // inverted index after removing stop words
	private HashMap<String, Integer> documentLengthStop; // document length after removing stop words 
	private HashMap<String, List<Posting>> invertedIndexStem; // inverted index using stemmed docs
	private HashMap<String, Integer> documentLengthStem; // document length of stemmed docs
	@SuppressWarnings("rawtypes")
	private List<HashMap> indexAndDocumentLength; // store inverted index and document length
	private final String fileRelevanceInfo = Constants.RELEVANCE_FILE; // path of relevance info file
	private final List<RelevanceInfo> relevanceInfoList = RelevanceInfos.readRelevanceInfoFromFile(fileRelevanceInfo); // list of relevant docs for each query
	private final String fileQuery = Constants.QUERY_FILE; // path of query file
	private List<Query> queryList;  // list of queries to be executed
	private final String fileStemmedCorpus = Constants.STEMMED_CORPUS_FILE; // file name of the stemmed corpus
	private final String directoryStemmedCorpus = Constants.STEM_DOCS_DIR; // directory of the stemmed corpus
	private final String directoryStemmedParsedCorpus = Constants.STEM_PARSED_DIR;
	private final String directoryRawCorpus = Constants.RAW_CORPUS_DIR; // directory of the raw corpus
	private final String directoryParsedCorpus = Constants.PARSED_CORPUS_DIR; // directory of the parsed corpus
	private List<Query> stemmedQueryList;
	
	// Results storing  Phase 1 Task1
	private List<Query> ResultTask1BM25; // query with updated results of BM25
	private List<Query> ResultTask1tfIdf; // query with updated results of tf-idf
	private List<Query> ResultTask1SQL; // query with updated results of SQL
	private List<Query> ResultTask1Lucene; // query with updated results of Lucene
	
	
	
	Scanner in;
	private List<Query> ResultTask3STOPBM25;
	private List<Query> ResultTask3STOPTFIDF;
	private List<Query> ResultTask3STOPSQL;
	private List<Query> ResultTask3StemBM25;
	private List<Query> ResultTask3StemTFIDF;
	private List<Query> ResultTask3StemSQL;
	private List<Query> phase2SnippetGeneratedQuery;
	private List<Query> ResultPhase2SQL;
	private List<Query> pseudoRelevanceUpdatedQueryList;
	private List<Query> ResultTask2PRF;
	
	/**
	 * @param nGram
	 * @throws IOException
	 * Parse and generate inverted index of raw corpus, using stopped words and of stemmed corpus
	 */
	@SuppressWarnings("unchecked")
	public Index(int nGram) throws IOException {
		
		in = new Scanner(System.in);
		System.out.println("Please wait initializing...");
		// Read queries from a file and add them to the list
		queryList = Queries.readQueriesFromFile(fileQuery);
		stemmedQueryList = Queries.readQueriesFromStemmedQueryFile(Constants.STEMMED_QUERY_FILE);
		//Parse and index raw corpus
		this.parseAndGenerateIndex(nGram);
		// Inverted index and document length after removing stop words
		this.indexAndDocumentLength = Indexers.getInvertedIndexAndDocumentLength(nGram, this.directoryPathBase, true);
		this.invertedIndexStop = this.indexAndDocumentLength.get(0);
		this.documentLengthStop = this.indexAndDocumentLength.get(1);
		//Write to file
		Indexers.writeIndextoFile(Constants.INDEX_DIR + "InvertedIndexStopped.txt", invertedIndexStop);
		Indexers.writeDocumentLengthToFile(Constants.DOCUMENT_LENGTH_DIR + "DocumentLengthStopped.txt", documentLengthStop);
		// Inverted index and document length of stemmed corpus
		StemDocumentHandlers.generateStemmedDocuments(fileStemmedCorpus, directoryStemmedCorpus);
		Parser.parseAllFiles(3, directoryStemmedCorpus, directoryStemmedParsedCorpus);
		this.indexAndDocumentLength = Indexers.getInvertedIndexAndDocumentLength(nGram, directoryStemmedParsedCorpus, false);
		this.invertedIndexStem = this.indexAndDocumentLength.get(0);
		this.documentLengthStem = this.indexAndDocumentLength.get(1);
		//Write to file
		Indexers.writeIndextoFile(Constants.INDEX_DIR + "InvertedIndexStemmed.txt", invertedIndexStem);
		Indexers.writeDocumentLengthToFile(Constants.DOCUMENT_LENGTH_DIR + "DocumentLengthStemmed.txt", documentLengthStem);
		System.out.println("Index and document length files are stored in " + Constants.INDEX_DIR);
	}
	
	/**
	 * @param nGram
	 * @throws IOException
	 * Parse and generate basic inverted index
	 */
	@SuppressWarnings("unchecked")
	private void parseAndGenerateIndex(int nGram) throws IOException {

		// Parse the raw corpus
		Parser.parseAllFiles(3, directoryRawCorpus, directoryParsedCorpus);
		//Basic Inverted Index and document length
		this.indexAndDocumentLength = Indexers.getInvertedIndexAndDocumentLength(nGram, this.directoryPathBase, false);
		this.invertedIndexBase = this.indexAndDocumentLength.get(0);
		this.documentLengthBase = this.indexAndDocumentLength.get(1);
		//Write to file
		Indexers.writeIndextoFile(Constants.INDEX_DIR + "InvertedIndexBasic.txt", invertedIndexBase);
		Indexers.writeDocumentLengthToFile(Constants.DOCUMENT_LENGTH_DIR + "DocumentLengthBasic.txt", documentLengthBase);
	}
	

	/**
	 * Choose a phase for performing various tasks in the given application
	 */
	public void choosePhase() {
		
		while(true) {
			System.out.println("Choose the phase"
					+ "\n1. Phase 1: Indexing and Retrieval"
					+ "\n2. Phase 2: Displaying Results"
					+ "\n3. Phase 3: Evaluation (Note: All runs should be executed before evaluation)"
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
	
	/**
	 * Snippet Generation
	 */
	private void phase2() {
		
		try {
			this.ResultPhase2SQL = QueryLikelihoodModel.executeSQLOnSystem(queryList, relevanceInfoList, invertedIndexBase, documentLengthBase);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		Results.writeResultsToFile(Constants.TASK1_PHASE1_SQL, ResultPhase2SQL);
		phase2SnippetGeneratedQuery = SnippetGeneration.performSnippetGeneration(ResultPhase2SQL);
		SnippetGeneration.writeSnippetToFile(Constants.PHASE2_SNIPPET, phase2SnippetGeneratedQuery);
	}
	
	/**
	 * Evaluation
	 */
	private void phase3() {
		
		Evaluation e;
		try {
			this.ResultTask1BM25 = BM25Models.executeBM25ModelOnSystem(queryList, invertedIndexBase, documentLengthBase);
			Results.writeResultsToFile(Constants.TASK1_PHASE1_BM25, ResultTask1BM25);
			e = Evaluations.getEvaluation(this.ResultTask1BM25);
			Evaluations.writeEvaluationToFile(Constants.PHASE3_BASELINE_BM25, e);
		}catch(NullPointerException ne) {
			
			ne.printStackTrace();
			System.out.println("Could not perform evaluation on BM25 Run(Phase 1)");
		}
		try {
			this.ResultTask1tfIdf = TfIdf.executeTfIdfOnSystem(queryList, invertedIndexBase, documentLengthBase);
			Results.writeResultsToFile(Constants.TASK1_PHASE1_TFIDF, ResultTask1tfIdf);
			e = Evaluations.getEvaluation(this.ResultTask1tfIdf);
			Evaluations.writeEvaluationToFile(Constants.PHASE3_BASELINE_TFIDF, e);
		}catch(NullPointerException ne) {
			
			ne.printStackTrace();
			System.out.println("Could not perform evaluation on TFIDF Run(Phase 1)");
		}
		try {
			this.ResultTask1SQL = QueryLikelihoodModel.executeSQLOnSystem(queryList, relevanceInfoList, invertedIndexBase, documentLengthBase);
			Results.writeResultsToFile(Constants.TASK1_PHASE1_SQL, ResultTask1SQL);
			e = Evaluations.getEvaluation(this.ResultTask1SQL);
			Evaluations.writeEvaluationToFile(Constants.PHASE3_BASELINE_SQL, e);
		}catch(NullPointerException ne) {
			
			ne.printStackTrace();
			System.out.println("Could not perform evaluation on Smoothed query likelihood Run(Phase 1)");
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		try {
			IndexFiles.startIndexing(Constants.LUCENE_INDEX_DIR, Constants.RAW_CORPUS_DIR);
			this.ResultTask1Lucene = LuceneOutputGeneration.outputGeneration(queryList);
			Results.writeResultsToFile(Constants.TASK1_PHASE1_Lucne, ResultTask1Lucene);
			e = Evaluations.getEvaluation(this.ResultTask1Lucene);
			Evaluations.writeEvaluationToFile(Constants.PHASE3_BASELINE_lUCENE, e);
		}catch(NullPointerException ne) {
			
			ne.printStackTrace();
			System.out.println("Could not perform evaluation on Lucene Run(Phase 1)");
		} catch (IOException e1) {
			
			e1.printStackTrace();
		} catch (Exception e1) {
			
			e1.printStackTrace();
		}
		try {
			this.ResultTask1SQL = QueryLikelihoodModel.executeSQLOnSystem(queryList, relevanceInfoList, invertedIndexBase, documentLengthBase);
			Results.writeResultsToFile(Constants.TASK1_PHASE1_SQL, ResultTask1SQL);
			this.pseudoRelevanceUpdatedQueryList = PseudoRelevanceFeedback.performPseudoRelevanceFeedback(ResultTask1SQL, invertedIndexBase, documentLengthBase);
			this.ResultTask2PRF = QueryLikelihoodModel.executeSQLOnSystem(pseudoRelevanceUpdatedQueryList, relevanceInfoList, invertedIndexBase, documentLengthBase);
			Results.writeResultsToFile(Constants.PHASE1_TASK2_PRF, ResultTask2PRF);
			e = Evaluations.getEvaluation(this.ResultTask2PRF);
			Evaluations.writeEvaluationToFile(Constants.PHASE3_QUERYREF, e);
		}catch(NullPointerException ne) {
			
			ne.printStackTrace();
			System.out.println("Could not perform evaluation on Query Likelihood PRF(Phase 2)");
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		try {
			this.ResultTask3STOPBM25 = BM25Models.executeBM25ModelOnSystem(queryList, invertedIndexStop, documentLengthStop);
			Results.writeResultsToFile(Constants.PHASE1_TASK3_STOP_BM25, ResultTask3STOPBM25);
			e = Evaluations.getEvaluation(this.ResultTask3STOPBM25);
			Evaluations.writeEvaluationToFile(Constants.PHASE3_STOPPED_BM25, e);
		}catch(NullPointerException ne) {
			
			ne.printStackTrace();
			System.out.println("Could not perform evaluation on BM25 Run Stopped(Phase 1)");
		}
		try {
			this.ResultTask3STOPSQL = QueryLikelihoodModel.executeSQLOnSystem(queryList, relevanceInfoList, invertedIndexStop, documentLengthStop);
			Results.writeResultsToFile(Constants.PHASE1_TASK3_STOP_SQL, ResultTask3STOPSQL);
			e = Evaluations.getEvaluation(this.ResultTask3STOPSQL);
			Evaluations.writeEvaluationToFile(Constants.PHASE3_STOPPED_SQL, e);
		}catch(NullPointerException ne) {
			
			ne.printStackTrace();
			System.out.println("Could not perform evaluation on Smoother query likelihood Run Stopped(Phase 1)");
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		try {
			this.ResultTask3STOPTFIDF = TfIdf.executeTfIdfOnSystem(queryList, invertedIndexStop, documentLengthStop);
			Results.writeResultsToFile(Constants.PHASE1_TASK3_STOP_TFIDF, ResultTask3STOPTFIDF);
			e = Evaluations.getEvaluation(this.ResultTask3STOPTFIDF);
			Evaluations.writeEvaluationToFile(Constants.PHASE3_STOPPED_TFIDF, e);
		}catch(NullPointerException ne) {
			
			ne.printStackTrace();
			System.out.println("Could not perform evaluation on TFIDF Run Stopped(Phase 1)");
		}	
		
	}
	
	/**
	 * Index generation, Run 4 baseline models and perform pseudo relevance feedback using the results 
	 * of Query likelihood model 
	 */
	private void phase1() {
		
		while(true) {
			System.out.println("Choose the task of Phase 1"
					+ "\n1. Task 1: Create inverted Indexer. Perform BM25, tf-idf, Smoothed Query Likelihood or Lucene baseline runs"
					+ "\n2. Task 2: Pseudo relevance feedback using one of the given runs" 
					+ "\n3. Task 3: Baseline runs after stopping and Stemming" 
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
	
	/**
	 * Perform tasks using stopped word list or on stemmed corpus
	 * Run BM25, TF-IDF< or query likelihood models on stopped or stemmed corpus
	 */
	@SuppressWarnings("unchecked")
	private void phase1Task3() {
		while(true) {
			System.out.println("Choose one of the following:"
					+ "\n1. Index documents using stop words"
					+ "\n2. Index Stemmed documents"
					+ "\n3. 3 Run BM25, query likelihood and tf-idf on stopped and stemmed index"
					+ "\n9. Go back to the previous menu." 
				    + "\n0. Exit "); 
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
				//3 runs for stopped and stemmed documents
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
	
	/**
	 * Run BM25, Query likelihood or TF-IDF using either stopped word list of stemmed corpus
	 */
	private void phase1Task3Runs() {
		
		while(true) {
			System.out.println("Choose one of the following:"
					+ "\n1. Run BM25, Query likelihood and TF-IDF using stop words"
					+ "\n2. Run BM25, Query likelihood and TF-IDF on stemmed documents"
					+ "\n9. Go back to the previous menu."
					+ "\n0. Exit");
			switch(in.nextInt()) {
			
			case 1:
				// Run bm25 tfidf query likelihood on stopped
				this.ResultTask3STOPBM25 = BM25Models.executeBM25ModelOnSystem(queryList, invertedIndexStop, documentLengthStop);
				Results.writeResultsToFile(Constants.PHASE1_TASK3_STOP_BM25, ResultTask3STOPBM25);
				this.ResultTask3STOPTFIDF = TfIdf.executeTfIdfOnSystem(queryList, invertedIndexStop, documentLengthStop);
				Results.writeResultsToFile(Constants.PHASE1_TASK3_STOP_TFIDF, ResultTask3STOPTFIDF);
				try {
					this.ResultTask3STOPSQL = QueryLikelihoodModel.executeSQLOnSystem(queryList, relevanceInfoList, invertedIndexStop, documentLengthStop);
					Results.writeResultsToFile(Constants.PHASE1_TASK3_STOP_SQL, ResultTask3STOPSQL);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				break;
			
			case 2:
				// run bm25 tfidf query likelihood on stemmed
				this.ResultTask3StemBM25 = BM25Models.executeBM25ModelOnSystem(stemmedQueryList, invertedIndexStem, documentLengthStem);
				this.ResultTask3StemBM25.stream().forEach(query -> {
					query.resultList().stream().forEach(result -> {
						result.changeModelName(result.modelName() + "_Stemmed");
					});
				});
				Results.writeResultsToFile(Constants.PHASE1_TASK3_STEM_BM25, ResultTask3StemBM25);
				this.ResultTask3StemTFIDF = TfIdf.executeTfIdfOnSystem(stemmedQueryList, invertedIndexStem, documentLengthStem);
				this.ResultTask3StemTFIDF.stream().forEach(query -> {
					query.resultList().stream().forEach(result -> {
						result.changeModelName(result.modelName() + "_Stemmed");
					});
				});
				Results.writeResultsToFile(Constants.PHASE1_TASK3_STEM_TFIDF, ResultTask3StemTFIDF);
				try {
					this.ResultTask3StemSQL = QueryLikelihoodModel.executeSQLOnSystem(stemmedQueryList, relevanceInfoList, invertedIndexStem, documentLengthStem);
					this.ResultTask3StemSQL.stream().forEach(query -> {
						query.resultList().stream().forEach(result -> {
							result.changeModelName(result.modelName() + "_Stemmed");
						});
					});
					Results.writeResultsToFile(Constants.PHASE1_TASK3_STEM_SQL, ResultTask3StemSQL);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
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
	
	/**
	 * Parse and index the raw corpus
	 * Run 4 baseline models on the basic inverted index
	 */
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
					System.out.println("Parsed documents are stored in " + Constants.PARSED_CORPUS_DIR);
					System.out.println("Index and document length files are stored in " + Constants.INDEX_DIR);
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
	
	/**
	 * Choose 1 of the 4 baseline runs for the raw corpus
	 */
	/**
	 * 
	 */
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
				this.ResultTask1BM25 = BM25Models.executeBM25ModelOnSystem(queryList, invertedIndexBase, documentLengthBase);
				Results.writeResultsToFile(Constants.TASK1_PHASE1_BM25, ResultTask1BM25);
				break;
			case 2:
				// tf-idf retrieval mode
				this.ResultTask1tfIdf = TfIdf.executeTfIdfOnSystem(queryList, invertedIndexBase, documentLengthBase);
				Results.writeResultsToFile(Constants.TASK1_PHASE1_TFIDF, ResultTask1tfIdf);
				break;
			case 3:
				// Smoothed Query likelihood model
				try {
					this.ResultTask1SQL = QueryLikelihoodModel.executeSQLOnSystem(queryList, relevanceInfoList, invertedIndexBase, documentLengthBase);
					Results.writeResultsToFile(Constants.TASK1_PHASE1_SQL, ResultTask1SQL);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				break;
			case 4:
				//Lucene retrieval model
				try {
					IndexFiles.startIndexing(Constants.LUCENE_INDEX_DIR, Constants.RAW_CORPUS_DIR);
					ResultTask1Lucene = LuceneOutputGeneration.outputGeneration(queryList);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				Results.writeResultsToFile(Constants.TASK1_PHASE1_Lucne, ResultTask1Lucene);
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
	
	
	/**
	 * Perform pseudo relevance feedback on the results of returned by query likelihood model
	 * and again perform query likelihood on the updated list of queries given by pseudo relevance feedback
	 * algorithm
	 */
	private void phase1Task2() {
		
		try {
			this.ResultTask1SQL = QueryLikelihoodModel.executeSQLOnSystem(queryList, relevanceInfoList, invertedIndexBase, documentLengthBase);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Results.writeResultsToFile(Constants.TASK1_PHASE1_SQL, ResultTask1SQL);
		this.pseudoRelevanceUpdatedQueryList = PseudoRelevanceFeedback.performPseudoRelevanceFeedback(ResultTask1SQL, invertedIndexBase, documentLengthBase);
		try {
			this.ResultTask2PRF = QueryLikelihoodModel.executeSQLOnSystem(pseudoRelevanceUpdatedQueryList, relevanceInfoList, invertedIndexBase, documentLengthBase);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Results.writeResultsToFile(Constants.PHASE1_TASK2_PRF, ResultTask2PRF);
	}
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		Index i = new Index(1); // 1 is the nGram
		i.choosePhase();
	}
	
	
}
