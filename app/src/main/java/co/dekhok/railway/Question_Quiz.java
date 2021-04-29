package co.dekhok.railway;


import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Question_Quiz extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Model> list1;
    Question_Adapter adapter;
    String loco_id="jp1130";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question__quiz);
        recyclerView = (RecyclerView) findViewById(R.id.rec);
        question();
    }

    void question() {
        StringRequest req = new StringRequest(StringRequest.Method.POST, "http://www.chalchaksu.in/api/question_lp.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response);

                parse_history(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error+"");
                Toast.makeText(Question_Quiz.this, error+"", Toast.LENGTH_SHORT).show();

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
                Log.e("retry", error + "");
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    void parse_history(final String response) {
        Log.e("response", response);
        try {
            JSONArray jsonArray = new JSONArray(response);
            list1 = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject mJsonObject = jsonArray.getJSONObject(i);
                Model information = new Model();
                information.setQuestion(mJsonObject.getString("question"));

               information.setAnsr_one(mJsonObject.getString("last_answer"));
                information.setTwo(mJsonObject.getString("id"));
                information.setThree(mJsonObject.getString("department_id"));
                information.setFour(mJsonObject.getString("max"));
                list1.add(information);

            }
            adapter = new Question_Adapter(list1,Question_Quiz.this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(Question_Quiz.this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}