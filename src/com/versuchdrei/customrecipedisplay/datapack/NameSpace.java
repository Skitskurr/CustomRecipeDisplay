package com.versuchdrei.customrecipedisplay.datapack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.versuchdrei.customrecipedisplay.JSONFilter;
import com.versuchdrei.customrecipedisplay.recipe.Recipe;
import com.versuchdrei.customrecipedisplay.recipe.RecipeType;
import com.versuchdrei.customrecipedisplay.recipe.ingredient.Ingredient;
import com.versuchdrei.customrecipedisplay.recipe.ingredient.ItemIngredient;
import com.versuchdrei.customrecipedisplay.utils.JarUtils;

/**
 * a class for representing a namespace
 * @author VersuchDrei
 * @version 1.0
 */
public class NameSpace {
	
	private static final String ITEM_TAG_PATH = "tags/items/";
	private static final String RECIPE_PATH = "recipes/";
	private static final String VANILLA_NAMESPACE = "minecraft";
	
	public static Map<String, Tag> loadVanillaTags() {
		final Map<String, Tag> tags = new HashMap<>();
		
		try {
			final ZipFile jar = new ZipFile(JarUtils.getServerJarPath());
			final String path = DataPack.DATA_PATH + NameSpace.VANILLA_NAMESPACE + "/" + NameSpace.ITEM_TAG_PATH;
			
			for(final Enumeration<? extends ZipEntry> enumeration = jar.entries(); enumeration.hasMoreElements();) {
				final ZipEntry entry = enumeration.nextElement();
				if(entry.isDirectory() || !entry.getName().startsWith(path) || !FilenameUtils.getExtension(entry.getName()).equals("json")) {
					continue;
				}
				
				NameSpace.registerTag(NameSpace.VANILLA_NAMESPACE, FilenameUtils.getBaseName(entry.getName()), new BufferedReader(new InputStreamReader(jar.getInputStream(entry))), tags);
			}
		} catch (final IOException | ParseException e) {
			e.printStackTrace();
		}
		return tags;
	}
	
	private static void registerTag(final String namespace, final String tag, final Reader reader, final Map<String, Tag> tags) throws IOException, ParseException {
		final JSONParser parser = new JSONParser();
		final JSONObject json = (JSONObject) parser.parse(reader);
		
		final String tagName = namespace + ":" + tag;
		
		if(!tags.containsKey(tagName)) {
			tags.put(tagName, new Tag());
		}
		
		final Tag tagObject = tags.get(tagName);
		for(final Object tagType: (JSONArray) json.get("values")) {
			final String tagString = (String) tagType;
			if(tagString.startsWith("#")) {
				tagObject.addTag(tagString.substring(1));
			} else {
				tagObject.addType(Material.valueOf(tagString.substring(tagString.lastIndexOf(':') + 1).toUpperCase()));
			}
		}
	}
	
	private final String name;
	private final String path;
	
	public NameSpace(final String name, final String path) {
		this.name = name;
		this.path = path;
	}
	
