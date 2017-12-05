import java.io.IOException;
import java.util.HashMap;
import java.util.*;

public class QueryLikelihoodModel {



    public static String QueryLikelihood(String query,List<RelevanceInfo> qmap, HashMap<String,List<Posting>> index)
    {

        /*
        get query number
         */
        /*for(int i:qmap){
            if(i)
        }*/

        String Query_unigram[]=ParseQuery(query);
        for(String query_term:Query_unigram){

        }

        return "";
    }

    private static List<String> parseRelevant(int querynumber){
        /*
        generates a list of relevant documents
         */
        return new ArrayList<>();
    }

    private static String[] ParseQuery(String s){

        s=s.toString().replaceAll("\\s{2,}", " ").replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("(?<![0-9a-zA-Z])[\\p{Punct}]", "").replaceAll("[\\p{Punct}](?![0-9a-zA-Z])", "")
                .replaceAll("http.*?\\s", "");
        s=s.toLowerCase();
        String split[]=s.split(" ");
        return split;

    }

    public static void main(String[] args) throws IOException {
        Indexer i=new Indexer(1,"/Users/hardikshah/CS6200-Project/Documents/ParsedDocuments");
        HashMap<String, List<Posting>> index = i.generateIndex();
        List<RelevanceInfo> qmap = RelevanceInfos.readRelevanceInfoFromFile("/Users/hardikshah/CS6200-Project/ProblemStatement/cacm.rel.txt");
        QueryLikelihoodModel.QueryLikelihood("Hello world, This is Hardik",qmap,index);
    }

}
