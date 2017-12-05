import java.util.stream.Stream;

/**
 * @author Gaurav Gandhi
 *
 */
public interface RelevanceInfo {

	int queryId();
	
	String literal();
	
	String documentID();
	
	int systemName();
	
}
