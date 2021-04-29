package co.dekhok.railway;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import co.dekhok.railway.common.Api;
import co.dekhok.railway.model.AlpModel;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class AlpForm extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    EditText loco_no, alp_name, id, nli_name,train_no,lp_id;
    double lon,lat;
    double lati,longi;
    SessionManager sessionManager;
    String li_id,loco_id,train,loco_num,alp_na,alp_id;
    String dep_id,text2;
    TextView datenew;
    DatabaseHandler db;
    String base;
    Spinner cat;
    String[] category = { "Category of LP", "NA", "A", "B", "C"};
    String versionName;
    TextView ver;
    ImageView clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_alp_form);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ver=(TextView)findViewById(R.id.version);
        ver.setText("V-"+ versionName);
        db = new DatabaseHandler (this);
        sessionManager=new SessionManager(this);
        HashMap<String,String> data=sessionManager.getUserDetails();
        HashMap<String,String> data2=sessionManager.getDetails();
        li_id=data.get(sessionManager.KEY_USER_ID);
        loco_id=data2.get(sessionManager.KEY_lOCO_ID);
        train=data2.get(sessionManager.KEY_TRAIN);
        loco_num=data2.get(sessionManager.KEY_lOCO_NUMBER);
        alp_na=data2.get(sessionManager.KEY_ALP_NAME);
        alp_id=data2.get(sessionManager.KEY_ALP_ID);
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
        requestPermission();
        GPSTracker gps = new GPSTracker(this);
        cat=(Spinner)findViewById(R.id.editText7);
        cat.setOnItemSelectedListener(AlpForm.this);
        Button submit = (Button) findViewById(R.id.button);
        loco_no = (EditText) findViewById(R.id.editText3);
        alp_name = (EditText) findViewById(R.id.editText2);
        id = (EditText) findViewById(R.id.arrival_time);
        nli_name = (EditText) findViewById(R.id.editText4);
        train_no = (EditText) findViewById(R.id.editText5);
        lp_id = (EditText) findViewById(R.id.editText6);
        train_no.setText(train);
        lp_id.setText(loco_id);
        loco_no.setText(loco_num);
        alp_name.setText(alp_na);
        id.setText(alp_id);
        ArrayAdapter ab = new ArrayAdapter(this,android.R.layout.simple_spinner_item,category);
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat.setAdapter(ab);
        id.addTextChangedListener(watch);
        if (gps.canGetLocation()) {

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                lon = gps.getLongitude();
                lat = gps.getLatitude();
            }
        }

        if (ActivityCompat.checkSelfPermission(AlpForm.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  db.delete();
                db.delete();
                db.deleteAlpQuestion();
                if (TextUtils.isEmpty(id.getText())){
                    Toast.makeText(AlpForm.this,"Please enter ALP id",Toast.LENGTH_LONG).show();
                }else if (TextUtils.isEmpty(lp_id.getText())){
                    Toast.makeText(AlpForm.this,"Please enter LP id",Toast.LENGTH_LONG).show();

                }else if (TextUtils.isEmpty(train_no.getText())){
                    Toast.makeText(AlpForm.this,"Please enter Train number",Toast.LENGTH_LONG).show();
                }else {
                    form_local();
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent(AlpForm.this,Main2Activity.class);
        startActivity(intent);
        finish();

    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }
    TextWatcher watch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(i>=5){
                check_mobile(charSequence);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    public void check_mobile(final CharSequence id){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please Wait.....");
        pd.setIndeterminate(false);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SeverUrl+"check_loco_detail_alp.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        check_rsponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        pd.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("loco_id_alp",id+"");
                //params.put("department_id",dep_id);
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(AlpForm.this);
        requestQueue.add(stringRequest);
    }
    void check_rsponse(String response){
        try {
            JSONArray json=new JSONArray(response);
            JSONObject jsonObject=json.getJSONObject(0);
            alp_name.setText(jsonObject.getString("loco_name_alp"));



        }catch (Exception ex){
        }

    }

    void form_local()
    {
        String edit1= nli_name.getText().toString();
        base= Base64.encodeToString(edit1.getBytes(), Base64.NO_WRAP);

        AlpModel alpModel = new AlpModel();
        alpModel.setLocono_typeshed(loco_no.getText().toString());
        alpModel.setLpname_hq(alp_name.getText().toString());
        alpModel.setLoco_id_alp(id.getText().toString());
        alpModel.setNliname_lp(edit1);
        alpModel.setTrain_no(train_no.getText().toString());
        alpModel.setLoco_id(lp_id.getText().toString());
        alpModel.setLi_id(li_id);
        alpModel.setCaliber(text2);
        alpModel.setRefId(sessionManager.getRefKey(AlpForm.this));
        int alp_id = db.cerateAlpForm(alpModel);
        if (alp_id>0)
        {
            sessionManager.createFormSessionALP(id.getText().toString(),alp_name.getText().toString(),train_no.getText().toString());
            Intent intent=new Intent(this,Question_ALP.class);
            intent.putExtra("id",id.getText().toString());
            intent.putExtra("name",alp_name.getText().toString());
            intent.putExtra("train_no",train_no.getText().toString());
            startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        text2 = cat.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

