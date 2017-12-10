

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import utilities.Constants;
import utilities.FileHandler;


/**
 * Class used for parsing through the documents
 * Cleans the corpus
 */
public class Parser {

    private static String outputDirectoryPath = null;
    /**
     * @param parseType
     * @param directoryPath
     * @throws IOException
     */
    public static void parseAllFiles(int parseType, String directoryPath, String outputDirectoryPath1) throws IOException {
    	
    	outputDirectoryPath = outputDirectoryPath1;
    	Files.list(Paths.get(directoryPath))
    	.forEach(p -> {
    		try {
    			parsing(parseType, p.toString());
    		}catch(IOException e) {
    			e.printStackTrace();
    		}
    	});
    }
    
    /**
     * @param parseType
     * @param filePath
     * @throws IOException
     */
    private static void parsing(int parseType, String filePath) throws IOException {
    	
    	FileHandler textReader = new FileHandler(filePath, 1);
    	StringBuilder content = new StringBuilder();
    	String currentLine;
    	while((currentLine = textReader.readLine()) != null) {
    		content.append(currentLine);    	
    	}
    	//Remove the tags which are not required
    	Document doc = getRefinedDocument(Jsoup.parse(content.toString()));
    	
    	// Parse the text remove irrelevant text
		StringBuilder parsedText = getRefinedText(parseType, new StringBuilder(doc.text()));
		
		// create the parsed file
		createParsedFile(filePath, parsedText.toString());
    	
    	textReader.closeConnection();
    }
    
    /**
     * @param filePath
     * @param parsedText
     * @throws IOException
     */
    private static void createParsedFile(String filePath, String parsedText) throws IOException {
    	
    	String docName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
		docName = docName.substring(0, docName.indexOf('.'));
    	FileHandler textWriter = new FileHandler(outputDirectoryPath + docName + ".txt", 0);
    	textWriter.addText(parsedText.toString());
    	textWriter.closeConnection();
    }
    
    /**
     * @param doc
     * @return
     */
    private static Document getRefinedDocument(Document doc) {
    	
    	doc.getElementsByTag("table").remove();
		doc.getElementsByTag("img").remove();
		doc.getElementsByTag("input").remove();
		doc.getElementsByAttributeValueStarting("href", "#").remove();
		
		return doc;
    }
    
    /**
     * @param parseType
     * @param text
     * @return
     */
    private static StringBuilder getRefinedText(int parseType, StringBuilder text) {
    	
    	// Following steps are to remove hidden value(looks like extra space) ASCII value 9
    	char[] parsedTextArr = text.toString().toCharArray();
    	text = new StringBuilder("");
    	for(char a : parsedTextArr) {

    		if((int) a != 9)
    			text.append(a);
    		else
    			text.append(" ");
    	}
    	
    	//Case folding and removing irrelevant text
    	if(parseType == 1 || parseType == 3)
			text = new StringBuilder(performCaseFolding(text));
		if(parseType == 2 || parseType == 3)
			text = new StringBuilder(handlePunctuation(text));
		
		
    	
		return text;
    }
    
    /*
	 * Return a text by transforming to input to lower case
	 */
	/**
	 * @param parsedText
	 * @return
	 */
	public static String performCaseFolding(StringBuilder parsedText) {
		
		return parsedText.toString().toLowerCase();
		
	}
	
	/*
	 * Method to handlerPunctuation of the given text
	 * Returns a text after removing extra spaces, non ASCII characters, all irrelevant special characters,
	 * and the urls
	 */
	/**
	 * @param parsedText
	 * @return
	 */
	public static String handlePunctuation(StringBuilder parsedText) {
		
		return parsedText.toString().replaceAll("\\s{2,}", " ").replaceAll("[^\\p{ASCII}]", "")
				.replaceAll("(?<![0-9a-zA-Z])[\\p{Punct}]", "").replaceAll("[\\p{Punct}](?![0-9a-zA-Z])", "")
				.replaceAll("http.*?\\s", "");
	}



    public static void main(String[] args) throws FileNotFoundException, IOException {

    	/*System.out.println(Constants.raw_corpus_dir.toString());
    	System.out.println("Documents" + File.separator + "CACM" + File.separator);
    	String path = */
    	//Path p = Paths.get("Documents\\cacm\\");
        Parser.parseAllFiles(3, Constants.RAW_CORPUS_DIR, Constants.PARSED_CORPUS_DIR);
       // System.out.println(Parser.handlePunctuation(new StringBuilder("List all articles on EL1 and ECL (EL1 may be given as EL/1; I don't remember how they did it..")));

    }

}
