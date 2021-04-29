package co.dekhok.railway;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OtherFootplate extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    Button button;
    int genderValue = 0;
    String li_id;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_footplate);
        sessionManager=new SessionManager(this);
        HashMap<String,String> data=sessionManager.getUserDetails();
        li_id=data.get(sessionManager.KEY_USER_ID);
        addListenerButton();
    }
    private void addListenerButton() {
        radioGroup = findViewById(R.id.answersgrp);
        button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedID = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedID);

                if (selectedID == R.id.radioButton){
                    genderValue = 1;
                }else if(selectedID == R.id.radioButton2){
                    genderValue = 2;
                }else if(selectedID == R.id.radioButton3){
                    genderValue = 3;
                }else if(selectedID == R.id.radioButton4){
                    genderValue = 4;
                }else if(selectedID == R.id.radioButton5){
                    genderValue = 5;
                }else if(selectedID == R.id.radioButton6){
                    genderValue = 7;
                }else if(selectedID == R.id.radioButton7){
                    genderValue = 8;
                }
                Toast.makeText(OtherFootplate.this, genderValue+"",Toast.LENGTH_SHORT).show();
                submit();
            }
        });
    }

    void submit(){
        final ProgressDialog pd = new ProgressDialog(OtherFootplate.this);
        pd.setMessage("Please Wait..");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setProgress(0);
        pd.show();
        StringRequest req=new StringRequest(StringRequest.Method.POST, Config.SeverUrl+"daily_attendance.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parse(response);
                Log.e("responseaman",response);
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    // Toast.makeText(MainActivity.this,+response.statusCode, Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(OtherFootplate.this);

                    // set title
                    alertDialogBuilder.setTitle("");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage(response.statusCode)
                            .setIcon(R.drawable.alert)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
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
                        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(OtherFootplate.this);

                        // set title
                        alertDialogBuilder.setTitle("");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage(errorMessage)
                                .setIcon(R.drawable.alert)
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
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
                params.put("li_id", li_id);
                params.put("daily_att", genderValue+"");

                Log.e("params",""+params);

                return params;
            }
        };
        Volley.newRequestQueue(this).add(req);
    }
    void parse(String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.getString("status").equals("Success")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        OtherFootplate.this);

                // set title
                alertDialogBuilder.setTitle("");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Suceessfully Uploaded")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sessionManager.logoutLoco();
                                sessionManager.logoutAbnormality();
                                Intent intent=new Intent(OtherFootplate.this,Main3Activity.class);

                                startActivity(intent);
                                Log.e("abhisheksharmaold","abhisheksharma");
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }



        }catch(Exception e){
            e.printStackTrace();
        }
    }
    }
