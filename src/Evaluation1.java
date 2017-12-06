import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/*
 * What if the relevant documents don't make it to top 100 result list of that particular query? for  recall
 * Divide by what number
 * Sol. FInd the total number of relevant documents in top 100 of results by looking at cacm.rel file, this will be your denominator for recall.
 */

/**
 * @author Gaurav Gandhi
 *
 */
public class Evaluation1 implements Evaluation {
	
	private double map;
	private double mrr;
	private List<Query> queryList;
	
	public Evaluation1() {
		
		this.map = this.MAP();
		this.mrr = this.MRR();
	}
	
	@Override
	public double MAP() {
		
		generatePrecisionAndRecall();
		calculateMAP();
		calculateMRR();
		return this.map;
	}

	@Override
	public double MRR() {
		
		return this.mrr;
	}

	@Override
	public List<Query> queryListOfSystem() {
		
		return this.queryList;
	}
	
	@Override
	public void calculateMAP() {
		
		calculateAP();
		
	}
	

	@Override
	public void calculateMRR() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void generatePrecisionAndRecall() {
		
		this.queryList.stream().forEach(query -> {
			this.calculatePrecisionRecallByQuery(query);
		});
		
	}
	
	private List<Double> calculateAP() {
		
		List<Double> apList = new ArrayList<Double>();
		this.queryList.stream().forEach(query -> {
			apList.add(query.resultList().stream().mapToDouble(result -> result.precision()).average().getAsDouble());
		});;
		
		return apList;
	}
	
	
	/**
	 * @param query
	 */
	private void calculatePrecisionRecallByQuery(Query query) {
		
		AtomicInteger counterPrecision = new AtomicInteger(0);
		AtomicInteger counterRecall = new AtomicInteger(0);
		
		List<String> docsInResult = query.resultList().stream().map(Result::docID).distinct().collect(Collectors.toList());
		// stream query.listofRelevantDocuments and get the count of these documents which are present in query.resultList
		int noOfRelevantDocumentsInResult = (int) query.listOfRelevantDocuments().stream()
				.filter(relInfo -> docsInResult.contains(relInfo.documentID())).count();
		
		query.resultList().stream().forEach(result -> {
			
			//Precison: if document is relevant then increment relevant count and divide this by the rank of the document
			//Recall: if document is relevant then increment count amd divide by total number of relevant document retrieved
			if(query.listOfRelevantDocuments().stream().anyMatch(relevantDocs -> relevantDocs.documentID().equals(result.docID()))) {
				result.changePrecision((double) counterPrecision.incrementAndGet() / result.rank());
				result.changeRecall((double) counterRecall.incrementAndGet() / noOfRelevantDocumentsInResult); 
			}
			//Precision: if document is not relevant then divide the count by the rank of the document
			//Recall: if document is not relevant then divide the count by total number of relevant document retrieved
			else { 
				result.changePrecision((double) counterPrecision.get() / result.rank());
				result.changeRecall((double) counterRecall.get() / noOfRelevantDocumentsInResult); 
			}

		});
	}
	
	
	
	
	public static void main(String[] args) {
		
		Result r1 = new Result1("1", 0.00, 1);
		Result r2 = new Result1("2", 0.00, 1);
		List<Result> rList = new ArrayList<>(Arrays.asList(r1, r2));
		System.out.println(getMAP(rList));
	}

	

	

}
