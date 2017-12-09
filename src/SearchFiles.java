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

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SearchFiles {

    public static List<Result> searchQueries(String query, int query_id) throws Exception {
    	query = query.replaceAll("(?=[]\\[+&|!(){}^\"~*?:\\\\-])", "\\\\").replaceAll("/", "\\\\/");
        String index = Constants.LUCENE_INDEX_DIR;
        String field = "contents";
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new SimpleAnalyzer();
        QueryParser parser = new QueryParser(field, analyzer);

        Query q = parser.parse(query);
        System.out.println("Searching for: " + q.toString(field));
        return doPagingSearch(searcher, q, query_id);
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
    public static List<Result> doPagingSearch(IndexSearcher searcher, Query query, int query_id) throws IOException {
        TopDocs results = searcher.search(query, 100);
        ScoreDoc[] hits = results.scoreDocs;
        List<Result> resultList = new ArrayList<>();
        for(int i = 0; i < hits.length; i++ ){
            Document doc = searcher.doc(hits[i].doc);
            String doc_id = doc.get("path").substring(doc.get("path").lastIndexOf("\\")+1,doc.get("path").length());
            if (doc_id != null) {
                resultList.add(new Result1(doc_id,hits[i].score, query_id, "Lucene", "Parsed_punctuated"));
            }
        }
        return resultList;
    }
}


