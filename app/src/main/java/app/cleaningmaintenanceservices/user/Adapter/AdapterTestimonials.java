package app.cleaningmaintenanceservices.user.Adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDTestimonial;

public class AdapterTestimonials extends RecyclerView.Adapter<AdapterTestimonials.ViewHolder> {

    private ArrayList<MDTestimonial> data;

    public AdapterTestimonials(ArrayList<MDTestimonial> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_testimonial, null);
        return new ViewHolder(itemLayoutView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {

        Utils.loadImg(vh.image.getContext(), vh.image, data.get(position).image, false, false);
        vh.title.setText(data.get(position).translation.name);
        vh.detail.setText(data.get(position).translation.detail);
       // vh.date.setText(Utils.formatDate(data.get(position).created_at));

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, detail;//, date;

        public ViewHolder(View v) {
            super(v);

            image = v.findViewById(R.id.itemTestimonialImg);
            title = v.findViewById(R.id.itemTestimonialTitle);
            detail = v.findViewById(R.id.itemTestimonialDetail);
          //  date = v.findViewById(R.id.itemTestimonialDate);

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}



