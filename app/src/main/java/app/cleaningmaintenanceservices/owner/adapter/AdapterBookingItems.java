package app.cleaningmaintenanceservices.owner.adapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Calendar;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDBookingItem;

import static app.cleaningmaintenanceservices.helper.Utils.formatTime;

public class AdapterBookingItems extends RecyclerView.Adapter<AdapterBookingItems.ViewHolder> {

    private ArrayList<MDBookingItem> data;

    public AdapterBookingItems(ArrayList<MDBookingItem> data) {

        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_item, null);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {

        vh.date.setText(Utils.formatDate(data.get(position).date));
        vh.start.setText(Utils.formatTime(data.get(position).start_time));
        vh.end.setText(Utils.formatTime(data.get(position).end_time));
        vh.status.setText(data.get(position).getStatus(vh.status.getContext()));

        if (data.get(position).status.equals("finished")) {

            vh.startAt.setText(Utils.formatTime(data.get(position).started_at));
            vh.endAt.setText(Utils.formatTime(data.get(position).ended_at));

            vh.startTxt.setVisibility(View.VISIBLE);
            vh.endTxt.setVisibility(View.VISIBLE);
            vh.startAt.setVisibility(View.VISIBLE);
            vh.endAt.setVisibility(View.VISIBLE);

            vh.cancel.setVisibility(View.GONE);
            vh.finish.setVisibility(View.GONE);

        } else if (data.get(position).status.equals("cancelled")) {


            vh.cancel.setVisibility(View.GONE);
            vh.finish.setVisibility(View.GONE);

        } else if (data.get(position).status.equals("confirmed")) {

            vh.cancel.setVisibility(View.VISIBLE);
            vh.finish.setVisibility(View.VISIBLE);

        } else if (data.get(position).status.equals("pending")) {

            vh.cancel.setVisibility(View.VISIBLE);
            vh.finish.setEnabled(false);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView layout;
        TextView date, start, end, startTxt, endTxt, startAt, endAt, status;
        Button cancel, finish;

        long date_of_finish = 0, start_time = 0, end_time = 0;

        ViewHolder(View v) {
            super(v);

            layout = v.findViewById(R.id.itemBookingItem);
            date = v.findViewById(R.id.itemBookingItemDate);
            start = v.findViewById(R.id.itemBookingItemStart);
            end = v.findViewById(R.id.itemBookingItemEnd);
            startTxt = v.findViewById(R.id.itemBookingItemStartAtTxt);
            endTxt = v.findViewById(R.id.itemBookingItemEndAtTxt);
            startAt = v.findViewById(R.id.itemBookingItemStartAt);
            endAt = v.findViewById(R.id.itemBookingItemEndAt);
            status = v.findViewById(R.id.itemBookingItemStatus);

            cancel = v.findViewById(R.id.itemBookingItemCancel);
            finish = v.findViewById(R.id.itemBookingItemFinish);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new MaterialDialog.Builder(cancel.getContext()).title(R.string.cancel).content(R.string.cancel_booking_item_msg)
                            .negativeText(R.string.no).positiveText(R.string.yes).onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                //apiCancelBookingItem(cancel.getContext(), position);
                            }
                        }
                    }).show();
                }
            });

            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    date_of_finish = 0;
                    start_time = 0;
                    end_time = 0;

                    final MaterialDialog dialog = new MaterialDialog.Builder(finish.getContext()).customView(R.layout.dialog_mark_finish, false).build();

                    View dv = dialog.getCustomView();

                    final TextView dvDate, dvStart, dvEnd;
                    Button dvCancel, dvFinish;

                    if (dv != null) {

                        dvDate = dv.findViewById(R.id.dialogMarkFinishDate);
                        dvStart = dv.findViewById(R.id.dialogMarkFinishStart);
                        dvEnd = dv.findViewById(R.id.dialogMarkFinishEnd);
                        dvCancel = dv.findViewById(R.id.itemBookingItemCancel);
                        dvFinish = dv.findViewById(R.id.itemBookingItemFinish);

                        dvDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                final Calendar myCalendar = Calendar.getInstance();
                                DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        // TODO Auto-generated method stub

                                        myCalendar.set(Calendar.YEAR, year);
                                        myCalendar.set(Calendar.MONTH, monthOfYear);
                                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                        myCalendar.set(Calendar.HOUR_OF_DAY, 0);
                                        myCalendar.set(Calendar.MINUTE, 0);

                                        date_of_finish = myCalendar.getTimeInMillis() / 1000;
                                        dvDate.setText(Utils.formatDate(myCalendar.getTimeInMillis() / 1000));

                                        start_time = 0;
                                        dvStart.setText(R.string.select_time);

                                        end_time = 0;
                                        dvEnd.setText(R.string.select_time);
                                    }

                                };
                                DatePickerDialog mDatePicker = new DatePickerDialog(finish.getContext(), datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                                mDatePicker.show();

                            }
                        });

                        dvStart.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                if (date_of_finish != 0) {

                                    TimePickerDialog mTimePicker = new TimePickerDialog(finish.getContext(), new TimePickerDialog.OnTimeSetListener() {

                                        @Override
                                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                                            Calendar time = Calendar.getInstance();
                                            time.setTimeInMillis(date_of_finish * 1000);
                                            time.add(Calendar.HOUR_OF_DAY, selectedHour);
                                            time.add(Calendar.MINUTE, selectedMinute);

                                            start_time = time.getTimeInMillis() / 1000;
                                            dvStart.setText(formatTime(time.getTimeInMillis() / 1000));

                                            end_time = 0;
                                            dvEnd.setText(R.string.select_time);

                                        }
                                    }, 0, 0, false);

                                    mTimePicker.setTitle(R.string.select_start_time);
                                    mTimePicker.show();

                                } else {

                                    new MaterialDialog.Builder(finish.getContext()).title(R.string.alert).content(R.string.select_date_msg).negativeText(R.string.close).show();
                                }
                            }
                        });

                        dvEnd.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                if (start_time != 0) {

                                    TimePickerDialog mTimePicker = new TimePickerDialog(finish.getContext(), new TimePickerDialog.OnTimeSetListener() {

                                        @Override
                                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                                            Calendar time = Calendar.getInstance();
                                            time.setTimeInMillis(date_of_finish * 1000);
                                            time.add(Calendar.HOUR_OF_DAY, selectedHour);
                                            time.add(Calendar.MINUTE, selectedMinute);

                                            if (time.getTimeInMillis() / 1000 >= start_time) {

                                                end_time = time.getTimeInMillis() / 1000;
                                                dvEnd.setText(formatTime(time.getTimeInMillis() / 1000));

                                            } else {
                                                new MaterialDialog.Builder(finish.getContext()).title(R.string.alert).content(R.string.end_time_msg).negativeText(R.string.close).show();
                                            }

                                        }
                                    }, 0, 0, false);

                                    mTimePicker.setTitle(R.string.select_end_time);
                                    mTimePicker.show();

                                } else {

                                    new MaterialDialog.Builder(finish.getContext()).title(R.string.alert).content(R.string.select_start_msg).negativeText(R.string.close).show();
                                }
                            }
                        });

                        dvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();

                            }
                        });

                        dvFinish.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                int position = getAdapterPosition();
                                if (position != RecyclerView.NO_POSITION) {

                                    if (date_of_finish != 0 && start_time != 0 && end_time != 0) {

                                       // apiFinishBookingItem(finish.getContext(), position, start_time, end_time);

                                    } else {

                                        new MaterialDialog.Builder(finish.getContext()).title(R.string.alert).content(R.string.select_date_time).negativeText(R.string.close).show();
                                    }
                                }
                            }
                        });

                        dialog.show();
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

   /* private void apiCancelBookingItem(final Context c, final int position) {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(c).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        RequestParams params = new RequestParams();
        params.add("id", data.get(position).id + "");

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + Utils.token);
        client.get(Utils.webUrl + "user/bookings/cancel-booking-item", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String code = obj.getString("status_code");

                    if (code.equals("200")) {

                        data.get(position).status = "cancelled";
                        notifyItemChanged(position);
                        BookingDetail.needRefresh = true;

                    } else {

                        new MaterialDialog.Builder(c).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    new MaterialDialog.Builder(c).title(R.string.alert).content(R.string.unexpected_error).negativeText(R.string.close).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressDialog.dismiss();
                new MaterialDialog.Builder(c).title(R.string.alert).content(R.string.error_msg).negativeText(R.string.close).show();
            }
        });
    }*/

    /*private void apiFinishBookingItem(final Context c, final int position, long start_time, long end_time) {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(c).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        RequestParams params = new RequestParams();
        params.add("id", data.get(position).id + "");
        params.add("started_at", start_time + "");
        params.add("ended_at", end_time + "");

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + Utils.token);
        client.get(Utils.webUrl + "user/bookings/finish-booking-item", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String code = obj.getString("status_code");

                    if (code.equals("200")) {

                        data.get(position).status = "finished";
                        notifyItemChanged(position);
                        BookingDetail.needRefresh = true;

                    } else {

                        new MaterialDialog.Builder(c).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    new MaterialDialog.Builder(c).title(R.string.alert).content(R.string.unexpected_error).negativeText(R.string.close).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressDialog.dismiss();
                new MaterialDialog.Builder(c).title(R.string.alert).content(R.string.error_msg).negativeText(R.string.close).show();
            }
        });
    }*/

}



