package com.codecoalition.util.easyconfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class EasyConfig {
	private static boolean DEBUG = true;
	private HashMap<String, String> config;
	private ElementMapping elementMap;
	
	public EasyConfig() { 
		elementMap = null;
	}
	
	public EasyConfig(ElementMapping map){
		elementMap = map;
	}
	
	public void load(String path){
		ConfigHandler cf;
		if (elementMap != null) {
			cf = new ConfigHandler(elementMap);
		} else {
			cf = new ConfigHandler();
		}
		try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setContentHandler(cf);
			xmlReader.parse(path);
		} catch (SAXException e) {
			// Error reading the XML
			if (EasyConfig.DEBUG) {
				e.printStackTrace();
			}
		} catch (IOException i) {
			// Error reading the file
			if (EasyConfig.DEBUG) {
				i.printStackTrace();
			}
		} finally {
			config = cf.getConfig();
		}
	}
	
	public HashMap<String, String> getConfig(){
		return config;
	}
	
	// -- any instance --

	//has element
	public boolean hasKey(String key) {
		return config.containsKey(key);
	}
	
	//get value
	public String getValue(String key){
		return config.get(key);
	}
	
	//get attribute by name
	public String getAttribute(String key, String attribute){
		String nKey = key + "#" + attribute;
		return config.get(nKey);
	}
	
	public HashMap<String,String> getAttributes(String key){
		HashMap<String, String> ret = new HashMap<String, String>();
		Iterator<String> keys = config.keySet().iterator();
		while (keys.hasNext()) {
			String k = keys.next();
			String[] splits  = k.split("#");
			if (splits[0].equals(key)) {
				ret.put(splits[splits.length - 1], config.get(k));
			}
		}
		return ret;
	}
	//get set of attributes
	
	// -- if not using an Element Mapping -- (or with additional element name mapping features)
	// get parent
	// get children
	// get child by name
	// has children
	// has parent
	
	
	protected class ConfigHandler extends DefaultHandler {
		private StringBuffer currentValue;
		private StringBuffer currentElement;
		private HashMap<String, String> configMap;
		private ElementMapping eMap;
		
		public ConfigHandler(){
			currentElement = new StringBuffer();
			currentValue = new StringBuffer();
			configMap = new HashMap<String, String>();
			eMap = null;
		}
		
		public ConfigHandler(ElementMapping elemMap){
			currentElement = new StringBuffer();
			currentValue = new StringBuffer();
			configMap = new HashMap<String, String>();
			eMap = elemMap;
		}
		
		public HashMap<String, String> getConfig(){
			return configMap;
		}
		
		@Override
		public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
			currentValue = new StringBuffer();
			if (currentElement.toString().equals("")) {
				currentElement = new StringBuffer(localName);
			} else {
				currentElement.append(".");
				currentElement.append(localName);
			}
			for (int i = 0; i < attributes.getLength(); i++) {
				StringBuffer attName = new StringBuffer(currentElement.toString());
				//translate the element name if an element name map has been specified
				if (eMap != null && eMap.containsOriginalKey(attName.toString())) {
					attName = new StringBuffer(eMap.getTranslatedKey(attName.toString()));
				}
				attName.append("#");
				attName.append(attributes.getQName(i));
				configMap.put(attName.toString(),attributes.getValue(i));
			}
			
		}

		@Override
		public void endElement(String uri, String localName, String name) throws SAXException {
			if (!currentValue.toString().trim().equals("")) {
				if (eMap != null && eMap.containsOriginalKey(currentElement.toString())) {
					configMap.put(eMap.getTranslatedKey(currentElement.toString()), currentValue.toString());
				} else {
					configMap.put(currentElement.toString(), currentValue.toString());
				}
			}
			currentValue = new StringBuffer();
			if (currentElement.length() > localName.length()) {
				currentElement = new StringBuffer(currentElement.substring(0, (currentElement.length() - (localName.length() + 1))));
			} else {
				currentElement = new StringBuffer(currentElement.substring(0, (currentElement.length() - localName.length())));
			}
		}
		
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			char[] nch = new char[length];
			for (int i=0; i < length; i++){
				nch[i] = ch[start+i];
			}
			currentValue.append(nch);
		}
		
	}
}
