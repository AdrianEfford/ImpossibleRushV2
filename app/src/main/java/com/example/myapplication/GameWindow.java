package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Dialogs.Dialogs;
import com.example.myapplication.Falling.Falling;
import com.example.myapplication.Falling.INotifyGame;
import com.example.myapplication.Music.MusicPlayer;
import com.example.myapplication.Preferences.SPreferences;
import com.example.myapplication.Score.IScoreChange;
import com.example.myapplication.Score.ScoreHandling;
import com.example.myapplication.Settings.ISettingsChanged;
import com.example.myapplication.Settings.SettingsDialog;

import java.net.MalformedURLException;
import java.net.URL;

import tyrant.explosionfield.BuildConfig;

public class GameWindow extends AppCompatActivity implements INotifyGame, IScoreChange, ISettingsChanged {

    Falling playField;
    TextView score, bestScore, startButton, version;
    RelativeLayout gameLayout;
    private static Context context;
    public static Activity activity;

    public static MusicPlayer backgroundMusicGame;
    public static MusicPlayer backgroundMusicMenu;
    public static MusicPlayer explosionSound;

    private SettingsDialog _settingsDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_main);

        context = this;
        activity = this;

        _settingsDialog = new SettingsDialog(activity);
        _settingsDialog.setSettingsChangedEvent(this);
        playField = findViewById(R.id.playfield);
        score = findViewById(R.id.score);
        bestScore = findViewById(R.id.bestScore);
        gameLayout = findViewById(R.id.mainGameLayout);
        startButton = findViewById(R.id.startTV);
        version = findViewById(R.id.version);
        playField.setEvent(this);

        ScoreHandling.SetScoreChangedEvent(this);
        ScoreHandling.Init(this);

        checkRunState();

        //Set Version text
        String currentVersionName = BuildConfig.VERSION_NAME;
        version.setText("V. " + currentVersionName);

        //Initialize music/sounds
        backgroundMusicMenu = new MusicPlayer(context, R.raw.background_menu,1f);
        backgroundMusicGame = new MusicPlayer(context, R.raw.background_game,1f);
        explosionSound = new MusicPlayer(context, R.raw.explosion,0.1f);

        //Start Menu music
        backgroundMusicMenu.StartMusic();

        //Music and DarkMode setter
        OnDarkModeStateChanged((boolean) SPreferences.Get(context.getString(R.string.keyDarkmode),Boolean.class,context));
        //OnMusicStateChange((boolean)SPreferences.Get(context.getString(R.string.keyMusic), Boolean.class,context)); //Already done in the "onResume"-Method
    }

    @Override
    public void lose() {
        //END GAME
        ScoreHandling.Lose();
    }

    @Override
    public void win() {
        //ADD POINT
        ScoreHandling.Win();
    }

    @Override
    public void ScoreChanged(int score) {
        this.score.setText(getString(R.string.score_text) + score);
    }

    @Override
    public void BestScoreChanged(int score) {
        this.bestScore.setText(getString(R.string.bestScore_text) + score);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                AlertDialog alertDialog = _settingsDialog.Load();
                alertDialog.show();
            default:
                Log.i("Not implemented", "Clicked menu item no implemented");

        }
        return true;
    }

    private void checkRunState() {

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        int savedVersionCode = SPreferences.Get(getString(R.string.version_key),Integer.class,getApplicationContext());

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            return;

        } else if (savedVersionCode == getResources().getInteger(R.integer.defaultInteger)) {

            //This is a new install (or the user cleared the shared preferences)
            Util.NewUUID(this);

        } else if (currentVersionCode > savedVersionCode) {
            // TODO This is an upgrade
            //Welcome to the version ...
        }

        // Update the shared preferences with the current version code
        SPreferences.Save(getString(R.string.version_key),currentVersionCode,getApplicationContext());
        SPreferences.Save(getString(R.string.keyMusic),true,context);
        SPreferences.Save(getString(R.string.keyDarkmode),false,context);
    }
    public static void Toast(final String message){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                toast.setMargin(0f,0.05f); //horizontal and vertical margin
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        OnMusicStateChange(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        OnMusicStateChange(true);
    }

    @SuppressLint("NewApi")
    @Override
    public void OnDarkModeStateChanged(boolean activ) {
        //set darkMode state
        int bgColor;
        int fontColor;
        if(activ){
            bgColor = context.getColor(R.color.bg_darkMode_dark);
            fontColor = context.getColor(R.color.font_darkMode_dark);
        }else {
            bgColor = context.getColor(R.color.bg_darkMode_bright);
            fontColor = context.getColor(R.color.font_darkMode_bright);
        }

        score.setTextColor(fontColor);
        bestScore.setTextColor(fontColor);
        startButton.setTextColor(fontColor);
        version.setTextColor(fontColor);
        gameLayout.setBackgroundColor(bgColor);

        Log.i("DarkModeStateChanged: ", "activ: " + activ);
    }

    @Override
    public void OnMusicStateChange(boolean activ) {
        //set music state
        if(!activ){
            backgroundMusicMenu.StopMusic();
            backgroundMusicGame.StopMusic();
            explosionSound.StopMusic();
        }else backgroundMusicMenu.StartMusic();

        Log.i("MusicStateChanged: ", "activ: " + activ);
    }

    @Override
    public void onBackPressed() {
        Dialogs.QuitApp(context);
    }

}