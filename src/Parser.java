

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author hardikshah
 */
public class Parser {

    static String doc_id="";

    public void parser(String s) throws IOException {

        /*
        open the file from the documents obtained during crawling
        */
        System.out.println(s);
        String uc=readFile(s+".html");
        //System.out.println(uc);
        File input=new File(s+".html");
        Document doc;

        /*
        Convert it into document type
        Remove all table tags and data
        */
        try {
            doc = Jsoup.parse(input,"UTF-8","");
            doc.getElementsByTag("table").remove();

        } catch (IOException ex) {
            return;
        }

        /*
        parse the document just to retain the text
        */
        String txt=doc.text();
        //System.out.println(txt);

        /*
        convert the txt to lower case
        */
        txt=txt.toLowerCase();

        /*
        consider only the ascii characters; ignore other languages.
        */
        txt=txt.replaceAll("[^\\p{ASCII}]", "");
        String []split_corpus=txt.split(" ");

        /*
        remove all the urls from the text.
        */
        for(int i=0;i<split_corpus.length;i++){
            if(split_corpus[i].contains("https://")||split_corpus[i].contains("http://")
                    ||split_corpus[i].contains("www"))
                split_corpus[i]="";
        }
        String parsed_text="";

        /*
        Do separete parsing on numbers and other text.
        */
        for(int i=0;i<split_corpus.length;i++){

            /*
            Regex to find numbers in a string
            */
            Pattern p=Pattern.compile("\\d[0-9]*");
            Matcher m=p.matcher(split_corpus[i]);

            if(m.find()){
                /*
                if string contains numbers
                */

                char temp[]=split_corpus[i].toCharArray();

                /*
                RIdentifying punctuation at start of number
                */
                if(temp[0]==':'||temp[0]==','||temp[0]==')'||
                        temp[0]=='('||temp[0]=='^'||temp[0]=='"'||temp[0]==';'||temp[0]=='?'||
                        temp[0]=='/'||temp[0]=='\\'||temp[0]=='\''||temp[0]=='!'||temp[0]=='#')
                    temp[0]='.';
                int l=temp.length-1;

                /*
                Identifying punctuation at the end of number.
                */
                if(temp[l]==':'||temp[l]==','||temp[l]==')'||
                        temp[l]=='('||temp[l]=='^'||temp[l]=='"'||temp[l]==';'||temp[l]=='?'||
                        temp[l]=='/'||temp[l]=='\\'||temp[0]=='\''||temp[l]=='!'||temp[l]=='#')
                    temp[l]='.';

                /*
                removing trailing and precedding punctuation from number
                */
                String ss="";
                int flag1=0,flag2=0;

                if(temp[0]=='.')
                    flag1=1;

                if(temp[l]=='.')
                    flag2=1;

                if(flag1==1&&flag2==1)
                    for(int j=1;j<temp.length-1;j++)
                        ss=ss+temp[j];

                else if(flag1==1&&flag2==0)
                    for(int j=1;j<temp.length;j++)
                        ss=ss+temp[j];

                else  if(flag1==0&&flag2==1)
                    for(int j=0;j<temp.length-1;j++)
                        ss=ss+temp[j];

                else{
                    for(int j=0;j<temp.length;j++)
                        ss=ss+temp[j];
                }

                /*
                removing punctuation that may preceed the full stop
                eg: ). or ".
                */
                if(ss.charAt(ss.length()-1)==')'||ss.charAt(ss.length()-1)=='"'
                        ||ss.charAt(ss.length()-1)=='%')
                    ss=ss.substring(0, ss.length()-1);

                if(ss.charAt(0)=='('||ss.charAt(0)=='"'||ss.charAt(0)=='#')
                    ss=ss.substring(1, ss.length());

                char temp1[]=ss.toCharArray();
                String s2="";
                for(int j=0;j<temp1.length;j++){

                    if(temp1[j]=='^'||temp1[j]=='('||temp1[j]==')'||temp1[j]=='?'||temp1[j]=='['
                            ||temp1[j]==']'||temp1[j]=='{'||temp1[j]=='}'||temp1[j]=='\''
                            ||temp1[j]=='!'||temp1[j]=='#')
                        temp1[j]='#';

                }
                for(int j=0;j<temp1.length;j++){
                    if(temp1[j]!='#')
                        s2=s2+temp1[j];
                }
                parsed_text=parsed_text+s2+" ";
            }
            else{
                /*
                removing all punctuation from string which doesnt have numbers
                */
                char temp[]=split_corpus[i].toCharArray();
                for(int j=0;j<temp.length;j++){
                    if(temp[j]==':'||temp[j]==','||temp[j]=='"'||temp[j]=='^'||temp[j]=='('||
                            temp[j]==')'||temp[j]==';'||temp[j]=='\\'||temp[j]=='/'||temp[j]=='?'
                            ||temp[j]=='['||temp[j]==']'||temp[j]=='{' ||temp[j]=='}'||temp[j]=='\''
                            ||temp[j]=='!'||temp[j]=='#')
                        temp[j]='.';

                }
                String ss="";

                for(int j=0;j<temp.length;j++){
                    if(temp[j]!='.')
                        ss=ss+temp[j];
                }
                parsed_text=parsed_text+ss+" ";
            }
        }

        /*
        finding the doc_id
        */
        //System.out.println(parsed_text);

        String link_split[]=s.split("\\/");

        writeFile(parsed_text,link_split[link_split.length-1]);
        //doc_id=doc_id+link_split[link_split.length-1]+"\n";
    }
    /*
    function to read to a file
    */
    public String readFile(String name) throws FileNotFoundException{
        String s="";
        File file=new File(name);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            // System.out.println(scanner.nextLine());
            s=s+scanner.nextLine()+"\n";
        }
        scanner.close();
        return s;
    }
    /*
    function to write to a file
    */
    public void writeFile(String s,String title) throws IOException{
        File dir=new File("ParsedDocuments");
        dir.mkdir();
        File file=new File(dir,title+".txt");
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(s);
        bw.close();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Parser p=new Parser();
        /*
        taking all urls and title from file created during crawling
        */

        //String ut[]=uc.split("\n");

        for(int i=1;i<3205;i++){
            /*
            parser accepts two parameters, title and url of that title
            */
            //String uc=p.readFile("/Users/hardikshah/NetBeansProjects/Parser/cacm");
            if(i<10)
            p.parser("/Users/hardikshah/NetBeansProjects/Parser/cacm/CACM-000"+i);
            else if(i>9 && i< 100)
                p.parser("/Users/hardikshah/NetBeansProjects/Parser/cacm/CACM-00"+i);
            else if(i>99 && i<1000)
                p.parser("/Users/hardikshah/NetBeansProjects/Parser/cacm/CACM-0"+i);
            else
                p.parser("/Users/hardikshah/NetBeansProjects/Parser/cacm/CACM-"+i);
        }
        /*
        generating doc_id in file
        */
        //p.writeFile(doc_id,"doc_id");

    }

}
