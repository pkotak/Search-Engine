
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * TF-IDF retrieval model
 */
public class TfIdf {
    private static HashMap<String, Integer> documentWordTotal;
    private static HashMap<String, List<Posting>> invertedIndex;
    private static Query queryObj;
    private static List<Result> results;

    /**
     * @param queries
     * @param invertedIndex1
     * @param documentWordTotal1
     * @return list of queries with their corresponding results 
     */
    public static List<Query> executeTfIdfOnSystem(List<Query> queries, HashMap<String, List<Posting>> invertedIndex1,
                                         HashMap<String, Integer> documentWordTotal1) {

    	queries.stream().forEach(query -> {
    		List<Result> results = getResult(query, invertedIndex1, documentWordTotal1);
			query.putResultList(results);
		});

		return queries;
    }
    /**
     * @param query1 Query object
     * @param invertedIndex1 inverted index
     * @param documentWordTotal1 document map and its related frequencies
     * @return Results object -> Document_id, tfidf score, query_id
     */
    public static List<Result> getResult(Query query1, HashMap<String, List<Posting>> invertedIndex1,
                                         HashMap<String, Integer> documentWordTotal1){
        documentWordTotal = documentWordTotal1;
        invertedIndex = invertedIndex1;
        queryObj = query1;
        results = new ArrayList<>();

        for(String term : queryObj.query().split(" ")){
            List<Posting> postings = invertedIndex.get(term);
            if(postings != null) {
                for (Posting p : postings) {
                    double tf = calculateTf(p.docID(), p.termFrequency());
                    double idf = calculateIdf(postings.size());
                    double score = tf * idf;
                    results.add(new Result1(p.docID(), score, query1.queryID(),"TfIdf","CaseFolding_Punctuation"));
                }
            }
        }
//        System.out.println("Results: "+results.get(0).docID()+" "+results.get(0).Score());
        return Results.sortResultAndRank(results);
    }

    /**
     * @param docID  Document ID
     * @param termFrequency Number of times term occurs in a document
     * @return term frequency of term in document
     */
    private static double calculateTf(String docID, int termFrequency){
        int totalDocWords = documentWordTotal.get(docID);
        return (double) termFrequency/totalDocWords;
    }

    /**
     * @param docsWithTerm  Number of documents that contain term
     * @return idf
     */
    private static double calculateIdf(int docsWithTerm){
        double totalDocuments = documentWordTotal.size();
        return 1 + Math.log(totalDocuments/(double) (docsWithTerm+1));
    }
    //TODO: Testing using Indexers new methods.
//    public static void main(String args[]) throws IOException {
//        Indexer r = new Indexer(1, Constants.PARSED_DOC);
//        Query q = new Query1(1,"SETL, Very High Level Languages",null);
//        List<Result> res = getResult(q,r.generateIndex(),r.getWordCountOfDocuments());
//        for(int i=0; i<100; i++){
//            System.out.println(res.get(i).docID()+" "+res.get(i).Score());
//        }
//    }

}
