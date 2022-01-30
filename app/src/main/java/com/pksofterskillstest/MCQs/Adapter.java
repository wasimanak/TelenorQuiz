package com.pksofterskillstest.MCQs;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.List;

public class Adapter  extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    List<questionsmodel> questionsmodelList;
    Context context;
    String showAdds;

    public Adapter(List<questionsmodel> questionsmodelList, Context context,String showAdds) {
        this.questionsmodelList = questionsmodelList;
        this.context = context;
        this.showAdds=showAdds;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_questionnumber.setText("Poetry#"+(position+1));
        holder.tv_question.setText(questionsmodelList.get(position).getQuestion());
        holder.tv_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    holder.tv_answer.setText(questionsmodelList.get(position).getAnswer());
                }else
                {
                    Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionsmodelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_answer,tv_question,tv_questionnumber;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_questionnumber=itemView.findViewById(R.id.tv_questionnumber);
            tv_question=itemView.findViewById(R.id.tv_question);
            tv_answer=itemView.findViewById(R.id.tv_answer);
        }
    }
    private boolean checkConnection() {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMgr.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (showAdds.equals("true")) {
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(context, context.getString(R.string.interstitial_ad_unit), adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        interstitialAd.show((Activity) context);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                    }
                });
            }
            return true;
            // internet is there.
        } else {
            return false;

            // internet is not there.
        }

    }
}
