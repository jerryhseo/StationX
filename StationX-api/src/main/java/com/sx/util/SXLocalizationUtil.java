package com.sx.util;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class SXLocalizationUtil {
	static public Map<Locale, String> jsonToLocalizedMap( JSONObject localizedJson ){
		Iterator<String> langs = localizedJson.keys();
		Map<Locale, String> map = new HashMap<>();
		
		while( langs.hasNext() ) {
			String lang = langs.next();
			map.put(
					Locale.forLanguageTag(lang), 
					localizedJson.getString(lang)
			);
		}
		
		return map;
	}
	
	static public JSONObject mapToLocalizedJSON( Map<Locale, String> localizedMap){
		JSONObject json = JSONFactoryUtil.createJSONObject();
		
		for( Map.Entry<Locale, String> entry : localizedMap.entrySet() ) {
			json.put(entry.getKey().toLanguageTag(), entry.getValue());
		}
		
		return json;
	}
}
