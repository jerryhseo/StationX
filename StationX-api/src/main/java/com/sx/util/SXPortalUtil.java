package com.sx.util;

import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.LiferayPortletMode;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.security.auth.AuthTokenUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.sx.util.portlet.SXPortletURLUtil;

import java.util.Random;

import javax.portlet.PortletModeException;
import javax.portlet.PortletRequest;
import javax.portlet.WindowStateException;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Reference;

public class SXPortalUtil {
	@Reference
	private static DLAppService _dlAppService;
	
	public static final String getAuthToken( PortletRequest portletRequest ) {
		HttpServletRequest httpServletRequest = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(portletRequest) );
		
		System.out.println("AuthToken: " +  AuthTokenUtil.getToken(httpServletRequest) );
		return AuthTokenUtil.getToken(httpServletRequest);
	}
	
	public static JSONObject createPortletInstanceId(  PortletRequest portletRequest, String portletName ) throws PortalException, PortletModeException, WindowStateException {
		final String instance = "_INSTANCE_";

		String portletInstanceId = SXPortalUtil.generatePortletInstanceId();
		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
		
		String portletURL = SXPortletURLUtil.createURL(
				portletRequest, 
				themeDisplay, 
				portletName+instance+portletInstanceId,
				null,
				LiferayPortletMode.VIEW,
				LiferayWindowState.EXCLUSIVE );
		 
		JSONObject portletInstance = JSONFactoryUtil.createJSONObject();
		portletInstance.put( "url" , portletURL );
		portletInstance.put( "portletId", portletName+instance+portletInstanceId);
		portletInstance.put( "namespace", "_"+portletName+instance+portletInstanceId+"_" );
		
		return portletInstance;
	}
	
	public static final String generatePortletInstanceId() {
		final int length = 12;
		
		String alphanumericCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
		
		StringBuffer randomString = new StringBuffer(length);
		Random random = new Random();
		
		for( int i=0; i<length; i++ ) {
			int randomIndex = random.nextInt(alphanumericCharacters.length());
			char randomChar = alphanumericCharacters.charAt(randomIndex);
			randomString.append(randomChar);
		}
		
		return randomString.toString();
	}
	
	@Deprecated
	public static final long getDataFileFolderId(
			PortletRequest portletRequest, 
			long repositoryId,
			long parentFoderId,
			String dataTypeName,
			String dataTypeVersion,
			String termName,
			String termVersion,
			long dataId) throws PortalException {
	
		long parentFolderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;
		String rootDataFolderName = "Data Files";
		String description = "Folder to save uploaded files of structured data";
		
		long rootFolderId = getFolderId(portletRequest, repositoryId, parentFolderId, rootDataFolderName);
		long dataTypeFolderId = getFolderId(portletRequest, repositoryId, rootFolderId, dataTypeName);
		long dataTypeVersionFolderId = getFolderId(portletRequest, repositoryId, dataTypeFolderId, dataTypeVersion);
		long termNameFolderId = getFolderId(portletRequest, repositoryId, dataTypeVersionFolderId, termName);
		long termVersionFolderId = getFolderId(portletRequest, repositoryId, termNameFolderId, termVersion);
		long folderId = getFolderId(portletRequest, repositoryId, dataTypeVersionFolderId, String.valueOf(dataId) );
		
		return folderId;
	}
	
	@Deprecated
	private static final long getFolderId( PortletRequest portletRequest, long repositoryId, long parentFolderId, String folderName ) throws PortalException{
		Folder folder = null;
		try {
			folder = DLAppServiceUtil.getFolder(repositoryId, parentFolderId, folderName);
		} catch (PortalException e) {
			ServiceContext sc =  ServiceContextFactory.getInstance(DLFolder.class.getName(), portletRequest );
			folder = DLAppServiceUtil.addFolder(repositoryId, parentFolderId, folderName, "", sc);
		}
		
		return folder.getFolderId();
	}
}
