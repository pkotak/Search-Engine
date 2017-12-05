import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

public class Results {

	public static List<Result> sortResult(List<Result> unsortedResultList) {
		
		return unsortedResultList.stream().sorted((a, b) -> Double.compare(b.Score(), a.Score()))
				.collect(Collectors.toCollection(ArrayList<Result>::new));
	}
}
