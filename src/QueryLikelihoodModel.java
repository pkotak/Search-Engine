import utilities.Constants;

import javax.swing.text.Position;
import java.io.IOException;
import java.util.HashMap;
import java.util.*;

public class QueryLikelihoodModel {



    public static List<Result> QueryLikelihood(String query,List<RelevanceInfo> qmap, HashMap<String,List<Posting>> index
    		,HashMap<String, Integer> documentWordTotal) throws IOException {

        /*
         from a function get get query number by counting where query appears
         return qno;
         */
        SearchFiles sf=new SearchFiles();
        String file_content = sf.generateFileContent();
        List<String> processed_query = sf.getProcessedQueryList(file_content);
        Iterator<String> qitr=processed_query.iterator();
        int qno=0;
        int count=0;
        while (qitr.hasNext()){
            ++count;
            String x=qitr.next();
            if(x.equals(query))
                qno=count;
            break;
        }

        double lambda=0.35;
        List<String> reldocs=new ArrayList<>();

        /*
        Get list of relevant documents
         */
        Iterator<RelevanceInfo> itr=qmap.iterator();
        while (itr.hasNext()){
            RelevanceInfo ri=itr.next();
            if(ri.queryId()==qno){
                reldocs.add(ri.documentID());
            }
        }
        double score;
        List<Result> scoremap=new ArrayList<>();
        String Query_unigram[]=ParseQuery(query);
        for(String query_term:Query_unigram){

        	try {
        		System.out.println(query_term);
	                List<Posting> plist=index.get(query_term);
	                int cqi=plist.size();
	                //System.out.println(cqi);
	                Iterator<Posting> pitr=plist.iterator();
	                int collection_length=0;
	                int docSize = documentWordTotal.get(p.docID());
	                while (pitr.hasNext()){
	
	                    Posting p=pitr.next();
	                    if(!reldocs.contains(p.docID()))
	                        collection_length += documentWordTotal.get(p.docID());//p.docLength;
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
	                            if(r1.docID().equals(p1.docID())){
	                                r1.changeScore(score);
	                                flag=1;
	                            }
	                        }
	                        if(flag==1){
	                            scoremap.add(new Result1(p1.docID(),score, qno));
	                        }
	                    }
	                    else{
	                        score=lambda*cqi/collection_length;
	                        Iterator<Result> rlist=scoremap.iterator();
	                        int flag=0;
	                        while(rlist.hasNext()){
	                            Result r1=rlist.next();
	                            if(r1.docID().equals(p1.docID())){
	                                r1.changeScore(score);
	                                flag=1;
	                            }
	                        }
	                        if(flag==1){
	                            scoremap.add(new Result1(p1.docID(),score, qno));
	                        }
	
	                    }
	                }
	
	            System.out.println(scoremap.toString());
        	}catch(NullPointerException ne) {
        		ne.printStackTrace();
        	}
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
        Indexer i=new Indexer(1,Constants.PARSED_CORPUS);
        HashMap<String, List<Posting>> index = i.generateIndex();

        List<RelevanceInfo> qmap = RelevanceInfos.readRelevanceInfoFromFile("../ProblemStatement/cacm.rel.txt");

       // System.out.println(qmap.toString());
       List<Result> r = QueryLikelihoodModel.QueryLikelihood("What articles exist which deal with TSS (Time Sharing System), an operating system for IBM computers?"
    		   ,qmap,index, i.getWordCountOfDocuments());
       
       //System.out.println(r.toString());
    }

}
