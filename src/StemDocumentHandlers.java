import utilities.Constants;
import utilities.FileHandler;

import java.io.File;
import java.io.IOException;

/**
 * Helper class to read stemmed documents
 */
public class StemDocumentHandlers {

    /**
     * @param stemmeDocDir
     * @param stemOutputDir
     * @throws IOException
     * @Effects Creates stemmed documents by reading stemmed document file
     */
    public static void generateStemmedDocuments(String stemmeDocDir, String stemOutputDir) throws IOException {
        FileHandler reader = new FileHandler(stemmeDocDir, 1);
        String currentLine;
        StringBuilder content = new StringBuilder();

        //Making a local copy of the file content
        while((currentLine = reader.readLine()) != null) {
            content.append(currentLine);
        }

        processingDocument(content.toString(), stemOutputDir);

    }

    /**
     * @param content file content
     * @param stemOutputDir Directory where the stemmed documents will be written
     * @throws IOException
     */
    private static void processingDocument(String content,String stemOutputDir) throws IOException {
        String[] splitByHash = content.toString().split("#");
        for(int i = 0; i < splitByHash.length; i++){
            StringBuilder processed_line = new StringBuilder(splitByHash[i].trim());
            // To remove the query numbers from the start of the line
            for(int j=0; j<4; j++){
                if(processed_line.length() !=0 && Character.isDigit(processed_line.charAt(j))){
                    processed_line.setCharAt(j,' ');
                }
            }
            writeDocuments(stemOutputDir,processed_line.toString(),i);
        }
    }

    /**
     * @param stemOutputDir Directory where the stemmed documents will be written
     * @param processed_line processed document
     * @param docId Document ID
     * @throws IOException
     */
    public static void writeDocuments(String stemOutputDir,String processed_line ,int docId) throws IOException {
        FileHandler writer = new FileHandler(stemOutputDir+ File.separator+"stem_"+docId+".txt", 0);
        //To remove any empty lines that may be present
        writer.addText(processed_line.trim());
        writer.closeConnection();
    }
    public static void main(String args[]) throws IOException {
        generateStemmedDocuments(Constants.STEMMED_CORPUS_FILE, Constants.STEM_DOCS_DIR);
    }
}
