package app.cleaningmaintenanceservices.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.model.MDSettings;
import app.cleaningmaintenanceservices.model.MDUser;

/**
 * Created by Hassan on 1/31/2018.
 */

public class Utils {

    public static String webUrl = "https://www.mytechnology.ae/test/cleaners-maintainers/en/api/";
    public static Locale locale = Locale.getDefault();
    public static MDUser user = new MDUser();
    public static boolean isAED = false;
    public static String token = "";
    public static MDSettings settings;

    public static void loadImg(Context context, ImageView imageView, String path, boolean wide, boolean fullSize) {

        if (path != null && !path.equals("") && !path.contains("http")) {
            String img = webUrl + "/" + path;
            img = img.replaceAll(" ", "%20");
            if (!wide) {
                if (fullSize) {
                    Picasso.with(context).load(img).placeholder(R.drawable.progress).error(R.drawable.placeholder).into(imageView);
                } else {
                    Picasso.with(context).load(img).placeholder(R.drawable.progress).error(R.drawable.placeholder).resize(300, 300).centerInside().into(imageView);
                }
            } else {
                if (fullSize) {
                    Picasso.with(context).load(img).placeholder(R.drawable.progress_wide).error(R.drawable.placeholder_wide).into(imageView);
                } else {
                    Picasso.with(context).load(img).placeholder(R.drawable.progress_wide).error(R.drawable.placeholder_wide).resize(300, 300).centerInside().into(imageView);
                }
            }
        }

        if (path != null && path.contains("http")) {
            if (!wide) {
                if (fullSize) {
                    Picasso.with(context).load(path).placeholder(R.drawable.progress).error(R.drawable.placeholder).into(imageView);
                } else {
                    Picasso.with(context).load(path).placeholder(R.drawable.progress).error(R.drawable.placeholder).resize(300, 300).centerInside().into(imageView);
                }
            } else {
                if (fullSize) {
                    Picasso.with(context).load(path).placeholder(R.drawable.progress_wide).error(R.drawable.placeholder_wide).into(imageView);
                } else {
                    Picasso.with(context).load(path).placeholder(R.drawable.progress_wide).error(R.drawable.placeholder_wide).resize(300, 300).centerInside().into(imageView);
                }
            }
        }
    }


    public static String formatDate(long timestamp) {

        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(timestamp * 1000);
        String month = date.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        return date.get(Calendar.DAY_OF_MONTH) + " " + month + " " + date.get(Calendar.YEAR);
    }

    public static String formatTime(long timestamp, boolean small) {

        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(timestamp * 1000);
        SimpleDateFormat format = new SimpleDateFormat("h:mm a", locale);

        if (small) {
            return format.format(date.getTime()) + " (" + TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT) + ")";
        } else {
            return format.format(date.getTime()) + " (" + TimeZone.getDefault().getDisplayName() + ")";
        }
    }

    public static String formatTime(long timestamp) {

        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(timestamp * 1000);
        SimpleDateFormat format = new SimpleDateFormat("h:mm a", locale);
        return format.format(date.getTime());

    }


    public static boolean isNetAvailable(Context c, boolean enablePopUp) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Boolean state = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        if (!state && enablePopUp) {
            new MaterialDialog.Builder(c).title(R.string.no_network)
                    .content(R.string.connect_to_net)
                    .positiveText(R.string.ok).show();
        }

        return state;
    }
}
