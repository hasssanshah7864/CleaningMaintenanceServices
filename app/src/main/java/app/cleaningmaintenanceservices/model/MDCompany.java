package app.cleaningmaintenanceservices.model;

import java.util.ArrayList;

public class MDCompany {

    public int id;
    public int user_id;
    public MDRate rate_per_hour;
    public long time_starts;
    public long time_ends;
    public String company_type;
    public int pro_check;
    public String full_name;
    public String phone;
    public String image;
    public String address;
    public double longitude;
    public double latitude;
    public float average_rating;
    public MDTranslation translation = new MDTranslation();
    public ArrayList<MDTranslation> companyTranslations = new ArrayList<>();
    public ArrayList<MDService> services = new ArrayList<>();
    public ArrayList<MDEmployee> employees = new ArrayList<>();

    public ArrayList<MDReview> reviews = new ArrayList<>();
    public int can_review;
    public MDBooking booking;

    public MDUserMin user = new MDUserMin();

    public class MDUserMin {

        public int id;
        public String email;
    }
}
