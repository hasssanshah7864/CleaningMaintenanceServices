package app.cleaningmaintenanceservices.common.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
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

public class Login extends AppCompatActivity {

    CountryCodePicker ccp;
    EditText number;
    Button next;
    CheckBox check;
    String whichLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        init();

        next.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ccp.isValidFullNumber()) {
                            if (check.isChecked()) {
                                apiRegister();

                            } else {
                                Snackbar.make(findViewById(android.R.id.content), R.string.required_terms_conditions, Snackbar.LENGTH_LONG).show();
                            }

                        } else {
                            Snackbar.make(findViewById(android.R.id.content), R.string.invalid_phone, Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
        );

    }

    public void init(){
        number = findViewById(R.id.actLoginNumber);
        ccp = findViewById(R.id.actLoginCpp);
        next = findViewById(R.id.actLoginSubmit);
        check = findViewById(R.id.actLoginTermsAndConditionCheck);

        ccp.registerCarrierNumberEditText(number);
        ccp.setDefaultCountryUsingNameCode("ae");
        ccp.resetToDefaultCountry();

        if(getIntent().getExtras() != null){
            whichLogin = getIntent().getStringExtra("whichLogin");
        }

    }



    private void apiRegister() {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(Login.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        RequestParams params = new RequestParams();
        params.add("mobile", ccp.getFullNumberWithPlus());

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.addHeader("Accept", "application/json");
        client.post(Utils.webUrl + "user/register/phone", params, new TextHttpResponseHandler() {

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

                        if(!Utils.user.is_active && !Utils.user.password){

                        }
                        if(!Utils.user.email_verified){
                            Intent intent = new Intent(Login.this, Verification.class);
                            intent.putExtra("whichLogin",whichLogin);
                            intent.putExtra("mobile",ccp.getFullNumberWithPlus());
                            startActivity(intent);
                            finish();
                        }
                        else if(!Utils.user.password){
                            Intent intent = new Intent(Login.this, Register.class);
                            intent.putExtra("whichLogin",whichLogin);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Intent intent = new Intent(Login.this, Password.class);
                            intent.putExtra("whichLogin",whichLogin);
                            intent.putExtra("mobile",ccp.getFullNumberWithPlus());
                            startActivity(intent);
                            finish();

                        }

                    } else {

                        new MaterialDialog.Builder(Login.this).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressDialog.dismiss();
                new MaterialDialog.Builder(Login.this).title(R.string.alert).content(R.string.error_msg).negativeText(R.string.close).show();
            }
        });
    }
}
