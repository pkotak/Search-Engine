package utilities;

import java.io.File;

public class Constants {
	
    public static final String RAW_CORPUS_DIR = "Documents" + File.separator + "cacm" + File.separator;
    public static final String RESULTS_DIR ="Results" + File.separator;
    public static final String PHASE1="Phase1"+File.separator;
    public static final String PHASE2="Phase2"+File.separator;
    public static final String PHASE3="Phase3"+File.separator;
    public static final String TASK1=RESULTS_DIR+"Task1"+File.separator;
    public static final String TASK2=RESULTS_DIR+"Task2"+File.separator;
    public static final String TASK3=RESULTS_DIR+"Task3"+File.separator;
    public static final String PARSED_DOC="ParsedDocuments"+File.separator;

    public static final String QUERY_FILE ="ProblemStatement" + File.separator +  "cacm.query.txt";
    public static final String LUCENE_INDEX_DIR =RESULTS_DIR+"LuceneIndex"+ File.separator;
    public static final String PARSED_CORPUS_DIR = RESULTS_DIR+ PARSED_DOC +"RawCorpus"+ File.separator;
    public static final String RELEVANCE_FILE = "ProblemStatement" + File.separator +"cacm.rel.txt";
    public static final String INDEX_DIR = RESULTS_DIR+"Index"+File.separator;
    public static final String DOCUMENT_LENGTH_DIR = RESULTS_DIR+"DocumentLength"+File.separator;
    public static final String COMMON_WORDS_FILE = "ProblemStatement" + File.separator + "common_words";
    public static final String STEMMED_CORPUS_FILE = "ProblemStatement" + File.separator + "cacm_stem.txt";
    public static final String STEM_DOCS_DIR = RESULTS_DIR+ "StemmedCorpus"+File.separator;
    public static final String STEM_PARSED_DIR = RESULTS_DIR+PARSED_DOC+"StemmedCorpus"+File.separator;

    public static final  String RESULT_BM25 = "BM25"+File.separator;
    public static final  String RESULT_TFIDF = "TFIDF"+File.separator;
    public static final  String RESULT_SQL = "QueryLikelihood"+File.separator;
    public static final  String RESULT_Lucene = "Lucene"+File.separator;

    public static final String TASK1_PHASE1_BM25=PHASE1+TASK1+RESULT_BM25;
    public static final String TASK1_PHASE1_SQL=PHASE1+TASK1+RESULT_SQL;
    public static final String TASK1_PHASE1_Lucne=PHASE1+TASK1+RESULT_Lucene;
    public static final String TASK1_PHASE1_TFIDF=PHASE1+TASK1+RESULT_TFIDF;

}
