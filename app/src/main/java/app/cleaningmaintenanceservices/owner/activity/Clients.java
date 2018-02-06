package app.cleaningmaintenanceservices.owner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.model.MDUser;
import app.cleaningmaintenanceservices.owner.adapter.AdapterClient;

public class Clients extends AppCompatActivity {

    RecyclerView list;
    AdapterClient adapter;
    ArrayList<MDUser> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_clients);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        list = findViewById(R.id.actClientsList);

       // apiClients();
    }

    private void populateData() {

        adapter = new AdapterClient(data);
        adapter.setOnListItemClickedListener(new AdapterClient.OnListItemClickedListener() {
            @Override
            public void onClick(int position, MDUser data) {

                Intent intent = new Intent(Clients.this, ClientDetail.class);
                intent.putExtra("client", new Gson().toJson(data));
                startActivity(intent);

            }
        });

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

    }

    /*private void apiClients() {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(Clients.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + Utils.token);
        client.get(Utils.webUrl + "user/clients", new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String code = obj.getString("status_code");

                    if (code.equals("200")) {

                        data = new Gson().fromJson(obj.getJSONArray("data").toString(), new TypeToken<ArrayList<MDUser>>() {
                        }.getType());

                        populateData();

                    } else {

                        new MaterialDialog.Builder(Clients.this).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressDialog.dismiss();
                new MaterialDialog.Builder(Clients.this).title(R.string.alert).content(R.string.error_msg).negativeText(R.string.close).show();
            }
        });
    }*/
}
