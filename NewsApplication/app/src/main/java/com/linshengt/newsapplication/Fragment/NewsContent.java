package com.linshengt.newsapplication.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.CircularArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.linshengt.newsapplication.Activity.SpecialActivity;
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
import com.linshengt.newsapplication.View.header.PtrMeiTuanHeader;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsContent extends Fragment {

    private String TAG = "NewsContent";
    private View root;
    private Context mContext;
    private CircularArray<DesignItem> mDatas;
    private LoadMoreAdapter mAdapter;
    private String newsTypeCn;
    private String newsTypeEn;
    private boolean isFirstEnter = false;

    private PtrFrameLayout mPtrFrame;
    private RecyclerView mRecyclerView;
    private NewsItemInfoDao newsItemInfoDao;
    private LoadNewsModel loadNewsModel;
    private PtrMeiTuanHeader header;
    private Button btnTop;

    public static NewsContent newInstance(String[] newsType) {
        NewsContent newFragment = new NewsContent();
        Bundle bundle = new Bundle();
        bundle.putString("newsTypeCn", newsType[0]);
        bundle.putString("newsTypeEn", newsType[1]);
        newFragment.setArguments(bundle);
        return newFragment;

    }
    public NewsContent() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.newsTypeCn = args.getString("newsTypeCn");
            this.newsTypeEn = args.getString("newsTypeEn");
        }
        /*初始化第一次进入fragment的数据*/
        mContext = getActivity();
        isFirstEnter = true;
        newsItemInfoDao = new NewsItemInfoDao(mContext);
        loadNewsModel = new LoadNewsModel(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(root == null){
            setTextSize();
            root =  inflater.inflate(R.layout.fragment_news_content, container, false);
        }
        findView();
        if(isFirstEnter == true){
            initData();
            isFirstEnter = false;
        }
        initView();
        return root;
    }

    /*查找控件*/
    private void findView(){
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        mPtrFrame = (PtrFrameLayout) root.findViewById(R.id.view_pager_ptr_frame);
        btnTop = (Button) root.findViewById(R.id.btnTop);
    }

    /*设置控件*/
    private void initView(){
        initRecycleView();
        btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.scrollToPosition(0);
            }
        });
        header = new PtrMeiTuanHeader(mContext);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);

        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public boolean onRefreshBegin(PtrFrameLayout frame) {

                loadNewsModel.getNewsFromNet(newsTypeEn);
                loadNewsModel.setCallback(new LoadNewsModel.callback() {
                    @Override
                    public void getNewsInfo(List<NewsInfo> list) {

                        List<NewsInfo> listTemp = new ArrayList<NewsInfo>();

                        for (int i = list.size()-1; i >= 0; i --){
                            listTemp.add(list.get(i));
                        }

                        configureNewsByNet(listTemp);
                        newsItemInfoDao.addNewsItemInfoList(list);

                        mPtrFrame.refreshComplete();
                    }
                });
                return true;
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return !canChildScrollUp(mRecyclerView);
            }
        });

    }

    private void initRecycleView(){
        mAdapter = new LoadMoreAdapter(mRecyclerView, mDatas, mContext);
        mAdapter.setmItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                HLog.i(TAG, "position:" + postion);
                HLog.i(TAG,  mDatas.get(postion).getTitle());
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
                newsItemInfoDao.loadMoreNewsItemInfoList(newsTypeCn, Long.parseLong(newsItemInfoDao.getdateInt(mDatas.get(mDatas.size()-2).title)), false);
                newsItemInfoDao.setCallback(new NewsItemInfoDao.callback() {
                    @Override
                    public void getNewsInfo(List<NewsInfo> list) {
                        if(list.size() <= 0){
                            mAdapter.setLoadingNoMore();
                        }else{
                            mAdapter.setLoadingComplete();
                        }
                         configureNewsByDao(list);
                    }
                });
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
    }
    /*初始化数据*/
    protected void initData() {
        mDatas = new CircularArray<>();

        newsItemInfoDao = new NewsItemInfoDao(mContext);
        newsItemInfoDao.getNewsItemInfoList(newsTypeCn, false);
        newsItemInfoDao.setInitDataCallback(new NewsItemInfoDao.initDataCallback() {
            @Override
            public void initNewsInfo(List<NewsInfo> list) {
                configureNewsByDao(list);
            }
        });
    }
    private void configureNewsByNet(List<NewsInfo> list){
        int nLen = 0;

        for(int i = 0; i < list.size(); i ++){
            if(list.get(i).getCategory().equals(newsTypeCn)){
                if(true == newsItemInfoDao.isExistField(list.get(i).getTitle())){
                    continue;
                }
                nLen ++;
                mDatas.addFirst(new DesignItem(list.get(i).getAuthor_name(), list.get(i).getTitle(), list.get(i).getThumbnail_pic_s()));
            }
        }

        mAdapter.notifyItemRangeInserted(0,  nLen);
        mRecyclerView.scrollToPosition(0);
    }
    private void configureNewsByDao(List<NewsInfo> list){
        int nLen = 0;

        for(int i = 0; i < list.size(); i ++){
            if(list.get(i).getCategory().equals(newsTypeCn)){
                nLen ++;
                mDatas.addLast(new DesignItem(list.get(i).getAuthor_name(), list.get(i).getTitle(), list.get(i).getThumbnail_pic_s()));
            }
        }
        mAdapter.notifyItemRangeInserted(mDatas.size()-nLen-1,  nLen);
    }
    private void setTextSize(){
        if(PreferenceUtil.readInt(mContext, "CustuomCheckBoxStatus") == 1){
            mContext.setTheme(R.style.littleTextSize);
        }else if(PreferenceUtil.readInt(mContext, "CustuomCheckBoxStatus") == 2){
            mContext.setTheme(R.style.middleTextSize);
        }else if(PreferenceUtil.readInt(mContext, "CustuomCheckBoxStatus") == 3){
            mContext.setTheme(R.style.largeTextSize);
        }else if(PreferenceUtil.readInt(mContext, "CustuomCheckBoxStatus") == 4){
            mContext.setTheme(R.style.SlargeTextSize);
        }
    }
    public void update(){
        HLog.i(TAG, "update");
        mAdapter.notifyDataSetChanged();
    }
}
