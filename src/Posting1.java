import java.util.List;

public class Posting1 implements Posting {

	private String docID;
	private int termFrequency;
	private List<Integer> termPosition;
	
	public Posting1(String docID, int termFrequency, List<Integer> termPosition) {
		
		this.docID = docID;
		this.termFrequency = termFrequency;
		this.termPosition = termPosition;
	}
	
	

	@Override
	public String docID() {
		
		return this.docID;
	}

	@Override
	public int termFrequency() {
		
		return this.termFrequency;
	}

	@Override
	public List<Integer> termPosition() {
		
		return this.termPosition;
	}

	@Override
	public void changeTermFrequency(int newTermFrequency) {
		
		this.termFrequency = newTermFrequency;
		
	}
	
	@Override
	public String toString() {
		
		return (this.docID() + " " + this.termFrequency());
	}
	
	public static void main(String[] args) {
		
		System.out.println("asd");
	}

	
}
