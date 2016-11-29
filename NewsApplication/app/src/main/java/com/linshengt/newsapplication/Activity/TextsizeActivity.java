package com.linshengt.newsapplication.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.linshengt.newsapplication.Fragment.TextSizeFragment;
import com.linshengt.newsapplication.R;


public class TextsizeActivity extends AppCompatActivity {

    private String TAG = "TextsizeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textsize);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_main, new TextSizeFragment(), null);
        ft.commit();
    }


}
