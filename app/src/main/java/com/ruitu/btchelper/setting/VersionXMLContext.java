package com.ruitu.btchelper.setting;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
public class VersionXMLContext extends DefaultHandler {
	private String version;
	private String preTag;

	public String getVersion() {
		return version;
	}

	@Override
	public void startDocument() throws SAXException {
		version = new String();
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		preTag = qName;
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		preTag = null;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String data = new String(ch, start, length);
		if ("version".equals(preTag)) {
			version = data;
		}
	}

}
