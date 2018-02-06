package app.cleaningmaintenanceservices.model;

import java.util.ArrayList;

/**
 * Created by Hassan on 2/1/2018.
 */

public class MDSettings {

    public MDSiteSettings site_settings = new MDSiteSettings();
    MDPages pages = new MDPages();
    public ArrayList<MDFeaturedServices> featured_services = new ArrayList<>();
    public ArrayList<MDTestimonial> top_testimonials = new ArrayList<>();


    public class MDSiteSettings {
        public String contact_email;
        public String contact_phone;
        public String contact_address;
        public String twitter;
        public String facebook;
        public String company_latitude;
        public String company_longitude;
    }

}
