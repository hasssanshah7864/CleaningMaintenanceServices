package app.cleaningmaintenanceservices.model;

import android.content.Context;

import app.cleaningmaintenanceservices.R;


public class MDBookingItem {

    public int id;
    public int booking_id;
    public String status;
    public long date;
    public long start_time;
    public long end_time;
    public long started_at;
    public long ended_at;
    public double price;

    public String getStatus(Context c) {

        switch (status) {

            case "pending":

                return c.getString(R.string.pending);

            case "confirmed":

                return c.getString(R.string.confirmed);

            case "finished":

                return c.getString(R.string.finished);

            case "cancelled":

                return c.getString(R.string.cancelled);
        }

        return "";
    }
}
