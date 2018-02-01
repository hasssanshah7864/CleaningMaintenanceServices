package app.cleaningmaintenanceservices.user.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.model.MDUserAddress;


public class AdapterAddress extends RecyclerView.Adapter<AdapterAddress.ViewHolder> {

    private ArrayList<MDUserAddress> data;
    OnListItemClickedListener listener;

    public interface OnListItemClickedListener {
        void onClick(int type, int position, MDUserAddress data);
    }

    public void setOnListItemClickedListener(OnListItemClickedListener listener) {
        this.listener = listener;
    }

    public AdapterAddress(ArrayList<MDUserAddress> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, null);
        return new ViewHolder(itemLayoutView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {

        vh.title.setText(vh.title.getContext().getString(R.string.address) + " " + (position + 1));
        vh.detail.setText(data.get(position).address);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layout;
        TextView title, detail;
        ImageView delete;
        ImageButton edit;

        public ViewHolder(View v) {
            super(v);

            layout = v.findViewById(R.id.itemAddressLayout);
            title = v.findViewById(R.id.itemAddressTitle);
            detail = v.findViewById(R.id.itemAddressDetail);
            delete = v.findViewById(R.id.itemAddressDelete);
            edit = v.findViewById(R.id.itemEditAddress);


            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onClick(0, getAdapterPosition(), data.get(getAdapterPosition()));

                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onClick(1, getAdapterPosition(), data.get(getAdapterPosition()));
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onClick(2, getAdapterPosition(), data.get(getAdapterPosition()));
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}



