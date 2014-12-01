package com.ruitu.btchelper.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ruitu.btchelper.domain.Orders;
import com.ruitu.btchelper.util.DataUtil;
import com.ruitu.btchelper.R;

public class OrdersAdapter extends BaseAdapter {
    private List<Orders> m_orders;
    private Context mContext;

    public OrdersAdapter(Context context, List<Orders> orders) {
        this.mContext = context;
        refreshData(orders);
    }

    public void refreshData(List<Orders> orders) {
        if (orders != null) {
            this.m_orders = orders;
        } else {
            this.m_orders.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return m_orders.size();
    }

    @Override
    public Object getItem(int position) {
        return m_orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(this.mContext,
                    R.layout.listview_page1_fragment2_item, null);
            viewHolder = new ViewHolder();

            viewHolder.ivBuy = convertView.findViewById(R.id.v_buy_amount);
            viewHolder.ivBuyEmpty = convertView
                    .findViewById(R.id.v_buy_amount_empty);
            viewHolder.m_buy_order = (TextView) convertView
                    .findViewById(R.id.tv_buy_amount);
            viewHolder.m_buy_price = (TextView) convertView
                    .findViewById(R.id.tv_buy_price);
            viewHolder.m_sell_order = (TextView) convertView
                    .findViewById(R.id.tv_sell_amount);
            viewHolder.m_sell_price = (TextView) convertView
                    .findViewById(R.id.tv_sell_price);
            viewHolder.ivSell = convertView.findViewById(R.id.v_sell_amount);
            viewHolder.ivSellEmpty = convertView
                    .findViewById(R.id.v_sell_amount_empty);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            if (m_orders != null && !m_orders.isEmpty()) {
                Orders orders = m_orders.get(position);
                viewHolder.m_sell_order.setText(DataUtil
                        .changeToFivePoint(orders.getSell_vol()));
                viewHolder.m_sell_price.setText(DataUtil
                        .changeToTwoPoint(orders.getSell_price()));
                viewHolder.m_buy_order.setText(DataUtil
                        .changeToFivePoint(orders.getBuy_vol()));
                viewHolder.m_buy_price.setText(DataUtil.changeToTwoPoint(orders
                        .getBuy_price()));
            }
            updateRateView(position, viewHolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private void updateRateView(int pos, ViewHolder viewHolder) {
        double buy = 0.00;
        double sell = 0.00;
        if (m_orders.get(pos).getBuy_vol() != null)
            buy = Double.parseDouble(m_orders.get(pos).getBuy_vol());
        if (m_orders.get(pos).getSell_vol() != null)
            sell = Double.parseDouble(m_orders.get(pos).getSell_vol());
        double max = m_orders.get(pos).getMax_vol();
        double rateBuy = buy / max;
        double rateBuyEmpty = 1 - rateBuy;
        double rateSell = sell / max;
        double rateSellEmpty = 1 - rateSell;

        viewHolder.ivBuy.setLayoutParams(getParamByWeight(1 - rateBuy));
        viewHolder.ivBuyEmpty
                .setLayoutParams(getParamByWeight(1 - rateBuyEmpty));

        viewHolder.ivSell.setLayoutParams(getParamByWeight(1 - rateSell));
        viewHolder.ivSellEmpty
                .setLayoutParams(getParamByWeight(1 - rateSellEmpty));

    }

    private LinearLayout.LayoutParams getParamByWeight(double weight) {
        int value = (int) (weight * 100);
        if (value < 1) {
            value = 1;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT, value);
        return params;
    }

    public class ViewHolder {
        public View ivBuy;
        public View ivBuyEmpty;
        public TextView m_buy_order;
        public TextView m_buy_price;
        public TextView m_sell_order;
        public TextView m_sell_price;
        public View ivSell;
        public View ivSellEmpty;
    }

}
