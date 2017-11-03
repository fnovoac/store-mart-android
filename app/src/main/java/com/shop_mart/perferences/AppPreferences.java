package com.shop_mart.perferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by parallels on 11/3/17.
 */
public class AppPreferences {

    private SharedPreferences sharedPreferences;
    private Context context;

    public AppPreferences(Context context) {
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void putString(String key, String value) {
        try {
            SharedPreferences.Editor prefEdit = sharedPreferences.edit();
            prefEdit.putString(key, value);
            prefEdit.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getString(String key, String defValue) throws ClassCastException {
        return sharedPreferences.getString(key, defValue);
    }

    public void putInt(String key, Integer value) {
        try {
            SharedPreferences.Editor prefEdit = sharedPreferences.edit();
            prefEdit.putInt(key, value);
            prefEdit.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer getInt(String key, Integer defValue) throws ClassCastException {
        return sharedPreferences.getInt(key, defValue);
    }

    public void putLong(String key, Long value) {
        try {
            SharedPreferences.Editor prefEdit = sharedPreferences.edit();
            prefEdit.putLong(key, value);
            prefEdit.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Long getLong(String key, Long defValue) throws ClassCastException {
        return sharedPreferences.getLong(key, defValue);
    }

    public void putBoolean(String key, Boolean defValue) {
        try {
            SharedPreferences.Editor prefEdit = sharedPreferences.edit();
            prefEdit.putBoolean(key, defValue);
            prefEdit.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout(Class clazz) {
        try {
            SharedPreferences.Editor prefEdit = sharedPreferences.edit();
            prefEdit.clear();
            prefEdit.commit();
            Intent intent = new Intent(context, clazz);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean getBoolean(String key, Boolean defValue) throws ClassCastException {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public void putStringSet(String key,List<String> stringList){
        try{
            Set<String> set = new HashSet<>();
            set.addAll(stringList);
            SharedPreferences.Editor preEdit = sharedPreferences.edit();
            preEdit.putStringSet(key,set);
            preEdit.commit();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Set<String> getStringSet(String key,Set<String> defValue){
        return sharedPreferences.getStringSet(key,defValue);
    }

}
