package com.example.myapplication.Score;

import android.content.Context;

import com.example.myapplication.Falling.Falling;
import com.example.myapplication.Preferences.SPreferences;
import com.example.myapplication.R;

public class ScoreHandling {
    private static IScoreChange scoreChanged;
    public static int Score = 0;
    public static int bestScore = -1;
    private static Context context;
    private static boolean isInit = false;
    private static boolean newBestScore = false;

    public static void SetScoreChangedEvent(IScoreChange event){
        scoreChanged = event;
    }

    public static boolean isInitialized(){
        return isInit;
    }

    /**
     * If the event is not set before and this method gets used, the best score value wont be shown on screen
     *      Call "SetScoreChangedEvent" before using this method
     * */
    public static void Init(Context ctx){
        context = ctx;
        bestScore = SPreferences.Get(context.getString(R.string.best_score_key), Integer.class, context);

        if(scoreChanged != null){
            scoreChanged.BestScoreChanged(bestScore);
        }

        isInit = true;
    }

    public static void Win(){
        Score++;
        Falling.FallSpeedHandling();
        scoreChanged.ScoreChanged(Score);
        checkBestScore();
    }
    public static void Lose(){
        Score = 0;
        scoreChanged.ScoreChanged(Score);

        if(newBestScore == true){
            newBestScore = false;
           //TODO Update in DB
        }
    }
    private static void checkBestScore(){
        if(Score > bestScore){
            newBestScore = true;
            bestScore = Score;
            SPreferences.Save(context.getString(R.string.best_score_key),bestScore,context);
            scoreChanged.BestScoreChanged(bestScore);
        }
    }
}
