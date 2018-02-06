package app.cleaningmaintenanceservices.owner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDEmployee;
import app.cleaningmaintenanceservices.owner.adapter.AdapterEmployees;

public class MangeCleaners extends AppCompatActivity {

    RecyclerView list;
    AdapterEmployees adapter;
    ImageView add;

    public static boolean needRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mange_cleaners);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (getIntent().hasExtra("isNew")) {

            findViewById(R.id.back).setVisibility(View.INVISIBLE);
            findViewById(R.id.actOwnerManageEmployeeFinish).setVisibility(View.VISIBLE);
            findViewById(R.id.actOwnerManageEmployeeFinish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(MangeCleaners.this, OwnerHome.class));
                    finish();

                }
            });
        }

        list = findViewById(R.id.actOwnerManageEmployeesList);

        add = findViewById(R.id.actOwnerManageEmployeesAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MangeCleaners.this, AddCleaner.class));
            }
        });

      //  apiGetEmployees();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (needRefresh) {

          //  apiGetEmployees();
            needRefresh = false;
        }
    }

    private void populateData() {

        adapter = new AdapterEmployees(Utils.user.company.employees);
        adapter.setOnListItemClickedListener(new AdapterEmployees.OnListItemClickedListener() {
            @Override
            public void onClick(int position, MDEmployee data) {

                Intent intent = new Intent(MangeCleaners.this, AddCleaner.class);
                intent.putExtra("employee", new Gson().toJson(data));
                startActivity(intent);

            }
        });

        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(adapter);
    }

    /*private void apiGetEmployees() {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(MangeCleaners.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + Utils.token);
        client.get(Utils.webUrl + "company/employees", new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String code = obj.getString("status_code");

                    if (code.equals("200")) {

                        Utils.user.company.employees = new Gson().fromJson(obj.getJSONArray("data").toString(), new TypeToken<ArrayList<MDEmployee>>() {
                        }.getType());

                        populateData();

                    } else {

                        new MaterialDialog.Builder(MangeCleaners.this).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressDialog.dismiss();
                new MaterialDialog.Builder(MangeCleaners.this).title(R.string.alert).content(R.string.error_msg).negativeText(R.string.close).show();
            }
        });
    }*/
}
