package app.cleaningmaintenanceservices.model;

import java.util.ArrayList;

public class MDSettings {

    public ArrayList<MDCurrency> currencies = new ArrayList<>();
    public MDSiteSettings site_settings = new MDSiteSettings();
    public ArrayList<MDLanguage> languages = new ArrayList<>();
    public ArrayList<MDService> cleaner_services = new ArrayList<>();
    public ArrayList<MDService> servant_services = new ArrayList<>();

    public class MDSiteSettings {

        public String contact_email;
        public String address;
        public String telephone_number;
        public String website;
        public String facebook_page_url;
        public String twitter_page_url;
        public String google_page_url;
        public String linkedin_page_url;
        public String youtube_page_url;
        public ContactPage contact_page_detail = new ContactPage();
        public String contact_us_template;
        public String instagram_page_url;
        public String about_us;

    }

    public class ContactPage {

        public String english;
        public String arabic;
        public String urdu;
    }

}
