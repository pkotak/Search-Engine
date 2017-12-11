package utilities;

import java.io.File;

public class Constants {
	
    public static final String RAW_CORPUS_DIR = "ProblemStatement" + File.separator + "cacm" + File.separator;
    public static final String RESULTS_DIR ="Results" + File.separator;
    public static final String PHASE1="Phase1"+File.separator;
    public static final String PHASE2="Phase2"+File.separator;
    public static final String PHASE3="Phase3"+File.separator;
    public static final String TASK1="Task1"+File.separator;
    public static final String TASK2="Task2"+File.separator;
    public static final String TASK3="Task3"+File.separator;
    public static final String PARSED_DOC="ParsedDocuments"+File.separator;

    public static final String QUERY_FILE ="ProblemStatement" + File.separator +  "cacm.query.txt";
    public static final String LUCENE_INDEX_DIR =RESULTS_DIR+ "Index" + "LuceneIndex" +  File.separator;
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
    public static final  String RESULT_LUCENE = "Lucene"+File.separator;

    public static final String TASK1_PHASE1_BM25= RESULTS_DIR + PHASE1+TASK1+RESULT_BM25;
    public static final String TASK1_PHASE1_SQL= RESULTS_DIR +PHASE1+TASK1+RESULT_SQL;
    public static final String TASK1_PHASE1_Lucne= RESULTS_DIR + PHASE1 + TASK1 + RESULT_LUCENE;
    public static final String TASK1_PHASE1_TFIDF= RESULTS_DIR + PHASE1+TASK1+RESULT_TFIDF;
    
    public static final String PHASE1_TASK2_PRF = RESULTS_DIR + PHASE1 + TASK2;
    
    public static final String STOPPED = "Stopped" + File.separator;
    public static final String STEMMED = "Stemmed" + File.separator;
    
    public static final String PHASE1_TASK3_STOP_BM25 = RESULTS_DIR + PHASE1 + TASK3 + STOPPED + RESULT_BM25;
    public static final String PHASE1_TASK3_STOP_TFIDF = RESULTS_DIR + PHASE1 + TASK3 + STOPPED +RESULT_TFIDF;
    public static final String PHASE1_TASK3_STOP_SQL = RESULTS_DIR + PHASE1 + TASK3 + STOPPED +RESULT_SQL;
    
    public static final String PHASE1_TASK3_STEM_BM25 = RESULTS_DIR + PHASE1 + TASK3 + STEMMED + RESULT_BM25;
    public static final String PHASE1_TASK3_STEM_TFIDF = RESULTS_DIR + PHASE1 + TASK3 + STEMMED +RESULT_TFIDF;
    public static final String PHASE1_TASK3_STEM_SQL = RESULTS_DIR + PHASE1 + TASK3 + STEMMED +RESULT_SQL;
    
    public static final String PHASE2_SNIPPET = RESULTS_DIR + PHASE2;
    
    public static final String BASELINE = "BaselineRuns" + File.separator;
    public static final String PHASE3_BASELINE_BM25 = RESULTS_DIR + PHASE3 + BASELINE + RESULT_BM25;
    public static final String PHASE3_BASELINE_lUCENE = RESULTS_DIR + PHASE3 + BASELINE + RESULT_LUCENE;
    public static final String PHASE3_BASELINE_SQL = RESULTS_DIR + PHASE3 + BASELINE + RESULT_SQL;
    public static final String PHASE3_BASELINE_TFIDF = RESULTS_DIR + PHASE3 + BASELINE + RESULT_TFIDF;
    
    public static final String PHASE3_STOPPED = "RunsUsingStoppedWords" + File.separator;
    public static final String PHASE3_STOPPED_BM25 = RESULTS_DIR + PHASE3 + PHASE3_STOPPED + RESULT_BM25;
    public static final String PHASE3_STOPPED_SQL = RESULTS_DIR + PHASE3 + PHASE3_STOPPED + RESULT_SQL;
    public static final String PHASE3_STOPPED_TFIDF = RESULTS_DIR + PHASE3 + PHASE3_STOPPED + RESULT_TFIDF;
    
    public static final String PHASE3_QUERYREF = RESULTS_DIR + PHASE3 + "QueryRefinements" + File.separator;
    
    public static final String STEMMED_QUERY_FILE = "ProblemStatement" + File.separator +"cacm_stem.query.txt";
    
    

}
