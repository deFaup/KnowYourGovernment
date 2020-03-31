package com.gregoiredf.knowYourGovernment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends AppCompatActivity
{
    private ConstraintLayout photoDetailLayout;
    private ImageView photoDetailPhoto, photoDetailLogo;
    private TextView photoDetailLocation, photoDetailOffice, photoDetailName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        photoDetailLayout  =findViewById(R.id.photoDetailLayout);
        photoDetailPhoto  =findViewById(R.id.photoDetailPortrait);
        photoDetailLogo  =findViewById(R.id.photoDetailLogo);
        photoDetailLocation  =findViewById(R.id.photoDetailLocation);
        photoDetailOffice  =findViewById(R.id.photoDetailOffice);
        photoDetailName  =findViewById(R.id.photoDetailName);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.ACTION_ATTACH_DATA)) {
            Office thisOffice = (Office) intent.getSerializableExtra(Intent.ACTION_ATTACH_DATA);
            photoDetailLocation.setText(intent.getStringExtra("IntentLocation"));
            photoDetailOffice.setText(thisOffice.getOfficeName());
            photoDetailName.setText(thisOffice.getOfficialName());

            Picasso picasso = new Picasso.Builder(this).build();
            picasso.load(thisOffice.getImageURL()).error(R.drawable.missing)
                    .placeholder(R.drawable.placeholder)
                    .into(photoDetailPhoto);

            String party = thisOffice.getParty();
            if (party.equals("Democratic Party")) {
                photoDetailLayout.setBackgroundColor(Color.BLUE);
                photoDetailLogo.setImageDrawable(getDrawable(R.drawable.dem_logo));
            } else if (party.equals("Republican Party")) {
                photoDetailLayout.setBackgroundColor(Color.RED);
                photoDetailLogo.setImageDrawable(getDrawable(R.drawable.rep_logo));
            } else
                photoDetailLayout.setBackgroundColor(Color.BLACK);
        }
    }
}
