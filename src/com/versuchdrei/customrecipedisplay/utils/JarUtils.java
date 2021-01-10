package com.versuchdrei.customrecipedisplay.utils;

import java.net.URISyntaxException;

import org.bukkit.Bukkit;

/**
 * a util class for methods regarding the jar file
 * @author VersuchDrei
 * @version 1.0
 */
public class JarUtils {
	
	public static String getServerJarPath() {
		try {
			return Bukkit.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		} catch (final URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

}
