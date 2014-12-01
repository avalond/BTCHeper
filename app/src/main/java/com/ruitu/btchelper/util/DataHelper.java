package com.ruitu.btchelper.util;

public interface DataHelper {
    //测试本地
   // public static final String BASE = "http://192.168.0.60:8080/";
    //服务器
    public static final String BASE = "http://www.btcoinhelp.com/";
   // 美元兑人民币汇率
    public static final String URL_USD_TO_CNY = "http://api.uihoo.com/currency/currency.http.php?from=usd&to=cny&format=json";
    // 更新apk下载地址
    public static final String apkUrl = BASE
            + "resources/version/BTCHelper.apk";// 更新apk下载地址
    public static final String versionUrl = BASE + "UpdateServlert";// 版本号
    /**
     * 广告信息文件名
     */
    public static final String NOTICE_FILE_NAME = "notice.txt";
    /**
     * 实时详情请求接口
     */
    public static final String TICKER_URL = BASE + "TickerServlert";

    /**
     * 委托信息接口
     */
    public static final String ORDER_URL = BASE + "OrderServlert";
    /**
     * 最近成交接口
     */
    public static final String TRANSACTIONS_URL = BASE + "TransactionServlert";
    /**
     * k线图接口
     */
    public static final String KLINE_URL = "http://www.btcte.com/chart/";
     //  public static final String KLINE_URL = "http://192.168.0.2:8080/testkkk";
    /**
     * 新闻url
     */
    public static final String NEWS_URL = BASE + "NewsServlert" + "/{1}";
    /**
     * 广告url
     */
    public static final String NOTICE_URL = BASE + "NewsServlert?type=7&page=1";;
    /**
     * 登录url
     */
    public static final String LOGIN_URL = BASE + "LoginServlert";
    /**
     * 注册url
     */
    public static final String REGISTER_URL = BASE + "RegisterServlert";
    /**
     * 意见url
     */
    public static final String FEEDBACK_URL = BASE + "FeedbackServlert";

    public static String netword = "www.btcoinhelp.com";

    public static final String NEWS_SER = "news_ser";
    public static final String about_us = "比特币助手的说明";

    public static final String WARN_ACTION = "com.ruitu.warn.action";
    public static final String WARN_TICKER = "warn_ticker";
    /**
     * 信息缓存目录
     */
    public static final String ENVIROMENT_DIR_CACHE = android.os.Environment
            .getExternalStorageDirectory().getPath() + "/btchelper/cache/";
    /**
     * 缓存的文件名
     */
    public static final String FILE_NAME = "{filename}.txt";
    /**
     * IMAGE_PATH_KEY
     */
    public static final String IMAGE_PATH_KEY = "image_path";
    /**
     * 系统设置
     */
    public static final String SYSTEM_SETTING = "system_setting";
    /**
     * 屏幕宽度
     */
    public static final String SCREEN_W = "screen_width";
    /**
     * 屏幕高度
     */
    public static final String SCREEN_H = "screen_high";
    /**
     * 屏幕密度
     */
    public static final String DENSITY = "density";
    /**
     * 
     */
    public static final String DENSITYDPI = "densitydpi";
    /**
     * 版本号
     */
    public static final String VERSION = "0.0.1";
    /**
     * 自动启动的广播
     */
    public static final String AUTO_OPEN = "com.ruitu.btchelper.auto";
    /**
     * 广告是否有更新
     */
    public static final String IS_FLUSH = "isflush";
    /**
     * 第一次登陆标志
     */
    public static final String FIRST_LOGIN = "is_first_login";
    /**
     * 实时行情刷新时间
     */
    public static final String TICKER_UPDATE_TIME = "ticker_update_time";
    /**
     * 震动与否
     */
    public static final String IS_SHAKE = "is_shake";
    /**
     * 响铃与否
     */
    public static final String IS_BELL = "is_bell";
    /**
     * 是否监控
     */
    public static final String IS_MONITOR = "is_monitor";
    public static final String PLAT_NAME = "platform";
    /**
     * 市场深度刷新时间
     */
    public static final String DEPTH_UPDATE_TIME = "depth_update_time";
    /**
     * 实时行情定时更新广播接收action
     */
    public static final String TICKER_ALARM_BROADCAST = "com.ruitu.alarm.ticker.action";
    /**
     * 实时行情定时更新收标志
     */
    public static final int TICKER_ACTION = 1001;
    /**
     * 市场深度中委单信息定时刷新标志
     */
    public static final int DEPTH_ORDER_ACTION = 1002;
    /**
     * 市场深度中成交信息定时刷新标志
     */
    public static final int DEPTH_TRANSACTIONS_ACTION = 1003;
    /**
     * k线图标志
     */
    public static final int DEPTH_KLINE_ACTION = 1004;
    /**
     * 实时详情界面广告切换
     */
    public static final int NOTICE_ACTION_TICKER = 10011;
    /**
     * 新闻首页广告切换
     */
    public static final int NOTICE_ACTION_NEWS = 10022;
    /**
     * 实时详情页面广告
     */
    public static final int NOTICE_ACTION = 1006;
    /**
     * 首页加载页面广告
     */
    public static final int NOTICE_ACTION_INDEX = 10003;

