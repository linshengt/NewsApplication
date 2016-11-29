package com.linshengt.newsapplication.Activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.linshengt.newsapplication.MainActivity;
import com.linshengt.newsapplication.R;
import com.linshengt.newsapplication.View.AdhesionHorialProgress;


public class Welcome extends AppCompatActivity {

    private AdhesionHorialProgress mAdhesionHorialProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        findView();
        initView();

    }

    private void findView(){
        mAdhesionHorialProgress = (AdhesionHorialProgress) findViewById(R.id.AdhesionHorialProgress);
    }

    private void initView(){

        mAdhesionHorialProgress.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Welcome.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}
