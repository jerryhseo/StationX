package com.sx.util;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.sx.constant.StationXPropertyKeys;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.portlet.PortletRequest;

public final class SXFileUtil {
	static public final String SX_DATA_DIR = PropsUtil.get ( StationXPropertyKeys.STATIONX_DATA_FOLDER_PROPERTY_KEY );

	static public final Path getDataGroupFolderPath ( PortletRequest portletRequest ) {
		ThemeDisplay themeDisplay = ( ThemeDisplay ) portletRequest.getAttribute ( WebKeys.THEME_DISPLAY );

		Path folderPath = Paths.get ( SX_DATA_DIR );

		folderPath = folderPath.resolve ( String.valueOf ( themeDisplay.getCompanyId () ) );
		folderPath = folderPath.resolve ( String.valueOf ( themeDisplay.getScopeGroupId () ) );
		folderPath = folderPath.resolve ( "dataFiles" );

		return folderPath;
	}

	static public final Path getDataGroupFolderPath ( long companyId, long groupId ) {
		Path folderPath = Paths.get ( SX_DATA_DIR );

		folderPath = folderPath.resolve ( String.valueOf ( companyId ) );
		folderPath = folderPath.resolve ( String.valueOf ( groupId ) );

		return folderPath;
	}

	/**
	 * 
	 * @param uploadRequest
	 * @param fields
	 * @param baseDir
	 * @return JSON
	 */
	public static final JSONArray UploadFieldFiles (
				UploadPortletRequest uploadRequest, String[] upLoadFields, Path baseDir
	) {
		JSONArray errorFiles = JSONFactoryUtil.createJSONArray ();

		for ( String fileField : upLoadFields ) {
			String[] fileNames = uploadRequest.getFileNames ( fileField );
			String contentType = uploadRequest.getContentType ( fileField );
			File[] files = uploadRequest.getFiles ( fileField );

			if ( Validator.isNotNull ( files ) ) {
				for ( int i = 0; i < files.length; i++ ) {
					File file = files[i];
					String fileName = fileNames[i];
					System.out.println ( "FileName: " + file.getName () );
					System.out.println (
								"fileField: " + fileField + ",  : " + fileName + ", " + contentType + ", "
											+ file.length ()
					);

					// Choose where to save it
					Path destinationPath = baseDir.resolve ( fileName );

					// Copy file to destination
					try ( InputStream in = new FileInputStream ( file )) {
						Files.copy ( in, destinationPath );
					} catch ( IOException e ) {
						JSONObject errorFile = JSONFactoryUtil.createJSONObject ();
						errorFile.put ( "fileName", file.getName () );
						errorFile.put ( "error", "duplicated" );
						errorFiles.put ( errorFile );
					}
				}
			}
		}

		return errorFiles;
	}

	public static final JSONArray saveUploadFieldFiles (
				UploadPortletRequest uploadRequest, String uploadFieldName, Path baseDir
	) {
		JSONArray errorFiles = JSONFactoryUtil.createJSONArray ();

		String[] fileNames = uploadRequest.getFileNames ( uploadFieldName );
		String contentType = uploadRequest.getContentType ( uploadFieldName );
		File[] files = uploadRequest.getFiles ( uploadFieldName );

		if ( Validator.isNotNull ( files ) ) {
			for ( int i = 0; i < files.length; i++ ) {
				File file = files[i];
				String fileName = fileNames[i];
				System.out.println ( "FileName: " + file.getName () );
				System.out.println (
							"fileField: " + uploadFieldName + ",  : " + fileName + ", " + contentType + ", "
										+ file.length ()
				);

				// Choose where to save it
				Path destinationPath = baseDir.resolve ( fileName );

				// Copy file to destination
				try ( InputStream in = new FileInputStream ( file )) {
					Files.copy ( in, destinationPath );
				} catch ( IOException e ) {
					JSONObject errorFile = JSONFactoryUtil.createJSONObject ();
					errorFile.put ( "fileName", file.getName () );
					errorFile.put ( "error", "duplicated" );
					errorFiles.put ( errorFile );
				}
			}
		}

		return errorFiles;
	}

	/**
	 * Empty all child folders and files of the folderPath. If folderPath doesn't exit, it creates the
	 * folder including all parent folders to reach the folder.
	 * 
	 * @param folderPath
	 * @param create
	 * @throws IOException
	 */
	public static final void emptyFolder ( Path folderPath, boolean create ) throws IOException {
		if ( Files.exists ( folderPath ) ) {
			try ( Stream<Path> walk = Files.walk ( folderPath )) {
				walk.filter ( path -> !path.equals ( folderPath ) ) // skip the folder itself
							.sorted ( ( a, b ) -> b.compareTo ( a ) ) // delete children before parents
							.forEach ( path -> {
								try {
									Files.delete ( path );
								} catch ( Exception e ) {
									throw new RuntimeException ( e );
								}
							} );
			} catch ( IOException e ) {
				e.printStackTrace ();

				throw e;
			}
		} else {
			Files.createDirectories ( folderPath );
		}
	}

	public static final boolean isFolderEmpty ( Path folderPath ) throws IOException {
		DirectoryStream<Path> dirStream = Files.newDirectoryStream ( folderPath );

		return !dirStream.iterator ().hasNext ();
	}

	/**
	 * Delete the folderPath, even if the folder has children folders or files.
	 * 
	 * @param folderPath
	 * @throws IOException
	 */
	public static final void deleteFolder ( Path folderPath ) throws IOException {
		if ( Files.exists ( folderPath ) ) {
			emptyFolder ( folderPath, false );

			Files.delete ( folderPath );
		}
	}

	public static final void deleteFile ( Path filePath ) throws IOException {
		if ( Files.exists ( filePath ) ) {
			Files.delete ( filePath );
		}
	}

	public static final List<String> lookUpFolder ( Path folderPath ) {
		List<String> fileList = new ArrayList<> ();

		System.out.println ( "dataFileFolderPath: " + folderPath );
		if ( Files.notExists ( folderPath ) ) {
			return fileList;
		}

		try ( DirectoryStream<Path> stream = Files.newDirectoryStream ( folderPath )) {
			for ( Path entry : stream ) {
				if ( Files.isRegularFile ( entry ) ) {
					System.out.println ( entry.getFileName () );

					fileList.add ( entry.getFileName ().toString () );
				}
			}
		} catch ( IOException e ) {
			e.printStackTrace ();
		}

		return fileList;
	}

	/*
	 * public static final List<JSONObject> lookUpFolder ( Path folderPath ) { List<JSONObject> fileList
	 * = new ArrayList<> ();
	 * 
	 * try ( DirectoryStream<Path> stream = Files.newDirectoryStream ( folderPath )) { for ( Path entry
	 * : stream ) { if ( Files.isRegularFile ( entry ) ) { JSONObject file =
	 * JSONFactoryUtil.createJSONObject (); System.out.println ( entry.getFileName () );
	 * 
	 * file.put ( "fileName", entry.getFileName () );
	 * 
	 * BasicFileAttributes fileAttributes = Files.readAttributes ( folderPath,
	 * BasicFileAttributes.class, null );
	 * 
	 * file.put ( "lastModifiedTime", fileAttributes.lastModifiedTime () ); } } } catch ( IOException e
	 * ) { e.printStackTrace (); }
	 * 
	 * return fileList; }
	 */
}
