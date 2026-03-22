package com.sx.util;

import java.security.SecureRandom;

public class SXStringUtil {

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	private static final SecureRandom random = new SecureRandom ();

	public static String generateRandomString ( int length ) {
		StringBuilder result = new StringBuilder ( length );

		for ( int i = 0; i < length; i++ ) {
			int index = random.nextInt ( CHARACTERS.length () );
			result.append ( CHARACTERS.charAt ( index ) );
		}

		return result.toString ();
	}

}
