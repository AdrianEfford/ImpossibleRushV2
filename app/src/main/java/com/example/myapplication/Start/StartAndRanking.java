package com.example.myapplication.Start;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.myapplication.GameWindow;
import com.example.myapplication.R;

public class StartAndRanking extends RelativeLayout {
    private Context context;
    private IStart startEvent;
    private TextView startTV;
    private Activity activity;

    public void setEvent(IStart event){
        this.startEvent = event;
    }
    public StartAndRanking(Context context) {
        super(context);
        init(context);
    }

    public StartAndRanking(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(final Context context) {
        this.context = context;
        activity = (Activity)context;
        LayoutInflater.from(context).inflate(R.layout.startbutton, this);
        final Animation startScale = AnimationUtils.loadAnimation(getContext(),R.anim.scalestart);

        Typeface font = ResourcesCompat.getFont(context, R.font.startfontblack);
        startTV = findViewById(R.id.startTV);
        startTV.setTypeface(font);

        startTV.startAnimation(startScale);



        final RelativeLayout startLayout = findViewById(R.id.startlayout);
        startLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation zoomOut = AnimationUtils.loadAnimation(getContext(),R.anim.zoom_out);
                zoomOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        //
                        GameWindow.backgroundMusicMenu.StopMusic();
                        GameWindow.backgroundMusicGame.StartMusic();
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        startEvent.startGame();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        //
                    }
                });

                startTV.startAnimation(zoomOut);
                startLayout.setOnClickListener(null);
            }
        });
    }
}
