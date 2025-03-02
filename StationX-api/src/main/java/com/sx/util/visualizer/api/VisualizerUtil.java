package com.sx.util.visualizer.api;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.sx.constant.RepositoryTypes;

import java.io.IOException;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.RenderRequest;
import javax.portlet.ValidatorException;

/**
 * @author jerry
 */
public class VisualizerUtil {
	public static VisualizerConfig getVisualizerConfig( RenderRequest renderRequest, PortletDisplay portletDisplay, User user ) {
		VisualizerConfig config = new VisualizerConfig();
		
		boolean showBorders = ParamUtil.getBoolean( renderRequest, "showBorders", false );
		
		PortletPreferences preferences = portletDisplay.getPortletSetup();
		config.showBorders = showBorders;
		try {
			preferences.setValue("portletSetupShowBorders", String.valueOf(showBorders));
			preferences.store();
		} catch (ReadOnlyException | ValidatorException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		config.namespace = portletDisplay.getNamespace();
		config.portletId = portletDisplay.getId();
		
		config.portletWidth = preferences.getValue("portletWidth", "");
		config.portletHeight = preferences.getValue("portletHeight", "");
		config.portletScroll = preferences.getValue("portletScroll", "");
		
		config.disabled = ParamUtil.getString(renderRequest, "disabled", "false");
		config.employer = ParamUtil.getString(renderRequest, "employer", "");
		
		try {
			config.menuOptions = JSONFactoryUtil.createJSONObject( ParamUtil.getString(renderRequest, "menuOptions", "{}" ) );
			if( config.menuOptions.length() == 0) {
				config.menuOptions.put("menu",GetterUtil.getBoolean(preferences.getValue("menu", "false")));
				config.menuOptions.put("sample",GetterUtil.getBoolean(preferences.getValue("sample", "false")));
				config.menuOptions.put("openLocalFile",GetterUtil.getBoolean(preferences.getValue("openLocalFile", "false")));
				config.menuOptions.put("openServerFile",GetterUtil.getBoolean(preferences.getValue("openServerFile", "false")));
				config.menuOptions.put("saveAsLocalFile",GetterUtil.getBoolean(preferences.getValue("saveAsLocalFile", "false")));
				config.menuOptions.put("saveAsServerFile",GetterUtil.getBoolean(preferences.getValue("saveAsServerFile", "false")));
				config.menuOptions.put("saveAsDBRecord",GetterUtil.getBoolean(preferences.getValue("saveAsDBRecord", "false")));
				config.menuOptions.put("download",GetterUtil.getBoolean(preferences.getValue("download", "false")));
			}
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		//Set initial repository 
		try {
			config.initData = JSONFactoryUtil.createJSONObject( ParamUtil.getString(renderRequest, "initData", "{}") );
			String portletRepository = GetterUtil.getString(preferences.getValue("portletRepository", RepositoryTypes.USER_HOME.toString()));
			String repositoryType = config.initData.getString("repositoryType", portletRepository);
			config.initData.put("repositoryType", repositoryType );
			String userScreenName = config.initData.getString("user", user.getScreenName() );
			config.initData.put("user", userScreenName );
		} catch( JSONException e ) {
			config.initData = JSONFactoryUtil.createJSONObject();
		}
		
		return config;
	}
}