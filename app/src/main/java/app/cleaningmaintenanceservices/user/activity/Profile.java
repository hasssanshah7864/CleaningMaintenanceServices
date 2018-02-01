package app.cleaningmaintenanceservices.user.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;

public class Profile extends AppCompatActivity implements View.OnClickListener{

    ImageButton nameEdit, phoneEdit, emailEdit, genderEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_profile);

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

    }

    public void init(){
        nameEdit = findViewById(R.id.actProfileNameEdit);
        phoneEdit = findViewById(R.id.actProfilePhoneEdit);
        emailEdit = findViewById(R.id.actProfileMailEdit);
        genderEdit = findViewById(R.id.actProfileGenderEdit);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.actProfileNameEdit:

                MaterialDialog nameDialog = new MaterialDialog.Builder(this).title(R.string.edit_name).content(R.string.edit_name_msg)
                        .input(getString(R.string.name), "", false, new MaterialDialog.InputCallback() {

                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                                if (input.length() > 2) {

                                    //apiEditProfile("full_name", input.toString());

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
                        .input(getString(R.string.name), "", false, new MaterialDialog.InputCallback() {

                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                                //apiEditProfile("full_name", input.toString());

                            }

                        }).positiveText(R.string.update).negativeText(R.string.cancel).show();

                if (phoneDialog.getInputEditText() != null) {

                    final EditText phoneEt = phoneDialog.getInputEditText();
                    phoneEt.setInputType(InputType.TYPE_CLASS_PHONE);


                }

                break;

            case R.id.actProfileMailEdit:

                MaterialDialog emailDialog = new MaterialDialog.Builder(this).title(R.string.edit_email).content(R.string.edit_email_msg)
                        .input(getString(R.string.email), "", false, new MaterialDialog.InputCallback() {

                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                                if (Patterns.EMAIL_ADDRESS.matcher(input).matches()) {

                                    //apiEditProfile("email", input.toString());

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
               /* if (Utils.user.gender.equals("female")) {
                    selectedGender = 1;
                }*/

                new MaterialDialog.Builder(this).title(R.string.edit_gender).content(R.string.select_gender_msg)
                        .items(genders)
                        .itemsCallbackSingleChoice(selectedGender, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                if (which == 0) {
                                    //apiEditProfile("gender", "male");
                                }

                                if (which == 1) {
                                    //apiEditProfile("gender", "female");
                                }

                                return true;
                            }
                        }).positiveText(R.string.update).negativeText(R.string.cancel).show();

                break;
        }
    }
}
