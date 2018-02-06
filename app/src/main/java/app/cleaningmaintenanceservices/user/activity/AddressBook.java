package app.cleaningmaintenanceservices.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDUserAddress;
import app.cleaningmaintenanceservices.user.adapter.AdapterAddress;
import cz.msebera.android.httpclient.Header;

public class AddressBook extends AppCompatActivity {

    RecyclerView list;
    ArrayList<MDUserAddress> addresses;
    AdapterAddress adapter;
    Button addAddress;

    public static boolean needRefresh = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_address_book);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        init();

        apiAddresses();

        addAddress.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AddressBook.this, AddressAdd.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
        );
    }


    public void init(){
        list = findViewById(R.id.actAddressBookList);
        addresses = new ArrayList<>();
        addAddress = findViewById(R.id.actAddAddress);
    }



    @Override
    protected void onResume() {
        super.onResume();

        if (needRefresh) {

            apiAddresses();
            needRefresh = false;
        }
    }

    private void populateData() {

        adapter = new AdapterAddress(addresses);
        adapter.setOnListItemClickedListener(new AdapterAddress.OnListItemClickedListener() {

            @Override
            public void onClick(int type, int position, MDUserAddress data) {


                if (type == 0) {
                    if (getCallingActivity() != null) {

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("address", new Gson().toJson(data));
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                    }
                }

                if (type == 1) {
                    apiDeleteAddress(data.id);
                }

                if (type == 2){

                    Intent intent = new Intent(AddressBook.this, AddressAdd.class);
                    intent.putExtra("address", new Gson().toJson(data));
                    startActivity(intent);
                }

            }
        });

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
    }

    private void apiAddresses() {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(AddressBook.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + Utils.token);
        client.get(Utils.webUrl + "user/addresses", new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String code = obj.getString("status_code");

                    if (code.equals("200")) {

                        addresses = new Gson().fromJson(obj.getJSONArray("data").toString(), new TypeToken<ArrayList<MDUserAddress>>() {
                        }.getType());

                        populateData();

                    } else {

                        new MaterialDialog.Builder(AddressBook.this).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressDialog.dismiss();
                new MaterialDialog.Builder(AddressBook.this).title(R.string.alert).content(R.string.error_msg).negativeText(R.string.close).show();
            }
        });
    }

    private void apiDeleteAddress(int id) {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(AddressBook.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        RequestParams params = new RequestParams();
        params.add("id", id + "");

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + Utils.token);
        client.post(Utils.webUrl + "user/address/destroy", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String code = obj.getString("status_code");

                    if (code.equals("200")) {

                        apiAddresses();

                    } else {

                        new MaterialDialog.Builder(AddressBook.this).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressDialog.dismiss();
                new MaterialDialog.Builder(AddressBook.this).title(R.string.alert).content(R.string.error_msg).negativeText(R.string.close).show();
            }
        });
    }
}
