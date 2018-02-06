package app.cleaningmaintenanceservices.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.common.activity.ServiceDetail;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDService;
import app.cleaningmaintenanceservices.user.Adapter.AdapterService;
import cz.msebera.android.httpclient.Header;

public class Services extends AppCompatActivity {

    RecyclerView list;
    AdapterService adapter;
    ArrayList<MDService> services;
    String company_type;
    boolean hasType = false;
    TextView title, done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_services);

       // Utils.webUrl = "https://www.mytechnology.ae/test/cleaners-maintainers/en/api/";

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        title = findViewById(R.id.title);

        list = findViewById(R.id.actServicesList);

        if (getIntent().hasExtra("company_type")) {

            hasType = true;

            done = findViewById(R.id.actServicesDone);
            done.setVisibility(View.VISIBLE);

            company_type = getIntent().getStringExtra("company_type");

            if (company_type.equals("cleaning")) {

                services = new ArrayList<>(Utils.settings.cleaner_services);

            } else if (company_type.equals("maintenance")) {

                title.setText(R.string.maintenance);
                services = new ArrayList<>(Utils.settings.servant_services);
            }

            populateData();

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ArrayList<MDService> selectedServices = new ArrayList<>();

                    for (MDService sv : services) {

                        if (sv.isSelected) {
                            selectedServices.add(sv);
                        }
                    }

                    if (selectedServices.size() > 0) {

                       /* Intent intent = new Intent(Services.this, CompanyListing.class);
                        intent.putExtra("company_type", company_type);
                        intent.putExtra("services", new Gson().toJson(selectedServices));

                        if (getIntent().hasExtra("latitude") && getIntent().hasExtra("longitude")) {

                            intent.putExtra("latitude", getIntent().getDoubleExtra("latitude", 0.0));
                            intent.putExtra("longitude", getIntent().getDoubleExtra("longitude", 0.0));
                        }

                        startActivity(intent);
                        finish();*/

                    } else {
                        Snackbar.make(findViewById(android.R.id.content), R.string.select_services, Snackbar.LENGTH_LONG).show();
                    }
                }
            });

        } else {

            apiServices();
//            done.setVisibility(View.INVISIBLE);
        }
    }

    private void populateData() {

        adapter = new AdapterService(hasType, services);
        adapter.setOnListItemClickedListener(new AdapterService.OnListItemClickedListener() {

            @Override
            public void onClick(int position, MDService data) {

                if (!hasType) {

                    Intent intent = new Intent(Services.this, ServiceDetail.class);
                    intent.putExtra("service", new Gson().toJson(data));
                    startActivity(intent);
                }
            }
        });

        list.setLayoutManager(new GridLayoutManager(this, 2));
        list.setAdapter(adapter);

    }

    private void apiServices() {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(Services.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + Utils.token);
        client.get(Utils.webUrl + "services", new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String code = obj.getString("status_code");

                    if (code.equals("200")) {

                        services = new Gson().fromJson(obj.getJSONArray("data").toString(), new TypeToken<ArrayList<MDService>>() {
                        }.getType());

                        populateData();

                    } else {

                        new MaterialDialog.Builder(Services.this).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressDialog.dismiss();
                new MaterialDialog.Builder(Services.this).title(R.string.alert).content(R.string.error_msg).negativeText(R.string.close).show();
            }
        });
    }
}
