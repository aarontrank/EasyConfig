package com.codecoalition.util.easyconfig.test;

import java.util.HashMap;
import java.util.Iterator;

import com.codecoalition.util.easyconfig.ElementMapping;

import junit.framework.TestCase;

public class ElementMappingTest extends TestCase {

	protected class TestMapping extends ElementMapping {
		public TestMapping(){
			configNameMapping = new HashMap<String, String>();
			this.addMapping("oldkey1.subkey1.subkey2", "old1sub2");
			this.addMapping("oldkey1.subkey1.subkey3", "old1sub3");
			this.addMapping("oldkey1.subkey17", "old1sub17");
			this.addMapping("oldkey17.subkey1", "old17sub1");
		}
	}
	
	public void testMapping() {
		TestMapping tmap = new TestMapping();
		assertTrue(tmap.containsOriginalKey("oldkey1.subkey17"));
		assertFalse(tmap.containsOriginalKey("old1sub17"));
		assertTrue(tmap.containsTranslatedKey("old1sub17"));
		assertFalse(tmap.containsTranslatedKey("oldkey1.subkey17"));
		assertEquals("old1sub3", tmap.getTranslatedKey("oldkey1.subkey1.subkey3"));
		assertEquals("oldkey1.subkey1.subkey3", tmap.getOriginalKey("old1sub3"));
		assertEquals("", tmap.translate("asdfasdf"));
		assertEquals("old1sub3", tmap.translate("oldkey1.subkey1.subkey3"));
		assertEquals("oldkey1.subkey1.subkey3", tmap.translate("old1sub3"));
		assertEquals("old1sub3", tmap.translate(tmap.translate("old1sub3")));
	}
}
