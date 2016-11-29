package com.linshengt.newsapplication.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;


import com.linshengt.newsapplication.Bean.NewsInfo;
import com.linshengt.newsapplication.Utils.HLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by linshengt on 2016/9/9.
 */
public class NewsItemInfoDao {
    private String TAG = "NewsItemInfoDao";
    private Context context;
    private NewsItemInfoSqlHelper newsItemInfoSqlHelper;
    private final int REPEAT_MSG = 0x01;
    private final int CALLBACK_LOADMORE_OK = 0x02;
    private final int CALLBACK_INIT_OK = 0x03;
    private final int CALLBACK_ERROR = 0x04;

    public NewsItemInfoDao(Context context) {
        this.context = context;
        newsItemInfoSqlHelper = new NewsItemInfoSqlHelper(context);
    }

    public void addNewsItemInfoList(List<NewsInfo> list){
        HLog.i(TAG, "addNewsItemInfoList-->1");
        SQLiteDatabase db = newsItemInfoSqlHelper.getWritableDatabase();

        for (int i = 0; i < list.size(); i ++){
            if (isExistField(list.get(i).getTitle())== true){
                continue;//跳过已经存在的记录
            }
            ContentValues cv = new ContentValues();
            cv.put("title", list.get(i).getTitle());
            cv.put("date", list.get(i).getDate());
            cv.put("dateInt", DataStirngToInt(list.get(i).getDate()));
            cv.put("author_name", list.get(i).getAuthor_name());
            cv.put("thumbnail_pic_s", list.get(i).getThumbnail_pic_s());
            cv.put("url", list.get(i).getUrl());
            cv.put("category", list.get(i).getCategory());
            cv.put("collection", list.get(i).getCollection());

            db.insert("NEWSITEMINFO", null, cv);
        }
        HLog.i(TAG, "addNewsItemInfoList-->4");
    }
    public boolean isExistField(String str){
        SQLiteDatabase db = newsItemInfoSqlHelper.getReadableDatabase();
        String sql = String.format("SELECT * FROM NEWSITEMINFO WHERE title = '%s'", str);
        Cursor c = db.rawQuery(sql, null);

        while (c.moveToNext()){
            if(c.getString(c.getColumnIndex("title")).equals(str)){
                return true;
            }
        }
        return false;
    }
    public void getNewsItemInfoList(final String newsType, final boolean isColllection){
//        return  sortNewsItem(list);

        new Thread(new Runnable() {

            @Override
            public void run() {
                HLog.i(TAG, "getNewsItemInfoList-->date");
                String sql = null;

                if(isColllection == false){
                    sql = String.format("SELECT * FROM NEWSITEMINFO WHERE category='%s' ORDER BY dateInt DESC LIMIT 0, 7", newsType);
                }else{
                    sql = String.format("SELECT * FROM NEWSITEMINFO WHERE collection = 1 ORDER BY dateInt DESC LIMIT 0, 7");
                }


                SQLiteDatabase db = newsItemInfoSqlHelper.getReadableDatabase();
                Cursor c = db.rawQuery(sql, null);
                List<NewsInfo>  list = new ArrayList<>();

                while (c.moveToNext()){
                    NewsInfo newsInfo = new NewsInfo();
                    newsInfo.setTitle(c.getString(c.getColumnIndex("title")));
                    HLog.i(TAG, "title:"+c.getString(c.getColumnIndex("dateInt")));
                    newsInfo.setDate(c.getString(c.getColumnIndex("date")));
                    newsInfo.setAuthor_name(c.getString(c.getColumnIndex("author_name")));
                    newsInfo.setThumbnail_pic_s(c.getString(c.getColumnIndex("thumbnail_pic_s")));
                    newsInfo.setUrl(c.getString(c.getColumnIndex("url")));
                    newsInfo.setCategory(c.getString(c.getColumnIndex("category")));
                    list.add(newsInfo);
                }
                HLog.i(TAG, "getNewsItemInfoList-->1");
                HLog.i(TAG, "list.size:" + list.size());
                handler.sendMessage(handler.obtainMessage(CALLBACK_INIT_OK, list));
            }
        }).start();

    }
    public void loadMoreNewsItemInfoList(final String newsType, final long date, final boolean isCollection){
        HLog.i(TAG, "loadMoreNewsItemInfoList-->date" + date);

        String s = String.valueOf(date);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sql = null;
                if(isCollection == false){
                    sql = String.format("SELECT * FROM NEWSITEMINFO WHERE category='%s'  AND dateInt < %s ORDER BY dateInt DESC LIMIT 0, 7", newsType, String.valueOf(date));
                }else {
                    sql = String.format("SELECT * FROM NEWSITEMINFO WHERE collection= 1  AND dateInt < %s ORDER BY dateInt DESC LIMIT 0, 7", String.valueOf(date));
                }

                SQLiteDatabase db = newsItemInfoSqlHelper.getReadableDatabase();
                Cursor c = db.rawQuery(sql, null);
                List<NewsInfo>  list = new ArrayList<>();
                while (c.moveToNext()){
                    NewsInfo newsInfo = new NewsInfo();
                    newsInfo.setTitle(c.getString(c.getColumnIndex("title")));
                    newsInfo.setDate(c.getString(c.getColumnIndex("date")));
                    newsInfo.setAuthor_name(c.getString(c.getColumnIndex("author_name")));
                    newsInfo.setThumbnail_pic_s(c.getString(c.getColumnIndex("thumbnail_pic_s")));
                    newsInfo.setUrl(c.getString(c.getColumnIndex("url")));
                    newsInfo.setCategory(c.getString(c.getColumnIndex("category")));
                    list.add(newsInfo);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(handler.obtainMessage(CALLBACK_LOADMORE_OK, list));
            }
        }).start();
    }
    public List<NewsInfo> sortNewsItem(List<NewsInfo> list){
        for (int i = 0; i < list.size(); i ++){
            for (int j = i+1; j < list.size(); j ++){
                if(DataStirngToInt(list.get(i).getDate()) < DataStirngToInt(list.get(j).getDate())){
                    Collections.swap(list, i, j);
                }
            }
        }
        return list;
    }
    private long DataStirngToInt(String str){

        String strTime = String.format("%s%s%s%s%s", str.substring(2,4), str.substring(5, 7), str.substring(8,10), str.substring(11, 13), str.substring(14, 16));

        return Long.parseLong(strTime);
    }
    public String getNewsUrl(String title){
        String url = new String();
        SQLiteDatabase db = newsItemInfoSqlHelper.getReadableDatabase();
        String sql = String.format("SELECT * FROM NEWSITEMINFO WHERE title = '%s'", title);
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()){
             url = c.getString(c.getColumnIndex("url"));
        }
        return url;
    }
    public int getNewsCollectionByUrl(String url){
        int collection = 0;

        SQLiteDatabase db = newsItemInfoSqlHelper.getReadableDatabase();
        String sql = String.format("SELECT * FROM NEWSITEMINFO WHERE url = '%s'", url);
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()){
            collection = c.getInt(c.getColumnIndex("collection"));
        }
        return collection;
    }
    public void setNewsCollection(int collection, String url){
        SQLiteDatabase db = newsItemInfoSqlHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("collection", collection);
        db.update("NEWSITEMINFO",cv, "url=?", new String[]{url});
    }

    public String getdateInt(String title){

        String dateInt = new String();

        SQLiteDatabase db = newsItemInfoSqlHelper.getReadableDatabase();
        String sql = String.format("SELECT * FROM NEWSITEMINFO WHERE title = '%s'", title);
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()){
            dateInt = c.getString(c.getColumnIndex("dateInt"));
        }

        return dateInt;
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CALLBACK_LOADMORE_OK:
                    if (callback != null){
                        List<NewsInfo>  list = (List<NewsInfo>) msg.obj;
                        callback.getNewsInfo(list);
                    }
                    break;
                case CALLBACK_INIT_OK:
                    if (initDataCallback != null){
                        List<NewsInfo>  list = (List<NewsInfo>) msg.obj;
                        initDataCallback.initNewsInfo(list);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    public void setCallback(NewsItemInfoDao.callback callback) {
        this.callback = callback;
    }

    private callback callback = null;
    public interface callback{
        public void getNewsInfo(List<NewsInfo> list);
    }

    public void setInitDataCallback(NewsItemInfoDao.initDataCallback initDataCallback) {
        this.initDataCallback = initDataCallback;
    }

    private initDataCallback initDataCallback = null;
    public interface initDataCallback{
        public void initNewsInfo(List<NewsInfo> list);
    }
}
