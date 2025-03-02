package com.sx.util.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.servlet.http.HttpServletRequest;

public class SXPortletURLUtil {
	public static final String createURL( 
					String friendlyURLKey, 
					boolean privateURL, 
					PortletRequest portletRequest,
					ThemeDisplay themeDisplay,
					String portletId, 
					Map<String, String[]> paramMap,
					PortletMode portletMode,
					WindowState windowState) throws PortalException, PortletModeException, WindowStateException {
		
		HttpServletRequest httpServletRequest = PortalUtil.getOriginalServletRequest( PortalUtil.getHttpServletRequest(portletRequest) );
		
		String friendlyURL = PropsUtil.get(friendlyURLKey);
		Layout layoutView = LayoutLocalServiceUtil.getFriendlyURLLayout(
																	themeDisplay.getSiteGroupId(), 
																	false,  
																	friendlyURL );
		
		PortletURL url = PortletURLFactoryUtil.create(
													httpServletRequest,
													portletId, 
													layoutView.getPlid(),
													PortletRequest.RENDER_PHASE);
		
		url.setPortletMode(portletMode);
		url.setWindowState(windowState);
		
		if( Validator.isNotNull(paramMap) && paramMap.size() > 0  ) {
			url.setParameters( paramMap );
		}
		
		return url.toString(); 
	}
	
	public static final String createURL( 
					PortletRequest portletRequest,
					ThemeDisplay themeDisplay,
					String portletId, 
					Map<String, String[]> paramMap,
					PortletMode portletMode,
					WindowState windowState) throws PortalException, PortletModeException, WindowStateException {
		
		HttpServletRequest httpServletRequest = PortalUtil.getOriginalServletRequest( PortalUtil.getHttpServletRequest(portletRequest) );
		
		PortletURL url = PortletURLFactoryUtil.create(
													httpServletRequest,
													portletId, 
													themeDisplay.getPlid(),
													PortletRequest.RENDER_PHASE);
		
		url.setPortletMode(portletMode);
		url.setWindowState(windowState);
		
		if( Validator.isNotNull(paramMap) && paramMap.size() > 0  ) {
			url.setParameters( paramMap );
		}
	
		return url.toString(); 
	}
	
	public static final String createURL( 
			PortletRequest portletRequest,
			ThemeDisplay themeDisplay,
			String portletId, 
			PortletMode portletMode,
			WindowState windowState) throws PortalException, PortletModeException, WindowStateException {

		HttpServletRequest httpServletRequest = PortalUtil.getOriginalServletRequest( PortalUtil.getHttpServletRequest(portletRequest) );
		
		PortletURL url = PortletURLFactoryUtil.create(
													httpServletRequest,
													portletId, 
													themeDisplay.getPlid(),
													PortletRequest.RENDER_PHASE);
		
		url.setPortletMode(portletMode);
		url.setWindowState(windowState);
		
		return url.toString(); 
	}
}
