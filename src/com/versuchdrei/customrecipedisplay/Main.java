package com.versuchdrei.customrecipedisplay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.versuchdrei.customrecipedisplay.datapack.DataPack;
import com.versuchdrei.customrecipedisplay.recipe.Recipe;
import com.versuchdrei.customrecipedisplay.recipe.RecipeType;
import com.versuchdrei.customrecipedisplay.recipe.ingredient.Ingredient;
import com.versuchdrei.customrecipedisplay.recipe.ingredient.TagIngredient;
import com.versuchdrei.customrecipedisplay.recipe.menu.RecipeMenu;
import com.versuchdrei.customrecipedisplay.recipe.menu.RecipeMenuItem;
import com.versuchdrei.menumanager.Menu;

/**
 * the main class of the recipe display
 * @author VersuchDrei
 * @version 1.0
 */
public class Main extends JavaPlugin{
	
	private static final String SERVER_PROPERTIES_PATH = "server.properties";
	private static final String LEVEL_NAME = "level-name";
	private static final String DATA_PACKS_PATH = "datapacks/";
	
	private Menu recipeMenu = null;
	
	@Override
	public void onEnable() {
		
		try(FileInputStream inputStream = new FileInputStream(Main.SERVER_PROPERTIES_PATH)){
			final Properties properties = new Properties();
			properties.load(inputStream);
			
			final String worldName = (String) properties.get(Main.LEVEL_NAME);
			final String datapacksPath = worldName + "/" + Main.DATA_PACKS_PATH;
			final File datapacksFolder = new File(datapacksPath);
			
			final List<DataPack> datapackList = Arrays.stream(datapacksFolder.list(new FolderFilter()))
					.map(datapack -> new DataPack(datapacksPath + datapack + "/")).collect(Collectors.toList());
			
			// tags
			final Map<String, Set<Material>> tags = CustomRecipeDisplay.getTags();
			
			for(final DataPack datapack: datapackList) {
				datapack.registerTags(tags);
			}
			
			// ingredients
			final Map<String, Ingredient> ingredients = new HashMap<>();
			for(final Entry<String, Set<Material>> entry: tags.entrySet()) {
				ingredients.put(entry.getKey(), new TagIngredient(entry.getValue().stream().toArray(Material[]::new)));
			}
			
			// recipes
			final List<Recipe> recipes = new ArrayList<>();
			for(final DataPack datapack: datapackList) {
				datapack.registerRecipes(recipes, ingredients);
			}
			
			final Map<RecipeType, List<RecipeMenuItem>> menuItems = recipes.stream()
					.map(recipe -> new RecipeMenuItem(recipe)).collect(Collectors.groupingBy(item -> item.getType()));
			
			this.recipeMenu = new RecipeMenu(menuItems);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		
		if(command.getLabel().equals("recipes")) {
			if(!(sender instanceof Player)) {
				return true;
			}
			
			if(this.recipeMenu != null) {
				this.recipeMenu.open((Player) sender);
			}
			
			return true;
		}
		
		return false;
	}

}
