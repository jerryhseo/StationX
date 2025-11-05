package com.sx.util;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;

import java.util.List;

public class SXUtil {
	public static JSONArray convertListToJSONArray(List<?> list) {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray(list);
		
		return jsonArray;
	}
}
