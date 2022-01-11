package com.example.erreparseparatas;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.erreparseparatas.databinding.ActivityFullscreenBinding;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            VideoView videoHolder = new VideoView(this);
            setContentView(videoHolder);
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash);
            videoHolder.setVideoURI(video);

            videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    jump();
                }
            });

            MediaController mController = new MediaController(this);
            videoHolder.setMediaController(mController);
            mController.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    int width = videoHolder.getMeasuredWidth();
                    int height = videoHolder.getMeasuredHeight();
                    //we add 10 pixels to the current size of the video view every time you touch
                    //the media controller.
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width+10, height+10);
                    videoHolder.setLayoutParams(params);

                    return true;
                }
            });
            videoHolder.start();
        } catch (Exception ex) {
            jump();
        }
    }

    private void jump() {
        if (isFinishing())
            return;
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}