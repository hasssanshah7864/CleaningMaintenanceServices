package app.cleaningmaintenanceservices.user.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;

/**
 * Created by Hassan on 1/31/2018.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private ArrayList<String> data;
    private Context context;

    public ViewPagerAdapter(ArrayList<String> data, Context context){
        this.data = data;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.viewpager_row, container, false);

        final ImageView image = itemView.findViewById(R.id.image_view);

        Utils.loadImg(context, image, data.get(position), false, false);

        container.addView(itemView);



        return itemView;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }
}
