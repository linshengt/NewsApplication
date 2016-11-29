package com.linshengt.newsapplication.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by linshengt on 2016/9/6.
 */
public class NewsItemSqlHelper extends SQLiteOpenHelper {

    public NewsItemSqlHelper(Context context) {
        super(context, "NewsItem.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS NewsItemTable ( id INTEGER PRIMARY KEY, newsItem VCHAR, newsItemEn VCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
