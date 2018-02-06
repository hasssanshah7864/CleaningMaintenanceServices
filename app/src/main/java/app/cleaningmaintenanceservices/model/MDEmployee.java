package app.cleaningmaintenanceservices.model;

import java.util.ArrayList;

public class MDEmployee {

    public int id;
    public int company_id;
    public String image;
    public String full_name;
    public String phone;
    public String gender;
    public String cnic;
    public String address;
    public String about;
    public ArrayList<MDService> services = new ArrayList<>();
    public MDAssignment pivot = new MDAssignment();

    public class MDAssignment {

        public int booking_id;
        public int employee_id;
        public int service_id;
    }

    @Override
    public String toString() {
        return full_name;
    }
}
