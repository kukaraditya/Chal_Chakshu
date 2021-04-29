package co.dekhok.railway;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import androidx.recyclerview.widget.RecyclerView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Question extends AppCompatActivity {
    TextView question, given_number;
    Button prev, next;
    int counter = 0;
    final int count = counter;
    int length;
    SessionManager sessionManager;
    ArrayList<Model> list1;
    Spinner spinner;
    String qid, text;
    String child, marks;
    int maxi;
    TextView name;
    Button submit, start;
    TableRow tableRow;
    Boolean flag;
    String newbtn;
    String json_ansr, json_qstn,json_lati,json_longi,json_qstnn;
    RecyclerView recyclerView;
    List<String> list = new ArrayList<String>();
    List<String> list2 = new ArrayList<String>();
    //Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10};
    String[] country = {};
    String[] country_new = {"1", "2", "3", "4", "5"};
    RadioGroup rgp;
    String li_id, loco_id, dep_id,train_no;
    private FusedLocationProviderClient client;
    double lati,longi;
    final JSONArray devices = new JSONArray();
    final JSONArray devicesnew = new JSONArray();
    final JSONArray dev_lati=new JSONArray();
    final JSONArray dev_longi=new JSONArray();
    DatabaseHandler db;
    String value,id,new_ide;
    String globalResponce,questionNo;
    String versionName;
    TextView ver;
    ImageView clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_question);
        versionName = BuildConfig.VERSION_NAME;
        SharedPreferences prefs = getSharedPreferences("MY PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("pageName","page");
        editor.apply();
        Config.abhi=true;
        name = (TextView) findViewById(R.id.name);
        ver=(TextView)findViewById(R.id.version);
        ver.setText("V-"+ versionName);
        client = LocationServices.getFusedLocationProviderClient(this);
        db = new DatabaseHandler (this);
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
        loco_id = b.getString("id");
        name.setText(b.getString("name"));
        train_no=b.getString("train_no");
        questionNo=b.getString("questionNo");


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

        HashMap<String, String> data1 = sessionManager.getGobalResponse();
        globalResponce = data1.get(sessionManager.KEY_USER_RESPONSE);
        if (globalResponce != null && questionNo != null) {
            counter=Integer.parseInt(questionNo);
            parse_history(globalResponce);
            sessionManager.setInQuestion(String.valueOf(counter),globalResponce);
        }
        else {
            question();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
        StringRequest req = new StringRequest(StringRequest.Method.POST, Config.SeverUrl + "question_lp.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                globalResponce=response;
                sessionManager.setInQuestion(String.valueOf(counter),globalResponce);

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
                information.setReferId(sessionManager.getRefKey(Question.this));
                db.create(information);
            }

            length = jsonArray.length();
            JSONObject mJsonObject = jsonArray.getJSONObject(counter);

            question.setText(Html.fromHtml(mJsonObject.getString("question")));
            id = mJsonObject.getString("id");
            given_number.setText(mJsonObject.getString("last_answer"));
            marks = mJsonObject.getString("max");
            maxi = Integer.parseInt(marks);
            qid = mJsonObject.getString("id");
            dep_id = mJsonObject.getString("department_id");
            // list2.add(qid);
            rgp.removeAllViews();


            for (int i = 0; i <= maxi; i++) {
                final RadioButton rbn = new RadioButton(Question.this);
                rbn.setId(View.generateViewId());
                rbn.setText("" + i);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                rbn.setLayoutParams(params);
                rgp.addView(rbn);
                rbn.isChecked();

                rgp.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int v) {
                        for (int i = 0; i < rgp.getChildCount(); i++) {
                            RadioButton btn = (RadioButton) rgp.getChildAt(i);
                            if (btn.getId() == v) {

                                text = (String) btn.getText();
                                db.update(text,id);
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

                        Intent intent = new Intent(Question.this, RadioButtonn.class);
                        intent.putExtra("dep_id", dep_id);
                        intent.putExtra("li_id", li_id);
                        intent.putExtra("loco_id", loco_id);
                        intent.putExtra("q_id", json_qstn);
                        intent.putExtra("ansr_id", json_ansr);
                        intent.putExtra("lati",json_lati);
                        intent.putExtra("longi",json_longi);
                        intent.putExtra("train_no",train_no);
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

                        json_ansr = db.show(sessionManager.getRefKey(Question.this)).toString();
                        json_qstnn = db.show_q(sessionManager.getRefKey(Question.this)).toString();

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                Question.this);

                        // set title
                        alertDialogBuilder.setTitle("Final Submit");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Click yes to sumit")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        sessionManager.createAbnormalitySession(dep_id, li_id, loco_id, json_qstnn, json_ansr, json_lati, json_longi, train_no);

                                        Intent intent = new Intent(Question.this, RadioButtonn.class);
                                        intent.putExtra("dep_id", dep_id);
                                        intent.putExtra("li_id", li_id);
                                        intent.putExtra("loco_id", loco_id);
                                        intent.putExtra("q_id", json_qstnn);
                                        intent.putExtra("ansr_id", json_ansr);
                                        intent.putExtra("train_no", train_no);
                                        sessionManager.logoutQuestion();
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    } else {
                        counter++;
                        sessionManager.setInQuestion(String.valueOf(counter), globalResponce);
                        final JSONArray jsonArray = new JSONArray(response);
                        length = jsonArray.length();
                        JSONObject mJsonObject = jsonArray.getJSONObject(counter);
                        question.setText(Html.fromHtml(mJsonObject.getString("question")));
                        id = mJsonObject.getString("id");
                        given_number.setText(mJsonObject.getString("last_answer"));
                        marks = mJsonObject.getString("max");
                        maxi = Integer.parseInt(marks);
                        qid = mJsonObject.getString("id");
                        dep_id = mJsonObject.getString("department_id");
                        rgp.removeAllViews();
                        int lastChildPos = rgp.getChildCount();
                        for (int i = 0; i <= maxi; i++) {
                            final RadioButton rbn = new RadioButton(Question.this);
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
                                            db.update(text, id);
                                            return;
                                        }

                                    }


                                }
                            });

                        }


                        new_ide = db.fetch(id);
                        child = new_ide;
                        for (int i = 0; i < rgp.getChildCount(); i++) {
                            RadioButton btn = (RadioButton) rgp.getChildAt(i);
                            newbtn = (String) btn.getText();
                            if (newbtn.equals(child)) {
                                btn.setChecked(true);
                            }
                        }
                    }

                    } catch(Exception ex){
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
                    sessionManager.setInQuestion(String.valueOf(counter), globalResponce);
                    JSONArray jsonArray = new JSONArray(response);
                    length = jsonArray.length();
                    JSONObject mJsonObject = jsonArray.getJSONObject(counter);
                    question.setText(Html.fromHtml(mJsonObject.getString("question")));
                    id = mJsonObject.getString("id");
                    given_number.setText(mJsonObject.getString("last_answer"));
                    marks = mJsonObject.getString("max");
                    maxi = Integer.parseInt(marks);
                    qid = mJsonObject.getString("id");
                    dep_id = mJsonObject.getString("department_id");
                    rgp.removeAllViews();

                    for (int i = 0; i <= maxi; i++) {
                        final RadioButton rbn = new RadioButton(Question.this);
                        rbn.setId(View.generateViewId());
                        rbn.setText("" + i);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                        rbn.setLayoutParams(params);
                        rgp.addView(rbn);
                        rbn.isChecked();

                        rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            public void onCheckedChanged(RadioGroup group, int v) {
                                for (int i = 0; i < rgp.getChildCount(); i++) {
                                    RadioButton btn = (RadioButton) rgp.getChildAt(i);
                                    if (btn.getId() == v) {
                                        text = (String) btn.getText();

                                        db.update(text, id);
                                        // db.show();
                                        //  devices.put(text);
                                        //  json_ansr = devices.toString();
                                        return;
                                    }
                                }
                            }
                        });
                    }
                    try {

                        new_ide = db.fetch(id);
                        child = new_ide;
                        for (int i = 0; i < rgp.getChildCount(); i++) {
                            RadioButton btn = (RadioButton) rgp.getChildAt(i);
                            newbtn = (String) btn.getText();
                            Log.e("newbtn", newbtn);
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

}






