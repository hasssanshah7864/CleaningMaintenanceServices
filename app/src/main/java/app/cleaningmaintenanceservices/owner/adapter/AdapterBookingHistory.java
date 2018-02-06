package app.cleaningmaintenanceservices.owner.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDBooking;
import app.cleaningmaintenanceservices.model.MDService;

public class AdapterBookingHistory extends RecyclerView.Adapter<AdapterBookingHistory.ViewHolder> {

    private ArrayList<MDBooking> data;
    OnListItemClickedListener listener;

    public interface OnListItemClickedListener {
        void onClick(int position, MDBooking data);
    }

    public void setOnListItemClickedListener(OnListItemClickedListener listener) {
        this.listener = listener;
    }

    public AdapterBookingHistory(ArrayList<MDBooking> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_history, null);
        return new ViewHolder(itemLayoutView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {

        vh.date.setText(Utils.formatDate(data.get(position).start_date));
        vh.date2.setText(Utils.formatDate(data.get(position).end_date));
        vh.status.setText(data.get(position).getStatus(vh.status.getContext()));

        if (data.get(position).status.equals("finished")) {

            if (Utils.isAED) {
                vh.total.setText(data.get(position).total_bill.aed.symbol + " " + data.get(position).total_bill.aed.price);

            } else {
                vh.total.setText(data.get(position).total_bill.usd.symbol + " " + data.get(position).total_bill.usd.price);
            }

            vh.total.setVisibility(View.VISIBLE);

        } else {

            vh.total.setVisibility(View.INVISIBLE);
        }

        String temp = "";

        for (MDService s : data.get(position).services) {

            if (temp.equals("")) {
                temp = s.translation.title;
            } else {
                temp = temp.concat(", " + s.translation.title);
            }
        }

        vh.services.setText(temp);

        vh.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onClick(vh.getAdapterPosition(), data.get(vh.getAdapterPosition()));
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView layout;
        TextView status, total, date, date2, services;

        public ViewHolder(View v) {
            super(v);

            layout = v.findViewById(R.id.itemBookingHistory);
            status = v.findViewById(R.id.itemBookingHistoryStatus);
            total = v.findViewById(R.id.itemBookingHistoryTotal);
            date = v.findViewById(R.id.itemBookingHistoryDate);
            /*date2 = v.findViewById(R.id.itemBookingHistoryDate2);*/
            services = v.findViewById(R.id.itemBookingHistoryServices);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}



