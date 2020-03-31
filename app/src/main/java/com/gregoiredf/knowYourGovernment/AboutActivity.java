package com.gregoiredf.knowYourGovernment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView aboutGoogleApi = findViewById(R.id.aboutGoogleAPI);
        aboutGoogleApi.setTextColor(Color.WHITE);
        aboutGoogleApi.setTextSize(24);
        aboutGoogleApi.setGravity(Gravity.CENTER);
        aboutGoogleApi.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        //aboutGoogleApi.setText("Google Civic Information API");//̲𝖦̲𝗈̲𝗈̲𝗀̲𝗅̲𝖾̲ ̲𝖢̲𝗂̲𝗏̲𝗂̲𝖼̲ ̲𝖨̲𝗇̲𝖿̲𝗈̲𝗋̲𝗆̲𝖺̲𝗍̲𝗂̲𝗈̲𝗇̲ ̲𝖠̲𝖯̲𝖨̲
        aboutGoogleApi.setText("̲\uD835\uDDA6̲\uD835\uDDC8̲\uD835\uDDC8̲\uD835\uDDC0̲\uD835\uDDC5̲\uD835\uDDBE̲ ̲\uD835\uDDA2̲\uD835\uDDC2̲\uD835\uDDCF̲\uD835\uDDC2̲\uD835\uDDBC̲ ̲\uD835\uDDA8̲\uD835\uDDC7̲\uD835\uDDBF̲\uD835\uDDC8̲\uD835\uDDCB̲\uD835\uDDC6̲\uD835\uDDBA̲\uD835\uDDCD̲\uD835\uDDC2̲\uD835\uDDC8̲\uD835\uDDC7̲ ̲\uD835\uDDA0̲\uD835\uDDAF̲\uD835\uDDA8̲");
        final String civicApiUrl = "https://developers.google.com/civic-information/";

        aboutGoogleApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(civicApiUrl));
                startActivity(intent);
            }
        });


    }
}
