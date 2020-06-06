package com.skitskurr.customrecipedisplay;

import java.io.File;
import java.io.FilenameFilter;

public class JSONFilter implements FilenameFilter{

	@Override
	public boolean accept(final File dir, final String name) {
		return name.endsWith(".json");
	}

}
