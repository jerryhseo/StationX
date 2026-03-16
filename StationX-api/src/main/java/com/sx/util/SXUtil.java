package com.sx.util;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SXUtil {
	public static JSONArray convertListToJSONArray ( List<?> list ) {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray ( list );

		return jsonArray;
	}

	public static List<?> convertJSONArrayToList ( JSONArray jsonArray ) {
		List<Object> list = new ArrayList<> ();

		for ( int i = 0; i < jsonArray.length (); i++ ) {
			Object value = jsonArray.get ( i );

			if ( value instanceof JSONArray ) {
				// Recursively convert nested JSONArray
				value = convertJSONArrayToList ( ( JSONArray ) value );
			} else if ( value instanceof JSONObject ) {
				// Convert JSONObject to a Map (optional)
				value = convertJSONObjectToMap ( ( JSONObject ) value );
			}

			list.add ( value );
		}

		return list;

	}

	public static Map<String, Object> convertJSONObjectToMap ( JSONObject jsonObject ) {
		Map<String, Object> map = new HashMap<> ();

		Iterator<String> keys = jsonObject.keys ();
		while ( keys.hasNext () ) {
			String key = keys.next ();

			map.put ( key, jsonObject.get ( key ) );
		}

		return map;
	}

	public static JSONObject convertMapToJSONObject ( Map<String, Object> map ) {
		JSONObject json = JSONFactoryUtil.createJSONObject ();

		Iterator<String> keys = map.keySet ().iterator ();
		while ( keys.hasNext () ) {
			String key = keys.next ();
			json.put ( key, map.get ( key ) );
		}

		return json;
	}

	public static boolean contains ( int[] array, int value ) {
		for ( int n : array ) {
			if ( n == value )
				return true;
		}
		return false;
	}

	public static boolean contains ( long[] array, long value ) {
		for ( long n : array ) {
			if ( n == value )
				return true;
		}
		return false;
	}

	public static boolean contains ( double[] array, double value ) {
		for ( double n : array ) {
			if ( n == value )
				return true;
		}
		return false;
	}

	public static boolean contains ( char[] array, char value ) {
		for ( char c : array ) {
			if ( c == value )
				return true;
		}
		return false;
	}

	public static boolean contains ( String[] array, String value ) {
		for ( String s : array ) {
			if ( s.equals ( value ) )
				return true;
		}
		return false;
	}

}
