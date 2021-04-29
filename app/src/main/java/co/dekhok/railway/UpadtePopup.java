package co.dekhok.railway;

import android.app.ActivityManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by dekho on 09/10/2019.
 */

public class UpadtePopup extends DialogFragment {
    private UpadtePopup.Listener mListener;
    Button ok;

    public void setListener(UpadtePopup.Listener listener) {
        mListener = listener;
    }

    public static interface Listener {
        void returnData();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.update_popup, container, false);
        ok=(Button)v.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=co.dekhok.railway"));
                startActivity(intent);


            }
        });
        return v;
    }

}
