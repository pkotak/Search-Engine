import java.util.List;
import java.util.stream.Stream;

/**
 * @author Gaurav Gandhi
 *
 */
public interface Query {

	int queryID();
	
	String query();
	
	List<RelevanceInfo> listOfRelevantDocuments();
}
