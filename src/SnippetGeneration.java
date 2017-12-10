import java.util.*;
import java.io.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utilities.Constants;
import utilities.FileHandler;

/*
Class to sort HashMap
 */
class ValueComparator implements Comparator<String> {

    HashMap<String, Double> map = new HashMap<String, Double>();

    public ValueComparator(HashMap<String, Double> map) {
        this.map.putAll(map);
    }

    /*
    Function that overrides the function in Comparator Interface
     */
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
	
	/*
	Function to write snippet to file with .html format
	 */
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
	/*
	Function to Perform Snippet Generation for each query
	from query list
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

	/*
	Perform SnippetGeneration
	 */
    public static void GenerateSnippet(Query q) throws IOException {

        /*
        Get Each word from Query
         */
        String qsplit[]=q.query().split(" ");
        List<String> qlist=Arrays.asList(qsplit);

        /*
        Read Common Words from File
         */
        List<String> cwords=ReadCommonWords();

        /*
        Find term frequency for each query in Documents
         */
        Iterator<Result> it=q.resultList().iterator();
        while (it.hasNext()){
            Result r=it.next();
            String s= r.docID();

            String read=scanFile(s);
            read=read.replaceAll("[\\p{Punct}&&[^.-]]+", "");
            read=read.replaceAll("\n",". ");

            /*
            Generate Term Frequency for each query in HashMap
             */
            HashMap<String,Integer> tf=new HashMap<>();
            tf=GenerateTermfrequency(read);

            HashMap<String,Double> sentencerank=new HashMap<>();

            /*
            Generate Sentences from Raw documents
             */
            String sentencewithspace[]=read.split("\\. ");
            List<String> sentence=new ArrayList<>();
            for(String s1:sentencewithspace)
                if(!s1.isEmpty())
                    sentence.add(s1);

            Iterator<String> senitr=sentence.iterator();
            int flag=0;

            while(senitr.hasNext()){
                String sen=senitr.next();
                List<String> significant =new ArrayList<>();
                HashMap<String,Double> wordscore=new HashMap<>();
                String []sen_words=sen.split("\\s+");
                /*
                Score sentences according to length of Sentence
                and term Frequency.
                 */
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
                /*
                Sentences get ranked according to Significant Words
                 */
                if(!significant.isEmpty())
                {
                    String span="";
                    try {

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

                    }
                    /*
                    Generate Span for each sentence
                     */
                    span=span+significant.get(significant.size()-1);
                    double senscore=(double)(significant.size()*significant.size())/span.length();
                    /*
                    Rank sentences according to the span
                     */
                    sentencerank.put(span,senscore);
                }
            }
            /*
            Restrict the snippet sentences to three.
             */
            if(flag==0){
                double count=0;
                for(String ss:sentence){
                    count++;
                    sentencerank.put(ss,count);
                    if (count==3.0)
                        break;
                }
            }
            /*
            Generate Snippet
             */
            TreeMap<String, Double> sortedMap = sortMapByValue(sentencerank);
            String x="";
            for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {

                String key = entry.getKey();
                x = x+ key+" ... ";

            }
            /*
            Highlight terms that are present in the query.
             */
            String result="<p> ";
            String op[]=x.split(" ");
            for(String op1:op){
                if(qlist.contains(op1)) {
                    result=result+"<b> " + op1+" </b>";
                }
                else {
                    result= result+" "+op1;
                    }
            }
            result=result+" </p><br/><br/>";

            r.addSnippet(result);

        }
    }

    public static HashMap<String,Integer> GenerateTermfrequency(String read){
        HashMap<String,Integer> tf=new HashMap<>();
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
        return tf;
    }

    /*
    Read the commonWords from the file
     */
    public static List<String> ReadCommonWords() throws FileNotFoundException {
        String common=readFile(Constants.COMMON_WORDS_FILE);
        String []common_words=common.split("\n");
        List<String> cwords=Arrays.asList(common_words);
        return cwords;
    }

    /*
    Sort the HashMap according to values for ranking
     */
    public static TreeMap<String, Double> sortMapByValue(HashMap<String, Double> map) {

        Comparator<String> comparator = new ValueComparator(map);
        TreeMap<String, Double> result = new TreeMap<String, Double>(comparator);
        result.putAll(map);
        return result;
    }

    /*
    Scan the raw documents
     */
    public static String scanFile(String name) throws IOException {
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

    /*public static void main(String[] args) throws IOException {
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
    }*/

}
