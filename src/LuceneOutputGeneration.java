import java.util.ArrayList;
import java.util.List;

public class LuceneOutputGeneration {

    @SuppressWarnings("static-access")
	public static List<Query> outputGeneration(List<Query> queryList) throws Exception {
        SearchFiles sf = new SearchFiles();
        List<Query> outputQueryList = new ArrayList<>();
        int query_id = 1;
        for(Query q: queryList){
            Query qi = new Query1(query_id,q.query(),null);
            qi.putResultList(sf.searchQueries(q.query(),query_id));
            outputQueryList.add(qi);

            query_id++;
        }
        return outputQueryList;
    }
}

