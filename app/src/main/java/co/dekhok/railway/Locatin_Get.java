package co.dekhok.railway;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Locatin_Get extends AppCompatActivity {
    LocationHelper.LocationResult locationResult;
    LocationHelper locationHelper;
    Button btnMyLocation;
    TextView tvMyLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locatin__get);
        btnMyLocation = (Button) findViewById(R.id.btnMyLocation);
        tvMyLocation = (TextView) findViewById(R.id.tvMyLocation);
        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager mlocManager = null;
                LocationListener mlocListener;
                mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                mlocListener = new MyLocationListener();
                if (ActivityCompat.checkSelfPermission(Locatin_Get.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Locatin_Get.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

                if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if(MyLocationListener.latitude>0)
                    {
                        tvMyLocation.append("Latitude:- " + MyLocationListener.latitude + '\n');
                        tvMyLocation.append("Longitude:- " + MyLocationListener.longitude + '\n');
                        Log.e("longi",tvMyLocation+"");
                    }
                    else
                    {
                       /* alert.setTitle("Wait");
                        alert.setMessage("GPS in progress, please wait.");
                        alert.setPositiveButton("OK", null);
                        alert.show();*/
                    }
                } else {
                    tvMyLocation.setText("GPS is not turned on...");
                }

            }
        });

    }
}
