package com.ruitu.btchelper.db.daoi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ruitu.btchelper.db.MySQLiteDB;
import com.ruitu.btchelper.db.OrderTable;
import com.ruitu.btchelper.db.TransactionTable;
import com.ruitu.btchelper.db.dao.OrderDao;
import com.ruitu.btchelper.db.dao.TransactionDao;
import com.ruitu.btchelper.domain.Orders;
import com.ruitu.btchelper.domain.Transaction;
import com.ruitu.btchelper.util.DataHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionDaoImpl implements TransactionDao, TransactionTable {
    private MySQLiteDB mySQLiteDB;

    public TransactionDaoImpl( Context context ){
        mySQLiteDB = MySQLiteDB.getInstance( context );
    }


    @Override
    public long save( Transaction ticker ){
        SQLiteDatabase db = mySQLiteDB.getWritableDB();
        ContentValues values = new ContentValues();
        values.put( TRANSACTION_NAME, ticker.getTra_name() );
        values.put( TRANSACTION_TIME, ticker.getTra_time() );
        values.put( TRANSACTION_PRICE, ticker.getTra_price() );
        values.put( TRANSACTION_VOLUME, ticker.getMax_volume() );
        long id = db.insert( TABLE_NAME, null, values );
        this.mySQLiteDB.close();
        return id;
    }

    @Override
    public Map< String, Object > getTransactions( int page, int infoNumber, String platName ){
        {

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
                cursor = db.query( TABLE_NAME, null, TRANSACTION_NAME, new String[] { platName }, null, null, TRANSACTION_TIME + " desc", infoNumber * ( page - 1 ) + "," + infoNumber );
            } else {

                cursor = db.query( TABLE_NAME, null, TRANSACTION_NAME, new String[] { platName }, null, null, null );
            }
            List< Transaction > list = new ArrayList< Transaction >();
            while ( cursor.moveToNext() ) {
                Transaction transaction = new Transaction();
                transaction.setTra_name( cursor.getString( cursor.getColumnIndex( TRANSACTION_NAME ) ) );
                transaction.setTra_price( cursor.getString( cursor.getColumnIndex( TRANSACTION_PRICE ) ) );
                transaction.setTra_time( cursor.getString( cursor.getColumnIndex( TRANSACTION_TIME ) ) );
                transaction.setTra_volume( cursor.getString( cursor.getColumnIndex( TRANSACTION_VOLUME ) ) );
                transaction.setTra_id( cursor.getString( cursor.getColumnIndex( TRANSACTION_ID ) ) );

                list.add( transaction );
            }
            cursor.close();
            this.mySQLiteDB.close();
            map.put( DataHelper.TRANSACTION, list );
            return map;

        }
    }

    @Override
    public boolean getTransactionById( String transactionId ){
        return false;
    }

    @Override
    public int deleteTransaction( Transaction ticker ){
        SQLiteDatabase db = mySQLiteDB.getWritableDB();
        return db.delete( TABLE_NAME, TRANSACTION_NAME + "=?", new String[] { ticker.getTra_name() } );
    }

    @Override
    public int clear(){
        SQLiteDatabase db = mySQLiteDB.getWritableDB();
        return db.delete( TABLE_NAME, null, null );
    }

}
