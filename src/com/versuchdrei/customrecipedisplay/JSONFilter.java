package com.versuchdrei.customrecipedisplay;

import java.io.File;
import java.io.FilenameFilter;

/**
 * a filename filter for .json files
 * @author VersuchDrei
 * @version 1.0
 */
public class JSONFilter implements FilenameFilter{

	@Override
	public boolean accept(final File dir, final String name) {
		return name.endsWith(".json");
	}

}