    public static final int LOAD_IMG = 1111111;
    public static final String NOTICES = "notices";
    /**
     * 新闻首页广告数据请求
     */
    public static final int NOTICE_ACTION_NEWS_INDEX = 1005;
    /**
     * 新闻页面
     */
    public static final int NEWS_ACTION_NEWS = 1007;
    /**
     * 登录action
     */
    public static final int LOGIN_ACTION = 1008;
    /**
     * 注册action
     */
    public static final int REGSITER_ACTION = 1009;
    /**
     * 意见action
     */
    public static final int FEEDBACK_ACTION = 10010;
    /**
     * 市场深度中委单信息定时刷新广播接受action
     */
    public static final String DEPTH_ORDER_ALARM_BROADCAST = "com.ruitu.alarm.depth.order.action";

    /**
     * 市场深度中成交信息定时刷新广播接受action
     */
    public static final String DEPTH_TRANSACTIONS_ALARM_BROADCAST = "com.ruitu.alarm.depth.transactions.action";

    /**
     * 更新K线图的action
     */
    public static final String DEPTH_KLINE_ALARM_BROADCAST = "com.ruitu.alarm.depth.kline.action";
    /**
     * 监控action
     */
    public static final String MONITOR_ALARM_BROADCAST = "com.ruitu.alarm.monitor.action";
    /**
     * 响铃action
     */
    public static final String ALARM_ALARM_BROADCAST = "com.ruitu.alarm.alarm.action";
    /**
     * 震动action
     */
    public static final String SHAKE_ALARM_BROADCAST = "com.ruitu.alarm.shake.action";
    /**
     * 实时详情默认选择的平台
     */
    public static final String BITSTAMP = "Bitstamp";
    public static final String BTCTRADE = "btcTrade";
    public static final String BTC_CHINA = "比特币中国";
    public static final String HUOBI = "火币网";
    public static final String OKCOIN = "OKCoin";
    public static final String FERA796 = "796期货";
    public static final String BITER = "比特儿";
    public static final String BTC_38 = "比特币时代";
    /**
     * 数码
     */
    public static final String PAGE = "page";
    /**
     * 新闻类型
     */
    public static final String NEWS_TYPE = "type";
    /**
     * 更新时间
     */
    public static final String UPDATE_TIME = "updatetime";
    /**
     * 网址
     */
    public static final String NET_ADDRESS = "net";
    /**
     * 订单时间
     */
    public static final String ORDER_TIME = "timestamp";
    /**
     * 最高价
     */
    public static final String HIGH = "high";
    /**
     * 最低价
     */
    public static final String LOW = "low";
    /**
     * 最近成交价 last
     */
    public static final String LAST = "last";
    /**
     * 成交量
     */
    public static final String VOLUME = "vol";
    /**
     * 买一价
     */
    public static final String BUY = "buy";
    /**
     * 卖一价
     */
    public static final String SELL = "sell";
    /**
     * 成交均价
     */
    public static final String VWAP = "vwap";
    /**
     * 委托买入
     */
    public static final String BIDS = "bids";
    /**
     * 委托卖出
     */
    public static final String ASKS = "asks";
    public static final String BUY_ORDERS = "buy_orders";
    public static final String SELL_ORDERS = "sell_orders";
    public static final String TRANSACTION = "transaction";
    public static final String HAS_NEXT = "hasNext";
    public static final String PLATFORM = "platform";
    /**
     * 成交时间
     */
    public static final String DATE = "date";
    /**
     * 成交价格
     */
    public static final String PRICE = "price";
    /**
     * 成交订单号
     */
    public static final String TID = "tid";
    /**
     * 成交量
     */
    public static final String AMOUNT = "amount";
    public static final String NEWS_LIST = "news";
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String CONTENT = "content";
    public static final String ICON = "icon";
    public static final String TIME = "time";
    public static final String SUCCESS = "success";
    public static final String INFO = "info";
    public static final int TICKER_TYPE = 10000001;
    public static final int DEPTH_TYPE = 10000002;

