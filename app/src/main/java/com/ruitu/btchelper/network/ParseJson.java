package com.ruitu.btchelper.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.util.Log;

import com.ruitu.btchelper.domain.BtcEntity;
import com.ruitu.btchelper.domain.News;
import com.ruitu.btchelper.domain.Orders;
import com.ruitu.btchelper.domain.Ticker;
import com.ruitu.btchelper.domain.Transaction;
import com.ruitu.btchelper.util.DataHelper;
import com.ruitu.btchelper.util.DataUtil;
import com.ruitu.btchelper.util.DateUtil;

public class ParseJson implements DataHelper {
    private static final String TAG = ParseJson.class.getSimpleName();

    public static double parseUsd_to_cny(String str) throws Exception {
        Log.e(TAG, str + "");
        double d = 0.0;
        if (str == null) {
            Log.e(TAG, "str==null");
            return d;
        }
        JSONObject root = new JSONObject(str);
        if (root.has("now")) {
            String str_now = root.getString("now");
            if (str_now != null && !"".equals(str_now)) {
                d = Double.parseDouble(str_now);
            }
        }
        return d;
    }
//
//    /**
//     * 实时行情
//     *
//     * @throws Exception
//     */
//    public static Ticker parseTicker(String str) throws Exception {
//        if (str == null)
//            return null;
//        JSONObject obJsonObject = new JSONObject(str);
//        Ticker ticker = new Ticker();
//        ticker.setName(BITSTAMP);
//        if (obJsonObject.has(UPDATE_TIME)) {
//            String update_time = obJsonObject.getString(UPDATE_TIME);
//            if (update_time != null)
//                ticker.setUpdateTime(DateUtil.getSimpleDateStr( Long.parseLong(update_time) ));
//        }
//
//        if (obJsonObject.has(BUY)) {
//            ticker.setBuy(DataUtil.changeToTwoPoint(obJsonObject.getString(BUY)));
//        }
//        if (obJsonObject.has(HIGH))
//            ticker.setHigh(DataUtil.changeToTwoPoint(obJsonObject
//                    .getString(HIGH)));
//        if (obJsonObject.has(LAST))
//            ticker.setLast(DataUtil.changeToTwoPoint(obJsonObject
//                    .getString(LAST)));
//        if (obJsonObject.has(LOW))
//            ticker.setLow(DataUtil.changeToTwoPoint(obJsonObject.getString(LOW)));
//        if (obJsonObject.has(SELL))
//            ticker.setSell(DataUtil.changeToTwoPoint(obJsonObject
//                    .getString(SELL)));
//        if (obJsonObject.has(VOLUME))
//            ticker.setVolume(DataUtil.changeToTwoPoint(obJsonObject
//                    .getString(VOLUME)));
//        return ticker;
//    }

    /**
     * 解析实时详情
     */
    public static List<Ticker> parseTickers(String str) throws Exception {
        Log.e( TAG,str );
        if (str == null)
            return null;
        JSONArray arrJsonArray = new JSONArray(str);
        if (arrJsonArray == null || arrJsonArray.length() == 0)
            return null;
        List<Ticker> list = new ArrayList<Ticker>();
        for (int i = 0; i < arrJsonArray.length(); i++) {
            Ticker ticker = new Ticker();
            JSONObject obJsonObject = arrJsonArray.getJSONObject(i);
            if (obJsonObject.has(NET_ADDRESS)) {
                ticker.setNet_name(obJsonObject.getString(NET_ADDRESS));
            }
            if (obJsonObject.has(PLAT_NAME)) {
                ticker.setName(obJsonObject.getString(PLAT_NAME));
            }
            if (obJsonObject.has(UPDATE_TIME)) {
                String update_time = obJsonObject.getString(UPDATE_TIME);
                if (update_time != null)
                    ticker.setUpdateTime(DateUtil.getSimpleDateStr(Long.parseLong(update_time)));
            }
            if (obJsonObject.has(BUY)) {
                ticker.setBuy(DataUtil.changeToTwoPoint(obJsonObject
                        .getString(BUY)));
            }
            if (obJsonObject.has(HIGH))
                ticker.setHigh(DataUtil.changeToTwoPoint(obJsonObject
                        .getString(HIGH)));
            if (obJsonObject.has(LAST))
                ticker.setLast(DataUtil.changeToTwoPoint(obJsonObject
                        .getString(LAST)));
            if (obJsonObject.has(LOW))
                ticker.setLow(DataUtil.changeToTwoPoint(obJsonObject
                        .getString(LOW)));
            if (obJsonObject.has(SELL))
                ticker.setSell(DataUtil.changeToTwoPoint(obJsonObject
                        .getString(SELL)));
            if (obJsonObject.has(VOLUME))
                ticker.setVolume(DataUtil.changeToTwoPoint(obJsonObject
                        .getString(VOLUME)));
            list.add(ticker);
        }
        return list;
    }

