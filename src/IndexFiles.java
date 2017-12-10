import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


/**
 * Creates an index to be used by Lucene's retrieval model
 */
public class IndexFiles {

	private static IndexWriter writer;
	private static Directory indexDirectory;
	private static Analyzer analyzer;
	private static IndexWriterConfig iwc;
	private static String docsDirectoryPath;
	


	   
	   // Initiates indexing 
	/**
	 * @param indexDirectoryPath
	 * @param docsDirectoryPath
	 * @throws IOException
	 */
	public static void startIndexing(String indexDirectoryPath, String docsDirectoryPath1) throws IOException {

		//this directory will contain the indexes
		indexDirectory = 
				FSDirectory.open(Paths.get(indexDirectoryPath));

		analyzer = new SimpleAnalyzer();
		iwc = new IndexWriterConfig(analyzer);
		docsDirectoryPath = docsDirectoryPath1;
		iwc.setOpenMode(OpenMode.CREATE);
		writer = new IndexWriter(indexDirectory, iwc);
		indexDocs(writer, Paths.get(docsDirectoryPath));
		writer.close();
	}
	   
	   /**
	 * @param writer
	 * @param path
	 * @throws IOException
	 * Loops through each document in a directory to create its index
	 */
	static void indexDocs(final IndexWriter writer, Path path) throws IOException {
		
		   if(Files.isDirectory(path)) {
			   
			   Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				   
				   @Override
				   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
					   
					   try {
						   indexDoc(writer, file);
					   } catch(IOException io) {
						   
					   }
					   return FileVisitResult.CONTINUE;
				   }
			   });
		   }
	   }
	   
	   /**
	 * @param writer
	 * @param file
	 * @throws IOException
	 * creates index of the current doc
	 */
	static void indexDoc(IndexWriter writer, Path file) throws IOException {
		   
		   try(InputStream stream = Files.newInputStream(file)) {
			   
			   Document doc = new Document();
			   
			   Field pathField = new StringField("path", file.toString(), Field.Store.YES);
			   doc.add(pathField);
			   
			   doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));
			   
			   //System.out.println("Adding: " + file);
			   writer.addDocument(doc);
		   }
	   }
}
