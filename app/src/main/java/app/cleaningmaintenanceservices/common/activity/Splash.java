package app.cleaningmaintenanceservices.common.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDSettings;
import app.cleaningmaintenanceservices.model.MDUser;
import app.cleaningmaintenanceservices.user.activity.UserHome;
import app.cleaningmaintenanceservices.user.activity.UserProfile;
import cz.msebera.android.httpclient.Header;

public class Splash extends AppCompatActivity {

    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);

        if(Utils.isNetAvailable(this,true)){

            preferences = PreferenceManager.getDefaultSharedPreferences(this);

            if(!preferences.contains("language")){
                startActivity(new Intent(Splash.this, Language.class));
                finish();
            }
            else {
                Utils.webUrl = "https://www.mytechnology.ae/test/cleaners-maintainers/" + preferences.getString("language", "en") + "/api/";
                setLanguage(this, preferences.getString("language","en"));
                apiSettings();
            }
        }
    }


    private void apiSettings() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Accept", "application/json");
        client.get(Utils.webUrl + "settings", new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String code = obj.getString("status_code");

                    if (code.equals("200")) {

                        Utils.settings = new Gson().fromJson(obj.getString("data"), MDSettings.class);
                        checkLogin();

                    } else {

                        new MaterialDialog.Builder(Splash.this).title(R.string.alert).content(obj.getString("message")).onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                finish();
                            }
                        }).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                new MaterialDialog.Builder(Splash.this).title(R.string.alert).content(R.string.error_msg).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                }).negativeText(R.string.close).show();
            }
        });
    }

    private void checkLogin() {

        String mobile = preferences.getString("mobile", "");
        String password = preferences.getString("password", "");

        if (!mobile.equals("") && !password.equals("")) {

            apiLogin(mobile, password);

        } else {
            startActivity(new Intent(Splash.this, UserHome.class));
            finish();
        }
    }


    private void apiLogin(String mobile, String password) {

        RequestParams params = new RequestParams();
        params.add("mobile", mobile);
        params.add("password", password);
        params.add("fcm_token", "123456");

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Accept", "application/json");
        client.post(Utils.webUrl + "user/login", params, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String code = obj.getString("status_code");

                    if (code.equals("200")) {

                        for (Header h : headers) {
                            if (h.getName().equals("Authorization")) {
                                Utils.token = h.getValue();
                            }
                        }

                        Utils.user = new Gson().fromJson(obj.getString("data"), MDUser.class);

                        //   Utils.pToast(Splash.this, getString(R.string.welcome) + " " + Utils.user.full_name);


                        if (Utils.user.user_type.equals("client")) {

                            startActivity(new Intent(Splash.this, UserHome.class));
                            finish();
                        }

                        if (Utils.user.user_type.equals("company")) {

                            /*startActivity(new Intent(Splash.this, OwnerHome.class));
                            finish();*/
                        }
                    }else{
                        Log.e("Err: Auto Login", responseString);
                        startActivity(new Intent(Splash.this, UserHome.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.e("Err: Auto Login", responseString);
                startActivity(new Intent(Splash.this, UserHome.class));
                finish();
            }
        });
    }

    public static void setLanguage(Context context, String languageCode) {

        Locale locale = new Locale(languageCode);
        Configuration conf = context.getResources().getConfiguration();
        conf.locale = locale;
        Locale.setDefault(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLayoutDirection(conf.locale);
        }

        context.getResources().updateConfiguration(conf, context.getResources().getDisplayMetrics());

        Utils.locale = locale;

    }
}
