package com.skitskurr.customrecipedisplay;

import java.io.File;
import java.io.FilenameFilter;

public class FolderFilter implements FilenameFilter{

	@Override
	public boolean accept(final File dir, final String name) {
		return new File(dir, name).isDirectory();
	}
}
