package com.linshengt.newsapplication.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by linshengt on 2016/9/9.
 */
public class NewsItemInfoSqlHelper extends SQLiteOpenHelper {
    public NewsItemInfoSqlHelper(Context context) {
        super(context, "NewsItemInfo.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS  NEWSITEMINFO (id INTEGER PRIMARY KEY, title VCHAR, date VCHAR, dateInt INTEGER, author_name VCHAR, thumbnail_pic_s VCHAR, url VCHAR, category VCHAR, collection INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