    /**
     * Usage: send a GET with the parameters: hashrate : hash rate in hashes /
     * second (NOT MH/s) difficulty : difficulty to use (optional - defaults to
     * current) exchange_rate : exchange rate to use for BTC -> dollar
     * calculations (optional - defaults to last BitStamp (or MtGox, if BitStamp
     * is down) price).
     * 
     * For example:
     * https://alloscomp.com/bitcoin/calculator/json?hashrate=900000000
     * 
     * Or with more parameters:
     * https://alloscomp.com/bitcoin/calculator/json?hashrate
     * =900000000&difficulty=300000&exchange_rate=10.0
     */
    public static final int ACTION_GET_CALCULATOR = 11;
    public static final int ACTION_GET_CALCULATOR_DEFAULT = 12;
    public static final int ACTION_CALCULATOR = 13;
    public static final int ACTION_GET_USD_ACTION = 14;
    public static final String BTC_GET_URL = "https://alloscomp.com/bitcoin/calculator/json";

    /**
     * hashrate : hash rate in hashes / second (NOT MH/s) h/s
     */
    public static final String HASHRATE = "hashrate";
    /**
     * difficulty : difficulty to use (optional - defaults to current)
     */
    public static final String DIFFICULTY = "difficulty";
    /**
     * exchange_rate : exchange rate to use for BTC -> dollar calculations
     * (optional - defaults to last BitStamp (or MtGox, if BitStamp is down)
     * price).
     */
    public static final int VER_BLOCK = 25;
    public static final int BTC_NUMBER = 2256;
    public static final String EXCHANGE_RATE = "exchange_rate";
    public static final String bc_per_block = "bc_per_block";
    public static final String coins_before_retarget = "coins_before_retarget";
    public static final String coins_per_hour = "coins_per_hour";
    public static final String coins_per_hour_after_retarget = "coins_per_hour_after_retarget";
    public static final String difficulty = "difficulty";
    public static final String dollars_before_retarget = "dollars_before_retarget";
    public static final String dollars_per_hour = "dollars_per_hour";
    public static final String dollars_per_hour_after_retarget = "dollars_per_hour_after_retarget";
    public static final String exchange_rate = "exchange_rate";
    public static final String exchange_rate_source = "exchange_rate_source";
    public static final String hashrate = "hashrate";
    public static final String next_difficulty = "next_difficulty";
    public static final String time_per_block = "time_per_block";

}
