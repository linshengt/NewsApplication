package com.linshengt.newsapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.linshengt.newsapplication.Dao.NewsItemInfoDao;
import com.linshengt.newsapplication.R;
import com.linshengt.newsapplication.Utils.HLog;
import com.linshengt.newsapplication.Utils.PreferenceUtil;
import com.linshengt.newsapplication.View.TitleBar;

public class SpecialActivity extends AppCompatActivity {

    private String url;
    private WebView mWebview;
    private TitleBar mTitleBar;
    private NewsItemInfoDao newsItemInfoDao;
    private Context mContext;
    private TextView mTextView;
    private WebSettings settings;
    private String TAG = "SpecialActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        mContext = this;
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        HLog.i(TAG, url);
        findView();
        initView();
    }

    private void findView(){
        mWebview = (WebView) findViewById(R.id.webView);
        mTitleBar = (TitleBar) findViewById(R.id.titleBar);
        mTextView = (TextView) findViewById(R.id.textView);
    }

    private void initView(){
        mWebview.setVisibility(View.INVISIBLE);
        mWebview.loadUrl(url);
        mWebview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress == 100){
                    mWebview.setVisibility(View.VISIBLE);
                    mTextView.setVisibility(View.INVISIBLE);
                }

            }
        });
        settings = mWebview.getSettings();
        if(PreferenceUtil.readInt(mContext, "CustuomCheckBoxStatus") == 1){
            settings.setTextSize(WebSettings.TextSize.SMALLEST);
        }else if(PreferenceUtil.readInt(mContext, "CustuomCheckBoxStatus") == 2){
            settings.setTextSize(WebSettings.TextSize.SMALLER);
        }else if(PreferenceUtil.readInt(mContext, "CustuomCheckBoxStatus") == 3){
            settings.setTextSize(WebSettings.TextSize.NORMAL);
        }else if(PreferenceUtil.readInt(mContext, "CustuomCheckBoxStatus") == 4){
            settings.setTextSize(WebSettings.TextSize.LARGER);
        }

        settings.setSupportZoom(true);


        newsItemInfoDao = new NewsItemInfoDao(mContext);
        if(0 == newsItemInfoDao.getNewsCollectionByUrl(url)){
            mTitleBar.setImage_right(false);
        }else{
            mTitleBar.setImage_right(true);
        }
        mTitleBar.setCommonTitle(View.VISIBLE, View.VISIBLE, View.INVISIBLE, View.VISIBLE);
        mTitleBar.setTitleBarTile(getString(R.string.CollectionNews));
        mTitleBar.setTv_left(getString(R.string.reverseBack));
        mTitleBar.setOnTv_leftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitleBar.setImage_rightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(0 == newsItemInfoDao.getNewsCollectionByUrl(url)){
                    newsItemInfoDao.setNewsCollection(1, url);
                    mTitleBar.setImage_right(true);
                }else{
                    newsItemInfoDao.setNewsCollection(0, url);
                    mTitleBar.setImage_right(false);
                }
            }
        });
    }

}
