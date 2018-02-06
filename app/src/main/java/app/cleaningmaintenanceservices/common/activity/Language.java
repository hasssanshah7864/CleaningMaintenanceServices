package app.cleaningmaintenanceservices.common.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDSettings;
import app.cleaningmaintenanceservices.user.activity.UserHome;
import cz.msebera.android.httpclient.Header;

public class Language extends AppCompatActivity {

    RadioGroup languageRadioGroup;
    RadioButton enRadioButton, arRadioButton;
    CheckBox saveLanguage;
    Button conitnue;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_language);

        init();


    }

    public void init(){
        languageRadioGroup = findViewById(R.id.actLanguageRadioGroup);
        enRadioButton = findViewById(R.id.actLanguageEnglishRadio);
        arRadioButton = findViewById(R.id.actLanguageArabicRadio);
        saveLanguage = findViewById(R.id.actSaveLanguageCheckBox);
        conitnue = findViewById(R.id.actLanguageSubmit);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        conitnue.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(enRadioButton.getId() == languageRadioGroup.getCheckedRadioButtonId()){

                            selectLanguage("en");
                        }
                        else {
                            selectLanguage("ar");
                        }


                    }
                }
        );
    }

    private void selectLanguage(String language) {
        if(saveLanguage.isChecked()){
            editor.putString("language", language);
            editor.apply();
        }
        Utils.language = language;
        Utils.webUrl = "https://www.mytechnology.ae/test/cleaners-maintainers/" + language + "/api/";
        setLanguage(this, language);
        apiSettings();
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


    private void apiSettings() {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(Language.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.addHeader("Accept", "application/json");
        client.get(Utils.webUrl + "settings", new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String code = obj.getString("status_code");

                    if (code.equals("200")) {

                        Utils.settings = new Gson().fromJson(obj.getString("data"), MDSettings.class);

                        if (Utils.isLoggedIn(Language.this, false)) {

                            if (Utils.user.user_type.equals("client")) {

                                startActivity(new Intent(Language.this, UserHome.class));
                                finish();
                            }

                            if (Utils.user.user_type.equals("company")) {

                                /*startActivity(new Intent(Language.this, OwnerHome.class));
                                finish();*/
                            }

                        } else {

                            startActivity(new Intent(Language.this, UserHome.class));
                            finish();
                        }

                    } else {

                        new MaterialDialog.Builder(Language.this).title(R.string.alert).content(obj.getString("message")).onNegative(new MaterialDialog.SingleButtonCallback() {
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

                progressDialog.dismiss();
                new MaterialDialog.Builder(Language.this).title(R.string.alert).content(R.string.error_msg).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                }).negativeText(R.string.close).show();
            }
        });
    }
}
