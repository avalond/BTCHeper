/**
 * @aAdministrator
 *
 * @下午1:15:34
 */
package com.ruitu.btchelper.setting;


import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

/**
 * @author 刘卫平
 * @since 2014年10月12日 下午1:15:34 Administrator
 */
public class SAXVersionService {
    public static String readRssXml(InputSource ins) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        VersionXMLContext hander = new VersionXMLContext();
        saxParser.parse(ins, hander);
        String version = hander.getVersion();
        System.out.println(version);
        return version;
    }
}
