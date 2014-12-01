package com.ruitu.btchelper.util;

import java.util.ArrayList;
import java.util.List;

public class PlatformNameUtils implements DataHelper {
    public static List<String> getPlatFormList() {
        List<String> list = new ArrayList<String>();
        list.add(BITSTAMP);
        list.add(BTCTRADE);
        list.add(BTC_38);
        list.add(BTC_CHINA);
        list.add(FERA796);
        list.add(OKCOIN);
        list.add(BITER);
        list.add(HUOBI);
        return list;
    }
}
