import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Results {
	

	public static List<Result> sortResult(List<Result> unsortedResultList) {
		
		AtomicInteger counter = new AtomicInteger(1);
		
		List<Result> resultList = unsortedResultList.stream().sorted((a, b) -> Double.compare(b.Score(), a.Score()))
				.collect(Collectors.toCollection(ArrayList<Result>::new));
		
		resultList.stream().forEach(x -> {
			x.changeRank(counter.getAndIncrement());
		});
		
		return resultList;
	}
}
