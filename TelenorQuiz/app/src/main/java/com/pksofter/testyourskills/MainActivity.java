package com.pksofterskillstest.testyourskills;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.BuildConfig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv_questions;
    KProgressHUD kProgressHUD;
    TextView btn_shareapp,btn_moreapps,btn_rateus,date;
    DatabaseReference databaseReference;
    List<questionsmodel> questionsmodelList;
    AdView mAdView;
    String showadds="false";
    final static int RQS_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        date=findViewById(R.id.date);
        initProgressDialog();
        setAlarm();
        btn_rateus=findViewById(R.id.btn_rateus);
        btn_moreapps=findViewById(R.id.btn_moreapps);
        btn_shareapp=findViewById(R.id.btn_shareapp);
        rv_questions=findViewById(R.id.rv_questions);
      initView();
      btn_moreapps.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              moreapps();
          }
      });
      btn_rateus.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intentRateUs = new Intent(Intent.ACTION_VIEW);
              intentRateUs.setData(Uri.parse("market://details?id=" + getPackageName()));
              startActivity(intentRateUs);
          }
      });
      btn_shareapp.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              Intent intentShare = new Intent(Intent.ACTION_SEND);
              intentShare.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
              intentShare.setType("text/plain");
              startActivity(Intent.createChooser(intentShare,"settings_share"));
          }
      });


        getDataFromDB();
    }


    private void initView() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMgr.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            AdView mAdMobAdView = (AdView) findViewById(R.id.admob_adview);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            mAdMobAdView.loadAd(adRequest);


            // internet is there.
        } else {


            LinearLayout ades = (LinearLayout) findViewById(R.id.adss);
            ades.setVisibility(View.GONE);

            AdView mAdMobAdView = (AdView) findViewById(R.id.admob_adview);
            mAdMobAdView.setVisibility(View.GONE);
            // internet is not there.
        }

    }
    public void getDataFromDB(){
        kProgressHUD.show();
        questionsmodelList=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("questions");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        questionsmodelList.add(new questionsmodel(
                                dataSnapshot.child("question").getValue(String.class),
                                dataSnapshot.child("correctans").getValue(String.class)
                        ));
                    }
                    checkAddsDB();

                }else {
                    Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void checkAddsDB(){

        kProgressHUD.show();
        databaseReference= FirebaseDatabase.getInstance().getReference("settings");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    showadds=snapshot.child("showadds").getValue(String.class);
                    date.setText(snapshot.child("lastupdate").getValue(String.class));
                    Adapter adapter=new Adapter(questionsmodelList,MainActivity.this,showadds);
                    rv_questions.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    rv_questions.setAdapter(adapter);
                    kProgressHUD.dismiss();
                }else {
                    Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void moreapps() {

        Uri uri = Uri.parse("market://search?q=pub:" + "pksofter");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/search?q=pub:" + "pksofter")));
        }

    }


    private void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0); // MIDNIGHT 12 AM
        calendar.set(Calendar.MINUTE,00);
        calendar.set(Calendar.SECOND, 00);



        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY ,  // EVERY TWO DAYS
                pendingIntent);

    }

    public void initProgressDialog() {
        kProgressHUD = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(4)
                .setBackgroundColor(getResources().getColor(R.color.kprogresshud_default_color))
                .setDimAmount(0.5f);
    }
}




