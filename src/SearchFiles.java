/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import utilities.Constants;
import utilities.FileHandler;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/** Simple command-line based search demo. */
public class SearchFiles {


    /** Simple command-line based search demo. */
    public static void searchQueries() throws Exception {
        SearchFiles sf = new SearchFiles();
        String index = Constants.LUCENE_INDEX_DIR;
        String field = "contents";
        String file_content = sf.generateFileContent();
        int query_id = 1;
        List<String> processed_query = sf.getProcessedQueryList(file_content);
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new SimpleAnalyzer();
        QueryParser parser = new QueryParser(field, analyzer);

        for(String q: processed_query){
            Query query = parser.parse(q);
            System.out.println("Searching for: " + query.toString(field));
            doPagingSearch(searcher, query, query_id);
            query_id++;
        }
    }

     String generateFileContent() throws IOException {
        FileHandler file_reader = new FileHandler(Constants.QUERY_FILE,1);
        StringBuilder content = new StringBuilder();
        String currentLine;
        while((currentLine = file_reader.readLine()) != null) {
            content.append(currentLine);
        }
        return content.toString();
    }
     List<String> getProcessedQueryList(String file_content){
        List<String> query_list = new ArrayList<>();
        String[] splitByDoc = file_content.split("</DOC>");

        for (String s : splitByDoc) {
//            int queryId = Integer.parseInt(s.split("</DOCNO>")[0].replace("<DOC>","")
//                    .replace("<DOCNO>", "").trim());
            String query = s.split("</DOCNO>")[1].replace("</DOC>", "");
            query_list.add(query.replaceAll("(?=[]\\[+&|!(){}^\"~*?:\\\\-])", "\\\\").replaceAll("/", "\\\\/"));
        }
        return query_list;
    }
    /**
     * This demonstrates a typical paging search scenario, where the search engine presents
     * pages of size n to the user. The user can then go to the next page if interested in
     * the next hits.
     *
     * When the query is executed for the first time, then only enough results are collected
     * to fill 5 result pages. If the user wants to page beyond this limit, then the query
     * is executed another time and all hits are collected.
     *
     */
    private static void doPagingSearch(IndexSearcher searcher, Query query, int query_id) throws IOException {

        TopDocs results = searcher.search(query, 100);
        ScoreDoc[] hits = results.scoreDocs;
        String sys_name = "Lucene";
        String content = "";
        for(int i = 0; i < hits.length; i++ ){
            Document doc = searcher.doc(hits[i].doc);
            String doc_id = doc.get("path").substring(doc.get("path").lastIndexOf("\\")+1,doc.get("path").length());
            if (doc_id != null) {
                content += query_id+" "+"Q0 "+doc_id+" "+(i+1)+" "+hits[i].score+" "+sys_name+"\n";
            }
        }
        FileHandler file_writer = new FileHandler(Constants.LUCENE_OUTPUT_DIR+"q"+query_id+".txt",0);
        file_writer.addText(content);

        file_writer.closeConnection();
    }
}


