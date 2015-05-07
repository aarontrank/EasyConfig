package com.codecoalition.util.easyconfig.test;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import com.codecoalition.util.easyconfig.EasyConfig;
import com.codecoalition.util.easyconfig.ElementMapping;

import junit.framework.TestCase;

public class EasyConfigTest extends TestCase {
	public void testLoad() {
		EasyConfig ec = new EasyConfig();
		ec.load("src"+File.separator+"com"+File.separator+"codecoalition"+File.separator+"util"+
				File.separator+"easyconfig"+File.separator+"test"+File.separator+"test.xml");
		HashMap<String, String> cfg = ec.getConfig();
		Iterator<Entry<String, String>> i = cfg.entrySet().iterator();
		while (i.hasNext()){
			Entry<String, String> e = i.next();
			System.out.println(e.getKey() + "=" + e.getValue());
		}
		System.out.println("<END OF TEST 1>\n\n");
	}
	
	public void testMapping() {
		ElementMapping em = new ElementMapping();
		em.addMapping("root.entry1", "r1");
		em.addMapping("root.entry2.innerentry", "r2I");
		em.addMapping("root.entry2.innerentry2", "r2I2");
		
		EasyConfig ec = new EasyConfig(em);
		ec.load("src"+File.separator+"com"+File.separator+"codecoalition"+File.separator+"util"+
				File.separator+"easyconfig"+File.separator+"test"+File.separator+"test.xml");
		HashMap<String, String> cfg = ec.getConfig();
		Iterator<Entry<String, String>> i = cfg.entrySet().iterator();
		while (i.hasNext()){
			Entry<String, String> e = i.next();
			System.out.println(e.getKey() + "=" + e.getValue());
		}
	}
}
