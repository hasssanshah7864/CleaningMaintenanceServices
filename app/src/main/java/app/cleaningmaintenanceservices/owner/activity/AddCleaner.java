package app.cleaningmaintenanceservices.owner.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDEmployee;
import app.cleaningmaintenanceservices.model.MDService;

public class AddCleaner extends AppCompatActivity {

    final int PICK_IMAGE = 33;
    File image;

    ImageView img, edit, delete;
    TextView services,tittle;
    EditText name, phone, cnic, address, about;
    RadioButton male, female;
    Button submit;

    ArrayList<MDService> selectedServices = new ArrayList<>();

    MDEmployee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add_cleaner);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        delete = findViewById(R.id.actOwnerManageEmployeesDelete);
        tittle = findViewById(R.id.tittle);
        img = findViewById(R.id.actOwnerAddEmployeeImg);
        edit = findViewById(R.id.actOwnerAddEmployeeImgEdit);
        name = findViewById(R.id.actOwnerAddEmployeeName);
        phone = findViewById(R.id.actOwnerAddEmployeePhone);
        male = findViewById(R.id.actOwnerAddEmployeeMale);
        female = findViewById(R.id.actOwnerAddEmployeeFemale);
        cnic = findViewById(R.id.actOwnerAddEmployeeCnic);
        services = findViewById(R.id.actOwnerAddEmployeeServices);
       /* address = findViewById(R.id.actOwnerAddEmployeeAddress);
        about = findViewById(R.id.actOwnerAddEmployeeAbout);*/
        submit = findViewById(R.id.actOwnerAddEmployeeSubmit);

        if (getIntent().hasExtra("employee")) {

            employee = new Gson().fromJson(getIntent().getStringExtra("employee"), MDEmployee.class);

            delete.setVisibility(View.VISIBLE);
            tittle.setText("Delete Employee");
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new MaterialDialog.Builder(AddCleaner.this).title(R.string.delete_employee).content(R.string.delete_employee_msg)
                            .positiveText(R.string.delete).onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                           // apiDeleteEmployee();
                        }
                    }).negativeText(R.string.cancel).show();
                }
            });

            Utils.loadImg(AddCleaner.this, img, employee.image, false, false);

            name.setText(employee.full_name);
            phone.setText(employee.phone);

            if (employee.gender.equals("female")) {
                female.setChecked(true);
            }

            cnic.setText(employee.cnic);

            selectedServices.clear();
            selectedServices.addAll(employee.services);

            for (MDService sv : employee.services) {

                if (services.getText().toString().equals("")) {

                    services.setText(sv.toString());

                } else {

                    services.append(", " + sv.toString());
                }
            }

            address.setText(employee.address);
            about.setText(employee.about);

        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageIntent();
            }
        });

        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Integer[] index = null;

                if (employee != null) {

                    index = new Integer[selectedServices.size()];

                    int pointer = 0;

                    for (int i = 0; i < Utils.user.company.services.size(); i++) {

                        for (int j = 0; j < selectedServices.size(); j++) {

                            if (Utils.user.company.services.get(i).id == selectedServices.get(j).id) {

                                index[pointer] = i;
                                pointer++;
                            }
                        }
                    }
                }

                new MaterialDialog.Builder(AddCleaner.this)
                        .title(R.string.select_services)
                        .items(Utils.user.company.services)
                        .itemsCallbackMultiChoice(index, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {

                                selectedServices.clear();

                                services.setText("");

                                for (int i : which) {

                                    selectedServices.add(Utils.user.company.services.get(i));

                                    if (services.getText().toString().equals("")) {

                                        services.setText(Utils.user.company.services.get(i).toString());

                                    } else {

                                        services.append(", " + Utils.user.company.services.get(i).toString());
                                    }
                                }

                                return true;
                            }
                        })
                        .positiveText(R.string.ok).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().toString().trim().length() > 2) {

                    if (phone.getText().toString().length() > 9 && phone.getText().toString().length() < 16) {

                        if (cnic.getText().toString().length() > 10 && cnic.getText().toString().length() < 20) {

                            if (!selectedServices.isEmpty()) {

                                if (!address.getText().toString().trim().equals("")) {

                                    //apiAddOrUpdateEmployee();

                                } else {
                                    Snackbar.make(findViewById(android.R.id.content), R.string.address_required, Snackbar.LENGTH_LONG).show();
                                }

                            } else {
                                Snackbar.make(findViewById(android.R.id.content), R.string.select_services, Snackbar.LENGTH_LONG).show();
                            }

                        } else {
                            Snackbar.make(findViewById(android.R.id.content), R.string.invalid_cnic, Snackbar.LENGTH_LONG).show();
                        }

                    } else {
                        Snackbar.make(findViewById(android.R.id.content), R.string.invalid_phone, Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    Snackbar.make(findViewById(android.R.id.content), R.string.invalid_name, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    /*private void apiAddOrUpdateEmployee() {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(AddCleaner.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        RequestParams params = new RequestParams();

        params.add("full_name", name.getText().toString().trim());

        String gender = "male";

        if (female.isChecked()) {
            gender = "female";
        }

        params.add("gender", gender);
        params.add("address", address.getText().toString().trim());
        params.add("cnic", cnic.getText().toString());
        params.add("phone", phone.getText().toString().trim());
        params.add("about", about.getText().toString().trim());
        params.add("company_id", Utils.user.company.id + "");

        if (image != null) {

            try {
                params.put("image", image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("FNF-Err", "image");
            }
        }

        for (int i = 0; i < selectedServices.size(); i++) {

            params.put("service_id[" + i + "]", selectedServices.get(i).id + "");
        }

        String url;

        if (employee == null) {

            url = "company/employees/store";

        } else {

            params.add("employee_id", employee.id + "");
            url = "company/employees/update";
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + Utils.token);
        client.post(Utils.webUrl + url, params, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String code = obj.getString("status_code");

                    if (code.equals("200")) {

                        MangeCleaners.needRefresh = true;
                        finish();

                    } else {

                        new MaterialDialog.Builder(AddCleaner.this).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressDialog.dismiss();
                new MaterialDialog.Builder(AddCleaner.this).title(R.string.alert).content(R.string.error_msg).negativeText(R.string.close).show();
            }
        });
    }

    private void apiDeleteEmployee() {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(AddCleaner.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        RequestParams params = new RequestParams();
        params.add("id", employee.id + "");

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + Utils.token);
        client.post(Utils.webUrl + "company/employees/destroy", params, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String code = obj.getString("status_code");

                    if (code.equals("200")) {

                        MangeCleaners.needRefresh = true;
                        finish();

                    } else {

                        new MaterialDialog.Builder(AddCleaner.this).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressDialog.dismiss();
                new MaterialDialog.Builder(AddCleaner.this).title(R.string.alert).content(R.string.error_msg).negativeText(R.string.close).show();
            }
        });
    }*/

    private void openImageIntent() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data == null) {
                Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                return;
            }

            CropImage.activity(data.getData())
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setFixAspectRatio(true)
                    .setActivityTitle(getResources().getString(R.string.CropImage))
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imgCall(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.e("err", result.getError().getMessage());
            }
        }
    }

    public void imgCall(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

            image = new File(getCacheDir(), "pic_" + System.currentTimeMillis() + ".jpg");
            image.createNewFile();

            int origWidth = bitmap.getWidth();
            int origHeight = bitmap.getHeight();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            if (origWidth > origHeight) {

                if (origWidth > 1200) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, 1200, Math.round(origHeight * (1200 / (float) origWidth)), false);
                }

            } else {
                if (origHeight > 1200) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, Math.round(origWidth * (1200 / (float) origHeight)), 1200, false);
                }
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            img.setImageBitmap(bitmap);

            FileOutputStream fos = new FileOutputStream(image);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
            Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
        }
    }
}
