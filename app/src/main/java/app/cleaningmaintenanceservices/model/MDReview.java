package app.cleaningmaintenanceservices.model;

public class MDReview {

    public int company_id;
    public int user_id;
    public float rating;
    public long created_at;
    public String review_text;
    public MDUser user = new MDUser();
}
