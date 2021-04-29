package co.dekhok.railway;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import co.dekhok.railway.model.ArrivalModel;
import co.dekhok.railway.model.FinalSubmitLocal;

public class FinalSubmit extends AppCompatActivity {
    String dep_id, loco_id, li_id, list, list1,lati,longi,train_no;
    Button submit;
    SessionManager sessionManager;
    ImageView clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_final_submit);
        sessionManager=new SessionManager(this);
        submit = (Button) findViewById(R.id.submit);
        Intent i = getIntent();
        i.getExtras();
        Bundle b = i.getExtras();
        dep_id = b.getString("dep_id");
        loco_id = b.getString("loco_id");
        li_id = b.getString("li_id");
        list = b.getString("q_id");
        list1 = b.getString("ansr_id");
        lati= b.getString("lati");
        longi= b.getString("longi");
        train_no= b.getString("train_no");
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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // submit();
                submitLocal();

            }
        });


    }
   void submitLocal()
    {
        DatabaseHandler db = new DatabaseHandler(FinalSubmit.this);
        FinalSubmitLocal avm = new FinalSubmitLocal();

        avm.setLoco_id(loco_id);
        avm.setLi_id(li_id);
        avm.setTrain_no(train_no);
        avm.setDepartment_id(dep_id);
        avm.setAnswer(list1);
        avm.setQuestion_id(list);
        avm.setRefId(sessionManager.getRefKey(FinalSubmit.this));
        int finalID= db.cerateFinal(avm);

        if (finalID>0)
        {
            sessionManager.logoutLoco();
            sessionManager.logoutAbnormality();
            sessionManager.clearRefId();
            Intent intent=new Intent(FinalSubmit.this,Main3Activity.class);

            startActivity(intent);
        }
    }

}
