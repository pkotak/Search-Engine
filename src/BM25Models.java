import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Gaurav Gandhi
 *
 */
public class BM25Models {
	
	private static HashMap<String, Integer> documentWordTotal;
	private static HashMap<String, List<Posting>> invertedIndex;
	private static List<RelevanceInfo> relevantDocuments;
	private static List<Result> results;
	private static Query queryObj;

	public static List<Result> getResult(Query query1, HashMap<String, List<Posting>> invertedIndex1, List<RelevanceInfo> relevantDocuments1
			,HashMap<String, Integer> documentWordTotal1) { 
		
		documentWordTotal = documentWordTotal1;
		invertedIndex = invertedIndex1;
		relevantDocuments = relevantDocuments1;
		queryObj = query1;
		results = new ArrayList<Result>();
		int ri, qfi;
		List<Posting> docsOfTerm;
		List<Result> result;
		for(String term : queryObj.query().split(" ")) {
			
			ri = calculateri(term,invertedIndex.get(term));
			qfi = calculateqfi(term);
			docsOfTerm = new ArrayList<Posting>(invertedIndex.get(term));
			for(Posting currPosting : docsOfTerm) {
				
				double currentScore = BM25ScoreOfDoc(currPosting.docID(), currPosting.termFrequency(), 
						invertedIndex.get(term).size(), qfi, ri);
				storeScoreOfDocForQuery(currPosting.docID(), currentScore);
			}
			
		}
		
		return null;
	}
	
	/*
	 * Stores the score of the document
	 * 1. If the document is not presents in the data structure then new field is inserted
	 * else the old score of the document is added to the new score
	 */
	private static void storeScoreOfDocForQuery(String docID, double newScore) {
		
		double oldScore = 0.00;
		if(results.stream().anyMatch(x -> x.docID().equals(docID))) {
			Result r = results.stream().filter(x -> x.docID().equals(docID)).findFirst().get();
			oldScore = r.Score();
			r.changeScore(newScore + oldScore);
		}
		else {
			results.add(new Result1(docID, newScore, queryObj.queryID()));
		}
	}
	
	/*
	 * Generates BM25 score of the document
	 */
	private static double BM25ScoreOfDoc(String docID, int fiint, int niint, int qfiint, int riint) {
		
		double k1 = 1.2;
		double b = 0.75;
		double k2 = 100;
		double ri = (double) riint;
		double R = (double) relevantDocuments.size();
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
		//System.out.println((Math.log((firstParameterNumerator / firstParameterDenominator) * secondParameter * thirdParameter)));
		return (Math.log((firstParameterNumerator / firstParameterDenominator) * secondParameter * thirdParameter));
	}
	
	/*
	 * Calculates the nomalization factor of BM25 (K)
	 * Requires document length (Comes from Document_Word_Total_Count.txt file
	 */
	private static double calculateK(double k1, double b, String docID) {
		
		double avdl = getAverageLengthOfDocuments();
		//System.out.println(avdl);
		
		double K = k1 * ((1 - b) + b * (double) documentWordTotal.get(docID) / avdl);
		
		return K;
	}
	
	/*
	 * Calculates the average of all document lengths
	 */
	private static double getAverageLengthOfDocuments() {
		
		return documentWordTotal.values().stream().mapToDouble(x -> x).average().getAsDouble();
	}
	
	private static int calculateri(String term, List<Posting> docsOfTerm) {
		
		return (int) docsOfTerm.stream()
				.filter(x -> relevantDocuments.stream().anyMatch(y -> y.documentID().equals(x)))
				.count();
	}
	
	/*
	 * Calculates the frequency of the given term in the query
	 */
	private static int calculateqfi(String term) {
		
		int count = 0;
		for(String word: queryObj.query().split(" ")) {
			if(word.equals(term))
				count++;
		}
		return count;
	}
	
	public static void main(String[] args) {
		
		
	}
}
