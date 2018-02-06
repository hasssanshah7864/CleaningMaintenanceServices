package app.cleaningmaintenanceservices.common.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import app.cleaningmaintenanceservices.owner.activity.OwnerProfile;
import app.cleaningmaintenanceservices.user.activity.UserHome;
import app.cleaningmaintenanceservices.user.activity.UserProfile;
import cz.msebera.android.httpclient.Header;

public class Verification extends AppCompatActivity {

    TextView verificationCodeSendText, correctIncorrectText;
    EditText verificationCodeEditText;
    Button submit;
    String whichLogin, mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_verification);

        init();

        submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(TextUtils.isEmpty(verificationCodeEditText.getText().toString())){
                            Snackbar.make(findViewById(android.R.id.content), R.string.invalid_code, Snackbar.LENGTH_LONG).show();
                        }
                        else {
                            apiVerification();
                        }
                    }
                }
        );
    }

    public void init(){
        verificationCodeSendText = findViewById(R.id.actVerificationCodeSendText);
        verificationCodeEditText = findViewById(R.id.actVerificationCodeEditText);
        correctIncorrectText = findViewById(R.id.actVerificationCorrectText);
        submit = findViewById(R.id.actVerificationCodeSubmit);

        if(getIntent().getExtras() != null){
            whichLogin = getIntent().getStringExtra("whichLogin");
            mobile = getIntent().getStringExtra("mobile");
            verificationCodeSendText.append(" " + mobile);
        }
    }

    public void apiVerification(){
        final MaterialDialog progressDialog = new MaterialDialog.Builder(Verification.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        RequestParams params = new RequestParams();
        params.add("verification_code", verificationCodeEditText.getText().toString());


        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + Utils.token);
        client.post(Utils.webUrl + "user/verify-phone", params, new TextHttpResponseHandler() {

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


                        if(!Utils.user.password){
                            Intent intent = new Intent(Verification.this, Register.class);
                            intent.putExtra("whichLogin",whichLogin);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            if(Utils.user.user_type.equalsIgnoreCase("client")){
                                if(whichLogin.equalsIgnoreCase("loginForProfile")){
                                    startActivity(new Intent(Verification.this, UserProfile.class));
                                    finish();
                                }
                                else {
                                    startActivity(new Intent(Verification.this, UserHome.class));
                                }
                            }
                            else {
                                startActivity(new Intent(Verification.this, OwnerProfile.class));
                            }

                        }

                    } else {

                        new MaterialDialog.Builder(Verification.this).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });
    }
}
