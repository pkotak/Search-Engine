import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import utilities.FileHandler;

/**
 * Helper class for Evaluation1
 * @author Gaurav Gandhi
 *
 */
public class Evaluations {
	
	private static FileHandler writer;

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
	
	/**
	 * @param filePath
	 * @param queryList
	 */
	public static void writeEvaluationToFile(String filePath, Evaluation eva) {
		
		
		eva.queryListOfSystem().stream().forEach(query -> {
			try {
				writer = new FileHandler(filePath + query.queryID() + ".txt", 0);
				writer.addText("P@5: " + getPAtK(query, 5) + "\n");
				writer.addText("P@20: " + getPAtK(query, 20) + "\n");
				query.resultList().stream().forEach(result -> {
					try {
						writer.addText(result.docID() + " " + result.precision() + " " + result.recall() + "\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				writer.closeConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		});
		System.out.println("Evaluations are stored in: "+ filePath);
	}
	
}
