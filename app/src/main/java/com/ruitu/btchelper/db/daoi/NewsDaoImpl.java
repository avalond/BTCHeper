package com.ruitu.btchelper.db.daoi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ruitu.btchelper.db.MySQLiteDB;
import com.ruitu.btchelper.db.NewsTable;
import com.ruitu.btchelper.db.dao.NewsDao;
import com.ruitu.btchelper.domain.News;
import com.ruitu.btchelper.util.DataHelper;

public class NewsDaoImpl implements NewsDao, NewsTable {
    private MySQLiteDB mySQLiteDB;

    public NewsDaoImpl(Context context) {
        mySQLiteDB = MySQLiteDB.getInstance(context);
    }

    @Override
    public long save(News news) {
        SQLiteDatabase db = mySQLiteDB.getWritableDB();
        ContentValues values = new ContentValues();
        values.put(NEWS_TYPE, news.getNews_type());
        values.put(NEWS_TITLE, news.getNews_title());
        values.put(NEWS_CONTENT, news.getNews_content());
        values.put(NEWS_AUTHOR, news.getNews_author());
        values.put(NEWS_ICON, news.getNews_icon());
        values.put(NEWS_NET, news.getNews_info_url());
        values.put(NEWS_TIME, news.getNews_time());
        long id = db.insert(TABLE_NAME, null, values);
        this.mySQLiteDB.close();
        return id;
    }

    @Override
    public Map<String, Object> getNewsByType(int type, int page, int infoNumber) {

        SQLiteDatabase db = mySQLiteDB.getReadableDB();
        Map<String, Object> map = new HashMap<String, Object>();
        String[] args = new String[] { String.valueOf(type) };
        Cursor cursor = null;
        if (infoNumber != 0) {
            Cursor cu = db.query(TABLE_NAME, null, NEWS_TYPE, args, null, null, null,
                    null);
            int count = cu.getCount();
            cu.close();
            int pageNum = count % infoNumber == 0 ? count / infoNumber : count
                    / infoNumber + 1;
            if (page <= 0)
                return null;
            if (page < pageNum) {
                map.put(DataHelper.HAS_NEXT, true);
            } else {
                map.put(DataHelper.HAS_NEXT, false);
            }
            cursor = db.query(TABLE_NAME, null, NEWS_TYPE, args, null, null,
                    NEWS_UPDATE_TIME + " desc", infoNumber * (page - 1) + ","
                            + infoNumber);
        } else {
            cursor = db.query(TABLE_NAME, null, NEWS_TYPE, args, null, null,
                    null);
        }
        List<News> list = new ArrayList<News>();
        while (cursor.moveToNext()) {
            News news = new News();
            news.setNews_author(cursor.getString(cursor
                    .getColumnIndex(NEWS_AUTHOR)));
            news.setNews_content(cursor.getString(cursor
                    .getColumnIndex(NEWS_CONTENT)));
            news.setNews_icon(cursor.getString(cursor.getColumnIndex(NEWS_ICON)));
            news.setNews_info_url(cursor.getString(cursor
                    .getColumnIndex(NEWS_NET)));
            news.setNews_time(cursor.getString(cursor.getColumnIndex(NEWS_TIME)));
            news.setNews_title(cursor.getString(cursor
                    .getColumnIndex(NEWS_TITLE)));
            news.setNews_type(cursor.getInt(cursor.getColumnIndex(NEWS_TYPE)));
            list.add(news);
        }
        cursor.close();
        this.mySQLiteDB.close();
        map.put(DataHelper.NEWS_LIST, list);
        return map;

    }


    @Override
    public boolean getNewsById(String newsId) {
        return false;
    }

    @Override
    public int deleteNews(News news) {
        SQLiteDatabase db = mySQLiteDB.getWritableDB();
        return db.delete(TABLE_NAME, NEWS_TITLE+"=?", new String[]{news.getNews_title()});
    }

    @Override
    public int clear() {
        SQLiteDatabase db = mySQLiteDB.getWritableDB();
        return db.delete(TABLE_NAME,null, null);
    }

}
