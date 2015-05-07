package com.codecoalition.util.easyconfig;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ElementMapping {
	protected HashMap<String, String> configNameMapping;

	public ElementMapping(){
		configNameMapping = new HashMap<String, String>();
	}
	
	private String getKey(String value){
		if (configNameMapping == null || configNameMapping.size() < 1) return "";
		Collection<String> vals = configNameMapping.values();
		Set<String> kys	= configNameMapping.keySet();
		if (configNameMapping.containsValue(value)) {
			String[] values = new String[vals.size()];
			values = vals.toArray(values);
			String[] keys = new String[kys.size()];
			keys = kys.toArray(keys);
			for (int i = 0; i < values.length; i++){
				if (values[i].equals(value)){
					return keys[i];
				}
			}
			return "";
		} else {
			return "";
		}
	}
	
	public String getOriginalKey(String newKey) {
		return getKey(newKey);
	}
	
	public String getTranslatedKey(String oldKey){
		return configNameMapping.get(oldKey);
	}
	
	public String translate(String old){
		if (containsOriginalKey(old)){
			return getTranslatedKey(old);
		} else if (containsTranslatedKey(old)){
			return getOriginalKey(old);
		} else {
			return "";
		}
	}
	
	public boolean containsOriginalKey(String key){
		return configNameMapping.containsKey(key);
	}
	
	public boolean containsTranslatedKey(String key){
		return configNameMapping.containsValue(key);
	}
	
	public void addMapping(String oldKey, String newKey){
		configNameMapping.put(oldKey,newKey);
	}
	
	public void addMappings(Map<String, String> map){
		configNameMapping.putAll(map);
	}
}
