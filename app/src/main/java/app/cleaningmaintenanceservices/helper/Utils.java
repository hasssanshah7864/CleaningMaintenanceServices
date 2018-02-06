package app.cleaningmaintenanceservices.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.common.activity.Login;
import app.cleaningmaintenanceservices.model.MDSettings;
import app.cleaningmaintenanceservices.model.MDUser;

/**
 * Created by Hassan on 1/31/2018.
 */

public class Utils {

    public static String webUrl;
    public static String token = "";
    public static Locale locale = Locale.getDefault();
    public static String language = "en";
    public static MDUser user = new MDUser();
    public static MDSettings settings;
    public SharedPreferences preferences;

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

    public static boolean isLoggedIn(final Context c, boolean enablePopUp) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);

        if(preferences.getBoolean("isLogin",false)){
            return true;
        }
        else if (enablePopUp) {

            new MaterialDialog.Builder(c).title(R.string.denied).content(R.string.login_register)
                    .positiveText(R.string.login).onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    c.startActivity(new Intent(c, Login.class));

                }
            }).negativeText(R.string.close).show();

            return false;

        } else {

            return false;
        }
    }
}