    /**
     * 解析委托信息
     * 
     * @throws Exception
     */
    public static List<Orders> parseOrder(String str) throws Exception {
        double max = 0.00;
        if (str == null)
            return null;
        JSONObject jObject = new JSONObject(str);
        List<Orders> list = new ArrayList<Orders>();
        String time = null;
        if (jObject.has(ORDER_TIME)) {
            String update_time = jObject.getString(ORDER_TIME);
            if (update_time != null) {
                time = DateUtil.getShortDateStr(Long.parseLong(update_time));
            }
        }
        JSONArray root1 = null;
        JSONArray root2 = null;
        if (jObject.has(ASKS)) {
            root1 = jObject.getJSONArray(ASKS);
        }
        if (jObject.has(BIDS)) {
            root2 = jObject.getJSONArray(BIDS);
        }
        if (root1 != null && root2 != null) {
            int size1 = root1.length();
            int size2 = root2.length();
            int minSize = size1 >= size2 ? size2 : size1;
            int maxSize = size1 <= size2 ? size2 : size1;
            for (int i = 0; i < minSize; i++) {
                Orders order = new Orders();
                JSONArray jsonArray = root1.getJSONArray(i);
                JSONArray jsonArray2 = root2.getJSONArray(i);
                order.setUpdate_time(time);
                order.setBuy_price(jsonArray2.getString(0));
                order.setBuy_vol(jsonArray2.getString(1));
                order.setSell_price(jsonArray.getString(0));
                order.setSell_vol(jsonArray.getString(1));
                if (Double.parseDouble(jsonArray2.getString(1)) > max) {
                    max = Double.parseDouble(jsonArray2.getString(1));
                }
                if (Double.parseDouble(jsonArray.getString(1)) > max) {
                    max = Double.parseDouble(jsonArray.getString(1));
                }
                list.add(order);
            }
            if (maxSize == size1) {
                for (int i = maxSize - minSize; i < maxSize; i++) {
                    Orders order = new Orders();
                    JSONArray jsonArray = root1.getJSONArray(i);
                    order.setUpdate_time(time);
                    order.setSell_price(jsonArray.getString(0));
                    order.setSell_vol(jsonArray.getString(1));
                    if (Double.parseDouble(jsonArray.getString(1)) > max) {
                        max = Double.parseDouble(jsonArray.getString(1));
                    }
                    list.add(order);
                }
            } else {
                for (int i = maxSize - minSize; i < maxSize; i++) {
                    Orders order = new Orders();
                    JSONArray jsonArray = root2.getJSONArray(i);
                    order.setUpdate_time(time);
                    order.setBuy_price(jsonArray.getString(0));
                    order.setBuy_vol(jsonArray.getString(1));
                    if (Double.parseDouble(jsonArray.getString(1)) > max) {
                        max = Double.parseDouble(jsonArray.getString(1));
                    }
                    list.add(order);
                }
            }
        }
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setMax_vol(max);
            }
        }
        return list;
    }


    public static Map<String, Object> parseTransaction(String str)
            throws Exception {
        Log.e(TAG, str + "");
        if (str == null)
            return null;
        JSONObject root = new JSONObject(str);
        Map<String, Object> map = new HashMap<String, Object>();
        if (root.has(HAS_NEXT)) {
            map.put(HAS_NEXT, root.getBoolean(HAS_NEXT));
        }
        if (root.has(PAGE)) {
            map.put(PAGE, root.getInt(PAGE));
        }
        if (root.has(PLAT_NAME)) {

        }
        if (root.has(TRANSACTION)) {
            double max = 0.00;
            JSONArray transactions = root.getJSONArray(TRANSACTION);
            if (transactions == null || transactions.length() == 0)
                return null;
            List<Transaction> list = new ArrayList<Transaction>();
            for (int i = 0; i < transactions.length(); i++) {
                Transaction transaction = new Transaction();
                JSONObject jsonObject = transactions.getJSONObject(i);
                if (jsonObject.has(DATE)) {
                    String time_str = jsonObject.getString(DATE);
                    transaction.setTra_time(DateUtil.getShortDateStr(Long
                            .parseLong(time_str)));
                }
                if (jsonObject.has(PRICE)) {
                    transaction.setTra_price(jsonObject.getString(PRICE));
                }
                if (jsonObject.has(AMOUNT)) {
                    transaction.setTra_volume(jsonObject.getString(AMOUNT));
                }
                if (jsonObject.has(TID)) {
                    transaction.setTra_id(jsonObject.getString(TID));
                }
                if (Double.parseDouble(jsonObject.getString(AMOUNT)) > max) {
                    max = Double.parseDouble(jsonObject.getString(AMOUNT));
                    Log.e(TAG, "max-------------" + max);
                }
                list.add(transaction);
            }

            for (Transaction transaction : list) {
                transaction.setMax_volume(max);
            }
            map.put(TRANSACTION, list);
        }
        return map;
    }

    /**
     * 解析网络新闻内容的json
     */
    public static Map<String, Object> parseNewsFromNet(String str) {
        Log.e(TAG, str + "");
        if (str == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            JSONObject root = new JSONObject(str);
            if (root.has(IS_FLUSH)) {
                map.put(IS_FLUSH, root.getBoolean(IS_FLUSH));
            }
            if (root.has(HAS_NEXT))
                map.put(HAS_NEXT,
                        Boolean.parseBoolean(root.getString(HAS_NEXT)));
            if (root.has(PAGE))
                map.put(PAGE, Integer.parseInt(root.getString(PAGE)));
            if (root.has(NEWS_LIST)) {
                ArrayList<News> list = new ArrayList<News>();
                JSONArray newsArr = root.getJSONArray(NEWS_LIST);
                for (int i = 0; i < newsArr.length(); i++) {
                    News news = new News();
                    JSONObject jsonObject = newsArr.getJSONObject(i);
                    if (jsonObject.has(TITLE)) {
                        news.setNews_title(jsonObject.getString(TITLE));
                    }
                    if (jsonObject.has(CONTENT)) {
                        news.setNews_content(jsonObject.getString(CONTENT));
                    }
                    if (jsonObject.has(ICON)) {
                        news.setNews_icon(jsonObject.getString(ICON));
                    }
                    if (jsonObject.has(AUTHOR)) {
                        news.setNews_author(jsonObject.getString(AUTHOR));
                    }
                    if (jsonObject.has(TIME)) {
                        news.setNews_time(jsonObject.getString(TIME));
                    }
                    if (jsonObject.has(NET_ADDRESS)) {
                        news.setNews_info_url(jsonObject.getString(NET_ADDRESS));
                    }
                    list.add(news);
                }
                map.put(NEWS_LIST, list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 解析注册信息
     */
    public static boolean parseRegisterStr(String str) throws JSONException {
        Log.e(TAG, str + "");
        if (str == null) {
            return false;
        }
        JSONObject root = new JSONObject(str);
        if (root.has(SUCCESS)) {
            return root.getBoolean(SUCCESS);
        }
        return false;
    }

    /**
     * 解析登录信息
     * 
     * @throws org.json.JSONException
     */
    public static Map<String, Object> parseLoginStr(String str)
            throws JSONException {
        Log.e(TAG, str + "");
        if (str == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject root = new JSONObject(str);
        if (root.has(SUCCESS)) {
            map.put(SUCCESS, root.getBoolean(SUCCESS));
        }
        if (root.has(INFO)) {
            map.put(INFO, root.getInt(INFO));
        }
        return map;
    }

    /**
     * 解析建议信息
     * 
     * @throws Exception
     */
    public static boolean parseFeedbackStr(String str) throws Exception {
        Log.e(TAG, str + "");
        if (str == null) {
            return false;
        }
        JSONObject root = new JSONObject(str);
        if (root.has(SUCCESS)) {
            return root.getBoolean(SUCCESS);
        }
        return false;
    }

    /**
     * 解析挖矿算力信息
     * 
     * @throws Exception
     */
    public static BtcEntity parseBtcEnStr(String str) throws Exception {
        Log.e(TAG, str + "");
        if (str == null || "".equals(str)) {
            return null;
        }
        JSONObject root = new JSONObject(str);
        BtcEntity btcEntity = new BtcEntity();
        if (root.has(exchange_rate)) {
            btcEntity.setExchange_rate(root.getDouble(exchange_rate));
        }
        if (root.has(time_per_block)) {
            btcEntity.setTime_per_block(root.getDouble(time_per_block));
        }
        if (root.has(dollars_per_hour_after_retarget)) {
            btcEntity.setDollars_per_hour_after_retarget(root
                    .getDouble(dollars_per_hour_after_retarget));
        }
        if (root.has(next_difficulty)) {
            btcEntity.setNext_difficulty(root.getDouble(next_difficulty));
        }
        if (root.has(hashrate)) {
            btcEntity.setHashrate(root.getDouble(hashrate));
        }
        if (root.has(exchange_rate_source)) {
            btcEntity.setExchange_rate_source(root
                    .getString(exchange_rate_source));
        }
        if (root.has(dollars_per_hour)) {
            btcEntity.setDollars_per_hour(root.getDouble(dollars_per_hour));
        }
        if (root.has(coins_before_retarget)) {
            btcEntity.setCoins_before_retarget(root
                    .getDouble(coins_before_retarget));
        }
        if (root.has(bc_per_block)) {
            btcEntity.setBc_per_block(root.getDouble(bc_per_block));
        }
        if (root.has(coins_per_hour)) {
            btcEntity.setCoins_per_hour(root.getDouble(coins_per_hour));
        }
        if (root.has(coins_per_hour_after_retarget)) {
            btcEntity.setCoins_per_hour_after_retarget(root
                    .getDouble(coins_per_hour_after_retarget));
        }
        if (root.has(difficulty)) {
            btcEntity.setDifficulty(root.getDouble(difficulty));
        }
        if (root.has(dollars_before_retarget)) {
            btcEntity.setDollars_before_retarget(root
                    .getDouble(dollars_before_retarget));
        }
        return btcEntity;
    }
}
