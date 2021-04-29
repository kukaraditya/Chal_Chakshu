package co.dekhok.railway;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button login;
    EditText user_name,password;
    SessionManager session;
    String base64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            user_name=(EditText)findViewById(R.id.user);
            password=(EditText)findViewById(R.id.password);


            byte[] encrpt= new byte[0];
            try {
                encrpt = user_name.getText().toString().getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            login=(Button)findViewById(R.id.login);
                session=new SessionManager(this);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                    /*Intent in = new Intent(MainActivity.this,Form.class);
                    startActivity(in);*/
                }
            });
        }
        void login(){
            final ProgressDialog pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please Wait..");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setIndeterminate(true);
            pd.setProgress(0);
            pd.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.SeverUrl+"login.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(".",response);
                            check_rsponse(response);
                            pd.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse response = error.networkResponse;
                            if(response != null && response.data != null){
                               // Toast.makeText(MainActivity.this,+response.statusCode, Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

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
                                AlertDialog alertDialog = alertDialogBuilder.create();

                                // show it
                                alertDialog.show();
                                pd.dismiss();
                            }else{
                                String errorMessage=error.getClass().getSimpleName();
                                if(!TextUtils.isEmpty(errorMessage)){
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

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
                                    AlertDialog alertDialog = alertDialogBuilder.create();

                                    // show it
                                    alertDialog.show();
                                  //  Toast.makeText(MainActivity.this,errorMessage, Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                }
                            }}
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    //Adding parameters to request
                    params.put("user_name", user_name.getText().toString());
                    params.put("password", password.getText().toString());
                    String abcd= user_name.getText().toString();
                    base64= Base64.encodeToString(abcd.getBytes(), Base64.NO_WRAP);

                    Log.e("basenew",base64);
                    //returning parameter
                    return params;
                }
            };


            //Adding the string request to the queue
            Volley.newRequestQueue(this).add(stringRequest);    }
        void check_rsponse(String response){
            try {
                JSONObject jsonObject=new JSONObject(response);
                Log.e("jsonobject",jsonObject+"");

                if(jsonObject.getString("status").equals("success")){
                    JSONObject jsonObject1= jsonObject.getJSONObject("data");
                    session.createUserLoginSession(jsonObject1.getString("id"),jsonObject1.getString("name"),jsonObject1.getString("mobile"),jsonObject1.getString("email"));
                    if(session.checkLogin()==false) {
                        Intent in = new Intent(MainActivity.this, FirstActivity.class);
                        startActivity(in);
                        finish();
                    }else {
                        Intent in = new Intent(MainActivity.this, FirstActivity.class);
                        startActivity(in);
                    }

                }
                else{
                    Toast.makeText(MainActivity.this,"User Name or Password Incorrect",Toast.LENGTH_LONG).show();

                }
            }catch (Exception ex){
            }
        }
}

