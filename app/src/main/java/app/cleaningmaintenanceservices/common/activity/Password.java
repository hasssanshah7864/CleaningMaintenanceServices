package app.cleaningmaintenanceservices.common.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDUser;
import app.cleaningmaintenanceservices.user.activity.UserHome;
import app.cleaningmaintenanceservices.user.activity.UserProfile;
import cz.msebera.android.httpclient.Header;

public class Password extends AppCompatActivity {

    EditText password;
    Button submit;
    String mobile, whichLogin;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_password);

        init();

        submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(TextUtils.isEmpty(password.getText().toString())){
                            Snackbar.make(findViewById(android.R.id.content), R.string.edit_name, Snackbar.LENGTH_LONG).show();
                        }
                        else {
                            apiLogin(mobile,password.getText().toString().trim());
                        }
                    }
                }
        );
    }

    public void init() {
        password = findViewById(R.id.actPasswordEditText);
        submit = findViewById(R.id.actPasswordSubmit);

        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();

        if (getIntent().getExtras() != null) {
            mobile = getIntent().getStringExtra("mobile");
            whichLogin = getIntent().getStringExtra("whichLogin");
        }
    }

    private void apiLogin(String mobile, final String password) {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(Password.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

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

                progressDialog.dismiss();

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

                        editor.putString("mobile", Utils.user.mobile);
                        editor.putString("password", password);
                        editor.putBoolean("isLogin", true);
                        editor.apply();
                        //   Utils.pToast(Splash.this, getString(R.string.welcome) + " " + Utils.user.full_name);


                        if(Utils.user.user_type.equalsIgnoreCase("client")){
                            if(whichLogin.equalsIgnoreCase("loginForProfile")){
                                startActivity(new Intent(Password.this, UserProfile.class));
                                finish();
                            }
                            else {
                                startActivity(new Intent(Password.this, UserHome.class));
                            }
                        }
                        else {
                            startActivity(new Intent(Password.this, UserHome.class));
                        }
                    } else {
                        Log.e("Err: Auto Login", responseString);
                        startActivity(new Intent(Password.this, UserHome.class));
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
                finish();
            }
        });
    }
}
