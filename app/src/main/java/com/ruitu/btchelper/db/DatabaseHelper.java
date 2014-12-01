package com.ruitu.btchelper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.ruitu.btchelper.bean.NewsBean;
import com.ruitu.btchelper.bean.OrderBean;
import com.ruitu.btchelper.bean.TickerBean;
import com.ruitu.btchelper.bean.TransactionBean;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TABLE_NAME = "sqlite-btchelper.db";
    /**
     * userDao ，每张表对于一个
     */
    private Dao< NewsBean, Integer > userDao;

    private DatabaseHelper( Context context ){
        super( context, TABLE_NAME, null, 2 );
    }

    @Override
    public void onCreate( SQLiteDatabase database, ConnectionSource connectionSource ){
        try {
            TableUtils.createTable( connectionSource, NewsBean.class );
            TableUtils.clearTable( connectionSource, TickerBean.class );
            TableUtils.clearTable( connectionSource, TransactionBean.class );
            TableUtils.clearTable( connectionSource, OrderBean.class );
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade( SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion ){
        try {
            TableUtils.dropTable( connectionSource, NewsBean.class, true );
            TableUtils.dropTable( connectionSource, TickerBean.class, true );
            TableUtils.dropTable( connectionSource, TransactionBean.class, true );
            TableUtils.dropTable( connectionSource, OrderBean.class, true );
            onCreate( database, connectionSource );
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    }

    private static DatabaseHelper instance;

    /**
     * 单例获取该Helper
     *
     * @param context
     *
     * @return
     */
    public static synchronized DatabaseHelper getHelper( Context context ){
        if ( instance == null ) {
            synchronized ( DatabaseHelper.class ) {
                if ( instance == null )
                    instance = new DatabaseHelper( context );
            }
        }

        return instance;
    }


    /**
     * 释放资源
     */
    @Override
    public void close(){
        super.close();
    }

}
