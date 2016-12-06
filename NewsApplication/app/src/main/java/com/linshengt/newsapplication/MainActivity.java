package com.linshengt.newsapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.linshengt.newsapplication.Activity.AboutActicity;
import com.linshengt.newsapplication.Activity.Collection;
import com.linshengt.newsapplication.Activity.TextsizeActivity;
import com.linshengt.newsapplication.Adapter.NewsAdapter;
import com.linshengt.newsapplication.Dao.NewsItemDao;
import com.linshengt.newsapplication.Utils.HLog;
import com.linshengt.newsapplication.Utils.PreferenceUtil;
import com.linshengt.newsapplication.View.CustomCheckBox;
import com.linshengt.newsapplication.View.TitleBar;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends FragmentActivity {

    private String TAG = "MainActivity";
    private TitleBar mTitleBar;
    private TabPageIndicator mTabPagerIndicator;
    private ViewPager mViewPager;
    private SlidingMenu slidingMenu;


    private Context context;
    private List<String[]> newsItemList = new ArrayList<>();
    private FragmentPagerAdapter adapter;

    @Override
    protected void onStart() {
        super.onStart();
        HLog.i(TAG, "onStart");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_main);
        findView();
        initData();
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        HLog.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        HLog.i(TAG, "onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        HLog.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HLog.i(TAG, "onDestroy");
    }


    private void findView(){
        mTabPagerIndicator = (TabPageIndicator) findViewById(R.id.tabPageIndicator);
        mTitleBar = (TitleBar) findViewById(R.id.titleBar);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);


    }

    private void initView(){
        mTitleBar.setCommonTitle(View.VISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
        mTitleBar.setTitleBarTile(getString(R.string.News));
        mTitleBar.setImageLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.toggle(true);
            }
        });

        NewsItemDao newsItemDao = new NewsItemDao(context);
        adapter = new NewsAdapter(getSupportFragmentManager(), newsItemDao.getNewsItemList());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position != 0){
                    slidingMenu.addIgnoredView(mViewPager);
                }else {
                    slidingMenu.removeIgnoredView(mViewPager);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabPagerIndicator.setViewPager(mViewPager);

        initSlidingMenu();

    }

    private void initSlidingMenu(){
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);

        slidingMenu.findViewById(R.id.item_collection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Collection.class);
                startActivity(intent);
            }
        });

        slidingMenu.findViewById(R.id.item_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TextsizeActivity.class);
                startActivity(intent);
            }
        });

        slidingMenu.findViewById(R.id.item_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutActicity.class);
                startActivity(intent);
            }
        });
    }

    private void initData(){

         if(PreferenceUtil.readBoolean(context,"News") == false) {
            String[][] INITNEWSITEM = {{"社会", "shehui"}, {"国内", "guonei"}, {"国际", "guoji"},
                    {"娱乐", "yule"}, {"体育", "tiyu"}, {"军事", "junshi"}};
            NewsItemDao newsItemDao = new NewsItemDao(context);
            for (int i = 0; i < INITNEWSITEM.length; i ++){
                newsItemList.add(INITNEWSITEM[i]);
            }
            newsItemDao.addNewsItemList(newsItemList);
            PreferenceUtil.write(context, "News", true);
        }
    }

}
