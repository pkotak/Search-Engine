import java.util.List;

/**
 * @author Gaurav Gandhi
 *
 */
public interface Posting {
	
	
	/**
	 * @return Document name
	 */
	String docID();
	
	/**
	 * @return frequency of the given term in the document
	 */
	int termFrequency();
	
	
	List<Integer> termPosition();

	void changeTermFrequency(int newTermFrequency);
}
