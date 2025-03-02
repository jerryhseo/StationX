package com.sx.debug;

import com.liferay.portal.kernel.util.ParamUtil;

import java.util.Enumeration;

import javax.portlet.PortletRequest;

public class Debug {
	public static final void printHeader( String title ) {
		System.out.println("===== Start "+title+" =====");
	}
	public static final void printFooter( String title ) {
		System.out.println("===== End of "+title+" =====");
	}
	
	public static final void printAllParameters( PortletRequest portletRequest ) {
		System.out.println("+++ All Parametrs +++");
		Enumeration<String> keys = portletRequest.getParameterNames();
		while( keys.hasMoreElements() ) {
			String key = keys.nextElement();
			
			System.out.println(key + ": " + ParamUtil.getString(portletRequest, key));
		}
	}
	
	public static final void printAllAttributes( PortletRequest portletRequest ) {
		System.out.println("+++ All Attributes +++");
		Enumeration<String> keys = portletRequest.getAttributeNames();
		while( keys.hasMoreElements() ) {
			String key = keys.nextElement();
			
			System.out.println(key + ": " + ParamUtil.getString(portletRequest, key));
		}
	}
}
