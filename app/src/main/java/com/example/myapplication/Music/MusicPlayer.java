package com.example.myapplication.Music;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.myapplication.Preferences.SPreferences;
import com.example.myapplication.R;

public class MusicPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener{
    MediaPlayer mPlayer;
    boolean isRunning = false;
    boolean isPaused = false;
    Integer playingFile;
    private static Context thisContext;
    float volume;
    public static boolean isMusicActive;

    /**Constructor
     * Set file to play and volume*/
    public MusicPlayer(Context context, int rawFile, float vol){
        isMusicActive = SPreferences.Get(context.getString(R.string.keyMusic_active),Boolean.class,context);
        thisContext = context;
        playingFile = rawFile;
        volume = vol;
        mPlayer = MediaPlayer.create(thisContext,playingFile);
        SetVolume(vol);
    }

    /**BEGIN: Getters*/
    public boolean isRunning(){return isRunning;}
    public boolean isPaused(){return isPaused;}
    public Integer getFilePlaying(){return playingFile;}
    public float getVolume(){return volume;}
    /**END: Getters*/

    /**Start playing music*/
    public boolean StartMusic(){
        if(!isRunning && isMusicActive){ //music is not playing and music setting is active
            mPlayer.setOnCompletionListener(this);
            mPlayer.setOnErrorListener(this);
            mPlayer.start();
            isRunning = true;
            return true;
        }
        return false;
    }

    /**Start music with a complete listener*/
    public boolean StartMusic(MediaPlayer.OnCompletionListener OnComplete){
        if(!isRunning && isMusicActive){ //if music is not running and music settings is active
            mPlayer.setOnCompletionListener(OnComplete);
            mPlayer.setOnErrorListener(this);
            mPlayer.start();
            isRunning = true;
            return true;
        }
        return false;
    }

    /**Stops the music*/
    public boolean StopMusic(){
        if(isRunning){ //only if the music is running
            mPlayer.setOnCompletionListener(null); //set event to null
            mPlayer.setOnErrorListener(null); //set event to null
            mPlayer.stop(); //stop music
            mPlayer = MediaPlayer.create(thisContext,playingFile); //create new music player

            //set running variables to false
            isRunning = false;
            isPaused = false;
            return true;
        }
        return false;
    }


    /**Pauses the music so you can resume it later*/
    public boolean PauseMusic(){
        if(isRunning && !isPaused && isMusicActive){ //pause if music is running and music isn't paused already and music setting is active
            mPlayer.pause(); //pause the music
            isPaused = true; //set paused to true
            return true;
        }
        return false;
    }

    /**Resumes the music after a stop*/
    public boolean ResumeMusic(){
        if(isRunning && isPaused && isMusicActive){ //resume if music is running but paused and music setting is active
            int actualPos = mPlayer.getCurrentPosition(); //get position the music stopped
            mPlayer.seekTo(actualPos); //go to the position the music stopped
            mPlayer.start(); //start music
            return true;
        }
        return false;
    }

    /**Reset music (reset song)*/
    public boolean ResetMusic(){
        mPlayer.reset();
        return true;

    }

    /**@annotation: Set vol between 0 and 1*/
    public void SetVolume(float volume){
        this.volume = volume;
        mPlayer.setVolume(volume,volume);
    }

    /**On music complete*/
    @Override
    public void onCompletion(MediaPlayer mp) {
        isRunning = false; //set running to false because music ended
        if(playingFile != null){ //if the file is still saved
            StartMusic(); //start playing again
        }
    }

    /**On error occurred*/
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return ResetMusic(); //reset the music
    }
}
