package com.ruitu.btchelper.activity;

import java.util.ArrayList;
import java.util.List;

import com.ruitu.btchelper.R;
import com.ruitu.btchelper.adapter.TeuInfoAdapter;
import com.ruitu.btchelper.domain.News;
import com.ruitu.btchelper.domain.Teu;
import com.ruitu.btchelper.util.DataUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TeuInfoAcitivity extends BaseActivity {
    private static final String TAG = TeuInfoAcitivity.class.getSimpleName();
    private TextView back_time_tv, sum_count_tv, max_count_tv, begin_mining_date_tv, day_elec_tv;
    private ListView lv_teu_info;
    private ArrayList< Teu > mTeus = new ArrayList< Teu >();

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.teu_info_layout );
        Intent inte = getIntent();
        if ( inte.hasExtra( "teu" ) ) {
            mTeus = ( ArrayList< Teu > ) getIntent().getSerializableExtra( "teu" );
        }
        initViews();
    }

    private void initViews(){
            this.back_time_tv = ( TextView ) this.findViewById( R.id.back_time_tv_teuinfo );
            this.sum_count_tv = ( TextView ) this.findViewById( R.id.sum_count_tv_teuinfo );
            this.max_count_tv = ( TextView ) this.findViewById( R.id.max_count_tv_teuinfo );
            this.begin_mining_date_tv = ( TextView ) this.findViewById( R.id.begin_mining_date_tv_teuinfo );
            this.day_elec_tv = ( TextView ) this.findViewById( R.id.day_elec_tv_teuinfo );
            this.lv_teu_info = ( ListView ) this.findViewById( R.id.lv_teu_info_teuinfo );
    }

    @Override
    protected void onResume(){
        initData();
        super.onResume();
    }

    private boolean isCantBack( List< Teu > list ){
        for ( Teu teu : list ) {
            if ( teu.getSum_gain() > 0 ) {
                return false;
            }
        }
        return true;
    }

    private void initData(){
        try {

            if ( mTeus != null && ! mTeus.isEmpty() ) {
                TeuInfoAdapter adapter = new TeuInfoAdapter( mTeus, this );
                this.lv_teu_info.setAdapter( adapter );
                Teu teu = mTeus.get( 0 );
                this.begin_mining_date_tv.setText( teu.getStart_date() );
                if ( isCantBack( mTeus ) ) {
                    this.back_time_tv.setText( "我也不知道啥时候能回本" );
                } else {
                    this.back_time_tv.setText( teu.getBack_days() + "天" );
                }
                this.day_elec_tv.setText( "￥" + DataUtil.changeToTwoPoint( String.valueOf( teu.getDay_elec() ) ) );
                this.sum_count_tv.setText( "￥" + DataUtil.changeToTwoPoint( String.valueOf( teu.getSum_cost() ) ) );
                this.max_count_tv.setText( "￥" + DataUtil.changeToTwoPoint( String.valueOf( teu.getMax_income() ) ) + "(" + teu.getMax_income_date() + ")" );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
