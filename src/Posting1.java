import java.util.List;

/**
 * <b>Constructor template:</b></br>
 * <p> new Posting1(documentID, termFrequency, termPosition) </p> </br></br>
 * <b>Interpretation</b></br>
 * <p>documentID: String represents the file name of the document</br>
 * termFrequency: int represent the number of times a term occurs in the document (Refer {@link Indexers inverted index}</br>
 * termPosition: List<Integer> represents the position(s) of a term in the document</p></br>
 * 
 * 
 *
 */
public class Posting1 implements Posting {

	private String docID;
	private int termFrequency;
	private List<Integer> termPosition;
	
	/**
	 * @param docID file name of the document
	 * @param termFrequency number of times a term occurs in the document
	 * @param termPosition position(s) of a term in the document
	 */
	public Posting1(String docID, int termFrequency, List<Integer> termPosition) {
		
		this.docID = docID;
		this.termFrequency = termFrequency;
		this.termPosition = termPosition;
	}
	
	

	/* (non-Javadoc)
	 * @see Posting#docID()
	 */
	@Override
	public String docID() {
		
		return this.docID;
	}

	/* (non-Javadoc)
	 * @see Posting#termFrequency()
	 */
	@Override
	public int termFrequency() {
		
		return this.termFrequency;
	}

	/* (non-Javadoc)
	 * @see Posting#termPosition()
	 */
	@Override
	public List<Integer> termPosition() {
		
		return this.termPosition;
	}

	/* (non-Javadoc)
	 * @see Posting#changeTermFrequency(int)
	 */
	@Override
	public void changeTermFrequency(int newTermFrequency) {
		
		this.termFrequency = newTermFrequency;
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		return "[" + (this.docID() + ", " + this.termFrequency()) + " ]";
	}
	
	public static void main(String[] args) {
		
		//TODO not tested
		System.out.println("asd");
	}

	
}
