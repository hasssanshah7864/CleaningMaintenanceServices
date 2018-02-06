package app.cleaningmaintenanceservices.owner.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.model.MDBooking;
import app.cleaningmaintenanceservices.owner.adapter.AdapterBooking;

import static app.cleaningmaintenanceservices.helper.Utils.formatDate;

//import app.cleaningservants.owner.activity.BookingDetail;

public class HomeMainBooking extends Fragment {

    View v;
    String type;

    TextView date;
    ImageView calender;
    RecyclerView recyclerView;
    AdapterBooking adapter;
    ArrayList<MDBooking> data;
    SwipeRefreshLayout refresh;
    long booking_date = 0;

    public boolean needRefresh = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_owner_home_main_booking, container, false);

        type = getArguments().getString("type");

       /* date = v.findViewById(R.id.fragOwnerHomeMainBookingDate);
        calender = v.findViewById(R.id.fragOwnerHomeMainBookingCalendar);*/
        recyclerView = v.findViewById(R.id.fragOwnerHomeMainBookingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        refresh = v.findViewById(R.id.swipeRefreshLayout);
       // refresh.setRefreshing(true);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                booking_date = 0;
                date.setText(R.string.all_bookings);
                data.clear();
                adapter.notifyDataSetChanged();
                //apiGetBookings();
            }
        });

       // apiGetBookings();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (needRefresh) {

            booking_date = 0;
            date.setText(R.string.all_bookings);
            data.clear();
            adapter.notifyDataSetChanged();
           // apiGetBookings();

            needRefresh = false;
        }
    }

    private void populateData() {

        adapter = new AdapterBooking(data);
        recyclerView.setAdapter(adapter);
        adapter.setOnListItemClickedListener(new AdapterBooking.OnListItemClickedListener() {
            @Override
            public void onClick(int position, MDBooking data) {

               /* Intent intent = new Intent(getContext(), BookingDetail.class);
                intent.putExtra("booking", new Gson().toJson(data));
                startActivity(intent);*/

            }
        });

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener picker = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub

                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        myCalendar.set(Calendar.HOUR_OF_DAY, 0);
                        myCalendar.set(Calendar.MINUTE, 0);
                        booking_date = myCalendar.getTimeInMillis() / 1000;
                        date.setText(formatDate(myCalendar.getTimeInMillis() / 1000));

                        refresh.setRefreshing(true);
                      //  apiGetBookings();
                    }

                };
                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), picker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.show();
            }
        });
    }

    /*private void apiGetBookings() {

        RequestParams params = new RequestParams();
        if (booking_date != 0) {
            params.add("booking_date", booking_date + "");
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + Utils.token);
        client.get(Utils.webUrl + "user/bookings/" + type, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String code = obj.getString("status_code");

                    if (code.equals("200")) {

                        data = new Gson().fromJson(obj.getJSONArray("data").toString(), new TypeToken<ArrayList<MDBooking>>() {
                        }.getType());

                        populateData();

                        if (data.isEmpty()) {
                            v.findViewById(R.id.empty).setVisibility(View.VISIBLE);
                        } else {
                            v.findViewById(R.id.empty).setVisibility(View.GONE);
                        }

                        refresh.setRefreshing(false);

                    } else {

                        new MaterialDialog.Builder(getActivity()).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    refresh.setRefreshing(false);
                    Snackbar.make(v.findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                refresh.setRefreshing(false);
                new MaterialDialog.Builder(getActivity()).title(R.string.alert).content(R.string.error_msg).negativeText(R.string.close).show();
            }
        });
    }*/
}
