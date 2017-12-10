import java.util.List;

/**
 * Represents the postings of a term in the {@link Indexers inverted index}</br></br>
 * <b>docID:</b> fileName document containing the term </br>
 * <b>termFrequency:</b> number of times the term occurred in the given document</br>
 * <b>termPosition:</b> position(s) of the given term in the document</br>
 * 
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
	
	
	/**
	 * @return list of positions where the term occurs in the document
	 */
	List<Integer> termPosition();

	/**
	 * @param newTermFrequency number of time term occurs in the given document
	 */
	void changeTermFrequency(int newTermFrequency);
}
