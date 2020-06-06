package com.skitskurr.customrecipedisplay.datapack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;

import com.skitskurr.customrecipedisplay.FolderFilter;
import com.skitskurr.customrecipedisplay.recipe.Recipe;
import com.skitskurr.customrecipedisplay.recipe.ingredient.Ingredient;

public class DataPack {
	
	private static final String DATA_PATH = "data/";
	
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
	
	public void registerTags(final Map<String, Set<Material>> tags) {
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
