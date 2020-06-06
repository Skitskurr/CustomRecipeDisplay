package com.skitskurr.customrecipedisplay.datapack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.skitskurr.customrecipedisplay.JSONFilter;
import com.skitskurr.customrecipedisplay.recipe.Recipe;
import com.skitskurr.customrecipedisplay.recipe.RecipeType;
import com.skitskurr.customrecipedisplay.recipe.ingredient.Ingredient;
import com.skitskurr.customrecipedisplay.recipe.ingredient.ItemIngredient;

public class NameSpace {
	
	private static final String ITEM_TAG_PATH = "tags/items/";
	private static final String RECIPE_PATH = "recipes/";
	
	private final String name;
	private final String path;
	
	public NameSpace(final String name, final String path) {
		this.name = name;
		this.path = path;
	}
	
	public void registerTags(final Map<String, Set<Material>> tags) {
		final String tagsPath = this.path + NameSpace.ITEM_TAG_PATH;
		final File folder = new File(tagsPath);
		if(!folder.exists()) {
			return;
		}
		
		for(final String tag: folder.list(new JSONFilter())) {
			try(FileReader reader = new FileReader(tagsPath + tag)){
				final JSONParser parser = new JSONParser();
				final JSONObject json = (JSONObject) parser.parse(reader);
				
				final String tagName = this.name + ":" + tag.substring(0, tag.lastIndexOf('.'));
				
				if(!tags.containsKey(tagName)) {
					tags.put(tagName, new HashSet<>());
				}
				
				final Set<Material> types = tags.get(tagName);
				for(final Object tagType: (JSONArray) json.get("values")) {
					final String tagString = (String) tagType;
					types.add(Material.valueOf(tagString.substring(tagString.lastIndexOf(':') + 1).toUpperCase()));
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
	
	public void registerRecipes(final List<Recipe> recipes, final Map<String, Ingredient> ingredients) {
		final String recipesPath = this.path + NameSpace.RECIPE_PATH;
		final File folder = new File(recipesPath);
		if(!folder.exists()) {
			return;
		}
		
		for(final String recipe: folder.list(new JSONFilter())) {
			try(FileReader reader = new FileReader(recipesPath + recipe)){
				final JSONParser parser = new JSONParser();
				final JSONObject json = (JSONObject) parser.parse(reader);
				
				final String typeString = (String) json.get("type");
				final RecipeType type = getType(typeString);
				final boolean isReplacing = this.name.equals("minecraft");
				
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
