package app.cleaningmaintenanceservices.owner.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDBooking;
import app.cleaningmaintenanceservices.model.MDEmployee;
import app.cleaningmaintenanceservices.model.MDService;
import app.cleaningmaintenanceservices.owner.adapter.AdapterServices;
import app.cleaningmaintenanceservices.owner.fragment.HomeMain;

public class BookingDetail extends AppCompatActivity implements View.OnClickListener {

    ImageView img;
    TextView title, txt, date, date2, phone, address, special, assignedTxt, assigned;
    Button assign, map, items, cancel, client;

    RecyclerView list;
    AdapterServices adapter;

    MDBooking booking;

    public static boolean needRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_booking_detail);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        title = findViewById(R.id.actOwnerBookingDetailTitle);
        img = findViewById(R.id.actOwnerBookingDetailImg);
        txt = findViewById(R.id.actOwnerBookingDetailTxt);
        date = findViewById(R.id.actOwnerBookingDetailDate);
       // date2 = findViewById(R.id.actOwnerBookingDetailDate2);
        phone = findViewById(R.id.actOwnerBookingDetailPhone);
        list = findViewById(R.id.actOwnerBookingDetailList);
        address = findViewById(R.id.actOwnerBookingDetailAddress);
        special = findViewById(R.id.actOwnerBookingDetailSpecial);
        assign = findViewById(R.id.actOwnerBookingDetailAssign);
        map = findViewById(R.id.actOwnerBookingDetailMap);
        items = findViewById(R.id.actOwnerBookingDetailItems);
        cancel = findViewById(R.id.actOwnerBookingDetailCancel);
        client = findViewById(R.id.actOwnerBookingDetailClient);

        assignedTxt = findViewById(R.id.actOwnerBookingDetailAssignedTxt);
        assigned = findViewById(R.id.actOwnerBookingDetailAssigned);

        if (getIntent().hasExtra("booking")) {

            booking = new Gson().fromJson(getIntent().getStringExtra("booking"), MDBooking.class);

            title.setText(booking.getStatus(this));

            Utils.loadImg(this, img, booking.user.image, false, false);
            txt.setText(booking.user.full_name);
            phone.setText(booking.user.mobile);
            date.setText(Utils.formatDate(booking.start_date));
            date2.setText(Utils.formatDate(booking.end_date));

        }

       // apiBookingDetail();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (needRefresh) {

          //  apiBookingDetail();
            HomeMain.needRefresh = true;
            needRefresh = false;
        }
    }

    private void populateData() {

        if (getIntent().hasExtra("id")) {

            Utils.loadImg(this, img, booking.user.image, false, false);
            txt.setText(booking.user.full_name);
            phone.setText(booking.user.mobile);
            date.setText(Utils.formatDate(booking.start_date));
            date2.setText(Utils.formatDate(booking.end_date));

        }

        title.setText(booking.getStatus(this));
        address.setText(booking.userAddress.address);
        special.setText(booking.instruction);

        if (!booking.employees.isEmpty()) {

            assigned.setText("");

            for (MDEmployee e : booking.employees) {
                for (MDService s : booking.services) {

                    if (e.pivot.service_id == s.id) {

                        assigned.append(s.translation.title + " : " + e.full_name + "\n");
                    }
                }
            }
            assignedTxt.setVisibility(View.VISIBLE);
            assigned.setVisibility(View.VISIBLE);

        } else {

            assignedTxt.setVisibility(View.GONE);
            assigned.setVisibility(View.GONE);

        }

        if (!booking.status.equals("pending")) {

            assign.setEnabled(false);

        } else {

            assign.setOnClickListener(this);
        }

        if (booking.status.equals("pending") || booking.status.equals("confirmed")) {

            cancel.setOnClickListener(this);

        } else {

            cancel.setEnabled(false);
        }

        adapter = new AdapterServices(booking.services);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(adapter);

        if (booking.userAddress.latitude != 0 && booking.userAddress.longitude != 0) {
            map.setOnClickListener(this);
        } else {
            map.setEnabled(false);
        }

        items.setOnClickListener(this);

        if (getIntent().hasExtra("from_client")) {

            client.setVisibility(View.GONE);
        } else {
            client.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.actOwnerBookingDetailAssign:

                /*Intent intent = new Intent(BookingDetail.this, AssignEmployee.class);
                intent.putExtra("booking", new Gson().toJson(booking));
                startActivityForResult(intent, 123);
*/
                break;

            case R.id.actOwnerBookingDetailMap:

                String strUri = "http://maps.google.com/maps?q=loc:" + booking.userAddress.latitude + "," + booking.userAddress.longitude + " (" + booking.userAddress.address + ")";
                Intent intentX = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                intentX.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intentX);

                break;

            case R.id.actOwnerBookingDetailItems:

                /*Intent intent2 = new Intent(BookingDetail.this, BookingItems.class);
                intent2.putExtra("booking", new Gson().toJson(booking));
                startActivityForResult(intent2, 123);*/

                break;

            case R.id.actOwnerBookingDetailCancel:

                new MaterialDialog.Builder(this).title(R.string.cancel_booking).content(R.string.cancel_booking_msg)
                        .positiveText(R.string.yes).onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

//                        apiCancelBooking();

                    }
                }).negativeText(R.string.no).show();


                break;

            case R.id.actOwnerBookingDetailClient:

                Intent intent3 = new Intent(BookingDetail.this, ClientDetail.class);
                intent3.putExtra("client", new Gson().toJson(booking.user));
                startActivity(intent3);

                break;
        }

    }

   /* private void apiCancelBooking() {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(BookingDetail.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        RequestParams params = new RequestParams();
        params.add("booking_id", booking.id + "");

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + Utils.token);
        client.get(Utils.webUrl + "user/bookings/cancel-booking", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String code = obj.getString("status_code");

                    if (code.equals("200")) {

                        HomeMain.needRefresh = true;
                        finish();

                    } else {

                        new MaterialDialog.Builder(BookingDetail.this).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressDialog.dismiss();
                new MaterialDialog.Builder(BookingDetail.this).title(R.string.alert).content(R.string.error_msg).negativeText(R.string.close).show();
            }
        });
    }

    private void apiBookingDetail() {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(BookingDetail.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        RequestParams params = new RequestParams();

        if (getIntent().hasExtra("id")) {

            params.add("id", getIntent().getStringExtra("id"));

        } else {

            params.add("id", booking.id + "");
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + Utils.token);
        client.get(Utils.webUrl + "user/booking/detail", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String code = obj.getString("status_code");

                    if (code.equals("200")) {

                        booking = new Gson().fromJson(obj.getJSONObject("data").toString(), MDBooking.class);

                        populateData();

                    } else {

                        new MaterialDialog.Builder(BookingDetail.this).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressDialog.dismiss();
                new MaterialDialog.Builder(BookingDetail.this).title(R.string.alert).content(R.string.error_msg).negativeText(R.string.close).show();
            }
        });
    }*/
}
