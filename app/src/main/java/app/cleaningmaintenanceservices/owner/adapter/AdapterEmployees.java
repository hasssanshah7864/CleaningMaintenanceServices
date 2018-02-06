package app.cleaningmaintenanceservices.owner.adapter;

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
import app.cleaningmaintenanceservices.model.MDEmployee;

public class AdapterEmployees extends RecyclerView.Adapter<AdapterEmployees.ViewHolder> {

    private ArrayList<MDEmployee> data;
    OnListItemClickedListener listener;

    public interface OnListItemClickedListener {
        void onClick(int position, MDEmployee data);
    }

    public void setOnListItemClickedListener(OnListItemClickedListener listener) {
        this.listener = listener;
    }

    public AdapterEmployees(ArrayList<MDEmployee> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, null);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {

        Utils.loadImg(vh.image.getContext(), vh.image, data.get(position).image, false, false);
        vh.title.setText(data.get(position).full_name);
        vh.phone.setText(data.get(position).phone);
        vh.address.setText(data.get(position).address);

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
        TextView title, phone, address;

        public ViewHolder(View v) {
            super(v);

            layout = v.findViewById(R.id.itemEmployee);
            image = v.findViewById(R.id.itemEmployeeImg);
            title = v.findViewById(R.id.itemEmployeeTitle);
            phone = v.findViewById(R.id.itemEmployeePhone);
            address = v.findViewById(R.id.itemEmployeeAddress);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}



