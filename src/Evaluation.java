import java.util.List;

/**
 * Represents the evaluation parameters of any system
 * 
 */
public interface Evaluation {

	/**
	 * @return mean average precision of the given system
	 */
	double MAP();
	
	/**
	 * @return Mean Reciprocal Rank
	 */
	double MRR();
	
	/**
	 * @return list of {@link Query} of the given system
	 */
	List<Query> queryListOfSystem();
}
