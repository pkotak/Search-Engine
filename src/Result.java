public interface Result {

	int queryID();
	
	int literal();
	
    String docID();
    
    double Score();
    
    int systemName();
    
    void changeScore(double sc);
    

}

