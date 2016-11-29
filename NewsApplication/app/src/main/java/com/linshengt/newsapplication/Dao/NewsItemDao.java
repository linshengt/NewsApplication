package com.linshengt.newsapplication.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linshengt on 2016/9/6.
 */
public class NewsItemDao {
    private String TAG = "NewsItemDao";
    private Context context;
    private NewsItemSqlHelper newsItemSqlHelper;

    public NewsItemDao(Context context) {
        this.context = context;
        newsItemSqlHelper = new NewsItemSqlHelper(context);
    }

    public void addNewsItemList(List<String[]> list){
        SQLiteDatabase db = newsItemSqlHelper.getWritableDatabase();

        for (int i = 0; i < list.size(); i ++){
            ContentValues cv = new ContentValues();
            cv.put("newsItem", list.get(i)[0]);
            cv.put("newsItemEn", list.get(i)[1]);
            db.insert("NewsItemTable", null, cv);
        }
    }

    public List<String[]> getNewsItemList(){
        List<String[]> list = new ArrayList<>();
        SQLiteDatabase db = newsItemSqlHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM NewsItemTable", null);

        while (c.moveToNext()){
            String[] str = {"", ""};
            str[0] =  c.getString(c.getColumnIndex("newsItem"));
            str[1] =  c.getString(c.getColumnIndex("newsItemEn"));
            list.add(str);
        }
        return list;
    }
}
