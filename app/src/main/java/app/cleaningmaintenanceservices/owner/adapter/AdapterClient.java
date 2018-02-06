package app.cleaningmaintenanceservices.owner.adapter;

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
import app.cleaningmaintenanceservices.model.MDUser;

public class AdapterClient extends RecyclerView.Adapter<AdapterClient.ViewHolder> {

    private ArrayList<MDUser> data;
    OnListItemClickedListener listener;

    public interface OnListItemClickedListener {
        void onClick(int position, MDUser data);
    }

    public void setOnListItemClickedListener(AdapterClient.OnListItemClickedListener listener) {
        this.listener = listener;
    }

    public AdapterClient(ArrayList<MDUser> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client, null);
        return new ViewHolder(itemLayoutView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {

        Utils.loadImg(vh.image.getContext(), vh.image, data.get(position).image, false, false);
        vh.title.setText(data.get(position).full_name);
        vh.phone.setText(data.get(position).mobile);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, phone;

        public ViewHolder(View v) {
            super(v);

            image = v.findViewById(R.id.itemClientImg);
            title = v.findViewById(R.id.itemClientTitle);
            phone = v.findViewById(R.id.itemClientPhone);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onClick(getAdapterPosition(), data.get(getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}



