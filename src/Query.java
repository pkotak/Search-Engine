import java.util.stream.Stream;

/**
 * @author Gaurav Gandhi
 *
 */
public interface Query {

	int queryID();
	
	String query();
	
	void readQueryFile(String filePath);
	
	Stream<Query> stream();
}
