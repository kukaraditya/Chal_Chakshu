package co.dekhok.railway;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,TimeDialog.Listener {
    ImageView lp,alp;
    SessionManager sessionManager;

    String loco_id,lp_name,alp_id,alp_name;
    TextView li_name;
    String li_na;
    String LP,Alp;
    int flag=1;
    int flag_new=2;
    Button time_popup;
    String versionName;
    TextView ver;

    String train_no, name,questionNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent i = getIntent();
        i.getExtras();
        Bundle b = i.getExtras();
        loco_id = b.getString("id");
        name = b.getString("name");
        train_no = b.getString("train_no");
        questionNo = b.getString("questionNo");

        versionName = BuildConfig.VERSION_NAME;
        li_name=(TextView)findViewById(R.id.textView5);
        ver=(TextView)findViewById(R.id.version);
        ver.setText("V-"+ versionName);
        sessionManager=new SessionManager(this);
        HashMap<String, String> data=sessionManager.getUserDetails();
        li_na=data.get(sessionManager.KEY_USER_NAME);
        time_popup=(Button)findViewById(R.id.button2);
        Log.e("li_na",li_na);
       // li_name.setText(li_na);
        LP= String.valueOf(flag);
        Alp= String.valueOf(flag_new);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);



       /* Intent i = getIntent();
        i.getExtras();
        Bundle b =  i.getExtras();
        loco_id = b.getString("id");
        lp_name = b.getString("name");
        alp_id = b.getString("alp_id");
        alp_name = b.getString("alp_name");*/
        lp=(ImageView)findViewById(R.id.lp);
        alp=(ImageView)findViewById(R.id.alp);
        lp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main2Activity.this,Question.class);
                intent.putExtra("id", loco_id);
                intent.putExtra("name", name);
                intent.putExtra("train_no", train_no);
                intent.putExtra("questionNo", questionNo);
                startActivity(intent);
            }
        });

        alp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main2Activity.this,AlpForm.class);
                intent.putExtra("alp",Alp);
                /*intent.putExtra("name",alp_name);
                intent.putExtra("id",alp_id);*/
                startActivity(intent);

            }
        });

        time_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("editDilaogue1");

                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                TimeDialog dialogFragment = new TimeDialog();
                dialogFragment.setListener(Main2Activity.this);
                Bundle bundle = new Bundle();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(ft, "editDilaogue1");
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
       // li_name.setText(li_na);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logout) {
            sessionManager.logoutUser();
            Intent intent=new Intent(Main2Activity.this,MainActivity.class);
            startActivity(intent);
            finish();
            // Handle the camera action
        }else if (id==R.id.about){
            Intent inten=new Intent(Main2Activity.this,About.class);
            startActivity(inten);
        }else if (id==R.id.report){
            Intent intentt=new Intent(Main2Activity.this,LiReport.class);
            startActivity(intentt);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void returnData() {

    }
}
