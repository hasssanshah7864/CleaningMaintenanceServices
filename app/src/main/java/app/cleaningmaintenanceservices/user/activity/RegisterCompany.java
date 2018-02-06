package app.cleaningmaintenanceservices.user.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import app.cleaningmaintenanceservices.R;

public class RegisterCompany extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register_company);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
