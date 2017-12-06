import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * What if the relevant documents don't make it to top 100 result list of that particular query? for  recall
 * Divide by what number
 * Sol. FInd the total number of relevant documents in top 100 of results by looking at cacm.rel file, this will be your denominator for recall.
 */

/**
 * @author Gaurav Gandhi
 *
 */
public class Evaluation {
	
	private static List<Result> resultList;
	private static List<Query> queryList;

	private static void calculatePrecisionOfResultByQuery(Query query, List<Result> result) {
		
		
	}
	
	/**
	 * @param result
	 * @See
	 * @return
	 */
	public static double getMeanAveragePrecision(List<Query> query, List<Result> result) {
		
		resultList = result;
		queryList = query;
		
		//Generate Average Precision Of Each Query
		generateAveragePrecisionByQuery();
		
		return result.stream().mapToDouble(x -> x.precision()).average().getAsDouble();
	}
	
	private static void generateAveragePrecisionByQuery() {
		
		// Generate Precision of all results By Query
		generatePrecisionByQuery();
		
	}
	
	private static void generatePrecisionByQuery() {
		
		queryList.stream().forEach(query -> {
			resultList.stream().filter(result -> result.queryID() == query.queryID())
			.sorted((a, b) -> Integer.compare(a.rank(), b.rank()))
			.forEach(result -> {
				
			});
		});
	}
	
	
	public static void main(String[] args) {
		
		Result r1 = new Result1("1", 0.00, 1);
		Result r2 = new Result1("2", 0.00, 1);
		List<Result> rList = new ArrayList<>(Arrays.asList(r1, r2));
		//System.out.println(getMAP(rList));
	}
}
