/**
 * @author Gaurav Gandhi
 *
 */
public interface Result {

	int queryID();
	
	int rank();
	
	String literal();
	
    String docID();
    
    double Score();
    
    int systemName();
    
    double precision();
    
    double recall();
    
    String snippet();
    
    void changeScore(double sc);
    
    void changePrecision(double newPrecision);
    
    void changeRecall(double newRecall);
    
    void changeRank(int newRank);

    void ApplyLog();
    
    void addSnippet(String newSnippet);
     

}

