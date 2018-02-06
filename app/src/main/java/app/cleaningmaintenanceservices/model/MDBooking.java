package app.cleaningmaintenanceservices.model;

import android.content.Context;

import java.util.ArrayList;

import app.cleaningmaintenanceservices.R;

public class MDBooking {

    public int id;
    public int user_id;
    public int company_id;
    public String status;
    public String instruction;
    public String image;
    public String video;
    public MDRate total_bill = new MDRate();
    public long start_date;
    public long end_date;


    public ArrayList<MDService> services = new ArrayList<>();
    public ArrayList<MDEmployee> employees = new ArrayList<>();
    public ArrayList<MDBookingItem> bookingItems = new ArrayList<>();

    public MDUserAddress userAddress = new MDUserAddress();
    public MDCompany company = new MDCompany();

    public MDUser user = new MDUser();

    public String getStatus(Context c) {

        switch (status) {

            case "pending":

                return c.getString(R.string.upcoming);

            case "confirmed":

                return c.getString(R.string.pending);

            case "finished":

                return c.getString(R.string.completed);

            case "cancelled":

                return c.getString(R.string.cancelled);
        }

        return "";
    }
}
