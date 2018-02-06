package app.cleaningmaintenanceservices.owner.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDReview;
import app.cleaningmaintenanceservices.owner.adapter.AdapterReview;

public class Reviews extends AppCompatActivity {

    TextView reviews, rating;
    RecyclerView list;
    AdapterReview adapter;

    ArrayList<MDReview> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_reviews);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        reviews = findViewById(R.id.actReviewsReviews);
        rating = findViewById(R.id.actReviewsRating);
        list = findViewById(R.id.actReviewsList);

        //apiReviews();
    }

    @SuppressLint("SetTextI18n")
    private void populateData() {

        reviews.setText(data.size() + "");
        rating.setText(Utils.user.company.average_rating + "");

        adapter = new AdapterReview(data);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

    }

   /* private void apiReviews() {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(Reviews.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + Utils.token);
        client.get(Utils.webUrl + "user/reviews", new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String code = obj.getString("status_code");

                    if (code.equals("200")) {

                        data = new Gson().fromJson(obj.getJSONArray("data").toString(), new TypeToken<ArrayList<MDReview>>() {
                        }.getType());

                        populateData();

                    } else {

                        new MaterialDialog.Builder(Reviews.this).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressDialog.dismiss();
                new MaterialDialog.Builder(Reviews.this).title(R.string.alert).content(R.string.error_msg).negativeText(R.string.close).show();
            }
        });
    }*/
}
