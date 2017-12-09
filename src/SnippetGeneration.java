import java.util.*;
import java.io.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utilities.Constants;
import utilities.FileHandler;

class ValueComparator implements Comparator<String> {

    HashMap<String, Double> map = new HashMap<String, Double>();

    public ValueComparator(HashMap<String, Double> map) {
        this.map.putAll(map);
    }

    @Override
    public int compare(String s1, String s2) {
        if (map.get(s1) >= map.get(s2)) {
            return -1;
        } else {
            return 1;
        }
    }
}

public class SnippetGeneration {
	
	private static FileHandler writer;
	
	
	public static void writeSnippetToFile(String filePath, List<Query> queryList) {
		
		queryList.stream().forEach(query -> {
			
			try {
				writer = new FileHandler(filePath + query.queryID() + ".html", 0);
				writer.addText("<html><body>");
				query.resultList().stream().map(x -> x.snippet()).forEach(snippet -> {
					
					try {
						writer.addText(snippet);
					} catch (IOException e) {

						e.printStackTrace();
					}
				});
				writer.addText("</body></html>");
				writer.closeConnection();
				
			} catch (IOException e) {

				e.printStackTrace();
			}
		});
		
		System.out.println("Snippets of each query are stored in:" + filePath);
	}
	
	
	/**
	 * @param queryList
	 * @return
	 */
	public static List<Query> performSnippetGeneration(List<Query> queryList) {
		
		queryList.stream().forEach(query -> {
			try {
				GenerateSnippet(query);
			} catch (IOException e) {

				e.printStackTrace();
			}
		});
		
		return queryList;
	}

    public static void GenerateSnippet(Query q) throws IOException {

        String qsplit[]=q.query().split(" ");
        List<String> qlist=Arrays.asList(qsplit);

        String common=readFile(Constants.COMMON_WORDS_FILE);
        String []common_words=common.split("\n");
        List<String> cwords=Arrays.asList(common_words);
        //System.out.println(cwords);

        Iterator<Result> it=q.resultList().iterator();
        while (it.hasNext()){
            Result r=it.next();
            String s= r.docID();

            String read=scanFile(s);
            read=read.replaceAll("[\\p{Punct}&&[^.-]]+", "");
            read=read.replaceAll("\n",". ");

            HashMap<String,Integer> tf=new HashMap<>();
            //System.out.println("---"+read);
            String[] wordsindoc=read.split(" ");
            for(String x:wordsindoc){
                for(String x2:wordsindoc){
                    if(x.equals(x2)){
                        if(tf.containsKey(x)){
                            int c=tf.get(x);
                            c=c+1;
                            tf.remove(x);
                            tf.put(x,c);
                        }
                        else{
                            tf.put(x,1);
                        }
                    }

                }
            }
            /*
            use term avg and sentence avg instead of 7 and 25
             */

            HashMap<String,Double> sentencerank=new HashMap<>();

            //System.out.println(tf);
            String sentencewithspace[]=read.split("\\. ");
            //System.out.println(sentence[1]);
            List<String> sentence=new ArrayList<>();
            for(String s1:sentencewithspace)
                if(!s1.isEmpty())
                    sentence.add(s1);
            //System.out.println(sentence);

            Iterator<String> senitr=sentence.iterator();
            int flag=0;
            while(senitr.hasNext()){
                String sen=senitr.next();
                List<String> significant =new ArrayList<>();
                HashMap<String,Double> wordscore=new HashMap<>();
                String []sen_words=sen.split("\\s+");
                for(String word:sen_words){
                    if(cwords.contains(word))
                        wordscore.put(word,0.0);
                    else{
                        if(sentence.size()<15){
                            double score=1-0.1*(15-sentence.size());
                            if(tf.containsKey(word)&&score<=tf.get(word)) {
                                significant.add(word);
                                flag=1;
                            }
                            wordscore.put(word,score);
                        }
                        else if(sentence.size()>=15&&sentence.size()<=25){
                            double score=1;
                            if(tf.containsKey(word)&&score<=tf.get(word)) {
                                significant.add(word);
                                flag=1;
                            }
                            wordscore.put(word,score);
                        }
                        else{
                            double score=1+0.1*(sentence.size()-25);
                            if(tf.containsKey(word)&&score<=tf.get(word)) {
                                significant.add(word);
                                flag=1;
                            }
                            wordscore.put(word,score);
                        }
                    }

                }
                //System.out.println(sen+" "+significant);
                //break;
                //System.out.println(wordscore);
                //System.out.println(sen+" "+significant);
                //break;
                if(!significant.isEmpty())
                {
                    String span="";
                    String temp = null;
                    try {
                    	// get first word from sen occurring in significant list
                    	//String[] spaceSplit = sen.split(" ");
                    	int startIndex = sen.length();
                    	int endIndex = 0;
                    	for(String word: significant) {
                    		
                    		if(sen.indexOf(word) < startIndex)
                    			startIndex = sen.indexOf(word);
                    		if((sen.indexOf(word) + (word.length() - 1)) > endIndex)
                    			endIndex = sen.indexOf(word) + (word.length() - 1);
 
                    	}
                        span = sen.substring(startIndex, endIndex);
                    }catch(Exception e){
                    	e.printStackTrace();
                    	System.out.println(span);
                    	System.out.println(sen);
                    	System.out.println(significant.toString());
                    	System.exit(1);
                    }
                    span=span+significant.get(significant.size()-1);
                    //System.out.println(span);
                    double senscore=(double)(significant.size()*significant.size())/span.length();
                    sentencerank.put(span,senscore);
                }
            }
            if(flag==0){
                double count=0;
                for(String ss:sentence){
                    count++;
                    sentencerank.put(ss,count);
                    if (count==3.0)
                        break;
                }
            }
            TreeMap<String, Double> sortedMap = sortMapByValue(sentencerank);
            //System.out.println(sortedMap);
            String x="";
            for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {

                String key = entry.getKey();
                double value = entry.getValue();
                x = x+ key+" ... ";
                //System.out.print(x);

            }
            // System.out.println();
            String result="<p> ";
            String op[]=x.split(" ");
            for(String op1:op){
                //System.out.println(op1);
                if(qlist.contains(op1)) {
                    result=result+"<b> " + op1+" </b>";
                    //System.out.print("<b>" + op1+"</b>");
                }
                else {
                    result= result+" "+op1;
                    //System.out.print(" " + op1);
                }
            }
            result=result+" </p><br/><br/>";
            System.out.println(q.queryID());
            System.out.println(result);
            r.addSnippet(result);
            System.out.println();

        }
        System.out.println();
    }

