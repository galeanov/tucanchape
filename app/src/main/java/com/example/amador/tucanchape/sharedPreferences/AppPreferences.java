package com.example.amador.tucanchape.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class AppPreferences {

    private static final String KEY_USER_AUTH = "user_auth";
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_REGISTER_EMPRESA = "register_empresa";
    private static final String KEY_SPLASH = "splash";

    private SharedPreferences prefs;
    private Gson gson;

    public AppPreferences(Context ctx) {
        prefs = ctx.getSharedPreferences("appPreference", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void setUserAuth(boolean val) {
        prefs.edit().putBoolean(KEY_USER_AUTH, val).apply();
    }

    public boolean getUserAuth() {
        return prefs.getBoolean(KEY_USER_AUTH,false);
    }

    public void setSplash(boolean val) {
        prefs.edit().putBoolean(KEY_SPLASH, val).apply();
    }

    public boolean getSplash() {
        return prefs.getBoolean(KEY_SPLASH,false);
    }

    public void setRegisterEmpresa(boolean val) {
        prefs.edit().putBoolean(KEY_REGISTER_EMPRESA, val).apply();
    }

    public boolean getRegisterEmpresa() {
        return prefs.getBoolean(KEY_REGISTER_EMPRESA,false);
    }

    public void setUserType(String val) {
        prefs.edit().putString(KEY_USER_TYPE, val).apply();
    }

    public String getUserType() {
        return prefs.getString(KEY_USER_TYPE,null);
    }

    public void logout(){
        setRegisterEmpresa(false);
        setUserType(null);
        setUserAuth(false);
    }
}
