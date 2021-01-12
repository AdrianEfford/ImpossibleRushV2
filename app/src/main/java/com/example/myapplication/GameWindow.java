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

import com.example.myapplication.Falling.INotifyGame;
import com.example.myapplication.Score.IScoreChange;
import com.example.myapplication.Settings.ISettingsChanged;

import java.net.MalformedURLException;
import java.net.URL;

public class GameWindow extends AppCompatActivity implements INotifyGame, IScoreChange, ISettingsChanged {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    @Override
    public void lose() {

    }

    @Override
    public void win() {

    }

    @Override
    public void ScoreChanged(int score) {

    }

    @Override
    public void BestScoreChanged(int score) {

    }

    @Override
    public void OnDarkModeStateChanged(boolean activ) {

    }

    @Override
    public void OnMusicStateChange(boolean activ) {

    }
}