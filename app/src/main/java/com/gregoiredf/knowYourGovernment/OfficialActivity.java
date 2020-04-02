package com.gregoiredf.knowYourGovernment;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;

public class OfficialActivity extends AppCompatActivity
{
    private ImageView officialPortrait;
    private String imageURL;
    private Picasso picasso;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        ScrollView scrollView = findViewById(R.id.officialScrollView);
        TextView officialLocation = findViewById(R.id.officialLocation);
        TextView officialName = findViewById(R.id.officialName);
        TextView officialOffice = findViewById(R.id.officialOffice);
        TextView officialParty = findViewById(R.id.officialParty);
        officialPortrait = findViewById(R.id.officialPortrait);
        ImageView officialLogo = findViewById(R.id.officialLogo);

        picasso = new Picasso.Builder(this).build();

        Intent intent = getIntent();
        if (intent.hasExtra("IntentLocation"))
            officialLocation.setText(intent.getStringExtra("IntentLocation"));

        if (intent.hasExtra(Intent.ACTION_ATTACH_DATA))
        {
            Office thisOffice = (Office) intent.getSerializableExtra(Intent.ACTION_ATTACH_DATA);

            officialOffice.setText(thisOffice.getOfficeName());
            officialName.setText(thisOffice.getOfficialName());
            officialParty.setText(thisOffice.getParty());
            setOfficialPortrait(thisOffice.getImageURL());

            /* from here we check all possible extra data and load them if they exist */
            LinearLayout linearLayout = findViewById(R.id.officialLinearLayout);
            TextView textView;
            // address            TextView textView;

            if (thisOffice.getAddress() != null)
            {
                textView = new TextView(this);
                textView.setTextColor(Color.WHITE);
                textView.setText("Address: \t\t ".concat(thisOffice.getAddress()).concat("\n"));
                textView.setTextSize(20);
                Linkify.addLinks(textView, Linkify.MAP_ADDRESSES);
                linearLayout.addView(textView);
            }
            //phone
            if (thisOffice.getPhone() != null)
            {
                textView = new TextView(this);
                textView.setTextColor(Color.WHITE);
                textView.setText("Phone: \t\t\t ".concat(thisOffice.getPhone()).concat("\n"));
                textView.setTextSize(20);
                Linkify.addLinks(textView, Linkify.PHONE_NUMBERS);
                linearLayout.addView(textView);
            }
            //url
            if (thisOffice.getUrl() != null)
            {
                textView = new TextView(this);
                textView.setTextColor(Color.WHITE);
                textView.setText("Website: \t\t ".concat(thisOffice.getUrl()).concat("\n"));
                textView.setTextSize(20);
                Linkify.addLinks(textView, Linkify.WEB_URLS);
                linearLayout.addView(textView);
            }
            //email
            if (thisOffice.getEmail() != null)
            {
                textView = new TextView(this);
                textView.setTextColor(Color.WHITE);
                textView.setText("Email: \t\t ".concat(thisOffice.getEmail()).concat("\n"));
                textView.setTextSize(20);
                Linkify.addLinks(textView, Linkify.EMAIL_ADDRESSES);
                linearLayout.addView(textView);
            }
            //channels
            if (thisOffice.getChannels() != null)
            {
                HashMap<String, String> channels = thisOffice.getChannels();
                int index = 1;
                for (String key : channels.keySet())
                {
                    ImageView img = null;
                    switch (index)
                    {
                        case 1:
                            img = findViewById(R.id.channel1);
                            break;
                        case 2:
                            img = findViewById(R.id.channel2);
                            break;
                        case 3:
                            img = findViewById(R.id.channel3);
                            break;
                        case 4:
                            img = findViewById(R.id.channel4);
                            break;
                    }
                    ++index;
                    final String id = channels.get(key);
                    switch (key)
                    {
                        case "GooglePlus":
                            img.setImageDrawable(getDrawable(R.drawable.googleplus));
                            img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    googlePlusClicked(v, id);
                                }
                            });
                            break;
                        case "Facebook":
                            img.setImageDrawable(getDrawable(R.drawable.facebook));
                            img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    facebookClicked(v, id);
                                }
                            });
                            break;
                        case "Twitter":
                            img.setImageDrawable(getDrawable(R.drawable.twitter));
                            img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    twitterClicked(v, id);
                                }
                            });
                            break;
                        case "YouTube":
                            img.setImageDrawable(getDrawable(R.drawable.youtube));
                            img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    youTubeClicked(v, id);
                                }
                            });
                            break;
                        default:
                            break;
                    }
                }
            }
        }


        if (officialParty.getText().toString().contains("Democratic"))
        {
            scrollView.setBackgroundColor(Color.BLUE);
            officialLogo.setImageDrawable(getDrawable(R.drawable.dem_logo));
        }
        else if (officialParty.getText().toString().contains("Republican"))
        {
            scrollView.setBackgroundColor(Color.RED);
            officialLogo.setImageDrawable(getDrawable(R.drawable.rep_logo));
        }
        else
            scrollView.setBackgroundColor(Color.BLACK);
    }

    private void setOfficialPortrait(final String imageURL)
    {
        this.imageURL = imageURL;

        if (imageURL == null)
        {
            officialPortrait.setImageDrawable(getDrawable(R.drawable.missing));
            return;
        }

        picasso.load(imageURL).error(R.drawable.missing)
                .placeholder(R.drawable.placeholder)
                .into(officialPortrait);
    }

    public void imageButtonClick(View v)
    {
        if (imageURL == null)
            return;

        Intent objToPass = getIntent();
        Office thisOffice = (Office) objToPass.getSerializableExtra(Intent.ACTION_ATTACH_DATA);

        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra(Intent.ACTION_ATTACH_DATA, thisOffice);
        intent.putExtra("IntentLocation", objToPass.getStringExtra("IntentLocation"));
        startActivity(intent);
    }

    /*** Social Network Intents ***/
    public void twitterClicked(View v, String id)
    {
        Intent intent = null;
        String name = id;//<the official’s twitter id from download>;
        try
        {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }
    public void facebookClicked(View v, String id)
    {
        String FACEBOOK_URL = "https://www.facebook.com/" + id;//+ <the official’s facebook id>;
        String urlToUse;
        PackageManager packageManager = getPackageManager();

        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;

            //newer versions of fb app
            if (versionCode >= 3002850)
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                //older versions of fb app
            else
                urlToUse = "fb://page/" + id ;//+ channels.get("Facebook");
        }
        catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL;
            //normal web url
        }
        Intent facebookIntent =new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }
    public void googlePlusClicked(View v, String id)
    {
        String name = id;//<the official’s google plus id from download>;
        Intent intent = null;
        try
        {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus","com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        }

        catch (ActivityNotFoundException e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + name)));
        }
    }
    public void youTubeClicked(View v, String id)
    {
        String name = id;//<the official’s youtube id from download>;
        Intent intent = null;
        try
        {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
        }
    }
}
