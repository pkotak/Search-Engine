import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;



/**
 * <b> Constructor template for Evaluation1
 * new Evaluation1()
 * 
 */
public class Evaluation1 implements Evaluation {
	
	private double map; // represents the mean average precision of the given system
	private double mrr; // represents the mean reciprocal rank of the given system
	private final List<Query> queryList; // represents the list of list of queries of the given system
	
	/**
	 * @param queryList
	 * @Where The queries with no relevant documents are removed
	 * @Effects Creates an Evaluation Object
	 * 			Generates precision, recall for each result in the query
	 * 			assigns mean average precision(map) and mean reciprocal rank(mrr)
	 */
	public Evaluation1(List<Query> queryList) {
		
		this.queryList = queryList;
		generatePrecisionAndRecall();
		calculateMAP();
		calculateMRR();
	}
	
	/* (non-Javadoc)
	 * @see Evaluation#MAP()
	 */
	@Override
	public double MAP() {
		
		return this.map;
	}

	/* (non-Javadoc)
	 * @see Evaluation#MRR()
	 */
	@Override
	public double MRR() {
		
		return this.mrr;
	}

	/* (non-Javadoc)
	 * @see Evaluation#queryListOfSystem()
	 */
	@Override
	public List<Query> queryListOfSystem() {
		
		return this.queryList;
	}
	
	
	/**
	 * @Effects calculates mean average precision, assigns it to map
	 */
	private void calculateMAP() {
		
		this.map = calculateAP().stream().mapToDouble(x -> x).average().getAsDouble();
		
	}
	

	/**
	 * @Effects calculates mean reciprocal rank, assigns it to mrr.
	 */
	private void calculateMRR() {

        this.mrr = calculateRR().stream().mapToDouble(x -> x).average().getAsDouble();
	}
	

	/**
	 * @Effects generates precision and recall for each result inside each query in the list of queries
	 */
	private void generatePrecisionAndRecall() {
		
		this.queryList.stream().forEach(query -> {
			this.calculatePrecisionRecallByQuery(query);
		});
		
	}
	
	/**
	 * @return list of Average precision of each query
	 */
	private List<Double> calculateAP() {
		
		List<Double> apList = new ArrayList<Double>();
		this.queryList.stream().forEach(query -> {
			apList.add(query.resultList().stream().mapToDouble(result -> result.precision()).average().getAsDouble());
			//System.out.println("---"+apList);
		});;
		
		return apList;
	}
	
	/**
	 * @return list of reciprocal rank of each query
	 */
	private List<Double> calculateRR() {

		List<Double> rrList = new ArrayList<Double>();
		this.queryList.stream().forEach(query -> {
			Optional<Result> opt = query.resultList().stream()
					.filter(result -> query.listOfRelevantDocuments().stream()
							.anyMatch(rel -> rel.documentID().contains(result.docID())))
					.findFirst();
			if(opt.isPresent()) {
			rrList.add((double) 1 / 
					(query.resultList().stream()
							.filter(result -> query.listOfRelevantDocuments().stream()
									.anyMatch(rel -> rel.documentID().contains(result.docID())))
							.findFirst().get().rank()));
			}
		});;

		return rrList;
	}
	
	
	/**
	 * @param query
	 * @Effects Calculates precision and recall for each result in the given query
	 * @see
	 * {@link Query Query class}
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
		
		//Result r1 = new Result1("1", 0.00, 1);
		//Result r2 = new Result1("2", 0.00, 1);
		//List<Result> rList = new ArrayList<>(Arrays.asList(r1, r2));
		//System.out.println(getMAP(rList));
	}

	

	

}
