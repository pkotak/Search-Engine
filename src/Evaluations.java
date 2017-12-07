import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class for Evaluation1
 * @author Gaurav Gandhi
 *
 */
public class Evaluations {

	/**
	 * @param queryList list of queries with their results
	 * @return Evaluation object
	 * @see
	 * {@link Evaluation1 class} implementing {@link Evaluation}
	 */
	public static Evaluation getEvaluation(List<Query> queryList) {
		
		return new Evaluation1(queryList.stream()
				.filter(query -> !query.listOfRelevantDocuments().isEmpty())
				.collect(Collectors.toList()));
	}
	
	/**
	 * @param q
	 * @param rank
	 * @Where System has been evaluated i.e {@link Evaluations#getEvaluation getEvaluation} method has been called
	 * @return precision at the given rank for the given query
	 */
	public static double getPAtK(Query q, int rank) {
		
		return q.resultList().stream()
				.filter(result -> result.rank() == rank)
				.findFirst().get().precision();
	}
	
}
