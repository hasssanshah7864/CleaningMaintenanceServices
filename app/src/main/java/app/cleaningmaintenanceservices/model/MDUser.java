package app.cleaningmaintenanceservices.model;


import app.cleaningmaintenanceservices.R;

public class MDUser {

    public int id;
    public String user_type;
    public String full_name;
    public String email;
    public String mobile;
    public String gender;
    public String image;
    public String fcm_token;
    public String locale;
    public String time_zone;
    public boolean email_verified;
    public boolean is_active;
    public boolean password;
    public long created_at;
    public long updated;
    public MDCompany company = new MDCompany();

    public int getGender() {

        if (gender.equals("female")) {
            return R.string.female;
        }

        return R.string.male;
    }
}
