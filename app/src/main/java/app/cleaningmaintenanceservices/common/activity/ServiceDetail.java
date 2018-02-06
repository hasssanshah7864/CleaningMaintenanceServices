package app.cleaningmaintenanceservices.common.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDService;

public class ServiceDetail extends AppCompatActivity {

    MDService service;
    ImageView img;
    TextView txt, detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_service_detail);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        service = new Gson().fromJson(getIntent().getStringExtra("service"), MDService.class);

        img = findViewById(R.id.actServiceDetailImg);
        txt = findViewById(R.id.title);
        detail = findViewById(R.id.actServiceDetailTxt);

        Utils.loadImg(this, img, service.image, true, true);
        txt.setText(service.translation.title);
        detail.setText(service.translation.description);

    }
}
