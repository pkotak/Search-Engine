package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Helper class for reading/writing  a text file
 *
 */
public class FileHandler {
	
	//private static final String FILE_NAME = "Links.txt";
	private BufferedWriter B_WRITER;
	private FileWriter F_WRITER;
	private BufferedReader B_READER;
	private FileReader F_READER;
	public FileHandler(String file_name, int type) throws IOException {
		//System.out.println();
		if(type == 0) {
			F_WRITER = new FileWriter(file_name);
			B_WRITER = new BufferedWriter(F_WRITER);
		}
		else {
			F_READER = new FileReader(file_name);
			B_READER = new BufferedReader(F_READER);
		}
	}
	
	public void emptyFile() throws IOException {
		B_WRITER.write("");
	}
	
	public void addText(String str) throws IOException {
		B_WRITER.append(str);
	}
	
	public String readLine() throws IOException {
		return B_READER.readLine();
	}
	
	public void closeConnection() throws IOException {
		try {
			B_WRITER.close();
			F_WRITER.close();
		}catch(NullPointerException ne) {
			
			B_READER.close();
			F_READER.close();
		}
			
	}
	
	
	
}

