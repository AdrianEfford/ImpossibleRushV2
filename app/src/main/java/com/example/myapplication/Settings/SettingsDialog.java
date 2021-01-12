package com.example.myapplication.Settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.example.myapplication.Music.MusicPlayer;
import com.example.myapplication.Preferences.SPreferences;
import com.example.myapplication.R;

public class SettingsDialog extends AlertDialog.Builder {

    AlertDialog dialog;
    Activity activity;
    Context context;
    ImageView btnClose;
    ImageView musicSetter,darkmodeSetter;
    boolean darkmodeActive;
    private static ISettingsChanged ISettingsChanged;

    public void setSettingsChangedEvent(ISettingsChanged event){
        this.ISettingsChanged = event;
    }

    public SettingsDialog(Activity activity){
        super( new ContextThemeWrapper(activity, R.style.AppTheme) );
        @SuppressLint("InflateParams") final // It's OK to use NULL in an AlertDialog it seems...
        View dialogLayout = LayoutInflater.from(activity).inflate(R.layout.settings, null);
        setView(dialogLayout);

        this.activity = activity;
        this.context = getContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SetUPViews(dialogLayout);
        }else{
            //TODO toast low version
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void SetUPViews(final View dialogView){
        //Get music and darkMode state from preferences
        MusicPlayer.isMusicActive = SPreferences.Get(context.getString(R.string.keyMusic),Boolean.class,context);
        darkmodeActive = SPreferences.Get(context.getString(R.string.keyDarkmode),Boolean.class,context);

        //Find views
        btnClose = dialogView.findViewById(R.id.btnCloseSettings);
        musicSetter = dialogView.findViewById(R.id.onOffMusic);
        darkmodeSetter = dialogView.findViewById(R.id.onOffDarkmode);

        //Click listeners
        musicSetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSetterClicked();
            }
        });
        darkmodeSetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                darkModeSetterClicked();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Set correct imageview
        //DarkMode
        if(darkmodeActive) darkmodeSetter.setImageDrawable(context.getDrawable(R.drawable.ic_on));
        else  darkmodeSetter.setImageDrawable(context.getDrawable(R.drawable.ic_off));
        //Music
        if(MusicPlayer.isMusicActive) musicSetter.setImageDrawable(context.getDrawable(R.drawable.ic_on));
        else  musicSetter.setImageDrawable(context.getDrawable(R.drawable.ic_off));


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void darkModeSetterClicked(){
        //Set darkMode state
        darkmodeActive = !darkmodeActive;
        if(darkmodeActive) darkmodeSetter.setImageDrawable(context.getDrawable(R.drawable.ic_on));
        else  darkmodeSetter.setImageDrawable(context.getDrawable(R.drawable.ic_off));

        //Check if event is null and raise if not
        if(ISettingsChanged != null) ISettingsChanged.OnDarkModeStateChanged(darkmodeActive);

        //Save in preferences
        SPreferences.Save(context.getString(R.string.keyDarkmode),darkmodeActive,context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void musicSetterClicked(){
        //Set music state
        MusicPlayer.isMusicActive = !MusicPlayer.isMusicActive;
        if(MusicPlayer.isMusicActive) musicSetter.setImageDrawable(context.getDrawable(R.drawable.ic_on));
        else  musicSetter.setImageDrawable(context.getDrawable(R.drawable.ic_off));

        //Check if event is null and raise if not
        ISettingsChanged.OnMusicStateChange(MusicPlayer.isMusicActive);

        //Save in preferences
        SPreferences.Save(context.getString(R.string.keyMusic),MusicPlayer.isMusicActive,context);
    }

    public AlertDialog Load(){
        if(dialog == null) dialog = super.create();
        return dialog;
    }

    @Override
    public AlertDialog show() {
        dialog = super.show();
        Window window = dialog.getWindow();
        if( window != null )
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return dialog;
    }
}