	public void registerTags(final Map<String, Tag> tags) {
		final String tagsPath = this.path + NameSpace.ITEM_TAG_PATH;
		final File folder = new File(tagsPath);
		if(!folder.exists()) {
			return;
		}
		
		for(final String tag: folder.list(new JSONFilter())) {
			try(Reader reader = new FileReader(tagsPath + tag)){
				NameSpace.registerTag(this.name, tag.substring(0, tag.lastIndexOf('.')), reader, tags);
			} catch (final IOException | ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void registerRecipes(final List<Recipe> recipes, final Map<String, Ingredient> ingredients) {
		final String recipesPath = this.path + NameSpace.RECIPE_PATH;
		final File folder = new File(recipesPath);
		if(!folder.exists()) {
			return;
		}
		
		for(final String recipe: folder.list(new JSONFilter())) {
			try(Reader reader = new FileReader(recipesPath + recipe)){
				final JSONParser parser = new JSONParser();
				final JSONObject json = (JSONObject) parser.parse(reader);
				
				final String typeString = (String) json.get("type");
				final RecipeType type = getType(typeString);
				final boolean isReplacing = this.name.equals(NameSpace.VANILLA_NAMESPACE);
				
				switch(typeString) {
				case "minecraft:crafting_shaped":
					final JSONObject csResultJSON = (JSONObject) json.get("result");
					final Material csResult = getMaterial((String) csResultJSON.get("item"));
					final int csAmount = csResultJSON.containsKey("count") ? (int)(long) csResultJSON.get("count") : 1;
					final Ingredient[][] csIngredients = parseShapedIngredients((JSONArray) json.get("pattern"), (JSONObject) json.get("key"), ingredients);
					recipes.add(new Recipe(type, csResult, csAmount, csIngredients, isReplacing));
					break;
				case "minecraft:crafting_shapeless":
					final JSONObject cslResultJSON = (JSONObject) json.get("result");
					final Material cslResult = getMaterial((String) cslResultJSON.get("item"));
					final int cslAmount = cslResultJSON.containsKey("count") ? (int)(long) cslResultJSON.get("count") : 1;
					final Ingredient[][] cslIngredients = parseShapelessIngredients((JSONArray) json.get("ingredients"), ingredients);
					recipes.add(new Recipe(type, cslResult, cslAmount, cslIngredients, isReplacing));
					break;
				case "minecraft:blasting":
				case "minecraft:campfire_cooking":
				case "minecraft:smelting":
				case "minecraft:smoking":
					final Material sResult = getMaterial((String) json.get("result"));
					final Ingredient sIngredient = getIngredient((JSONObject) json.get("ingredient"), ingredients);
					recipes.add(new Recipe(type, sResult, 1, new Ingredient[][] {{sIngredient}}, isReplacing));
					break;
				case "minecraft:stonecutting":
					final Material scResult = getMaterial((String) json.get("result"));
					final Ingredient scIngredient = getIngredient((JSONObject) json.get("ingredient"), ingredients);
					final int scAmount = (int)(long) json.get("count");
					recipes.add(new Recipe(type, scResult, scAmount, new Ingredient[][] {{scIngredient}}, isReplacing));
					break;
				default:
					Bukkit.broadcastMessage("unknown type: " + typeString);
					break;
				}
			} catch (final FileNotFoundException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			} catch (final ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	private RecipeType getType(final String type) {
		try {
			return RecipeType.valueOf(type.substring(type.lastIndexOf(':') + 1).toUpperCase());
		} catch(final IllegalArgumentException ex) {
			return RecipeType.ERROR;
		}
	}
	
	private Material getMaterial(final String type) {
		try {
			return Material.valueOf(type.substring(type.lastIndexOf(':') + 1).toUpperCase());
		} catch (final IllegalArgumentException ex) {
			return Material.BARRIER;
		}
	}
	
	private Ingredient getIngredient(final JSONObject json, final Map<String, Ingredient> ingredients) {
		if(json.containsKey("item")) {
			return new ItemIngredient(getMaterial((String) json.get("item")));
		} else if(json.containsKey("tag")) {
			final String tag = (String) json.get("tag");
			if(!ingredients.containsKey(tag)) {
				return new ItemIngredient(Material.BARRIER);
			}
			return ingredients.get(tag);
		} else {
			return new ItemIngredient(Material.BARRIER);
		}
	}
	
	private Ingredient[][] parseShapedIngredients(final JSONArray pattern, final JSONObject keys, final Map<String, Ingredient> ingredients){
		final Map<String, Ingredient> keyMap = new HashMap<>();
		
		for(final Object key: keys.keySet()) {
			keyMap.put((String) key, getIngredient((JSONObject) keys.get(key), ingredients));
		}
		
		final int size = pattern.size();
		final Ingredient[][] parsedIngredients = new Ingredient[size][];
		
		for(int i = 0; i < size; i++) {
			final String row = (String) pattern.get(i);
			parsedIngredients[i] = new Ingredient[row.length()];
			final char[] patternKeys = row.toCharArray();
			for(int j = 0; j < patternKeys.length; j++) {
				final String patternKey = "" + patternKeys[j];
				if(keyMap.containsKey(patternKey)) {
					parsedIngredients[i][j] = keyMap.get(patternKey);
				} else {
					parsedIngredients[i][j] = new ItemIngredient(Material.AIR);
				}
			}
		}
		
		return parsedIngredients;
	}
	
	private Ingredient[][] parseShapelessIngredients(final JSONArray json, final Map<String, Ingredient> ingredients){
		final int size = json.size();
		final int rows = (size + 2) / 3;
		final int columns = Math.min(3, size);
		
		final Ingredient[][] parsedIngredients = new Ingredient[rows][columns];
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				final int index = i * 3 + j;
				if(index < size) {
					parsedIngredients[i][j] = getIngredient((JSONObject) json.get(index), ingredients);
				} else {
					parsedIngredients[i][j] = new ItemIngredient(Material.AIR);
				}
			}
		}
		
		return parsedIngredients;
	}
}
