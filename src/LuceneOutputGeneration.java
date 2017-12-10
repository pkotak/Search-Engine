
import java.util.List;

public class LuceneOutputGeneration {

    /**
     * @param queryList
     * @return a list of queries
     * @throws Exception
     * Loops through each query and generates results by calling Lucene's retrieval model
     */
    @SuppressWarnings("static-access")
	public static List<Query> outputGeneration(List<Query> queryList) throws Exception {
        SearchFiles sf = new SearchFiles();
        //List<Query> outputQueryList = new ArrayList<>();
        //int query_id = 1;
        for(Query q: queryList){
            //Query qi = new Query1(query_id,q.query(),null);
            q.putResultList(sf.searchQueries(q.query(),q.queryID()));
            //outputQueryList.add(qi);

            //query_id++;
        }
        return queryList;
    }
}

