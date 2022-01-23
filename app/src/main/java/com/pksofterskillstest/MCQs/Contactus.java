package com.pksofterskillstest.MCQs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.net.URLEncoder;

public class Contactus extends AppCompatActivity {
    Button btn_close;
    public Activity c;
    Dialog d;
    ImageView iv_call,iv_facebook,iv_google,iv_youtube;
    String html=
            "<HTML>"+
                    "<head>"+
                    "</head>"+
                    "<body>"+
                    "<Style='color=#F44336'>This  Email is sender from Surah Mulk App user.</style>\n" +
                    "Contact : <Style='color=#2196F3'>+923477442050</style>\n"+
                    "Address : <Style='color=#2196F3'>Defence Shopping mal DHA phase 1 lahore pakistan </style>\n"+
                    "</body"+
                    "</html>";
    public void CustomDialogCenter(Activity a) {
//        super(a);
        this.c = a;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
        iv_call=findViewById(R.id.iv_call);
        iv_facebook=findViewById(R.id.iv_facebook);
        iv_google=findViewById(R.id.iv_google);
        iv_youtube=findViewById(R.id.iv_youtube);
        btn_close = (Button) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String defultapp = "WhatsApp";

                try {
                    String url = "https://api.whatsapp.com/send?phone=+923477442050" +
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
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("mailto:"+"pksofter@gmail.com"));
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
                String url="https://www.youtube.com/channel/UCTV_XVFjZAX2xg9OwQzf_nA";
                try {
                    intent =new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.google.android.youtube");
                    intent.setData(Uri.parse(url));
                    c.startActivity(intent);
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
                String YourPageURL = "https://www.facebook.com/n/?966338360104991";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));

                startActivity(browserIntent);
            }
        });
    }

}