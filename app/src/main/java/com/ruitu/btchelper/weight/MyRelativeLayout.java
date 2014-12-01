package com.ruitu.btchelper.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class MyRelativeLayout extends RelativeLayout {
    private static final String TAG = MyRelativeLayout.class.getSimpleName();

    public MyRelativeLayout( Context context ){
        super( context );
    }

    public MyRelativeLayout( Context context, AttributeSet attrs ){
        super( context, attrs );
    }

    public MyRelativeLayout( Context context, AttributeSet attrs, int defStyleAttr ){
        super( context, attrs, defStyleAttr );
    }

    private float xDown;
    private float yDown;
    private float xUp;
    private float yUp;

//    @Override
//    public boolean onTouchEvent( MotionEvent ev ){
//        Log.e( TAG, "MyRelativeLayout执行了" );
//        switch ( ev.getAction() ) {
//            case MotionEvent.ACTION_DOWN:
//                xDown = ev.getRawX();
//                yDown = ev.getRawY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                break;
//            case MotionEvent.ACTION_UP:
//                xUp = ev.getRawX();
//                yUp = ev.getRawY();
//                int distanceX = ( int ) ( xUp - xDown );
//                int distanceY = ( int ) ( yUp - yDown );
//                if ( distanceX >= distanceY && distanceX > 0 ) {//横向滑动
//                    Log.e( TAG, "" + distanceX + "=>=" + distanceY );
//                    return true;
//                } else if ( distanceX < distanceY && distanceY > 0 ) {//纵向滑动
//                    Log.e( TAG, "" + distanceX + "=<==" + distanceY );
//                    return false;
//                }
//                break;
//        }
//
//        return super.onInterceptTouchEvent( ev );
//    }

    @Override
    public boolean onInterceptTouchEvent( MotionEvent ev ){
        Log.e( TAG, "MyRelativeLayout执行了" );
        switch ( ev.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                xDown = ev.getRawX();
                yDown = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                xUp = ev.getRawX();
                yUp = ev.getRawY();
                int distanceX = ( int ) ( xUp - xDown );
                int distanceY = ( int ) ( yUp - yDown );
                if ( distanceX >= distanceY && distanceX > 0 ) {//横向滑动
                    Log.e( TAG, "" + distanceX + "=>=" + distanceY );
                    return true;
                } else if ( distanceX < distanceY && distanceY > 0 ) {//纵向滑动
                    Log.e( TAG, "" + distanceX + "=<==" + distanceY );
                    return false;
                }
                break;
        }

        return super.onInterceptTouchEvent( ev );
    }
}
