import javafx.geometry.Pos;

import javax.swing.text.Position;
import java.io.IOException;
import java.util.HashMap;
import java.util.*;

public class QueryLikelihoodModel {



    public static List<Result> QueryLikelihood(String query,List<RelevanceInfo> qmap, HashMap<String,List<Posting>> index)
    {

        /*
         from a function get get query number by counting where query appears
         return qno;
         */
        //

        double lambda=0.35;
        List<String> reldocs=new ArrayList<>();

        /*
        Get list of relevant documents
         */
        Iterator<RelevanceInfo> itr=qmap.iterator();
        while (itr.hasNext()){
            RelevanceInfo ri=itr.next();
            if(ri.queryId()==0){
                reldocs.add(ri.documentID());
            }
        }
        double score;
        List<Result> scoremap=new ArrayList<>();
        String Query_unigram[]=ParseQuery(query);
        for(String query_term:Query_unigram){

                List<Posting> plist=index.get(query_term);
                int cqi=plist.size();
                Iterator<Posting> pitr=plist.iterator();
                int collection_length=0;
                while (pitr.hasNext()){

                    Posting p=pitr.next();
                    if(!reldocs.contains(p.docID()))
                        collection_length=collection_length+40000;//p.docLength;
                }
                Iterator<Posting> pitr2=plist.iterator();
                //int score=0;
                while (pitr2.hasNext()){
                    Posting p1=pitr2.next();
                    if(reldocs.contains(p1.docID())){
                        score=(1-lambda)*(p1.termFrequency())/(40000);//p1.docLength;
                        Iterator<Result> rlist=scoremap.iterator();
                        int flag=0;
                        while(rlist.hasNext()){
                            Result r1=rlist.next();
                            if(r1.DocId().equals(p1.docID())){
                                r1.changeScore(score);
                                flag=1;
                            }
                        }
                        if(flag==1){
                            scoremap.add(new Result1(p1.docID(),score));
                        }
                    }
                    else{
                        score=lambda*cqi/collection_length;
                        Iterator<Result> rlist=scoremap.iterator();
                        int flag=0;
                        while(rlist.hasNext()){
                            Result r1=rlist.next();
                            if(r1.DocId().equals(p1.docID())){
                                r1.changeScore(score);
                                flag=1;
                            }
                        }
                        if(flag==1){
                            scoremap.add(new Result1(p1.docID(),score));
                        }

                    }
                }

            System.out.println(scoremap);
        }

        return  scoremap;

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

        System.out.println(index);
        List<RelevanceInfo> qmap = RelevanceInfos.readRelevanceInfoFromFile("/Users/hardikshah/CS6200-Project/ProblemStatement/cacm.rel.txt");

        System.out.println(qmap);
        //QueryLikelihoodModel.QueryLikelihood("What articles exist which deal with TSS (Time Sharing System), an\n" +
          //      "operating system for IBM computers?",qmap,index);
    }

}
