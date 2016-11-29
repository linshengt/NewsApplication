package com.linshengt.newsapplication.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.linshengt.newsapplication.R;
import com.linshengt.newsapplication.View.TitleBar;


public class AboutActicity extends AppCompatActivity {
    private TitleBar titleBar;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_acticity);
        findView();
        initView();
    }

    private void findView(){
        titleBar = (TitleBar) findViewById(R.id.titleBar);
    }
    private void initView(){
        titleBar.setCommonTitle(View.VISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
        titleBar.setTitleBarTile(getString(R.string.about));
        titleBar.setTv_left(getString(R.string.reverseBack));

        titleBar.setOnTv_leftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
