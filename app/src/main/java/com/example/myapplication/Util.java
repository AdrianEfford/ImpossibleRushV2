package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.myapplication.Preferences.SPreferences;

import java.util.Arrays;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Util {
    public static void StartActivity(Context context, Class _class){
        Intent intent = new Intent(context, _class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }


    /**
     * This method creates a new UUID and saves it in the shared preferences
     * */
    public static boolean NewUUID(Context context){
        String UUID = java.util.UUID.randomUUID().toString();
        return SPreferences.Save(context.getString(R.string.UUID),UUID, context);
    }

    /**
     * This method reads the shared preferences and returns the saved value
     * */
    public static String GetUUID(Context context){
        return SPreferences.Get(context.getString(R.string.UUID),String.class, context);
    }

    /**
     * This method returns the actual country without using any permission
     * */
    public static String GetCountry(Context context){
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkCountryIso().toUpperCase(); //returns a ISO 3166 ALPHA-2
    }

    /**
     * Creates a dialog with the defined text
     * IMPORTANT: This method returns a ProgressDialog. You will need the return value to hide the
     *            dialog when e.g a working thread has ended
     *
     * */
    public static ProgressDialog CreateDialog(Context context, String message, boolean cancelable){
        ProgressDialog dialog=new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCancelable(cancelable);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
        return dialog;
    }

    public static void ExceptionOcurred(Exception e){
        GameWindow.Toast("EXCEPTION! Please DEBUG (Should never happen)");
        System.out.println(e.getMessage()); //Set a breakpoint here to see the exception
    }

    public static void HideKeybaord(View v, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }

    public static <T> T[] AddToArray(T[] arr, T value){
        T[] toReturn = Arrays.copyOf(arr, arr.length + 1);
        toReturn[toReturn.length - 1] = value;
        return toReturn;
    }
}
