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
import com.ruitu.btchelper.db.OrderTable;
import com.ruitu.btchelper.db.dao.NewsDao;
import com.ruitu.btchelper.db.dao.OrderDao;
import com.ruitu.btchelper.domain.News;
import com.ruitu.btchelper.domain.Orders;
import com.ruitu.btchelper.util.DataHelper;

public class OrderDaoImpl implements OrderDao, OrderTable {
    private MySQLiteDB mySQLiteDB;

    public OrderDaoImpl( Context context ){
        mySQLiteDB = MySQLiteDB.getInstance( context );
    }

    @Override
    public long save( Orders orders ){
        SQLiteDatabase db = mySQLiteDB.getWritableDB();
        ContentValues values = new ContentValues();
        values.put( PLAT_NAME, orders.getPlat_name() );
        values.put( BUY_PRICE, orders.getBuy_price() );
        values.put( SELL_PRICE, orders.getSell_price() );
        values.put( BUY_VOLUME, orders.getBuy_vol() );
        values.put( SELL_VOLUME, orders.getSell_vol() );
        values.put( UPDATE_TIME, orders.getUpdate_time() );
        long id = db.insert( TABLE_NAME, null, values );
        this.mySQLiteDB.close();
        return id;
    }

    @Override
    public Map< String, Object > getOrders( int page, int infoNumber, String platName ){

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
            cursor = db.query( TABLE_NAME, null, PLAT_NAME, new String[] { platName }, null, null, UPDATE_TIME + " desc", infoNumber * ( page - 1 ) + "," + infoNumber );
        } else {

            cursor = db.query( TABLE_NAME, null, PLAT_NAME, new String[] { platName }, null, null, null );
        }
        List< Orders > list = new ArrayList< Orders >();
        while ( cursor.moveToNext() ) {
            Orders orders = new Orders();
            orders.setPlat_name( cursor.getString( cursor.getColumnIndex( PLAT_NAME ) ) );
            orders.setUpdate_time( cursor.getString( cursor.getColumnIndex( UPDATE_TIME ) ) );
            orders.setBuy_price( cursor.getString( cursor.getColumnIndex( BUY_PRICE ) ) );
            orders.setBuy_vol( cursor.getString( cursor.getColumnIndex( BUY_VOLUME ) ) );
            orders.setSell_price( cursor.getString( cursor.getColumnIndex( SELL_PRICE ) ) );
            orders.setSell_vol( cursor.getString( cursor.getColumnIndex( SELL_VOLUME ) ) );
            list.add( orders );
        }
        cursor.close();
        this.mySQLiteDB.close();
        map.put( DataHelper.NEWS_LIST, list );
        return map;

    }

    @Override
    public boolean getOrdersById( String ordersId ){
        return false;
    }


    @Override
    public int deleteOrder( Orders orders ){
        SQLiteDatabase db = mySQLiteDB.getWritableDB();
        return db.delete( TABLE_NAME, PLAT_NAME + "=?", new String[] { orders.getPlat_name() } );
    }

    @Override
    public int clear(){
        SQLiteDatabase db = mySQLiteDB.getWritableDB();
        return db.delete( TABLE_NAME, null, null );
    }

}
