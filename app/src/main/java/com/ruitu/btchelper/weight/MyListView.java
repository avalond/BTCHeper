package com.ruitu.btchelper.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

public class MyListView extends ListView{
    private static final String TAG = MyListView.class.getSimpleName();
    private int x;
    private int y;
    public MyListView( Context context ){
        super( context );
    }

    public MyListView( Context context, AttributeSet attrs, int defStyleAttr ){
        super( context, attrs, defStyleAttr );
    }

    public MyListView( Context context, AttributeSet attrs ){
        super( context, attrs );
    }
    private float xDown;
    private float yDown;
    private float xUp;
    private float yUp;
    @Override
    public boolean onInterceptTouchEvent( MotionEvent ev ){
        Log.e( TAG, "MyListView执行了" );
        switch ( ev.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                xDown = ev.getRawX();
                yDown = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                xUp = ev.getRawX();
                yUp = ev.getRawY();
                int distanceX = ( int ) ( xUp - xDown );
                int distanceY = ( int ) ( yUp - yDown );
                Log.e( TAG,distanceX+"===="+distanceY );
                if ( distanceX >= distanceY && distanceX > 0 ) {//横向滑动
                    Log.e( TAG, "" + distanceX + "=>=" + distanceY );
                    return true;
                } else if ( distanceX < distanceY && distanceY > 0 ) {//纵向滑动
                    Log.e( TAG, "" + distanceX + "=<==" + distanceY );
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                xUp = ev.getRawX();
                yUp = ev.getRawY();
                int distanceX1 = ( int ) ( xUp - xDown );
                int distanceY1 = ( int ) ( yUp - yDown );
                Log.e( TAG,distanceX1+"===="+distanceY1 );
                if ( distanceX1 >= distanceY1 && distanceX1 > 0 ) {//横向滑动
                    Log.e( TAG, "" + distanceX1 + "=>=" + distanceY1 );
                    return true;
                } else if ( distanceX1 < distanceY1 && distanceY1 > 0 ) {//纵向滑动
                    Log.e( TAG, "" + distanceX1 + "=<==" + distanceY1 );
                    return false;
                }
                break;
        }
        return false;
//        return super.onInterceptTouchEvent( ev );
    }

}
