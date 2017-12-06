import java.util.List;

/**
 * @author Gaurav Gandhi
 *
 */
public interface Query {

	int queryID();
	
	String query();
	
	List<RelevanceInfo> listOfRelevantDocuments();
}
