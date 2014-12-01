package com.ruitu.btchelper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteDB {
    private static final String TAG = MySQLiteDB.class.getSimpleName();
    private static final String DB_NAME = "btchelper.db";
    private static final int DB_VERSION = 1;
    private static MySQLiteDB instance;
    private SQLiteDBHelper mDbHelper;
    private SQLiteDatabase mDatabase;

    private MySQLiteDB(Context context) {
        if (null == mDbHelper) {
            mDbHelper = new SQLiteDBHelper(context);
        }
    }

    public SQLiteDatabase getWritableDB() {
        if (null == mDatabase) {
            mDatabase = mDbHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public SQLiteDatabase getReadableDB() {
        if (null == mDatabase) {
            mDatabase = mDbHelper.getReadableDatabase();
        }
        return mDatabase;
    }

    public void close() {
        mDatabase.close();
        mDatabase = null;
    }

    @Override
    protected void finalize() throws Throwable {
        Log.i(TAG, "---DB close!");
        close();
        super.finalize();
    }

    public synchronized static MySQLiteDB getInstance(Context context) {
        if (null == instance) {
            instance = new MySQLiteDB(context);
        }
        return instance;

    }

    private static class SQLiteDBHelper extends SQLiteOpenHelper {
        private SQLiteDBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.beginTransaction();
            db.execSQL(TickerTable.TABLE_CREATE);
            db.execSQL(TransactionTable.TABLE_CREATE);
            db.execSQL(OrderTable.TABLE_CREATE);
            db.execSQL(NewsTable.TABLE_CREATE);
            db.setTransactionSuccessful();
            db.endTransaction();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
