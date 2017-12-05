public class Result1 implements Result {

    private String doc_id;
    private double score;

    Result1(String doc_id,double score){
        this.doc_id=doc_id;
        this.score=score;
    }

    @Override
    public String DocId() {

        return this.doc_id;
    }

    @Override
    public double Score() {
        return this.score=score;
    }

    @Override
    public void changeScore(double sc) {
        this.score=this.score+sc;
    }
}
