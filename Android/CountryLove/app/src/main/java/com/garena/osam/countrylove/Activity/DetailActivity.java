package com.garena.osam.countrylove.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.garena.osam.countrylove.R;

public class DetailActivity extends AppCompatActivity {
    TextView companyText = null;
    TextView detailAddressText = null;
    TextView classificationText = null;
    TextView detailInfoText = null;
    TextView companyInfoText = null;
    TextView companyTelText = null;
    ImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        companyText = (TextView) findViewById(R.id.company);
        detailAddressText = (TextView) findViewById(R.id.detail_address);
        classificationText = (TextView) findViewById(R.id.classification);
        detailInfoText = (TextView) findViewById(R.id.detail_info);
        companyInfoText = (TextView) findViewById(R.id.company_info);
        companyTelText = (TextView) findViewById(R.id.company_tel);
        imageView = (ImageView) findViewById(R.id.image);

        Intent intent = getIntent();
        String company = intent.getExtras().getString("company");
        String detail_address = intent.getExtras().getString("detail_address");
        String classification = intent.getExtras().getString("classification");
        String detail_info = intent.getExtras().getString("detail_info");
        String company_info = intent.getExtras().getString("company_info");
        String company_tel = intent.getExtras().getString("company_tel");
        Bitmap image = (Bitmap) intent.getExtras().get("image");

        companyText.setText(company);
        detailAddressText.setText(detail_address);
        classificationText.setText(classification);
        detailInfoText.setText(detail_info);
        companyInfoText.setText(company_info);
        companyTelText.setText(company_tel);
        imageView.setImageBitmap(image);
    }
}
