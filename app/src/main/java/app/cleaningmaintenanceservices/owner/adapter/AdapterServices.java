package app.cleaningmaintenanceservices.owner.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.common.activity.ServiceDetail;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDService;

public class AdapterServices extends RecyclerView.Adapter<AdapterServices.ViewHolder> {

    private ArrayList<MDService> data;

    public AdapterServices(ArrayList<MDService> data) {

        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_rect_simple, null);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {

        Utils.loadImg(vh.image.getContext(), vh.image, data.get(position).image, false, false);
        vh.title.setText(data.get(position).translation.title);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        CardView layout;

        public ViewHolder(View v) {
            super(v);

            layout = v.findViewById(R.id.itemServiceRect);
            image = v.findViewById(R.id.itemServiceRectImg);
            title = v.findViewById(R.id.itemServiceRectTitle);
            v.findViewById(R.id.itemServiceRectCheck);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(layout.getContext(), ServiceDetail.class);
                    intent.putExtra("service", new Gson().toJson(data.get(getAdapterPosition())));
                    layout.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}



