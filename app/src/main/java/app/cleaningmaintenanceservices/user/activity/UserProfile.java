package app.cleaningmaintenanceservices.user.activity;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDUser;
import cz.msebera.android.httpclient.Header;

public class UserProfile extends AppCompatActivity implements View.OnClickListener{

    ImageButton nameEdit, phoneEdit, emailEdit, genderEdit, passwordEdit;
    TextView name, phone, email, gender;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_user_profile);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        init();
        nameEdit.setOnClickListener(this);
        phoneEdit.setOnClickListener(this);
        emailEdit.setOnClickListener(this);
        genderEdit.setOnClickListener(this);
        passwordEdit.setOnClickListener(this);

        name.setText(Utils.user.full_name);
        phone.setText(Utils.user.mobile);
        email.setText(Utils.user.email);
        gender.setText(Utils.user.gender);

        Utils.loadImg(this, image, Utils.user.image, false, true);


    }

    public void init(){
        nameEdit = findViewById(R.id.actProfileNameEdit);
        phoneEdit = findViewById(R.id.actProfilePhoneEdit);
        emailEdit = findViewById(R.id.actProfileMailEdit);
        genderEdit = findViewById(R.id.actProfileGenderEdit);
        passwordEdit = findViewById(R.id.actProfilePasswordEdit);

        name = findViewById(R.id.actProfileName);
        phone = findViewById(R.id.actProfilePhone);
        email = findViewById(R.id.actProfileMail);
        gender = findViewById(R.id.actProfileGender);

        image = findViewById(R.id.actProfileImage);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.actProfileNameEdit:

                MaterialDialog nameDialog = new MaterialDialog.Builder(this).title(R.string.edit_name).content(R.string.edit_name_msg)
                        .input(getString(R.string.name), Utils.user.full_name, false, new MaterialDialog.InputCallback() {

                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                                if (input.length() > 2) {

                                    apiEditProfile("full_name", input.toString());

                                } else {

                                    Snackbar.make(findViewById(android.R.id.content), R.string.invalid_name, Snackbar.LENGTH_LONG).show();
                                }

                            }

                        }).positiveText(R.string.update).negativeText(R.string.cancel).show();

                if (nameDialog.getInputEditText() != null) {

                    final EditText nameEt = nameDialog.getInputEditText();

                    nameEt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            if (charSequence.length() <= 2) {

                                nameEt.setError(getString(R.string.invalid_name));

                            } else {

                                nameEt.setError(null);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }

                break;

            case R.id.actProfilePhoneEdit:

                MaterialDialog phoneDialog = new MaterialDialog.Builder(this).title(R.string.edit_phone).content(R.string.edit_phone_msg)
                        .input(getString(R.string.name), Utils.user.mobile, false, new MaterialDialog.InputCallback() {

                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                                apiEditProfile("phone", input.toString());

                            }

                        }).positiveText(R.string.update).negativeText(R.string.cancel).show();

                if (phoneDialog.getInputEditText() != null) {

                    final EditText phoneEt = phoneDialog.getInputEditText();
                    phoneEt.setInputType(InputType.TYPE_CLASS_PHONE);


                }

                break;

            case R.id.actProfileMailEdit:

                MaterialDialog emailDialog = new MaterialDialog.Builder(this).title(R.string.edit_email).content(R.string.edit_email_msg)
                        .input(getString(R.string.email), Utils.user.email, false, new MaterialDialog.InputCallback() {

                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                                if (Patterns.EMAIL_ADDRESS.matcher(input).matches()) {

                                    apiEditProfile("email", input.toString());

                                } else {

                                    Snackbar.make(findViewById(android.R.id.content), R.string.invalid_email, Snackbar.LENGTH_LONG).show();
                                }

                            }

                        }).positiveText(R.string.update).negativeText(R.string.cancel).show();

                if (emailDialog.getInputEditText() != null) {

                    final EditText emailEt = emailDialog.getInputEditText();

                    emailEt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            if (!Patterns.EMAIL_ADDRESS.matcher(charSequence).matches()) {

                                emailEt.setError(getString(R.string.invalid_email));

                            } else {

                                emailEt.setError(null);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }

                break;

            case R.id.actProfileGenderEdit:

                ArrayList<String> genders = new ArrayList<>();
                genders.add(getString(R.string.male));
                genders.add(getString(R.string.female));

                int selectedGender = 0;

                new MaterialDialog.Builder(this).title(R.string.edit_gender).content(R.string.select_gender_msg)
                        .items(genders)
                        .itemsCallbackSingleChoice(selectedGender, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                if (which == 0) {
                                    apiEditProfile("gender", "male");
                                }

                                if (which == 1) {
                                    apiEditProfile("gender", "female");
                                }

                                return true;
                            }
                        }).positiveText(R.string.update).negativeText(R.string.cancel).show();

                break;

            case R.id.actProfilePasswordEdit:
                final EditText oldPassword, newPassword, confirmPassword;
                Button cancel, update;
                final Dialog passwordDialog = new Dialog(this);

                passwordDialog.setContentView(R.layout.change_password_dialog);

                oldPassword = passwordDialog.findViewById(R.id.dialogChangePasswordOldPassword);
                newPassword = passwordDialog.findViewById(R.id.dialogChangePasswordNewPassword);
                confirmPassword = passwordDialog.findViewById(R.id.dialogChangePasswordConfirmPassword);

                cancel = passwordDialog.findViewById(R.id.dialogChangePasswordCancel);
                update = passwordDialog.findViewById(R.id.dialogChangePasswordUpdate);

                update.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                boolean isEmpty = false;
                                if(TextUtils.isEmpty(oldPassword.getText().toString())){
                                    Snackbar.make(findViewById(android.R.id.content), R.string.enter_old_password, Snackbar.LENGTH_LONG).show();
                                    isEmpty = true;
                                }
                                else  if(TextUtils.isEmpty(newPassword.getText().toString())){
                                    Snackbar.make(findViewById(android.R.id.content), R.string.enter_new_password, Snackbar.LENGTH_LONG).show();
                                    isEmpty = true;
                                }
                                else  if(TextUtils.isEmpty(confirmPassword.getText().toString())){
                                    Snackbar.make(findViewById(android.R.id.content), R.string.enter_confirm_password, Snackbar.LENGTH_LONG).show();
                                    isEmpty = true;
                                }

                                if(!isEmpty){
                                    passwordDialog.dismiss();
                                    apiEditPassword(oldPassword.getText().toString(), newPassword.getText().toString(),
                                            confirmPassword.getText().toString());
                                }
                            }
                        }
                );

                cancel.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                passwordDialog.dismiss();
                            }
                        }
                );

                passwordDialog.show();

                break;
        }
    }


    private void apiEditProfile(final String key, String value) {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(UserProfile.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        RequestParams params = new RequestParams();

        params.add(key, value);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
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

                        name.setText(Utils.user.full_name);
                        email.setText(Utils.user.email);
                        gender.setText(Utils.user.getGender());


                    } else {

                        new MaterialDialog.Builder(UserProfile.this).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressDialog.dismiss();
                new MaterialDialog.Builder(UserProfile.this).title(R.string.alert).content(R.string.error_msg).negativeText(R.string.close).show();
            }
        });
    }

    private void apiEditPassword(final String oldPassword, String newPassword, String confirmPassword) {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(UserProfile.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        RequestParams params = new RequestParams();

        params.add("current_password", oldPassword);
        params.add("password", newPassword);
        params.add("password_confirmation", confirmPassword);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + Utils.token);
        client.post(Utils.webUrl + "user/change-password", params, new TextHttpResponseHandler() {

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

                        name.setText(Utils.user.full_name);
                        email.setText(Utils.user.email);
                        gender.setText(Utils.user.getGender());

                        new MaterialDialog.Builder(UserProfile.this).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();


                    } else {

                        new MaterialDialog.Builder(UserProfile.this).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressDialog.dismiss();
                new MaterialDialog.Builder(UserProfile.this).title(R.string.alert).content(R.string.error_msg).negativeText(R.string.close).show();
            }
        });
    }
}
