package co.dekhok.railway;

import android.Manifest;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import co.dekhok.railway.common.Api;
import co.dekhok.railway.model.FormModel;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Form extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, LocationListener {

    EditText loco_no, lp_name, loco_id, nli_name, train_no, guard_name, dep_station, alp_naam, alp_ID, no_wagon, bpc_no;
    SessionManager sessionManager;
    String li_id;
    String question="0";
    EditText dep_time;
    DatabaseHandler db;
    Date myDate;
        Spinner wgon,cat,working;
        String text,text2,text3;
        String[] wagon_type = { "Type of Stock", "Goods", "Passenger", "Light Engine"};
        String[] category = { "Category of LP", "A", "B", "C"};
        String[] workingcab = { "Working cab", "Short Hood", "Long Hood", "Cab-1" ,"Cab-2"};


    String base1,base2;

    Geocoder geocoder;
    List<Address> addresses;
    String address;

    long date;
    String filename;
    LocationManager locationManager;
    String provider;
    double longitude,latitude;
    String lat;
    String longi;
    String versionName;
    TextView ver;
    ImageView clear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_form);
        geocoder = new Geocoder(this, Locale.getDefault());
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd");
        myDate = new Date();
        filename = timeStampFormat.format(myDate);
        date = System.currentTimeMillis();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        db = new DatabaseHandler (this);
        sessionManager=new SessionManager(this);
        sessionManager.setInForm();
        ver=(TextView)findViewById(R.id.version);
        ver.setText("V-"+ versionName);
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

        HashMap<String,String> data=sessionManager.getUserDetails();
        li_id=data.get(sessionManager.KEY_USER_ID);
        Intent i = getIntent();
        i.getExtras();

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
          /*  Toast.makeText(getBaseContext(), "No Provider Found",
                    Toast.LENGTH_SHORT).show();*/
        }

        wgon=(Spinner)findViewById(R.id.editText7);
        cat=(Spinner)findViewById(R.id.editText11);
        working=(Spinner)findViewById(R.id.editText16);
        wgon.setOnItemSelectedListener(Form.this);
        cat.setOnItemSelectedListener(Form.this);
        working.setOnItemSelectedListener(Form.this);
        Button submit = (Button) findViewById(R.id.button);
        loco_no = (EditText) findViewById(R.id.editText3);
        lp_name = (EditText) findViewById(R.id.editText2);
        loco_id = (EditText) findViewById(R.id.arrival_time);
        nli_name = (EditText) findViewById(R.id.editText4);
        train_no = (EditText) findViewById(R.id.editText5);
        guard_name = (EditText) findViewById(R.id.editText6);
        dep_time = (EditText) findViewById(R.id.dep_time);
        dep_station = (EditText) findViewById(R.id.editText9);
        alp_naam = (EditText) findViewById(R.id.alp_namee);
        alp_ID=(EditText)findViewById(R.id.alp_ide);
        no_wagon = (EditText) findViewById(R.id.editText13);
        bpc_no = (EditText) findViewById(R.id.editText14);


        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,wagon_type);
        ArrayAdapter ab = new ArrayAdapter(this,android.R.layout.simple_spinner_item,category);
        ArrayAdapter ac = new ArrayAdapter(this,android.R.layout.simple_spinner_item,workingcab);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wgon.setAdapter(aa);
        cat.setAdapter(ab);
        working.setAdapter(ac);
        loco_id.addTextChangedListener(watch);
        alp_ID.addTextChangedListener(watche);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //db.delete();
                db.delete();
                db.deleteAlpQuestion();
                if (TextUtils.isEmpty(loco_id.getText())){
                    Toast.makeText(Form.this,"Please enter Loco id",Toast.LENGTH_LONG).show();

                }else if (TextUtils.isEmpty(train_no.getText())){
                    Toast.makeText(Form.this,"Please enter Train number",Toast.LENGTH_LONG).show();
                }else {
                    form_local();

                }

            }
        });

    }

    @Override
    protected void onDestroy() {
        finishAffinity();
        super.onDestroy();
    }




    @Override
    public void onBackPressed(){
        finishAffinity();

    }

    TextWatcher watch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                check_mobile(charSequence);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

    };

    TextWatcher watche = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            check_id(charSequence);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void check_mobile(final CharSequence loco_id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SeverUrl+"check_loco_detail.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        check_rsponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Form.this, "network not available", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("loco_id",loco_id+"");

                return params;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 5000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 5000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Form.this);
        requestQueue.add(stringRequest);
    }
    void check_rsponse(String response){
        try {
            JSONArray json=new JSONArray(response);
            JSONObject jsonObject=json.getJSONObject(0);
            lp_name.setText(jsonObject.getString("loco_name"));



        }catch (Exception ex){
        }

    }

    public void check_id(final CharSequence alp_ID ){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SeverUrl+"check_loco_detail_alp.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        checck_rsponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Form.this, "network not available", Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                    params.put("loco_id_alp",alp_ID+"");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Form.this);
        requestQueue.add(stringRequest);
    }
    void checck_rsponse(String response){
        try {
            JSONArray json=new JSONArray(response);
            JSONObject jsonObject=json.getJSONObject(0);
            alp_naam.setText(jsonObject.getString("loco_name_alp"));



        }catch (Exception ex){
        }

    }


    void form_local()
    {
        sessionManager.createLocoSession(train_no.getText().toString(),loco_id.getText().toString(),loco_no.getText().toString(),alp_ID.getText().toString(),alp_naam.getText().toString());

        String edit1= nli_name.getText().toString();
        String edit2= guard_name.getText().toString();

        base1= Base64.encodeToString(edit1.getBytes(), Base64.NO_WRAP);
        base2= Base64.encodeToString(edit2.getBytes(), Base64.NO_WRAP);

        FormModel fm = new FormModel();
        fm.setLocono_typeshed(loco_no.getText().toString());
        fm.setLpname_hq(lp_name.getText().toString());
        fm.setLoco_id(loco_id.getText().toString());
        fm.setNliname_lp(edit1);
        fm.setTrain_no(train_no.getText().toString());
        fm.setGurardname_hq(edit2);
        fm.setType_wagon(text);
        fm.setDep_time(dep_time.getText().toString());
        fm.setDep_station(dep_station.getText().toString());
        fm.setCaliber(text2);
        fm.setWagon_load(no_wagon.getText().toString());
        fm.setBpc_no(bpc_no.getText().toString());
        fm.setWorking_cab(text3);
        fm.setLoco_id_alp(alp_ID.getText().toString());
        fm.setAlpname_hq(alp_naam.getText().toString());
        fm.setLi_id(li_id);
        fm.setLongitude(longi+"");
        fm.setLatitude(lat+"");
        fm.setDep_address(address);
        fm.setDate(filename);


        int id = db.cerateForm(fm);
    if (id>0) {
        //db.showFormID(2);
        sessionManager.setRefKey(id,Form.this);
        sessionManager.createFormSession(loco_id.getText().toString(), lp_name.getText().toString(), train_no.getText().toString());
        Intent intent = new Intent(this, ArrivalDetails.class);
        intent.putExtra("loco_id", loco_id.getText().toString());
        intent.putExtra("lp_name", lp_name.getText().toString());
        intent.putExtra("train", train_no.getText().toString());
        intent.putExtra("questionno", question);
        sessionManager.logoutForm();
        sessionManager.createArrivalSession(train_no.getText().toString(), loco_id.getText().toString(), question, lp_name.getText().toString());
        sessionManager.createFormSessionALP(alp_ID.getText().toString(), alp_naam.getText().toString(), train_no.getText().toString());
        startActivity(intent);
             }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text = wgon.getSelectedItem().toString();
        text2 = cat.getSelectedItem().toString();
        text3 = working.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

