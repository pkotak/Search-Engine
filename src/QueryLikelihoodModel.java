import utilities.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of query likelihood retrieval model
 * Returns a list of queries with their corresponding results
 */
public class QueryLikelihoodModel {
	
	
	
	/**
	 * @param queries
	 * @param qmap
	 * @param index
	 * @param documentWordTotal
	 * @return list of queries with their corresponding result list updated
	 * @throws IOException
	 */
	public static List<Query> executeSQLOnSystem(List<Query> queries, List<RelevanceInfo> qmap, HashMap<String, List<Posting>> index
            , HashMap<String, Integer> documentWordTotal) throws IOException {             	
		
		queries.stream().forEach(query -> {
			List<Result> results;
			try {
				results = QueryLikelihood(query, qmap, index, documentWordTotal);
				query.putResultList(results);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		});
		return queries;
	}
	


    /**
     * @param query
     * @param qmap
     * @param index
     * @param documentWordTotal
     * @return list of results for the given query
     * @throws IOException
     */
    public static List<Result> QueryLikelihood(Query query, List<RelevanceInfo> qmap, HashMap<String, List<Posting>> index
            , HashMap<String, Integer> documentWordTotal) throws IOException {

        /*
		Setting constant as lambda
		 */
        double LAMBDA = 0.35;

    	/*
        Get query number
    	 */
        int qno = query.queryID();

        /*
        Get list of relevant documents
         */
        List<String> reldocs;
        try {
        	reldocs = query.listOfRelevantDocuments().stream().filter(x -> x.queryId() == qno).map(x -> x.documentID()).collect(Collectors.toList());
        }catch(NullPointerException ne) {
        	
        	reldocs = new ArrayList<>(Arrays.asList(""));
        }

        /*
        Parsing the query and generating each word from the query;
         */
        String Query_unigram[] = ParseQuery(query.query());

        /*
        Generate a list for each term which contains the Result.
         */
        double score;
        List<Result> scoremap = new ArrayList<>();
        for (String query_term : Query_unigram) {

            try {
                /*
                Get inverted index for each query term
                 */
                List<Posting> plist = index.get(query_term);
                /*
                Get number of times that query term occurs in the collection
                 */
                int cqi = plist.size();
                /*
                Get the Total words in collection
                 */
                int collection_length=getCollectionLength(query.query(),qmap,plist,documentWordTotal);
                Iterator<Posting> pitr2 = plist.iterator();
                while (pitr2.hasNext()) {
                    Posting p1 = pitr2.next();
                    /*
                    Check if document is Relevant
                     */
                    //relevant doc ID
                    
                    if (reldocs.contains(p1.docID())) {

                        /*
                        Calculate the score and add to the score list
                         */
                        score = (1 - LAMBDA) * (p1.termFrequency()) / documentWordTotal.get(p1.docID());
                        Iterator<Result> rlist = scoremap.iterator();
                        int flag = 0;
                        /*
                        if the document score is already available, add it to the old score
                         */
                        while (rlist.hasNext()) {
                            Result r1 = rlist.next();
                            if (r1.docID().equals(p1.docID())) {
                                r1.changeScore(score);
                                flag = 1;
                            }
                        }
                        /*
                        Add if it is not  present in the list
                         */
                        if (flag == 0) {
                            scoremap.add(new Result1(p1.docID(), score, qno,"QueryLikelihood","CaseFolding_Punctuation"));
                        }
                    }
                    /*
                    if document is not relevant
                     */
                    else {
                        score = LAMBDA * cqi / collection_length;
                        Iterator<Result> rlist = scoremap.iterator();
                        int flag = 0;
                        while (rlist.hasNext()) {
                            Result r1 = rlist.next();
                            if (r1.docID().equals(p1.docID())) {
                                r1.changeScore(score);
                                flag = 1;
                            }
                        }
                        if (flag == 0) {
                            scoremap.add(new Result1(p1.docID(), score, qno,"QueryLikelihood", "ParsedPunctuated"));
                        }

                    }
                }


            } catch (NullPointerException ne) {
            	//System.out.println(quer);
                //ne.printStackTrace();
            }
        }

        for(Result r:scoremap){
            r.ApplyLog();
        }
        return Results.sortResultAndRank(scoremap);

    }

    /**
     * @param query
     * @param qmap
     * @param plist
     * @param documentWordTotal
     * @return returns the length of the collection
     * @throws IOException
     */
    private static int getCollectionLength(String query, List<RelevanceInfo> qmap,List<Posting> plist,HashMap<String, Integer> documentWordTotal) throws IOException {
        List<String> reldocs;
        reldocs = getRelevantDocuments(qmap, query);
        Iterator<Posting> pitr = plist.iterator();
        int collection_length = 0;
        while (pitr.hasNext()) {
            Posting p = pitr.next();
            if (!reldocs.contains(p.docID()))
                collection_length += documentWordTotal.get(p.docID());
        }
        return collection_length;
    }

    /**
     * @param query
     * @return query number
     * @throws IOException
     */
    private static int getQueryNumber(String query) throws IOException {
    	/*
    	Get List of Queries
    	 */
        List<String> processed_query = getQueryList();
        Iterator<String> qitr = processed_query.iterator();

        int qno = 0;  //Returns the query no
        int count = 0;

        /*
        Check the user entered query to the query given in file
        to get query number
         */
        while (qitr.hasNext()) {
            ++count;
            String x = qitr.next();
            String temp[] = ParseQuery(x);
            x = "";
            for (String c : temp)
                x = x + c;

            String temp1[] = ParseQuery(query);
            String query1 = "";
            for (String c : temp1)
                query1 = query1 + c;

            if (x.equals(query1)) {
                qno = count;
                break;
            }

        }
        return qno;
    }

    /**
     * @param qmap
     * @param query
     * @return list of relevant documents corresponding to the given query
     * @throws IOException
     */
    private static List<String> getRelevantDocuments(List<RelevanceInfo> qmap, String query) throws IOException {

        List<String> reldocs = new ArrayList<>();
        Iterator<RelevanceInfo> itr = qmap.iterator();
        int qno = getQueryNumber(query);
        while (itr.hasNext()) {
            RelevanceInfo ri = itr.next();
            if (ri.queryId() == qno) {
                reldocs.add(ri.documentID());
            }
        }
        return reldocs;
    }

    /**
     * @return list of queries
     * @throws IOException
     */
    private static List<String> getQueryList() throws IOException {

        SearchFiles sf = new SearchFiles();
        String file_content = sf.generateFileContent();
        List<String> processed_query = sf.getProcessedQueryList(file_content);
        return processed_query;
    }


    /**
     * @param s
     * @return list of parsed string splitting the given string by space
     */
    private static String[] ParseQuery(String s) {

        s = s.toString().replaceAll("\\s{2,}", " ").replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("(?<![0-9a-zA-Z])[\\p{Punct}]", "").replaceAll("[\\p{Punct}](?![0-9a-zA-Z])", "")
                .replaceAll("http.*?\\s", "");
        s = s.toLowerCase();
        String split[] = s.split(" ");
        return split;

    }

    public static void main(String[] args) throws IOException {
        Indexer i = new Indexer(1, Constants.PARSED_CORPUS_DIR);
        HashMap<String, List<Posting>> index = i.generateIndex();

        List<RelevanceInfo> qmap = RelevanceInfos.readRelevanceInfoFromFile(Constants.RELEVANCE_FILE);
        List<Query> qList = Queries.readQueriesFromFile(Constants.QUERY_FILE);
        List<Result> r = QueryLikelihoodModel.QueryLikelihood(qList.get(0)
                , qmap, index, i.getWordCountOfDocuments());

        List<Result> output = Results.sortResultAndRank(r);
        System.out.println(output);
    }

}
