package app.cleaningmaintenanceservices.owner.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDBooking;

//import app.cleaningservants.helper.Utils;

public class AdapterBooking extends RecyclerView.Adapter<AdapterBooking.ViewHolder> {

    private ArrayList<MDBooking> data;
    OnListItemClickedListener listener;

    public interface OnListItemClickedListener {
        void onClick(int position, MDBooking data);
    }

    public void setOnListItemClickedListener(OnListItemClickedListener listener) {
        this.listener = listener;
    }

    public AdapterBooking(ArrayList<MDBooking> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, null);
        return new ViewHolder(itemLayoutView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {

        Utils.loadImg(vh.image.getContext(), vh.image, data.get(position).user.image, false, false);
        vh.title.setText(data.get(position).user.full_name);
        vh.date.setText(Utils.formatDate(data.get(position).start_date));
        vh.date2.setText(Utils.formatDate(data.get(position).end_date));

        String temp = "";

        /*for (MDService s : data.get(position).services) {

            if (temp.equals("")) {
                temp = s.translation.title;
            } else {
                temp = temp.concat(", " + s.translation.title);
            }
        }*/

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
        ImageView image;
        TextView title, date, date2, services;

        public ViewHolder(View v) {
            super(v);

            layout = v.findViewById(R.id.itemBooking);
            image = v.findViewById(R.id.itemBookingImg);
            title = v.findViewById(R.id.itemBookingTitle);
            date = v.findViewById(R.id.itemBookingDate);
            date2 = v.findViewById(R.id.itemBookingDate2);
           // services = v.findViewById(R.id.itemBookingServices);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}



