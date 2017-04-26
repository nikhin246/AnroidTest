package com.example.nikhin.anroidtest;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.HashMap;

public class OnRemote {
    public static String SERVER_CHOOSER = "http://financeniches.com/apps_backend/dynamic_remote/server_choose.json";
    public static String DATA_URL = "config.json";
    public static ArrayList<String> eClasses = new ArrayList<String>();
    public static ArrayList<HashMap<String, String>> eInstances = new ArrayList<>();

    public static void showAlert(Context c, String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(c);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setNeutralButton("OK", null);
        dialog.create().show();
    }

    public static void showAlertAndGoBack(final Context c, String title,
                                          String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(c);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setNeutralButton("OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity) c).finish();
            }
        });
        try {
            dialog.create().show();
        } catch (Exception e) {

        }
    }

    public static void savePreferences(Context mContext, String key,
                                       String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                "VUESOL", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String loadPreferences(Context mContext, String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                "VUESOL", Context.MODE_PRIVATE);
        String val = "";
        try {
            val = sharedPreferences.getString(key, "");
        } catch (Exception e) {
            val = "";
        }
        return val;
    }

    public static boolean iO() {
        try {
            Process p1 = Runtime.getRuntime().exec("ping -c 1    www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            if (reachable)
                return reachable;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progressdialog);
        return dialog;
    }

}
