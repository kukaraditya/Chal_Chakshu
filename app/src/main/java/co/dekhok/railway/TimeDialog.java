package co.dekhok.railway;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Created by dekho on 14/10/2019.
 */

public class TimeDialog extends DialogFragment {

    EditText arrival_time, train_no, loco_id;
    String li_id;
    Button ok;
    SessionManager sessionManager;
    String longi,lati;


    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public static interface Listener {
        void returnData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.time_dialog, container, false);
        Bundle mArgs = getArguments();
        longi=mArgs.getString("longi");
        lati=mArgs.getString("lati");
        Log.e("latituy", lati+"");
        sessionManager=new SessionManager(getActivity());
        HashMap<String,String> data=sessionManager.getUserDetails();
        li_id=data.get(sessionManager.KEY_USER_ID);
        arrival_time = (EditText) v.findViewById(R.id.arrival_time);
        train_no = (EditText) v.findViewById(R.id.train_no);
        loco_id = (EditText) v.findViewById(R.id.loco_id);
        ok=(Button)v.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        return v;
    }

    void submit(){
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Please Wait..");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setProgress(0);
        pd.show();
        StringRequest req=new StringRequest(StringRequest.Method.POST, Config.SeverUrl+"arrival_time.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parse(response);
                Log.e("responseaman",response);
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("loco_id", loco_id.getText().toString());
                params.put("li_id", li_id);
                params.put("train_no", train_no.getText().toString());
                params.put("arrival_time", arrival_time.getText().toString());
                params.put("longitude", longi+"");
                params.put("latitude", lati+"");

                Log.e("params",""+params);

                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(req);
    }
    void parse(String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.getString("status").equals("Success")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());

                // set title
                alertDialogBuilder.setTitle("");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Suceessfully Uploaded")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent=new Intent(getActivity(),Main3Activity.class);
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
