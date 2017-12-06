package utilities;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
	
    public static final String RAW_CORPUS_DIR = "Documents" + File.separator + "cacm" + File.separator;
    public static final String LUCENE_OUTPUT_DIR ="Results" + File.separator + "LuceneOutput" + File.separator ;
    public static final String QUERY_FILE ="ProblemStatement" + File.separator +  "cacm.query.txt";
    public static final String LUCENE_INDEX_DIR ="Documents" + File.separator + "LuceneIndex" + File.separator;
    public static final String PARSED_CORPUS_DIR = "Documents" + File.separator + "ParsedDocuments" + File.separator;
    public static final String RELEVANCE_FILE = "ProblemStatement" + File.separator +"cacm.rel.txt";
    public static final String INDEX_FILE = "Documents" + File.separator + "Index" + File.separator + "Index.txt";
    public static final String DOCUMENT_LENGTH_FILE = "Documents"+ File.separator + "DocumentLength" + File.separator + "DocumentLength.txt";
    public static final String COMMON_WORDS_FILE = "ProblemStatement" + File.separator + "common_words";
}
