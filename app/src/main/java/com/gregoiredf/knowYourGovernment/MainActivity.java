package com.gregoiredf.knowYourGovernment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "Gvt_MainActivity";
    private RecyclerView recycler;
    private OfficeAdapter officeAdapter;
    private ArrayList<Office> officesList = new ArrayList<>();
    private TextView mainLocation;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLocation = findViewById(R.id.mainLocation);

        // Recyler init
        recycler = findViewById(R.id.recycler);
        officeAdapter = new OfficeAdapter(officesList, this);
        recycler.setAdapter(officeAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        // Location init
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!appHasLocation()) {
            requestLocationAccess();
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        String location = "null";
        // if we have location authorization then we try to get the position back
            // but if location service was not turned on by the user then we load nothing
        // else we load nothing else is equivalent to location==null
        if (appHasLocation())
            location = getLocation();
        if (location.equals("null")) return;

        // Download data only if there is nothing yet
        if (!officesList.isEmpty())
            return;
        downloadRepresentatives(location);
    }

    /*** Location ***/
    @Override
    public void onRequestPermissionsResult
    (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_LOCATION_REQUEST_CODE_ID)
        {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PERMISSION_GRANTED) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
            else
                mainLocation.setText("Location services denied");
        }
    }
    public boolean appHasLocation()
    {
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED ;
    }
    public void requestLocationAccess()
    {
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                MY_LOCATION_REQUEST_CODE_ID);
    }
    @SuppressLint("MissingPermission")
    private String getLocation()
    {
        String bestProvider = "network";
        Location currentLocation = locationManager.getLastKnownLocation(bestProvider);
        if (currentLocation == null)
            currentLocation = new Location(bestProvider);

        // Location permission ok but GPS not activated
        if( (currentLocation == null) || (currentLocation.getLatitude()==0.0 && currentLocation.getLongitude()==0.0) )
            return "null";
        else
        {
            return String.format(Locale.getDefault(),"%.4f, %.4f",
                    currentLocation.getLatitude(),currentLocation.getLongitude());
        }
    }


    /* --- Menu --- */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_search:
                searchDialog();
                break;
            default: break;
        }
        return true;
    }


    @Override
    public void onClick(View v)
    {
        int pos = recycler.getChildLayoutPosition(v);
        Office officeToShow = officesList.get(pos);

        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra(Intent.ACTION_ATTACH_DATA, officeToShow);
        intent.putExtra("IntentLocation",mainLocation.getText().toString());

        startActivity(intent);
    }
    private ArrayList<Office> fakeList()
    {
        String[] casting = new String[] {
                "Michael Scott","Dwight Schrute","Jim Halpert","Andy Bernard","Creed Bratton",
                "Meredith Palmer","Pam Halpert","Erin Hannon","Angela Martin","Kevin Malone",
                "Oscar Martinez","Darryl Philbin","Phyllis Lapin-Vance","Stanley Hudson",
                "Toby Flenderson", "Ryan Howard"};
        String[] jobs = new String[] {"regional manager"," assistant TO the regional manager",
                " sales"," sales"," quality insurance"," customer relations"," secretary",
                " secretary"," accountant"," accountant"," accountant"," warehouse guy"," sales",
                " sales"," HR", "Temp."};

        String[] links = new String[]
        {
                "https://www.indiewire.com/wp-content/uploads/2017/12/shutterstock_5886251dc.jpg",
                "https://img.nbc.com/sites/nbcunbc/files/images/2013/11/12/dwight-500x500.jpg",
                "https://img.nbc.com/sites/nbcunbc/files/images/2013/11/12/jim-500x500.jpg",
                "https://img.nbc.com/sites/nbcunbc/files/images/2013/11/12/andy-500x500.jpg",
                "https://img.nbc.com/sites/nbcunbc/files/metaverse_assets/1/0/6/3/3/0/creed-500x500.jpg",
                "https://img.nbc.com/sites/nbcunbc/files/images/2013/11/12/meredith-500x500.jpg",
                "https://img.nbc.com/sites/nbcunbc/files/images/2013/11/12/pam-500x500.jpg",
                "https://img.nbc.com/sites/nbcunbc/files/images/2013/11/12/erin-500x500.jpg",
                "https://img.nbc.com/sites/nbcunbc/files/images/2013/11/12/angela-500x500.jpg",
                "https://img.nbc.com/sites/nbcunbc/files/images/2 013/11/12/kevin-500x500.jpg",
                "https://img.nbc.com/sites/nbcunbc/files/images/2013/11/12/oscar-500x500.jpg",
                "https://img.nbc.com/sites/nbcunbc/files/images/2013/11/12/darryl-500x500.jpg",
                "https://img.nbc.com/sites/nbcunbc/files/images/2013/11/12/phyllis-500x500.jpg",
                "https://img.nbc.com/sites/nbcunbc/files/images/2013/11/12/stanley-500x500.jpg",
                "https://img.nbc.com/sites/nbcunbc/files/images/2013/11/12/toby-500x500.jpg",
                "https://i.pinimg.com/originals/56/4b/4e/564b4e2e3c2d2f8ef17798b97c0628b0.jpg"
        };
        ArrayList<Office> theOffice = new ArrayList<>();
        for (int i =0; i < casting.length; ++i)
        {
            Office dude = new Office(jobs[i], casting[i],links[i]);
            theOffice.add(dude);
        }
        return theOffice;
    }

    /**** Handler ****/
    private boolean appHasNetwork()
    {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null)
            return netInfo.isConnected();
        else return false;
    }
    private static int MY_LOCATION_REQUEST_CODE_ID = 329;

    private void downloadRepresentatives(String location)
    {
        if (appHasNetwork())
            new AsyncCivicDownload(this, officesList, location).execute();
        else noNetworkDialog();
    }


    /**** Dialogs ****/
    private void noNetworkDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Network Connection");
        builder.setMessage("Turn on WiFi or Network connectivity then close and relaunch the app");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void searchDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        et.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        builder.setView(et);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                downloadRepresentatives(et.getText().toString());
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });

        builder.setTitle("Enter a city/state or a zipcode:");
        //builder.setMessage("Please enter a stock symbol:");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void gpsNotOnDialog()
    {
        AlertDialog.Builder gpsDialog = new AlertDialog.Builder(this);
        gpsDialog.setTitle("Turn on Location");
        gpsDialog.setMessage("Location is necessary to get your position");
        AlertDialog dialog = gpsDialog.create();
        dialog.show();
    }

    /*** Post Async ***/
    public void updateRecyclerView(){officeAdapter.notifyDataSetChanged();}
    public void updateLocation(String location) {mainLocation.setText(location);}



}