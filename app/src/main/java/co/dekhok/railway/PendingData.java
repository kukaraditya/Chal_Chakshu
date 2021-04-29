package co.dekhok.railway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import co.dekhok.railway.common.Api;
import co.dekhok.railway.model.PendingModel;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PendingData extends AppCompatActivity {
    RecyclerView res;
    DatabaseHandler db;
    ArrayList<PendingModel> data;
    Button btnuploadall;
    PendingAdapter adepter;
    ImageView clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_pending_data);
        db = new DatabaseHandler(this);
        res = (RecyclerView) findViewById(R.id.rec);
        btnuploadall = (Button) findViewById(R.id.btnuploadall);
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
        data = new ArrayList<PendingModel>();
        data.clear();
        Cursor c = db.showForm();

        if (c.moveToFirst()) {
            do {
                PendingModel om = new PendingModel();
                om.setId(c.getInt(0));
                om.setTrain(c.getString(5));
                om.setDate(c.getString(20));
                data.add(om);

            } while (c.moveToNext());
        }
        adepter = new PendingAdapter(data, PendingData.this);
        res.setAdapter(adepter);
        res.setLayoutManager(new LinearLayoutManager(PendingData.this));

        btnuploadall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor c = db.showForm();
                JSONArray jsonArray_Data = new JSONArray();
                if (c.moveToFirst()) {
                    do {

                        JSONObject postData = new JSONObject();
                        String loco_no = c.getString(1);
                        String lp_name = c.getString(2);
                        String loco_id = c.getString(3);
                        String nli_lp = c.getString(4);
                        String train_no = c.getString(5);
                        String guard = c.getString(6);
                        String stock = c.getString(7);
                        String dep_time = c.getString(8);
                        String dep_station = c.getString(9);
                        String category = c.getString(10);
                        String load = c.getString(11);
                        String bpc = c.getString(12);
                        String cab = c.getString(13);
                        String alp_id = c.getString(14);
                        String alp_name = c.getString(15);
                        String li_id = c.getString(16);
                        String dep_longitude = c.getString(17);
                        String dep_latitude = c.getString(18);
                        String dep_addres = c.getString(19);
                        String date = c.getString(20);


                        try {
                            postData.put("locono_typeshed", loco_no);

                        postData.put("lpname_hq", lp_name);
                        postData.put("loco_id", loco_id);
                        postData.put("nliname_lp", nli_lp);
                        postData.put("train_no", train_no);
                        postData.put("gurardname_hq", guard);
                        postData.put("type_wagon", stock);
                        postData.put("dep_time", dep_time);
                        postData.put("dep_station", dep_station);
                        postData.put("caliber", category);
                        postData.put("wagon_load", load);
                        postData.put("bpc_no", bpc);
                        postData.put("working_cab", cab);
                        postData.put("loco_id_alp", alp_id);
                        postData.put("alpname_hq", alp_name);
                        postData.put("li_id", li_id);
                        postData.put("dep_longitude", dep_longitude);
                        postData.put("dep_latitude", dep_latitude);
                        postData.put("dep_addres", dep_addres);
                        postData.put("date_new", date);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        /****   Arrival Data   ******/
                        Cursor abc = db.showArrival(c.getInt(0));
                        if (abc.moveToFirst()) {
                            String arrival_time = abc.getString(4);
                            String arrival_longitude = abc.getString(5);
                            String arrival_lati = abc.getString(6);
                            String arrival_station = abc.getString(7);
                            String arr_address = abc.getString(8);

                            try {
                                postData.put("arrival_time", arrival_time);

                            postData.put("arrival_longitude", arrival_longitude);
                            postData.put("arrival_lati", arrival_lati);
                            postData.put("arrival_station", arrival_station);
                            postData.put("arr_address", arr_address);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            do {

                                int size = abc.getColumnCount();
                                for (int i = 0; i < size; i++) {
                                    String id = abc.getString(i);
                                }
                            } while (abc.moveToNext());
                        }

                        /**********  Question **********/
                        JSONArray jsonArray = db.show(c.getInt(0));

                        /**********  Question **********/
                        JSONArray jsonArray1 = db.show_q(c.getInt(0));

                        /**********  Question **********/
                        Cursor cord = db.showRadioQuestionLp(c.getInt(0));
                        if (cord.moveToFirst()) {
                            String lp_question=cord.getString(1);
                            String lp_answer=cord.getString(2);
                            String abnormal_id=cord.getString(7);
                            String remark=cord.getString(8);

                            try {
                                postData.put("question_id",lp_question);
                                postData.put("answer",lp_answer);
                                postData.put("abnormal_id", abnormal_id);
                                postData.put("remark", remark);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            do {
                                int size = cord.getColumnCount();
                                for (int i = 0; i < size; i++) {
                                    String id = cord.getString(i);
                                }
                            } while (cord.moveToNext());
                        }

                        /***************     ALP FORM       **************/
                        Cursor alpcursor = db.showFormAlp(c.getInt(0));
                        if (cord.moveToFirst()) {
                            do {
                                int size = cord.getColumnCount();
                                for (int i = 0; i < size; i++) {
                                    String id = cord.getString(i);
                                }
                            } while (cord.moveToNext());
                        }


                        /**********  Question ALP **********/
                        JSONArray jsonArrayalp = db.showAlpQuestion(c.getInt(0));

                        /**********  Question ALP **********/
                        JSONArray jsonArrayalp1 = db.show_q_AlpQuestion(c.getInt(0));


                        /**********  Final submit **********/
                        Cursor finalsubmit = db.showfinalSubmit(c.getInt(0));

                        if (finalsubmit.moveToFirst()) {
                            String alp_question=finalsubmit.getString(1);
                            String alp_answer=finalsubmit.getString(2);
                            try {
                                postData.put("alp_question",alp_question);
                                postData.put("alp_answer",alp_answer);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            do {
                                int size = finalsubmit.getColumnCount();
                                for (int i = 0; i < size; i++) {
                                    String id = finalsubmit.getString(i);
                                      }
                            } while (finalsubmit.moveToNext());
                        }
                        jsonArray_Data.put(postData);
                    } while (c.moveToNext());

                }
                uploadPending(jsonArray_Data+"");

            }

        });



    }

    void  uploadPending(final String json){
        final ProgressDialog pd = new ProgressDialog(PendingData.this);
        pd.setMessage("Please Wait..");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setProgress(0);
        pd.show();

        OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        MediaType mediaType = MediaType.parse("text/plain");
                        RequestBody body = RequestBody.create(mediaType, json+"");
                        Request authorisedRequest = chain.request().newBuilder()
                                .method("POST", body)
                                .addHeader("Content-Type", "text/plain")
                                .addHeader("Cookie", "language=en-gb; currency=INR; OCSESSID=jsdhsdhagshdghiuwhwqe")
                                .build();
                        return chain.proceed(authorisedRequest);
                    }}).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.SeverUrl).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).client(defaultHttpClient).build();
        Api api = retrofit.create(Api.class);
        Call<String> call = api.updatePendingData();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                //Toast.makeText()
                if (response.isSuccessful()) {

                    try {
                        JSONObject j = new JSONObject(response.body());
                        if (j.getString("status").equals("Success"))
                        {
                            db.deleteAllData();
                            adepter.notifyDataSetChanged();
                            Intent intent=new Intent(PendingData.this,PendingData.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(PendingData.this, "Data Not Filled Properly", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    pd.dismiss();
                }
                else
                {
                    pd.dismiss();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pd.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent(PendingData.this,Main3Activity.class);
        startActivity(intent);

    }



}