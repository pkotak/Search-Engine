import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import utilities.FileHandler;

public class Results {
	

	/**
	 * @param unsortedResultList
	 * @return top 100 list of sorted and rank result
	 */
	public static List<Result> sortResultAndRank(List<Result> unsortedResultList) {
		
		AtomicInteger counter = new AtomicInteger(1);
		
		List<Result> resultList = unsortedResultList.stream().sorted((a, b) -> Double.compare(b.Score(), a.Score()))
				.limit(100)
				.collect(Collectors.toCollection(ArrayList<Result>::new));
		
		resultList.stream().forEach(x -> {
			x.changeRank(counter.getAndIncrement());
		});
		
		return resultList;
	}
	
	/**
	 * @param docID
	 * @param score
	 * @param queryID
	 * @return Result Object
	 */
	public static Result make(String docID, double score, int queryID) {
		
		return new Result1(docID, score, queryID, "sys", "model");
	}
	
	public static void writeResultsToFile(String filePath, List<Query> queryList) {

		queryList.stream().forEach(query -> {
			try {
				FileHandler writer = new FileHandler(filePath + query.queryID() + ".txt", 0);
				query.resultList().stream().forEach(result -> {
					try {
						writer.addText(result.toString() + "\n");
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				});
				try {
					writer.closeConnection();
				} catch (IOException e) {
					
					e.printStackTrace();
				}	
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		});	
		
		System.out.println("Reuls are stored in: " + filePath);
}
}
