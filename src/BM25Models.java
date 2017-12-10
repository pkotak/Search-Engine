import utilities.Constants;
import utilities.FileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Implementation of BM25 retrieval model
 * Returns a list of queries with results by using BM25 model
**/
public class BM25Models {
	
	private static HashMap<String, Integer> documentWordTotal;
	private static HashMap<String, List<Posting>> invertedIndex;
	private static List<RelevanceInfo> relevantDocuments;
	private static List<Result> resultList;
	private static Query queryObj;
	
	/**
	 * @param queries
	 * @param invertedIndex1
	 * @param documentWordTotal1
	 * @return a list of queries with results updated after performing BM25 retrieval model
	 */
	public static List<Query> executeBM25ModelOnSystem(List<Query> queries, HashMap<String, List<Posting>> invertedIndex1, HashMap<String, Integer> documentWordTotal1) {
		
		queries.stream().forEach(query -> {
			List<Result> results = getResult(query, invertedIndex1, documentWordTotal1);
			query.putResultList(results);
		});
		return queries;
	}

	/**
	 * @param query1 Query object
	 * @param invertedIndex1 Inverted Index
	 * @param documentWordTotal1 length of each document of the corpus used for generating invertedIndex1
	 * @return a list of Result
	 * @see
	 * <b>Query:</b> Refer {@link Query} </br>
	 * <b>Inverted Index:</b> Refer {@link Indexers} class </br>
	 * <b>Relevant Documents:</b> Refer {@link RelevanceInfo} </br>
	 * <b>Document length:</b> Refer {@link Indexers} class </br>
	 */
	public static List<Result> getResult(Query query1, HashMap<String, List<Posting>> invertedIndex1, HashMap<String, Integer> documentWordTotal1) { 
		
		documentWordTotal = documentWordTotal1;
		invertedIndex = invertedIndex1;
		relevantDocuments = query1.listOfRelevantDocuments();
		boolean relevanceFlag = true;
		if(relevantDocuments == null)
			relevanceFlag = false;
		
		queryObj = query1;
		resultList = new ArrayList<Result>();
		int ri, qfi;
		List<Posting> docsOfTerm;
		//System.out.println(queryObj.query());
		for(String term : queryObj.query().toLowerCase().split(" ")) {
			try {
				//System.out.println(term);
				if(!relevanceFlag)
					ri = 0;
				else
					ri = calculateri(term,invertedIndex.get(term));
				//ri = 0;
				qfi = calculateqfi(term);
				docsOfTerm = new ArrayList<Posting>(invertedIndex.get(term));
				for(Posting currPosting : docsOfTerm) {
					
					double currentScore = BM25ScoreOfDoc(currPosting.docID(), currPosting.termFrequency(), 
							invertedIndex.get(term).size(), qfi, ri);
					storeScoreOfDocForQuery(currPosting.docID(), currentScore);
				}
			}catch(NullPointerException ne) {
				
				//ne.printStackTrace();
			}
			
		}
		
		return Results.sortResultAndRank(resultList);
	}
	
	/**
	 * @param docID document ID
	 * @param newScore score of the given document
	 * @Effects Creates or updates the result object and adds it to the list of Result
	 * @See
	 * {@link Result}
	 */
	private static void storeScoreOfDocForQuery(String docID, double newScore) {
		
		double oldScore = 0.00;
		if(resultList.stream().anyMatch(x -> x.docID().equals(docID))) {
			Result r = resultList.stream().filter(x -> x.docID().equals(docID)).findFirst().get();
			oldScore = r.Score();
			r.changeScore(newScore + oldScore);
		}
		else {
			resultList.add(new Result1(docID, newScore, queryObj.queryID(),"BM25","CaseFolding_Punctuation"));
		}
	}
	
