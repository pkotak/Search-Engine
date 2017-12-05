public interface Result {

	int queryID();
	
	String literal();
	
    String docID();
    
    double Score();
    
    int systemName();
    
    void changeScore(double sc);
     

}

