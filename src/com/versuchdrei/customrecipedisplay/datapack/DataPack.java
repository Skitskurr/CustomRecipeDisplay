package com.versuchdrei.customrecipedisplay.datapack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.versuchdrei.customrecipedisplay.FolderFilter;
import com.versuchdrei.customrecipedisplay.recipe.Recipe;
import com.versuchdrei.customrecipedisplay.recipe.ingredient.Ingredient;

/**
 * a class for representing a datapack
 * @author VersuchDrei
 * @version 1.0
 */
public class DataPack {
	
	static final String DATA_PATH = "data/";
	
	private final String path;
	private final List<NameSpace> namespaces = new ArrayList<>();
	
	public DataPack(final String path) {
		this.path = path;
		
		registerNamespaces();
	}
	
	private void registerNamespaces() {
		final String dataPath = this.path + DataPack.DATA_PATH;
		final File dataFolder = new File(dataPath);
		if(!dataFolder.exists()) {
			return;
		}
		
		final String[] namespaceArray = dataFolder.list(new FolderFilter());
		
		for(final String namespace: namespaceArray) {
			namespaces.add(new NameSpace(namespace, dataPath + namespace + "/"));
		}
	}
	
	public void registerTags(final Map<String, Tag> tags) {
		for(final NameSpace namespace: namespaces) {
			namespace.registerTags(tags);
		}
	}
	
	public void registerRecipes(final List<Recipe> recipes, final Map<String, Ingredient> ingredients) {
		for(final NameSpace namespace: namespaces) {
			namespace.registerRecipes(recipes, ingredients);
		}
	}

}