	/**
	 * @param docID document ID
	 * @param fiint frequency of the term in the document
	 * @param niint number of documents containing the term
	 * @param qfiint frequency of the term in the query
	 * @param riint number of relevant document containing the term
	 * @return BM25 Score of the given document
	 * @see
	 * <b> Term:</b> Term is a word of the query and is present in the inverted index </br>
	 * <b> Query: </b> Refer {@link Query} </br>
	 * <b> Inverted Index: </b> Refer {@link Indexers} </br></br>
	 * <b> Other parameters of BM25: </b> </br>
	 * <p><b>k1:</b> determines the weighting factor of the term frequency component in a document</br>
	 * <b>b:</b> is used to calculate K</br>
	 * <b>k2:</b> determines the weighting factor of the term frequency component in a query</br>
	 * <b>R:</b> is the number of relevant documents for the given Query. Refer {@link Query}</br>
	 * <b>K:</b> Normalization factor of the term frequency component
	 */
	private static double BM25ScoreOfDoc(String docID, int fiint, int niint, int qfiint, int riint) {
		
		double k1 = 1.2;
		double b = 0.75;
		double k2 = 100;
		double ri = (double) riint;
		double R;
		try {
			R = (double) relevantDocuments.size();
		}catch(NullPointerException ne) {
			R = 0;
		}
		//double R = 0;
		double K = calculateK(k1, b, docID);
		double ni = (double) niint;
		double fi = (double) fiint;
		double qfi = (double) qfiint;
		
		
		double firstParameterNumerator = (ri + 0.5) / (R - ri + 0.5);
		//System.out.println("first NUM" + firstParameterNumerator);
		double firstParameterDenominator = (ni - ri + 0.5) / (documentWordTotal.size() - ni - R + ri + 0.5);
		//System.out.println("first DEN" + firstParameterDenominator);
		double secondParameter = ((k1 + 1) * fi) / (K + fi);
		//System.out.println("second" + secondParameter);
		double thirdParameter = ((k2 + 1) * qfi) / (k2 + qfi);
		//System.out.println("third" + thirdParameter);
		//System.out.println((firstParameterNumerator / firstParameterDenominator) * secondParameter * thirdParameter);
		//System.out.println((Math.log((firstParameterNumerator / firstParameterDenominator) * secondParameter * thirdParameter)));
		return (Math.log((firstParameterNumerator / firstParameterDenominator) * secondParameter * thirdParameter));
	}
	
	/**
	 * @param k1 Refer {@link #BM25ScoreOfDoc() BM25 scoring method}
	 * @param b Refer {@link #BM25ScoreOfDoc() BM25 scoring method}
	 * @param docID document ID
	 * @return normalizing factor of the term frequency component of the given document
	 */
	private static double calculateK(double k1, double b, String docID) {
		
		double avdl = getAverageLengthOfDocuments();
		//System.out.println(avdl);
		
		double K = k1 * ((1 - b) + b * (double) documentWordTotal.get(docID) / avdl);
		
		return K;
	}
	
	/**
	 * @return average of length of all documents in the corpus
	 */
	private static double getAverageLengthOfDocuments() {
		
		return documentWordTotal.values().stream().mapToDouble(x -> x).average().getAsDouble();
	}
	
	/**
	 * @param term a term
	 * @param docsOfTerm list of {@link Posting} having the given term
	 * @return count of all the relevant documents ({@link Query} and {@link RelevanceInfo}) containing the given term
	 */
	private static int calculateri(String term, List<Posting> docsOfTerm) {
		try {
			return (int) docsOfTerm.stream()
				.filter(x -> relevantDocuments.stream().anyMatch(y -> y.documentID().equals(x)))
				.count();
		}catch(NullPointerException nfe) {
			return 0;
		}
	}
	
	/**
	 * @param term a term
	 * @return count of the occurrence of the given term in the {@link Query}
	 */
	private static int calculateqfi(String term) {
		
		int count = 0;
		for(String word: queryObj.query().toLowerCase().split(" ")) {
			if(word.equals(term))
				count++;
		}
		return count;
	}
	
	public static void main(String[] args) throws IOException {
		
		
		List<Query> q = Queries.readQueriesFromFile(Constants.QUERY_FILE);
		
		/*q.stream().forEach(x -> {
			System.out.println(x.query());
		});*/
		
		Indexer i = new Indexer(1, Constants.PARSED_CORPUS_DIR);
		List<RelevanceInfo> relList = RelevanceInfos.readRelevanceInfoFromFile(Constants.RELEVANCE_FILE);
		relList = RelevanceInfos.getRelevanceInfoByQueryID(1, relList);
		List<Result> r = BM25Models.getResult(q.stream().findFirst().get()
				, i.generateIndex(), i.getWordCountOfDocuments());
		
		r.stream().forEach(Result::toString);
		
		FileHandler tr = new FileHandler("Query.txt", 0);
		for(Result ro : r) {
			
			tr.addText(ro.toString() + "\n");
		}
		tr.closeConnection();
	}
}
