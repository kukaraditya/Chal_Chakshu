package co.dekhok.railway;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Main3Activity extends AppCompatActivity implements UpadtePopup.Listener {
    SessionManager sessionManager;
    String loco_id, lp_name, train_no;
    String version_code, version_name;
    String vername;
    String verCode;
    String version_url = Config.SeverUrl + "Version.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        sessionManager = new SessionManager(this);
            Thread thread = new Thread() {
                public void run() {
                    try {
                        sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                            vername = pInfo.versionName;
                            verCode = String.valueOf(pInfo.versionCode);
                            version_check();
                        } catch (SQLiteConstraintException | PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();
        }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void version_check(){
        StringRequest req = new StringRequest(StringRequest.Method.POST, version_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parse(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;

            }
        };
        Volley.newRequestQueue(this).add(req);
    }
    void parse(String response) {
        try {
            JSONObject jso = new JSONObject(response);
            version_code=jso.getString("version_code");
            version_name=jso.getString("version_name");

            if (version_code.equalsIgnoreCase(verCode) && version_name.equalsIgnoreCase(vername)){

                if(sessionManager.getInQuestion()==true) {
                    HashMap<String, String> data = sessionManager.getFormDetails();
                    String QuestionNo = data.get(sessionManager.KEY_USER_QUESTIONNO);
                    String li_id = data.get(sessionManager.KEY_LP_ID);
                    String nm = data.get(sessionManager.KEY_LP_NAME);
                    String train_no = data.get(sessionManager.KEY_USER_TRAIN);
                    Intent a=new Intent(Main3Activity.this,Question.class);
                    a.putExtra("id",li_id);
                    a.putExtra("name",nm);
                    a.putExtra("train_no",train_no);
                    a.putExtra("questionNo",QuestionNo);
                    startActivity(a);
                    finish();
                }else if (sessionManager.getInAbnormality()==true){
                    HashMap<String, String> data = sessionManager.getAbnormalityDetails();
                    String dep_id = data.get(sessionManager.KEY_DEP_ID_AB);
                    String li_id = data.get(sessionManager.KEY_LI_ID_AB);
                    String loco_id = data.get(sessionManager.KEY_LOCO_ID_AB);
                    String q_id = data.get(sessionManager.KEY_Q_ID_AB);
                    String ans_id = data.get(sessionManager.KEY_ANS_ID_AB);
                    String lat_id = data.get(sessionManager.KEY_LAT_ID_AB);
                    String long_id = data.get(sessionManager.KEY_LONG_ID_AB);
                    String train_nmb = data.get(sessionManager.KEY_TRAIN_NO_AB);
                    HashMap<String, String> data_new = sessionManager.getAbnormalityData();
                    String signal = data_new.get(sessionManager.KEY_SIGNAL);
                    String engineering = data_new.get(sessionManager.KEY_ENGINEERING);
                    String traffic = data_new.get(sessionManager.KEY_TRAFFIC);
                    String trd = data_new.get(sessionManager.KEY_TRD);
                    String other = data_new.get(sessionManager.KEY_OTHERS);
                    String matter = data_new.get(sessionManager.KEY_MATTER);
                    String station = data_new.get(sessionManager.KEY_STATION);
                    Intent a=new Intent(Main3Activity.this,RadioButtonn.class);
                    a.putExtra("dep_id",dep_id);
                    a.putExtra("loco_id",loco_id);
                    a.putExtra("li_id",li_id);
                    a.putExtra("q_id",q_id);
                    a.putExtra("ansr_id",ans_id);
                    a.putExtra("lati",lat_id);
                    a.putExtra("longi",long_id);
                    a.putExtra("train_no",train_nmb);
                    a.putExtra("signal",signal);
                    a.putExtra("engineering",engineering);
                    a.putExtra("traffic",traffic);
                    a.putExtra("trd",trd);
                    a.putExtra("other",other);
                    a.putExtra("matter",matter);
                    a.putExtra("station",station);
                    startActivity(a);
                    finish();

                }else if (sessionManager.getInQuestionAlp()==true){
                    HashMap<String, String> data = sessionManager.getFormDetailsALP();
                    String QuestionNo = data.get(sessionManager.KEY_USER_QUESTIONNOALP);
                    String li_id = data.get(sessionManager.KEY_LP_ID);
                    String nm = data.get(sessionManager.KEY_ALP_NAME);
                    String train_no = data.get(sessionManager.KEY_USER_TRAIN);
                    Intent a=new Intent(Main3Activity.this,Question_ALP.class);
                    a.putExtra("id",li_id);
                    a.putExtra("name",nm);
                    a.putExtra("train_no",train_no);
                    a.putExtra("questionNo",QuestionNo);
                    startActivity(a);
                    finish();

                }else if (sessionManager.getInForm()==true){
                    HashMap<String, String> data = sessionManager.getFormSession();
                    String lp_id =data.get(sessionManager.LP_ID);
                    String lp_name =data.get(sessionManager.LP_NAME);
                    String loco_nmbr =data.get(sessionManager.LOCO_NUMBER);
                    String nli_name =data.get(sessionManager.NLI_NAME);
                    String train =data.get(sessionManager.TRAIN);
                    String dep_station =data.get(sessionManager.DEP_STATION);
                    String guard =data.get(sessionManager.GUARD);
                    String stock =data.get(sessionManager.STOCK);
                    String dep_time =data.get(sessionManager.DEP_TIME);
                    String category =data.get(sessionManager.CATEGORY);
                    String load =data.get(sessionManager.LOAD);
                    String bpc =data.get(sessionManager.BPC_NUMBER);
                    String cab =data.get(sessionManager.WORKING_CAB);
                    String alp_id =data.get(sessionManager.ALP_ID);
                    String alp_name =data.get(sessionManager.ALP_NAME);

                    Intent a=new Intent(Main3Activity.this,Form.class);
                    a.putExtra("lp_id",lp_id);
                    a.putExtra("lp_name",lp_name);
                    a.putExtra("loco_nmbr",loco_nmbr);
                    a.putExtra("nli_name",nli_name);
                    a.putExtra("train",train);
                    a.putExtra("dep_station",dep_station);
                    a.putExtra("guard",guard);
                    a.putExtra("stock",stock);
                    a.putExtra("dep_time",dep_time);
                    a.putExtra("category",category);
                    a.putExtra("load",load);
                    a.putExtra("bpc",bpc);
                    a.putExtra("cab",cab);
                    a.putExtra("alp_id",alp_id);
                    a.putExtra("alp_name",alp_name);
                    startActivity(a);
                    finish();
                }
                else if (sessionManager.getInArrival()==true){
                    HashMap<String, String> data = sessionManager.getArrivalSession();
                    String loco_id =data.get(sessionManager.looco_id);
                    String lp_name =data.get(sessionManager.lpp_name);
                    String questionno =data.get(sessionManager.questionno);
                    String train =data.get(sessionManager.train);


                    Intent a=new Intent(Main3Activity.this,ArrivalDetails.class);
                    a.putExtra("loco_id",loco_id);
                    a.putExtra("lp_name",lp_name);
                    a.putExtra("questionno",questionno);
                    a.putExtra("train",train);
                    startActivity(a);
                    finish();

                }
                else if(sessionManager.checkLogin()==false){
                    Intent a=new Intent(Main3Activity.this,FirstActivity.class);
                    startActivity(a);
                    finish();
                }else {
                    Intent b=new Intent(Main3Activity.this,MainActivity.class);
                    startActivity(b);
                    finish();

                }

            }else {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("editDilaogue1");
                UpadtePopup dialiPopup = new UpadtePopup();
                dialiPopup.setListener(this);
                dialiPopup.setCancelable(false);
                dialiPopup.show(ft, String.valueOf(prev));
            }
        } catch (Exception e) {

        }
    }


    @Override
    public void returnData() {

    }



}

