package com.sx.util;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class SXLocalizationUtil {
	static public Map<Locale, String> jsonToLocalizedMap( JSONObject localizedJson ){
		Map<Locale, String> map = new HashMap<>();

		if(Validator.isNotNull(localizedJson)) {
			Iterator<String> langs = localizedJson.keys();
			while( langs.hasNext() ) {
				String lang = langs.next();
				map.put(
						Locale.forLanguageTag(lang), 
						localizedJson.getString(lang)
				);
			}
		}
		
		return map;
	}
	
	static public JSONObject mapToLocalizedJSON( Map<Locale, String> localizedMap){
		JSONObject json = JSONFactoryUtil.createJSONObject();
		
		if(Validator.isNotNull(localizedMap)) {
			for( Map.Entry<Locale, String> entry : localizedMap.entrySet() ) {
				json.put(entry.getKey().toLanguageTag(), entry.getValue());
			}
		}
		
		return json;
	}
	
	static public Map<Locale, String> jsonToLocalizedMap( String localizedString ) throws JSONException{
		JSONObject localizedJson = JSONFactoryUtil.createJSONObject(localizedString);
		
		Map<Locale, String> map = new HashMap<>();
		
		if(Validator.isNotNull(localizedJson)) {
			Iterator<String> langs = localizedJson.keys();
			while( langs.hasNext() ) {
				String lang = langs.next();
				map.put(
						Locale.forLanguageTag(lang), 
						localizedJson.getString(lang)
						);
			}
		}
		
		return map;
	}
}
