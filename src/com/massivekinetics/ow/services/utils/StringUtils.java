package com.massivekinetics.ow.services.utils;

public class StringUtils {

	public static boolean isNullOrEmpty(String string) {
		return string == null || 
				string.equals("");
	}
}
