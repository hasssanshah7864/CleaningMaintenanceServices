package app.cleaningmaintenanceservices.common.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

public class Register extends AppCompatActivity {

    RadioGroup genderRadioGroup;
    RadioButton maleRadioButton, femaleRadioButton;
    EditText name, email, password, confirmPassword;
    Button submit;
    String whichLogin, gender = "male";
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register);

        init();

        submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isEmpty = false;
                        if(TextUtils.isEmpty(name.getText().toString())){
                            Snackbar.make(findViewById(android.R.id.content), R.string.enter_name, Snackbar.LENGTH_LONG).show();
                            isEmpty = true;
                        }
                        else  if(TextUtils.isEmpty(email.getText().toString())){
                            Snackbar.make(findViewById(android.R.id.content), R.string.enter_email, Snackbar.LENGTH_LONG).show();
                            isEmpty = true;
                        }
                        else  if(TextUtils.isEmpty(password.getText().toString())){
                            Snackbar.make(findViewById(android.R.id.content), R.string.enter_password, Snackbar.LENGTH_LONG).show();
                            isEmpty = true;
                        }
                        else  if(TextUtils.isEmpty(confirmPassword.getText().toString())){
                            Snackbar.make(findViewById(android.R.id.content), R.string.enter_confirm_password, Snackbar.LENGTH_LONG).show();
                            isEmpty = true;
                        }

                        if(!isEmpty){
                            apiRegister();
                        }

                    }
                }
        );

        genderRadioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if(radioGroup.getId() == R.id.actRegisterMailRadio){
                            gender = "male";
                        }
                        else {
                            gender = "female";
                        }
                    }
                }
        );
    }

    public void init(){
        genderRadioGroup = findViewById(R.id.actRegisterGenderRadioGroup);
        maleRadioButton = findViewById(R.id.actRegisterMailRadio);
        femaleRadioButton = findViewById(R.id.actRegisterFemaleRadio);
        name = findViewById(R.id.actRegisterName);
        email = findViewById(R.id.actRegisterEmail);
        password = findViewById(R.id.actRegisterPassword);
        confirmPassword = findViewById(R.id.actRegisterPasswordConfirmed);
        submit = findViewById(R.id.actRegisterSubmit);
        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();

        if(getIntent().getExtras() != null){
            whichLogin = getIntent().getStringExtra("whichLogin");
        }
    }

    public void apiRegister(){
        final MaterialDialog progressDialog = new MaterialDialog.Builder(Register.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        RequestParams params = new RequestParams();
        params.add("full_name",name.getText().toString());
        params.add("email",email.getText().toString());
        params.add("password",password.getText().toString());
        params.add("password_confirmation",confirmPassword.getText().toString());
        params.add("gender",gender);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + Utils.token);
        client.post(Utils.webUrl + "user/profile", params, new TextHttpResponseHandler() {

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

                        editor.putString("mobile",Utils.user.mobile);
                        editor.putString("password",password.getText().toString().trim());
                        editor.putBoolean("isLogin",true);
                        editor.apply();

                        if(Utils.user.user_type.equalsIgnoreCase("client")){
                            if(whichLogin.equalsIgnoreCase("loginForProfile")){
                                startActivity(new Intent(Register.this, UserProfile.class));
                                finish();
                            }
                            else {
                                startActivity(new Intent(Register.this, UserHome.class));
                            }
                        }
                        else {
                            startActivity(new Intent(Register.this, OwnerProfile.class));
                        }


                    } else {

                        new MaterialDialog.Builder(Register.this).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
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
