package com.versuchdrei.customrecipedisplay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.versuchdrei.customrecipedisplay.datapack.DataPack;
import com.versuchdrei.customrecipedisplay.datapack.NameSpace;
import com.versuchdrei.customrecipedisplay.datapack.Tag;
import com.versuchdrei.customrecipedisplay.recipe.Recipe;
import com.versuchdrei.customrecipedisplay.recipe.RecipeType;
import com.versuchdrei.customrecipedisplay.recipe.ingredient.Ingredient;
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
			final Map<String, Tag> tags = NameSpace.loadVanillaTags();
			
			for(final DataPack datapack: datapackList) {
				datapack.registerTags(tags);
			}
			
			for(final Entry<String, Tag> entry: tags.entrySet()) {
				// almost all of the tags will not reference other tags, so this should spare a lot of runtime
				final Tag tag = entry.getValue();
				if(tag.getSubTags().isEmpty()) {
					continue;
				}
				// the sentinel set is to avoid loops
				final Set<String> sentinel = new HashSet<>();
				sentinel.add(entry.getKey());
				final List<Tag> subTags = new ArrayList<>();
				for(final String subTag: tag.getSubTags()) {
					sentinel.add(subTag);
					subTags.add(tags.get(subTag));
				}
				
				// we modify the list while iterating through it, so a for each loop would cause a ConcurrentModificationException
				// hence the classic for loop
				for(int i = 0; i < subTags.size(); i++) {
					final Tag subTag = subTags.get(i);
					tag.addTypes(subTag.getTypes());
					for(final String subSubTag: subTag.getSubTags()) {
						if(sentinel.contains(subSubTag)) {
							continue;
						}
						sentinel.add(subSubTag);
						subTags.add(tags.get(subSubTag));
					}
				}
			}
			
			// ingredients
			final Map<String, Ingredient> ingredients = new HashMap<>();
			for(final Entry<String, Tag> entry: tags.entrySet()) {
				ingredients.put(entry.getKey(), entry.getValue().asIngredient());
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
