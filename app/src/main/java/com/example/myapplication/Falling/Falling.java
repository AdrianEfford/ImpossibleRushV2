package com.example.myapplication.Falling;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.GameWindow;
import com.example.myapplication.R;
import com.example.myapplication.Start.IStart;
import com.example.myapplication.Start.StartAndRanking;

import java.util.Random;

import static androidx.core.content.ContextCompat.getDrawable;

public class Falling extends RelativeLayout implements IStart {

    private static Context context;
    private StartAndRanking startButton;
    private static int FallAnimDuration;
    private int _rotateSquareDuration;
    private int _colorOnTop;
    private RelativeLayout square;
    private int startDegrees;
    private int endDegrees;
    private INotifyGame event;
    Colors ballColor;
    Colors topColor;
    TextView color_four, color_three, color_two, color_one;

    public void setEvent(INotifyGame event){
        this.event = event;
    }

    public Falling(Context context) {
        super(context);
        init(context);
    }

    public Falling(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        topColor = Colors.blue;
        ballColor = Colors.blue;
        FallAnimDuration = context.getResources().getInteger(R.integer.fallAnimation_duration);
        _rotateSquareDuration = context.getResources().getInteger(R.integer.rotateSquare_duration);
        _colorOnTop = 0;
        endDegrees = 0;
        startDegrees = -90;

        Animation loadAnim = AnimationUtils.loadAnimation(context, R.anim.scalestart);
        LayoutInflater.from(context).inflate(R.layout.falling, this);
        square = findViewById(R.id.squareLayout);

        square.startAnimation(loadAnim);

        color_four = findViewById(R.id.pink);
        color_three = findViewById(R.id.green);
        color_two = findViewById(R.id.yellow);
        color_one = findViewById(R.id.blue);


        startButton = findViewById(R.id.startButton);
        startButton.setEvent(this);

    }

    private void RotateSquare(){

        startDegrees+=90;
        endDegrees+=90;

        if(endDegrees == 360){
            startDegrees = -90;
            endDegrees = 0;
            _colorOnTop = -1;
        }

        _colorOnTop++;
        topColor = Colors.values()[_colorOnTop];


        RotateAnimation rotate = new RotateAnimation(startDegrees, endDegrees, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(0);
        rotate.setFillAfter(true);
        rotate.setDuration(_rotateSquareDuration);

        square.startAnimation(rotate);

    }

    private void FallAnimation(){

        final TextView ball = findViewById(R.id.ball);
        ball.setBackground(RandomBall());

        float middle = getResources().getDisplayMetrics()
                .heightPixels / 2 - 100;


        TranslateAnimation animation = new TranslateAnimation(0,0,0,middle);
        animation.setFillAfter(false);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(FallAnimDuration);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //do nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(ballColor == topColor){
                    FallAnimation();
                    event.win();
                }else{
                    event.lose();
                    explode(square);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //do nothing
            }
        });
        ball.startAnimation(animation);

    }
    private Drawable RandomBall(){
        int pick = new Random().nextInt(Colors.values().length);
        Colors randomColorEnum = Colors.values()[pick];

        switch (randomColorEnum){
            case blue:
                ballColor = Colors.blue;
                return getDrawable(context, R.drawable.blue_ballshape);

            case pink:
                ballColor = Colors.pink;
                return getDrawable(context, R.drawable.pink_ballshape);

            case green:
                ballColor = Colors.green;
                return getDrawable(context, R.drawable.green_ballshape);

            case yellow:
                ballColor = Colors.yellow;
                return getDrawable(context, R.drawable.yellow_ballshape);
            default:
                //Throw exception
                return null;


        }

    }

    @Override
    public void startGame() {
        startButton.setVisibility(GONE);
        final RelativeLayout myLayout = findViewById(R.id.mylayout);
        myLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RotateSquare();
            }
        });
        FallAnimation();

    }
    private void restart(){
        removeAllViews();
        init(context);
    }
    private void explode(View v){
        GameWindow.backgroundMusicGame.StopMusic();
        GameWindow.explosionSound.StartMusic(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                GameWindow.backgroundMusicMenu.StartMusic();
            }
        });
        final ExplosionField explosionField = ExplosionField.attach2Window((Activity)context);
        explosionField.explode(v);
        restart();
    }

    public static void FallSpeedHandling(){
        if(FallAnimDuration > context.getResources().getInteger(R.integer.minFallSpeed)){
            FallAnimDuration -= 10;
        }
    }
}
