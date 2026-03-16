package com.sx.util;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.Validator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.portlet.PortletRequest;

public class SXLocalizationUtil {
	static public Map<Locale, String> jsonToLocalizedMap ( JSONObject localizedJson ) {
		Map<Locale, String> map = new HashMap<> ();

		if ( Validator.isNotNull ( localizedJson ) ) {
			Iterator<String> langs = localizedJson.keys ();
			while ( langs.hasNext () ) {
				String lang = langs.next ();
				map.put ( Locale.forLanguageTag ( lang ), localizedJson.getString ( lang ) );
			}
		}

		return map;
	}

	static public JSONObject mapToLocalizedJSON ( Map<Locale, String> localizedMap ) {
		JSONObject json = JSONFactoryUtil.createJSONObject ();

		if ( Validator.isNotNull ( localizedMap ) ) {
			for ( Map.Entry<Locale, String> entry : localizedMap.entrySet () ) {
				json.put ( entry.getKey ().toLanguageTag (), entry.getValue () );
			}
		}

		return json;
	}

	static public Map<Locale, String> jsonToLocalizedMap ( String localizedString ) throws JSONException {
		JSONObject localizedJson = JSONFactoryUtil.createJSONObject ( localizedString );

		Map<Locale, String> map = new HashMap<> ();

		if ( Validator.isNotNull ( localizedJson ) ) {
			Iterator<String> langs = localizedJson.keys ();
			while ( langs.hasNext () ) {
				String lang = langs.next ();
				map.put ( Locale.forLanguageTag ( lang ), localizedJson.getString ( lang ) );
			}
		}

		return map;
	}

	public static String translate ( PortletRequest request, String key ) {
		return LanguageUtil.get ( request.getLocale (), key );
	}

	public static String translate ( PortletRequest request, String key, String... args ) {
		return LanguageUtil.format ( request.getLocale (), key, Arrays.stream ( args ).toArray () );
	}

	public static String translate ( PortletRequest request, String key, int... args ) {
		return LanguageUtil.format ( request.getLocale (), key, Arrays.stream ( args ).boxed ().toArray () );
	}

	public static String translate ( PortletRequest request, String key, long... args ) {
		return LanguageUtil.format ( request.getLocale (), key, Arrays.stream ( args ).boxed ().toArray () );
	}

	public static String translate ( PortletRequest request, String key, boolean... args ) {
		Object[] ary = new Object[args.length];

		for ( int i = 0; i < args.length; i++ ) {
			ary[i] = new Boolean ( args[i] );
		}

		return LanguageUtil.format ( request.getLocale (), key, ary );
	}
}
