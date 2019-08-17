package com.example.things;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.daimajia.androidanimations.library.Techniques;
import com.spark.submitbutton.SubmitButton;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class MainActivity extends AwesomeSplash {

    @Override
    public void initSplash(ConfigSplash configSplash) {

        // circular reveal customization
        configSplash.setBackgroundColor(R.color.black);
        configSplash.setAnimCircularRevealDuration(2000);
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);

        //customise logo
        configSplash.setLogoSplash(R.mipmap.splashlogo);
        configSplash.setAnimLogoSplashDuration(2000);
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn);
        configSplash.setTitleTextColor(R.color.black);
        configSplash.setAnimTitleDuration(1000);

    }

    @Override
    public void animationsFinished() {

        setContentView(R.layout.activity_main);

        SubmitButton getStartedButton = findViewById(R.id.getStartedButton);
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CountDownTimer(3000, 1000) {
                    public void onFinish() {
                        openLoginActivity();
                    }

                    public void onTick(long millisUntilFinished) {
                    }
                }.start();
            }
        });
    }

    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}


