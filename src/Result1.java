public class Result1 implements Result {

    private String doc_id;
    private int score;

    @Override
    public void setDocId(String doc_id) {
        this.doc_id=doc_id;
    }

    @Override
    public void setScore(int score) {
        this.score=score;
    }
}
