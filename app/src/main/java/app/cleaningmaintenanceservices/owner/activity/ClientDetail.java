package app.cleaningmaintenanceservices.owner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDBooking;
import app.cleaningmaintenanceservices.model.MDUser;
import app.cleaningmaintenanceservices.owner.adapter.AdapterBookingHistory;

public class ClientDetail extends AppCompatActivity {

    RecyclerView list;
    AdapterBookingHistory adapter;

    MDUser client;

    ImageView img;
    TextView title, phone;

    ArrayList<MDBooking> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_client_detail);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        client = new Gson().fromJson(getIntent().getStringExtra("client"), MDUser.class);

        img = findViewById(R.id.actClientDetailImg);
        title = findViewById(R.id.actClientDetailTitle);
        phone = findViewById(R.id.actClientDetailPhone);
        list = findViewById(R.id.actClientDetailList);

        Utils.loadImg(this, img, client.image, false, false);
        title.setText(client.full_name);
        phone.setText(client.mobile);

      //  apiClientDetail();
    }

    private void populateData() {


        adapter = new AdapterBookingHistory(data);
        adapter.setOnListItemClickedListener(new AdapterBookingHistory.OnListItemClickedListener() {
            @Override
            public void onClick(int position, MDBooking data) {

                data.user = client;

                Intent intent = new Intent(ClientDetail.this, BookingDetail.class);
                intent.putExtra("booking", new Gson().toJson(data));
                intent.putExtra("from_client", true);
                startActivity(intent);
            }
        });

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

    }

    /*private void apiClientDetail() {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(ClientDetail.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        RequestParams params = new RequestParams();
        params.add("client_id", client.id + "");

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + Utils.token);
        client.get(Utils.webUrl + "user/client/detail", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String code = obj.getString("status_code");

                    if (code.equals("200")) {

                        data = new Gson().fromJson(obj.getJSONObject("data").getJSONArray("bookings").toString(), new TypeToken<ArrayList<MDBooking>>() {
                        }.getType());

                        populateData();

                    } else {

                        new MaterialDialog.Builder(ClientDetail.this).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressDialog.dismiss();
                new MaterialDialog.Builder(ClientDetail.this).title(R.string.alert).content(R.string.error_msg).negativeText(R.string.close).show();
            }
        });
    }*/
}
