package com.sx.util.visualizer.api;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;

public class VisualizerConfig {
	public boolean showBorders;
	public String portletWidth;
	public String portletHeight;
	public String portletScroll;
	public String disabled;
	public JSONObject initData;
	public String employer;
	public JSONObject menuOptions;
	public String namespace;
	public String portletId;
	
	public String getDisplayStyle() {
		String displayStyle = "";
		if( !portletHeight.isEmpty() ){
			displayStyle += "height:"+portletHeight+";";
		}
		if( !portletWidth.isEmpty() ){
			displayStyle += "width:"+portletWidth+";";
		}
		if( !portletScroll.isEmpty() ){
			displayStyle += "overflow:"+portletScroll+";";
		}
		
		return displayStyle;
	}
	
	public String toString() {
		JSONObject json = JSONFactoryUtil.createJSONObject();
		
		json.put("showBorders", showBorders);
		json.put("portletWidth", portletWidth);
		json.put("portletHeight", portletHeight);
		json.put("portletScroll", portletScroll);
		json.put("disabled", disabled);
		json.put("initData", initData);
		json.put("employer", employer);
		json.put("menuOptions", menuOptions);
		json.put("namespace", namespace);
		json.put("portletId", portletId);
		
		try {
			return json.toString( 4 );
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}
}
