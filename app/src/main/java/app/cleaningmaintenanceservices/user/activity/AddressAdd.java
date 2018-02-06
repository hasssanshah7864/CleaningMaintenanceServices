package app.cleaningmaintenanceservices.user.activity;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v13.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDUserAddress;
import cz.msebera.android.httpclient.Header;

public class AddressAdd extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    GoogleMap mMap;
    Marker myMarker;
    LatLng myLatLng;
    final int LOCATION_REQUEST = 101;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    EditText addressEditText;
    Button addAddress;

    MDUserAddress address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_address_add);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        init();

        if (getIntent().hasExtra("address")) {

            address = new Gson().fromJson(getIntent().getStringExtra("address"), MDUserAddress.class);
            myLatLng = new LatLng(address.latitude, address.longitude);
            addressEditText.setText(address.address);

        } else {
            myLatLng = new LatLng(0, 0);
        }

        addAddress.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(TextUtils.isEmpty(addressEditText.getText().toString())){
                            Snackbar.make(findViewById(android.R.id.content), R.string.enter_address, Snackbar.LENGTH_LONG).show();

                        }
                        else {
                            apiAddAddress();
                        }
                    }
                }
        );


    }

    public void init(){
        addressEditText = findViewById(R.id.actAddAddressEditText);
        addAddress = findViewById(R.id.actAddAddressButton);
        myLatLng = new LatLng(0,0);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (myLatLng.longitude == 0 || myLatLng.latitude == 0) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);

            } else {

                LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            mLastLocation = location;
                            setLocation();
                        }
                    }
                });
            }

        } else {

            myMarker = mMap.addMarker(new MarkerOptions().position(myLatLng).title(getString(R.string.marked_location)));
            myMarker.setDraggable(true);
            myMarker.showInfoWindow();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLatLng.latitude + 0.01, myLatLng.longitude), 12.0f));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

        Toast.makeText(this, R.string.connection_suspended, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(this, R.string.connection_failed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker marker) {
                addressEditText.setText(getCompleteAddressString(marker.getPosition().latitude,marker.getPosition().longitude));
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }
        });
    }

    private void setLocation() {
        if (mLastLocation != null) {
            myLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            myMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).title(getString(R.string.you)));
            myMarker.showInfoWindow();
            myMarker.setDraggable(true);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude() + 0.01, mLastLocation.getLongitude()), 12.0f));
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST: {

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                mLastLocation = location;
                                setLocation();
                            }
                        }
                    });

                } else {
                    Toast.makeText(this, R.string.permission_required, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void apiAddAddress() {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(AddressAdd.this).content(R.string.please_wait).progress(true, 0).cancelable(false).show();

        RequestParams params = new RequestParams();
        params.add("address", addressEditText.getText().toString().trim());
        params.add("latitude", myLatLng.latitude + "");
        params.add("longitude", myLatLng.longitude + "");

        String url = "user/address/store";

        if (getIntent().hasExtra("address")) {

            params.add("address_id", address.id + "");
            url = "user/address/update";
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + Utils.token);
        client.post(Utils.webUrl + url , params, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String code = obj.getString("status_code");

                    if (code.equals("200")) {
                        AddressBook.needRefresh = true;
                        finish();

                    } else {

                        new MaterialDialog.Builder(AddressAdd.this).title(R.string.alert).content(obj.getString("message")).negativeText(R.string.close).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressDialog.dismiss();
                new MaterialDialog.Builder(AddressAdd.this).title(R.string.alert).content(R.string.error_msg).negativeText(R.string.close).show();
            }
        });
    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }
}
