import java.util.List;

/**
 * @author Gaurav Gandhi
 *
 */
public interface Evaluation {

	double MAP();
	
	double MRR();
	
	List<Query> queryListOfSystem();
	
	void calculateMAP();
	
	void calculateMRR();
	
	void generatePrecisionAndRecall();
}
