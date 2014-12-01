package com.ruitu.btchelper.db.daoi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ruitu.btchelper.db.MySQLiteDB;
import com.ruitu.btchelper.db.TickerTable;
import com.ruitu.btchelper.db.TransactionTable;
import com.ruitu.btchelper.db.dao.TickerDao;
import com.ruitu.btchelper.db.dao.TransactionDao;
import com.ruitu.btchelper.domain.Ticker;
import com.ruitu.btchelper.domain.Transaction;
import com.ruitu.btchelper.util.DataHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TickerDaoImpl implements TickerDao, TickerTable {
    private MySQLiteDB mySQLiteDB;

    public TickerDaoImpl( Context context ){
        mySQLiteDB = MySQLiteDB.getInstance( context );
    }


    @Override
    public long save( Ticker ticker ){
        SQLiteDatabase db = mySQLiteDB.getWritableDB();
        ContentValues values = new ContentValues();
        values.put( NET_ADDRESS, ticker.getNet_name() );
        values.put( TICKER_BUY_PRICE, ticker.getBuy() );
        values.put( TICKER_LAST_PRICE, ticker.getLast() );
        values.put( TICKER_NAME, ticker.getName() );
        values.put( TICKER_HIGH_PRICE, ticker.getHigh() );
        values.put( TICKER_LOW_PRICE, ticker.getLow() );
        values.put( TICKER_SELL_PRICE, ticker.getSell() );
        values.put( TICKER_VOLUME, ticker.getVolume() );
        values.put( UPDATE_TIME, ticker.getUpdateTime() );
        values.put( TICKER_WAP, ticker.getVwap() );
        long id = db.insert( TABLE_NAME, null, values );
        this.mySQLiteDB.close();
        return id;
    }

    @Override
    public Map< String, Object > getTickers( int page, int infoNumber, String platName ){

        SQLiteDatabase db = mySQLiteDB.getReadableDB();
        Map< String, Object > map = new HashMap< String, Object >();
        Cursor cursor = null;
        if ( infoNumber != 0 ) {
            Cursor cu = db.query( TABLE_NAME, null, null, null, null, null, null, null );
            int count = cu.getCount();
            cu.close();
            int pageNum = count % infoNumber == 0 ? count / infoNumber : count / infoNumber + 1;
            if ( page <= 0 )
                return null;
            if ( page < pageNum ) {
                map.put( DataHelper.HAS_NEXT, true );
            } else {
                map.put( DataHelper.HAS_NEXT, false );
            }
            cursor = db.query( TABLE_NAME, null, TICKER_NAME, new String[] { platName }, null, null, UPDATE_TIME + " desc", infoNumber * ( page - 1 ) + "," + infoNumber );
        } else {

            cursor = db.query( TABLE_NAME, null, TICKER_NAME, new String[] { platName }, null, null, null );
        }
        List< Ticker > list = new ArrayList< Ticker >();
        while ( cursor.moveToNext() ) {

            Ticker ticker = new Ticker();
            ticker.setBuy( cursor.getString( cursor.getColumnIndex( TICKER_BUY_PRICE ) ) );
            ticker.setHigh( cursor.getString( cursor.getColumnIndex( TICKER_HIGH_PRICE ) ) );
            ticker.setLast( cursor.getString( cursor.getColumnIndex( TICKER_LAST_PRICE ) ) );
            ticker.setLow( cursor.getString( cursor.getColumnIndex( TICKER_LOW_PRICE ) ) );
            ticker.setName( cursor.getString( cursor.getColumnIndex( TICKER_NAME ) ) );
            ticker.setSell( cursor.getString( cursor.getColumnIndex( TICKER_SELL_PRICE ) ) );
            ticker.setUpdateTime( cursor.getString( cursor.getColumnIndex( UPDATE_TIME ) ) );
            ticker.setVwap( cursor.getString( cursor.getColumnIndex( TICKER_WAP ) ) );
            ticker.setNet_name( cursor.getString( cursor.getColumnIndex( NET_ADDRESS ) ) );
            list.add( ticker );
        }
        cursor.close();
        this.mySQLiteDB.close();
        map.put( DataHelper.TRANSACTION, list );
        return map;


    }

    @Override
    public boolean getTickersById( String tickerId ){
        return false;
    }

    @Override
    public int deleteTicker( Ticker ticker ){
        SQLiteDatabase db = mySQLiteDB.getWritableDB();
        return db.delete( TABLE_NAME, TABLE_NAME + "=?", new String[] { ticker.getName() } );
    }

    @Override
    public int clear(){
        SQLiteDatabase db = mySQLiteDB.getWritableDB();
        return db.delete( TABLE_NAME, null, null );
    }

}
