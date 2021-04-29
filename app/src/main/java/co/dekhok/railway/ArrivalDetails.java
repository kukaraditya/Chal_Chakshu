package co.dekhok.railway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import co.dekhok.railway.model.ArrivalModel;

public class ArrivalDetails extends AppCompatActivity implements LocationListener {

    EditText arrival_time;
    EditText arrival_station;
    Button submit;
    String train_no, loco_id, li_id, name,questionNo;
    long date;
    LocationManager locationManager;
    String provider;
    double longitude,latitude;
    String lat;
    String longi;
    SessionManager sessionManager;
    Geocoder geocoder;
    List<Address> addresses;
    String address;
    String versionName;
    TextView ver;
    ImageView clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_arrival_details);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ver=(TextView)findViewById(R.id.version);
        ver.setText("V-"+ versionName);
        sessionManager=new SessionManager(this);
        sessionManager.setInArrival();
        HashMap<String,String> data=sessionManager.getUserDetails();
        li_id=data.get(sessionManager.KEY_USER_ID);
        arrival_time = (EditText) findViewById(R.id.arrival_time);
        arrival_station=findViewById(R.id.arrival_station);
        submit = (Button) findViewById(R.id.button);
        geocoder = new Geocoder(this, Locale.getDefault());
        clear=(ImageView)findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // clearing app data
                    String packageName = getApplicationContext().getPackageName();
                    Runtime runtime = Runtime.getRuntime();
                    runtime.exec("pm clear "+packageName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Intent i = getIntent();
        i.getExtras();
        Bundle b = i.getExtras();
        name = b.getString("lp_name");
        loco_id = b.getString("loco_id");
        train_no = b.getString("train");
        questionNo = b.getString("questionno");

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }
        // Getting LocationManager object
        statusCheck();

        locationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE);

        // Creating an empty criteria object
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);

        if (provider != null && !provider.equals("")) {
            if (!provider.contains("gps")) { // if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings",
                        "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
            // Get the location from the given provider
            Location location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 500, 0, this);

            if (location != null)
                onLocationChanged(location);
            else
                location = locationManager.getLastKnownLocation(provider);
            if (location != null)
                onLocationChanged(location);
            else{

            }

        } else {
        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_local();
            }
        });
    }

    void send_local()
    {

        DatabaseHandler db = new DatabaseHandler(ArrivalDetails.this);
        ArrivalModel avm = new ArrivalModel();
        avm.setLoco_id(loco_id);
        avm.setLi_id(li_id);
        avm.setTrain_no(train_no);
        avm.setArrival_time(arrival_time.getText().toString());
        avm.setLongitude(longi+"");
        avm.setLatitude(lat+"");
        avm.setArrival_station(arrival_station.getText().toString());
        avm.setArr_address(address);
        avm.setRefId(sessionManager.getRefKey(ArrivalDetails.this));
       int id= db.cerateArrival(avm);
       if (id>0)
       {
           Intent intent = new Intent(ArrivalDetails.this, Question.class);
           intent.putExtra("id", loco_id);
           intent.putExtra("name", name);
           intent.putExtra("train_no", train_no);
           intent.putExtra("questionNo", questionNo);
           sessionManager.logoutArrival();
           startActivity(intent);
       }
    }

    public void statusCheck () {
            final LocationManager manager = (LocationManager) getSystemService(
                    Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();

            }
        }

        private void buildAlertMessageNoGps () {
            final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setMessage(
                    "Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false).setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog,
                                            final int id) {
                            startActivity(new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog,
                                            final int id) {
                            dialog.cancel();
                        }
                    });
            final androidx.appcompat.app.AlertDialog alert = builder.create();
            alert.show();
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            /* getMenuInflater().inflate(R.menu.activity_main, menu); */
            return true;
        }

        @Override
        public void onLocationChanged (Location location){
            // Getting reference to TextView tv_longitude
           longitude=location.getLongitude();
           latitude=location.getLatitude();

           lat=latitude+"";
           longi=longitude+"";



            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            } catch (IOException e) {
                e.printStackTrace();
            }




        }

        @Override
        public void onProviderDisabled (String provider){
            // TODO Auto-generated method stub

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Check Permissions Now
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }
        }

        @Override
        public void onProviderEnabled (String provider){
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged (String provider,int status, Bundle extras){
            // TODO Auto-generated method stub
        }
    }

