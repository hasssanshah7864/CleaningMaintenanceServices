package app.cleaningmaintenanceservices.user.Adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.common.activity.ServiceDetail;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDService;

public class AdapterService extends RecyclerView.Adapter<AdapterService.ViewHolder> {

    private final boolean showCheck;
    private ArrayList<MDService> data;
    OnListItemClickedListener listener;

    public interface OnListItemClickedListener {
        void onClick(int position, MDService data);
    }

    public void setOnListItemClickedListener(OnListItemClickedListener listener) {
        this.listener = listener;
    }

    public AdapterService(boolean showCheck, ArrayList<MDService> data) {

        this.showCheck = showCheck;
        this.data = data;
    }

    @Override
    public AdapterService.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, null);
        return new AdapterService.ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final AdapterService.ViewHolder vh, int position) {

        Utils.loadImg(vh.image.getContext(), vh.image, data.get(position).image, true, false);
        vh.title.setText(data.get(position).translation.title);

        if (showCheck) {

            if (data.get(position).isSelected) {

                vh.check.setChecked(true);

            } else {

                vh.check.setChecked(false);
            }

            vh.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                    data.get(vh.getAdapterPosition()).isSelected = isChecked;
                }
            });

            vh.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    vh.check.toggle();
                }
            });

        } else {

            vh.check.setVisibility(View.INVISIBLE);
            vh.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    vh.check.toggle();
                }
            });

            vh.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(vh.layout.getContext(), ServiceDetail.class);
                    intent.putExtra("service", new Gson().toJson(data.get(vh.getAdapterPosition())));
                    vh.layout.getContext().startActivity(intent);
                }
            });
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        CheckBox check;
        CardView layout;

        public ViewHolder(View v) {
            super(v);

            layout = v.findViewById(R.id.itemService);
            image = v.findViewById(R.id.itemServiceImg);
            title = v.findViewById(R.id.itemServiceTitle);
            check = v.findViewById(R.id.itemServiceCheck);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}



