import java.util.*;
import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utilities.Constants;

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

    public void GenerateSnippet(LinkedHashMap<String,Integer> ranked,String query) throws IOException {

        String qsplit[]=query.split(" ");
        List<String> qlist=Arrays.asList(qsplit);

        String common=readFile(Constants.COMMON_WORDS_FILE);
        String []common_words=common.split("\n");
        List<String> cwords=Arrays.asList(common_words);
        //System.out.println(cwords);

        Iterator it=ranked.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair=(Map.Entry)it.next();
            String s= (String)pair.getKey();

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
                    String span=sen.substring(sen.indexOf(significant.get(0)),sen.indexOf(significant.get(significant.size()-1)));
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
            String result="<html> <body> <p> ";
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
            result=result+" </p> </body> </html>";
            System.out.println(result);
            System.out.println();
        }

    }

    public static TreeMap<String, Double> sortMapByValue(HashMap<String, Double> map) {

        Comparator<String> comparator = new ValueComparator(map);
        TreeMap<String, Double> result = new TreeMap<String, Double>(comparator);
        result.putAll(map);
        return result;
    }


    public String scanFile(String name) throws IOException {
        String s="";
        File file=new File("/Users/hardikshah/SnippetGeneration/cacm/"+name+".html");
        Document d=Jsoup.parse(file,"UTF-8","");
        s=d.text();

        return s;
    }

    public String readFile(String name) throws FileNotFoundException{
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
        SnippetGeneration sg=new SnippetGeneration();
        sg.GenerateSnippet(test,query);
    }

}
