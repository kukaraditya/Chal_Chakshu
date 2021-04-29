package co.dekhok.railway;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.BuildConfig;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import co.dekhok.railway.model.RadioQuestionModel;

public class RadioButtonn extends AppCompatActivity {
    String dep_id, loco_id, li_id, list, list1, text_ansr, text_q, lati, longi, train_no;
    String signal,engineering,traffic,trd,other,matter,station;
    Button submit;
    public static ArrayList<String> arrayValue = new ArrayList<String>();
    public static ArrayList<String> arrayque = new ArrayList<String>();
    String one = "1";
    String two = "2";
    String three = "3";
    String four = "4";
    String five = "5";
    String six = "6";
    EditText edt1, edt2, edt3, edt4, edt5, edt6, arrival_station, arrival_time;
    String text, valu;
    String base1,base2,base3,base4,base5,base6;
    SessionManager sessionManager;
    String globalResponce;
    String versionName;
    TextView ver;
    ImageView clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_radio_button);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        submit = (Button) findViewById(R.id.submit);
        sessionManager = new SessionManager(this);
        sessionManager.setInAbnormality();
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
        Intent i = getIntent();
        i.getExtras();
        Bundle b = i.getExtras();
        dep_id = b.getString("dep_id");
        loco_id = b.getString("loco_id");
        li_id = b.getString("li_id");
        list = b.getString("q_id");
        list1 = b.getString("ansr_id");
        lati = b.getString("lati");
        longi = b.getString("longi");
        train_no = b.getString("train_no");

        signal = b.getString("signal");
        engineering = b.getString("engineering");
        traffic = b.getString("traffic");
        trd = b.getString("trd");
        other = b.getString("other");
        matter = b.getString("matter");
        station = b.getString("station");


        edt1 = (EditText) findViewById(R.id.spinnerMulti);
        edt2 = (EditText) findViewById(R.id.spinnerMulti2);
        edt3 = (EditText) findViewById(R.id.spinnerMulti3);
        edt4 = (EditText) findViewById(R.id.spinnerMulti4);
        edt5 = (EditText) findViewById(R.id.spinnerMulti5);
        edt6 = (EditText) findViewById(R.id.matter);
        arrival_station = (EditText) findViewById(R.id.arstu);

        edt1.setText(signal);
        edt2.setText(engineering);
        edt3.setText(traffic);
        edt4.setText(trd);
        edt5.setText(other);
        edt6.setText(matter);
        arrival_station.setText(station);

        arrayque.add(one);
        arrayque.add(two);
        arrayque.add(three);
        arrayque.add(four);
        arrayque.add(five);
        arrayque.add(six);
        // arrayValue.clear();
        text = arrayque.toString().replace("[", "").replace("]", "");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> data1 = sessionManager.getGobalResponse_new();
                globalResponce = data1.get(sessionManager.KEY_USER_RESPONSE_NEW);
                if (globalResponce != null) {
                    parse(globalResponce);
                }else {
                   // submit();
                    subMitLocal();
                }

            }
        });
    }

    @Override
    protected void onPause() {

        sessionManager.AbnormalityData(edt2.getText().toString(),edt3.getText().toString(),edt1.getText().toString(),edt5.getText()
                .toString(),edt4.getText().toString(),edt6.getText().toString(),arrival_station.getText().toString());
        HashMap<String, String> data_new = sessionManager.getAbnormalityData();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        finishAffinity();
        super.onDestroy();
    }

    void  subMitLocal()
    {
        DatabaseHandler db = new DatabaseHandler(RadioButtonn.this);
        String edit1= edt1.getText().toString();
        String edit2= edt2.getText().toString();
        String edit3= edt3.getText().toString();
        String edit4= edt4.getText().toString();
        String edit5= edt5.getText().toString();
        String edit6= edt6.getText().toString();
        base1= Base64.encodeToString(edit1.getBytes(), Base64.NO_WRAP);
        base2= Base64.encodeToString(edit2.getBytes(), Base64.NO_WRAP);
        base3= Base64.encodeToString(edit3.getBytes(), Base64.NO_WRAP);
        base4= Base64.encodeToString(edit4.getBytes(), Base64.NO_WRAP);
        base5= Base64.encodeToString(edit5.getBytes(), Base64.NO_WRAP);
        base6= Base64.encodeToString(edit6.getBytes(), Base64.NO_WRAP);
        arrayValue.add(base1);
        arrayValue.add(base2);
        arrayValue.add(base3);
        arrayValue.add(base4);
        arrayValue.add(base5);
        arrayValue.add(base6);
        valu = arrayValue.toString().replace("[", "").replace("]", "");

        RadioQuestionModel rqmodel = new RadioQuestionModel();
        rqmodel.setQuestion_id(list);
        rqmodel.setAnswer(list1);
        rqmodel.setDepartment_id(dep_id);
        rqmodel.setTrain_no(train_no);
        rqmodel.setLoco_id(loco_id);
        rqmodel.setLi_id(li_id);
        rqmodel.setAbnormal_id(text+"");
        rqmodel.setRemark(valu+"");
        rqmodel.setArrival_station(arrival_station.getText().toString());
        rqmodel.setRefId(sessionManager.getRefKey(RadioButtonn.this));

        int id = db.cerateQuestionRadio(rqmodel);
        if (id>0)
        {
            arrayValue.clear();
            arrayque.clear();
            Intent intent = new Intent(RadioButtonn.this, AlpForm.class);
            sessionManager.logoutQuestion();
            sessionManager.logoutQuestionAbnormality();
            startActivity(intent);
        }
    }

    void submit() {
        final ProgressDialog pd = new ProgressDialog(RadioButtonn.this);
        pd.setMessage("Please Wait..");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setProgress(0);
        pd.show();
        StringRequest req = new StringRequest(StringRequest.Method.POST, Config.SeverUrl + "answer.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parse(response);
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    // Toast.makeText(MainActivity.this,+response.statusCode, Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(RadioButtonn.this);

                    // set title
                    alertDialogBuilder.setTitle("");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage(response.statusCode)
                            .setIcon(R.drawable.alert)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finishAffinity();
                                    pd.dismiss();
                                }
                            });

                    // create alert dialog
                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                    pd.dismiss();
                }else{
                    String errorMessage=error.getClass().getSimpleName();
                    if(!TextUtils.isEmpty(errorMessage)){
                        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(RadioButtonn.this);

                        // set title
                        alertDialogBuilder.setTitle("");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("No Internet / Very Slow Connection")
                                .setIcon(R.drawable.alert)
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finishAffinity();
                                        pd.dismiss();
                                    }
                                });

                        // create alert dialog
                        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                        //  Toast.makeText(MainActivity.this,errorMessage, Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }}
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String edit1= edt1.getText().toString();
                String edit2= edt2.getText().toString();
                String edit3= edt3.getText().toString();
                String edit4= edt4.getText().toString();
                String edit5= edt5.getText().toString();
                String edit6= edt6.getText().toString();
                base1= Base64.encodeToString(edit1.getBytes(), Base64.NO_WRAP);
                base2= Base64.encodeToString(edit2.getBytes(), Base64.NO_WRAP);
                base3= Base64.encodeToString(edit3.getBytes(), Base64.NO_WRAP);
                base4= Base64.encodeToString(edit4.getBytes(), Base64.NO_WRAP);
                base5= Base64.encodeToString(edit5.getBytes(), Base64.NO_WRAP);
                base6= Base64.encodeToString(edit6.getBytes(), Base64.NO_WRAP);
                arrayValue.add(base1);
                arrayValue.add(base2);
                arrayValue.add(base3);
                arrayValue.add(base4);
                arrayValue.add(base5);
                arrayValue.add(base6);
                valu = arrayValue.toString().replace("[", "").replace("]", "");
                params.put("question_id", list);
                params.put("answer", list1);
                params.put("department_id", dep_id);
                params.put("train_no", train_no);
                params.put("loco_id", loco_id);
                params.put("li_id", li_id);
                params.put("abnormal_id", text + "");
                params.put("remark", valu + "");
                params.put("arrival_station", arrival_station.getText().toString());

                return params;
            }
        };
        Volley.newRequestQueue(this).add(req);
    }

    void parse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("Success")) {
                arrayValue.clear();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        RadioButtonn.this);

                // set title
                alertDialogBuilder.setTitle("");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Suceessfully Uploaded")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(RadioButtonn.this, AlpForm.class);
                                sessionManager.logoutQuestion();
                                sessionManager.logoutQuestionAbnormality();
                                startActivity(intent);
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}