package co.dekhok.railway;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Splash extends AppCompatActivity implements UpadtePopup.Listener {
    SessionManager sessionManager;
    String version_code,version_name;
    String vername;
    String verCode;
    String version_url = Config.SeverUrl + "Version.php";
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sessionManager=new SessionManager(this);
        db =  new DatabaseHandler(Splash.this);
        Thread thread=new Thread(){
            public void run(){
                try{
                    sleep(300);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    try {
                        PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                        vername = pInfo.versionName;
                        verCode = String.valueOf(pInfo.versionCode);
                        Log.e("verCode",verCode+"----"+vername);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    version_check();
                   /* if(sessionManager.checkLogin()==false){


                        Intent a=new Intent(Splash.this,Form.class);
                        startActivity(a);
                        finish();
                    }else {
                        Intent b=new Intent(Splash.this,MainActivity.class);
                        startActivity(b);
                        finish();

                    }
*/


                }
            }
        };
        thread.start();

    }
    void version_check(){
        StringRequest req = new StringRequest(StringRequest.Method.POST, version_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                parse(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Snackbar.make(recView, "Error updating content " + error, Snackbar.LENGTH_LONG).show();
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
                if(sessionManager.checkLogin()==false){


                    Intent a=new Intent(Splash.this,Main2Activity.class);
                    startActivity(a);
                    finish();
                }else {
                    Intent b=new Intent(Splash.this,MainActivity.class);
                    startActivity(b);
                    finish();

                }

            }else {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("editDilaogue1");
                UpadtePopup dialiPopup = new UpadtePopup();
                dialiPopup.setListener(this);
                dialiPopup.setCancelable(false);
                dialiPopup.show(ft, "editDilaogue1");
                Log.e("aaaa","aaaa");
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void returnData() {

    }


}

