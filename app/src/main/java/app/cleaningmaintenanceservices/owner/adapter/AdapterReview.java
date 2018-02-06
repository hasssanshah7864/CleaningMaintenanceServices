package app.cleaningmaintenanceservices.owner.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDReview;

public class AdapterReview extends RecyclerView.Adapter<AdapterReview.ViewHolder> {

    private ArrayList<MDReview> data;

    public AdapterReview(ArrayList<MDReview> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, null);
        return new ViewHolder(itemLayoutView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {

        Utils.loadImg(vh.image.getContext(), vh.image, data.get(position).user.image, false, false);
        vh.title.setText(data.get(position).user.full_name);
        vh.date.setText(Utils.formatDate(data.get(position).created_at) + " - " + Utils.formatTime(data.get(position).created_at));
        vh.rating.setText(data.get(position).rating + "");
        vh.ratingBar.setRating(data.get(position).rating);
        vh.detail.setText(data.get(position).review_text);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, date, rating, detail;
        RatingBar ratingBar;

        public ViewHolder(View v) {
            super(v);

            image = v.findViewById(R.id.itemReviewImg);
            title = v.findViewById(R.id.itemReviewTitle);
            date = v.findViewById(R.id.itemReviewDate);
            ratingBar = v.findViewById(R.id.itemReviewRatingBar);
            rating = v.findViewById(R.id.itemReviewRating);
            detail = v.findViewById(R.id.itemReviewDetail);

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}



