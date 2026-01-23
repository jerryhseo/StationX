package com.sx.util;

import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.LiferayPortletMode;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.security.auth.AuthTokenUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.sx.util.portlet.SXPortletURLUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import javax.portlet.PortletModeException;
import javax.portlet.PortletRequest;
import javax.portlet.WindowStateException;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Reference;

public class SXPortalUtil {
	@Reference
	private static DLAppService _dlAppService;
	
	private static String STATIONX_DATA_DIR_KEY = "stationx-data-dir";
	
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
	
	public static final Path getDataDirPath( long companyId, long groupId, String strPath ) {
		Path dataBasePath = Paths.get( PropsUtil.get(STATIONX_DATA_DIR_KEY) );
		
		Path path = dataBasePath.resolve(companyId + "/"+groupId+"/"+strPath);
		
		return path;
	}
	
	/**
	 * 
	 * @param uploadRequest
	 * @param fields
	 * @param baseDir
	 * @return
	 */
	public static final JSONArray saveFiles( UploadPortletRequest uploadRequest, String[] fields, Path baseDir) {
		JSONArray errorFiles = JSONFactoryUtil.createJSONArray();
		
		for(String fileField : fields) {
			String[] fileNames = uploadRequest.getFileNames(fileField);
			String contentType = uploadRequest.getContentType(fileField);
			File[] files = uploadRequest.getFiles(fileField);
			
			if( Validator.isNotNull(files)) {
				for(int i=0; i<files.length; i++) {
					File file = files[i];
					String fileName = fileNames[i];
					System.out.println("FileName: " + file.getName());
					System.out.println("fileField: "+fileField + ",  : " + fileName +", "+contentType+", "+file.length() );
					
					// Choose where to save it
					Path destinationPath = baseDir.resolve( fileName);

					// Copy file to destination
					try ( InputStream in = new FileInputStream(file) ){
						Files.copy(in, destinationPath);
					} catch ( IOException e ) {
						JSONObject errorFile = JSONFactoryUtil.createJSONObject();
						errorFile.put("fileName", file.getName());
						errorFile.put("error", "duplicated");
						errorFiles.put(errorFile);
					}
				}
			}
		}
		
		return errorFiles;
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