    public static TreeMap<String, Double> sortMapByValue(HashMap<String, Double> map) {

        Comparator<String> comparator = new ValueComparator(map);
        TreeMap<String, Double> result = new TreeMap<String, Double>(comparator);
        result.putAll(map);
        return result;
    }


    public static String scanFile(String name) throws IOException {
    	//TODO Use FileHandler
        String s="";
        File file=new File(Constants.RAW_CORPUS_DIR + name + ".html");
        Document d=Jsoup.parse(file,"UTF-8","");
        s=d.text();
        return s;
    }

    public static String readFile(String name) throws FileNotFoundException{
        String s="";
        File file=new File(name);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            s=s+scanner.nextLine()+"\n";
        }
        scanner.close();
        return s;
    }

    public static void main(String[] args) throws IOException {
        LinkedHashMap<String,Integer> test =new LinkedHashMap<>();
        test.put("CACM-0001",1);
        test.put("CACM-0002",1);
        test.put("CACM-0003",1);
        test.put("CACM-0004",1);
        String query="Extraction Repeated Digital";
        Indexer i = new Indexer(1, Constants.PARSED_CORPUS_DIR);
        List<RelevanceInfo> relList = RelevanceInfos.readRelevanceInfoFromFile(Constants.RELEVANCE_FILE);
        relList = RelevanceInfos.getRelevanceInfoByQueryID(1, relList);
        List<Query> q=Queries.readQueriesFromFile(Constants.QUERY_FILE);
        HashMap<String, List<Posting>> invertedIndex = i.generateIndex();
        HashMap<String, Integer> documentLength = i.getWordCountOfDocuments();
        //sg.GenerateSnippet("What articles exist which deal with TSS \\(Time Sharing System\\), anoperating system for IBM computers\\?");
        for(Query qq:q) {
                List<Result> r = QueryLikelihoodModel.QueryLikelihood(qq, relList, invertedIndex, documentLength);
                qq.putResultList(r);
                GenerateSnippet(qq);

        }

        //sg.GenerateSnippet(test,query);
    }

}
