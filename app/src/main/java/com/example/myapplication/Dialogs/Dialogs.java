package com.example.myapplication.Dialogs;

import android.app.Activity;
import android.content.Context;

import com.example.myapplication.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Dialogs {

    /**
     * Asks the user to log out
     * Cancel and Yes buttons
     * */
    public static void QuitApp(final Context context){
        SweetAlertDialog dialog = new SweetAlertDialog(context,SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setCustomImage(context.getDrawable(R.drawable.ic_quit))
                .setContentText(context.getString(R.string.sureToQuit))
                .setCancelButton("Cancel", null)
                .setConfirmButton("Yes!", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        ((Activity)context).finish();
                    }
                });
        dialog.show();
    }

}
