package com.pksofterskillstest.MCQs;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences=context.getSharedPreferences("Session",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
    public void setFirstRun(boolean firstrun){
        editor.putBoolean("firstrun",firstrun);
        editor.commit();
        editor.apply();
    }
    public boolean getFirstRun(){
        return   sharedPreferences.getBoolean("firstrun",false);
    }

}
