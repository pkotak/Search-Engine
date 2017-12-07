
/**
 * Helper class for Evaluation1
 * @author Gaurav Gandhi
 *
 */
public class Evaluations {

	
	/**
	 * @return Evaluation object
	 * @see
	 * {@link Evaluation1 class} implementing {@link Evaluation}
	 */
	public static Evaluation make() {
		
		return new Evaluation1();
	}
	
	/**
	 * @param q
	 * @param rank
	 * @return precision at the given rank for the given query
	 */
	public static double getPAtK(Query q, int rank) {
		
		return q.resultList().stream()
				.filter(result -> result.rank() == rank)
				.findFirst().get().precision();
	}
}
