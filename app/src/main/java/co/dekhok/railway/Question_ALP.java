package co.dekhok.railway;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.BuildConfig;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Question_ALP extends AppCompatActivity {
    TextView question, given_number;
    Button prev, next;
    int counter = 0;
    int length;
    SessionManager sessionManager;
    String qid, text;
    String  marks;
    int maxi;
    TextView name;
    Button submit;
    TableRow tableRow;
    String json_ansr, json_qstn,json_lati,json_longi,json_qstnn,train_no;
    List<String> list = new ArrayList<String>();
    List<String> list2 = new ArrayList<String>();
    RadioGroup rgp;
    String li_id, loco_id, dep_id,id;
    private FusedLocationProviderClient client;
    double lati,longi;
    final JSONArray devices = new JSONArray();
    final JSONArray devicesnew = new JSONArray();
    final JSONArray dev_lati=new JSONArray();
    final JSONArray dev_longi=new JSONArray();
    DatabaseHandler db;
    String child,newbtn,new_ide;
    String questionNo,globalResponce;
    String versionName;
    TextView ver;
    ImageView clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_question__alp);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ver=(TextView)findViewById(R.id.version);
        ver.setText("V-"+ versionName);
        db = new DatabaseHandler (this);
        name = (TextView) findViewById(R.id.name);
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
        client = LocationServices.getFusedLocationProviderClient(this);
        requestPermission();
        Intent i = getIntent();
        i.getExtras();
        Bundle b = i.getExtras();
        loco_id = b.getString("id");
        name.setText(b.getString("name"));
        train_no=b.getString("train_no");
        questionNo = b.getString("questionNo");
        question = (TextView) findViewById(R.id.question);
        given_number = (TextView) findViewById(R.id.given_number);
        prev = (Button) findViewById(R.id.prev);
        next = (Button) findViewById(R.id.next);
        submit = (Button) findViewById(R.id.submit);
        tableRow = (TableRow) findViewById(R.id.tab1);
        //  recyclerView=(RecyclerView) findViewById(R.id.rec);
        rgp = (RadioGroup) findViewById(R.id.radio_group);
        sessionManager = new SessionManager(this);
        HashMap<String, String> data = sessionManager.getUserDetails();
        li_id = data.get(sessionManager.KEY_USER_ID);

        HashMap<String, String> data1 = sessionManager.getGlobalResponseAlp();
        globalResponce = data1.get(sessionManager.KEY_USER_RESPONSE_ALP);

        if (globalResponce != null && questionNo != null) {


            counter = Integer.parseInt(questionNo);
            parse_history(globalResponce);
        } else {
            question();

        }
    }

    @Override
    protected void onDestroy() {
        finishAffinity();
        super.onDestroy();
    }


    void question() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please Wait..");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setProgress(0);
        pd.show();
        StringRequest req = new StringRequest(StringRequest.Method.POST, Config.SeverUrl + "question_alp.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                globalResponce=response;
                sessionManager.setInQuestionAlp(String.valueOf(counter),globalResponce);

                parse_history(response);
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("loco_id",loco_id);
                return params;
            }
        };
        req.setRetryPolicy(new RetryPolicy() {
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
        Volley.newRequestQueue(this).add(req);
    }

    void parse_history(final String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject mJsonObject = jsonArray.getJSONObject(i);
                Information information = new Information();
                information.setQuestion(mJsonObject.getString("question"));

                information.setId(mJsonObject.getString("id"));
                information.setReferId(sessionManager.getRefKey(Question_ALP.this));

                db.createAlpQuestion(information);
            }
            length = jsonArray.length();
            JSONObject mJsonObject = jsonArray.getJSONObject(counter);
            question.setText(Html.fromHtml(mJsonObject.getString("question")));
            id = mJsonObject.getString("id");
            given_number.setText(mJsonObject.getString("Last Answer"));
            marks = mJsonObject.getString("max");
            maxi = Integer.parseInt(marks);
            qid = mJsonObject.getString("id");
            dep_id = mJsonObject.getString("department_id");
            // list2.add(qid);
            rgp.removeAllViews();


            for (int i = 0; i <= maxi; i++) {
                final RadioButton rbn = new RadioButton(Question_ALP.this);
                rbn.setId(View.generateViewId());
                rbn.setText("" + i);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                rbn.setLayoutParams(params);
                rgp.addView(rbn);

                rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int v) {
                        for (int i = 0; i < rgp.getChildCount(); i++) {
                            RadioButton btn = (RadioButton) rgp.getChildAt(i);
                            if (btn.getId() == v) {
                                text = (String) btn.getText();
                                db.updateAlpQuestion(text, id);
                                return;
                            }

                        }


                    }
                });
            }
            if (counter >= length) {
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Question_ALP.this, RadioButtonn.class);
                        intent.putExtra("dep_id", dep_id);
                        intent.putExtra("li_id", li_id);
                        intent.putExtra("loco_id", loco_id);
                        intent.putExtra("q_id", json_qstn);
                        intent.putExtra("ansr_id", json_ansr);
                        intent.putExtra("lati",json_lati);
                        intent.putExtra("longi",json_longi);
                        startActivity(intent);
                    }
                });
            }

        } catch (Exception ex) {
        }


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (counter == length-1) {

                        json_ansr=db.showAlpQuestion(sessionManager.getRefKey(Question_ALP.this)).toString();
                        json_qstnn=db.show_q_AlpQuestion(sessionManager.getRefKey(Question_ALP.this)).toString();

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                Question_ALP.this);

                        // set title
                        alertDialogBuilder.setTitle("Final Submit");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Click yes to sumit")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        sessionManager.createAbnormalitySession(dep_id,li_id,loco_id,json_qstnn,json_ansr,json_lati,json_longi,train_no);

                                        Intent intent = new Intent(Question_ALP.this, FinalSubmit.class);
                                        intent.putExtra("dep_id", dep_id);
                                        intent.putExtra("li_id", li_id);
                                        intent.putExtra("loco_id", loco_id);
                                        intent.putExtra("q_id", json_qstnn);
                                        intent.putExtra("ansr_id", json_ansr);
                                        intent.putExtra("lati", json_lati);
                                        intent.putExtra("longi", json_longi);
                                        intent.putExtra("train_no", train_no);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        dialog.cancel();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    } else {
                        counter++;
                        sessionManager.setInQuestionAlp(String.valueOf(counter), globalResponce);
                        JSONArray jsonArray = new JSONArray(response);
                        length = jsonArray.length();
                        JSONObject mJsonObject = jsonArray.getJSONObject(counter);
                        question.setText(Html.fromHtml(mJsonObject.getString("question")));
                        id = mJsonObject.getString("id");
                        given_number.setText(mJsonObject.getString("Last Answer"));
                        marks = mJsonObject.getString("max");
                        maxi = Integer.parseInt(marks);
                        qid = mJsonObject.getString("id");
                        dep_id = mJsonObject.getString("department_id");
                        rgp.removeAllViews();
                        if (ActivityCompat.checkSelfPermission(Question_ALP.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        client.getLastLocation().addOnSuccessListener(Question_ALP.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    lati = location.getLatitude();
                                    longi = location.getLongitude();
                                }

                            }
                        });
                        dev_lati.put(lati);
                        dev_longi.put(longi);
                        json_lati = dev_lati.toString();
                        json_longi = dev_longi.toString();

                        for (int i = 0; i <= maxi; i++) {
                            final RadioButton rbn = new RadioButton(Question_ALP.this);
                            rbn.setId(View.generateViewId());
                            rbn.setText("" + i);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                            rbn.setLayoutParams(params);
                            rgp.addView(rbn);

                            rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                public void onCheckedChanged(RadioGroup group, int v) {
                                    for (int i = 0; i < rgp.getChildCount(); i++) {
                                        RadioButton btn = (RadioButton) rgp.getChildAt(i);
                                        if (btn.getId() == v) {
                                            text = (String) btn.getText();
                                            db.updateAlpQuestion(text, id);
                                            return;
                                        }

                                    }


                                }
                            });
                        }
                        new_ide = db.fetchAlpQuestion(id);
                        child = new_ide;
                        for (int i = 0; i < rgp.getChildCount(); i++) {
                            RadioButton btn = (RadioButton) rgp.getChildAt(i);
                            newbtn = (String) btn.getText();
                            if (newbtn.equals(child)) {
                                btn.setChecked(true);
                            }
                        }
                    }
                } catch (Exception ex) {

                }

            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    if (counter>0)
                    {
                    counter--;
                    sessionManager.setInQuestionAlp(String.valueOf(counter), globalResponce);
                    JSONArray jsonArray = new JSONArray(response);
                    length = jsonArray.length();
                    JSONObject mJsonObject = jsonArray.getJSONObject(counter);
                    question.setText(Html.fromHtml(mJsonObject.getString("question")));
                    id = mJsonObject.getString("id");
                    given_number.setText(mJsonObject.getString("Last Answer"));
                    marks = mJsonObject.getString("max");
                    maxi = Integer.parseInt(marks);
                    qid = mJsonObject.getString("id");
                    dep_id = mJsonObject.getString("department_id");
                    // list2.add(qid);
                    rgp.removeAllViews();

                    for (int i = 0; i <= maxi; i++) {
                        final RadioButton rbn = new RadioButton(Question_ALP.this);
                        rbn.setId(View.generateViewId());
                        rbn.setText("" + i);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                        rbn.setLayoutParams(params);
                        rgp.addView(rbn);

                        rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            public void onCheckedChanged(RadioGroup group, int v) {
                                for (int i = 0; i < rgp.getChildCount(); i++) {
                                    RadioButton btn = (RadioButton) rgp.getChildAt(i);
                                    if (btn.getId() == v) {
                                        text = (String) btn.getText();
                                        db.updateAlpQuestion(text, id);
                                        return;
                                    }

                                }

                            }
                        });


                    }
                    try {

                        new_ide = db.fetchAlpQuestion(id);
                        child = new_ide;
                        for (int i = 0; i < rgp.getChildCount(); i++) {
                            RadioButton btn = (RadioButton) rgp.getChildAt(i);
                            newbtn = (String) btn.getText();
                            if (newbtn.equals(child)) {
                                btn.setChecked(true);
                            }
                        }
                    } catch (IndexOutOfBoundsException e) {
                    }
                }

                } catch (Exception ex) {
                }
            }

        });

    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }
}
