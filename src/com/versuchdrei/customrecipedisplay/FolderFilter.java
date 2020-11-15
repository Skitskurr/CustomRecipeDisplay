package com.versuchdrei.customrecipedisplay;

import java.io.File;
import java.io.FilenameFilter;

/**
 * a filename filter for folders
 * @author VersuchDrei
 * @version 1.0
 */
public class FolderFilter implements FilenameFilter{

	@Override
	public boolean accept(final File dir, final String name) {
		return new File(dir, name).isDirectory();
	}
}
