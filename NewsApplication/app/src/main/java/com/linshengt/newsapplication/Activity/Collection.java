package com.linshengt.newsapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.CircularArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.linshengt.newsapplication.Bean.NewsInfo;
import com.linshengt.newsapplication.Dao.NewsItemInfoDao;
import com.linshengt.newsapplication.Model.LoadNewsModel;
import com.linshengt.newsapplication.R;
import com.linshengt.newsapplication.Utils.HLog;
import com.linshengt.newsapplication.Utils.PreferenceUtil;
import com.linshengt.newsapplication.View.RecyclerView.BaseLoadingAdapter;
import com.linshengt.newsapplication.View.RecyclerView.DesignItem;
import com.linshengt.newsapplication.View.RecyclerView.ItemClickListener;
import com.linshengt.newsapplication.View.RecyclerView.LoadMoreAdapter;
import com.linshengt.newsapplication.View.TitleBar;

import java.util.List;

public class Collection extends AppCompatActivity {

    private String TAG = "Collection";
    private TitleBar mTitleBar;
    private RecyclerView mRecyclerView;
    private NewsItemInfoDao newsItemInfoDao;
    private LoadNewsModel loadNewsModel;
    private Context mContext;
    private LoadMoreAdapter mAdapter;
    private CircularArray<DesignItem> mDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setTextSize();
        setContentView(R.layout.activity_collection);


        HLog.i(TAG, "onCreate-->1");
        findView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        HLog.i(TAG, "onCreate-->2");
        initData();
        HLog.i(TAG, "onCreate-->3");
        initView();
    }

    private void findView(){
        mTitleBar = (TitleBar) findViewById(R.id.titleBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void initData(){
        mDatas = new CircularArray<>();
        newsItemInfoDao = new NewsItemInfoDao(mContext);
        loadNewsModel = new LoadNewsModel(mContext);

        newsItemInfoDao = new NewsItemInfoDao(mContext);
        newsItemInfoDao.getNewsItemInfoList(null, true);
        newsItemInfoDao.setInitDataCallback(new NewsItemInfoDao.initDataCallback() {
            @Override
            public void initNewsInfo(List<NewsInfo> list) {
                HLog.i(TAG, "initNewsInfo");
                configureNewsByDao(list);
            }
        });
    }

    private void initView(){
        mTitleBar.setCommonTitle(View.VISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
        mTitleBar.setTitleBarTile(getString(R.string.CollectionNews));
        mTitleBar.setTv_left(getString(R.string.reverseBack));
        mTitleBar.setOnTv_leftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initRecycleView();
    }
    private void initRecycleView(){
        mAdapter = new LoadMoreAdapter(mRecyclerView, mDatas, mContext);
        mAdapter.setmItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent = new Intent(mContext, SpecialActivity.class);
                NewsItemInfoDao newsItemInfoDao = new NewsItemInfoDao(mContext);
                intent.putExtra("url", newsItemInfoDao.getNewsUrl(mDatas.get(postion).getTitle()));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int postion) {
                Log.i(TAG, "onItemLongClick: ");
            }

        });
        mAdapter.setOnLoadingListener(new BaseLoadingAdapter.OnLoadingListener() {
            @Override
            public void loading() {
                HLog.i(TAG, "" + mDatas.get(mDatas.size()-2).getTitle());
                newsItemInfoDao.loadMoreNewsItemInfoList(null, Long.parseLong(newsItemInfoDao.getdateInt(mDatas.get(mDatas.size()-2).title)), true);
                newsItemInfoDao.setCallback(new NewsItemInfoDao.callback() {
                    @Override
                    public void getNewsInfo(List<NewsInfo> list) {
                        mAdapter.setLoadingComplete();
                        configureNewsByDao(list);

                    }
                });
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
    }
    private void configureNewsByDao(List<NewsInfo> list){
        int nLen = 0;

        HLog.i(TAG, "configure:" + list.size());
        for(int i = 0; i < list.size(); i ++){
                nLen ++;
                mDatas.addLast(new DesignItem(list.get(i).getAuthor_name(), list.get(i).getTitle(), list.get(i).getThumbnail_pic_s()));
        }

        mAdapter.notifyItemRangeInserted(mDatas.size()-nLen-1,  nLen);
    }
    private void setTextSize(){
        if(PreferenceUtil.readInt(mContext, "CustuomCheckBoxStatus") == 1){
            setTheme(R.style.littleTextSize);
        }else if(PreferenceUtil.readInt(mContext, "CustuomCheckBoxStatus") == 2){
            setTheme(R.style.middleTextSize);
        }else if(PreferenceUtil.readInt(mContext, "CustuomCheckBoxStatus") == 3){
            setTheme(R.style.largeTextSize);
        }else if(PreferenceUtil.readInt(mContext, "CustuomCheckBoxStatus") == 4){
            setTheme(R.style.SlargeTextSize);
        }
    }

}
