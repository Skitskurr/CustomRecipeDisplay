package com.skitskurr.customrecipedisplay.recipe;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.skitskurr.customrecipedisplay.recipe.ingredient.Ingredient;
import com.skitskurr.menumanager.ClickEvent;
import com.skitskurr.menumanager.Menu;
import com.skitskurr.menumanager.utils.ItemUtils;
import com.skitskurr.menumanager.utils.MHFHead;

public class Recipe extends Menu{
	
	private static final int ARROW_SLOT = 23;
	private static final int RESULT_SLOT = 25;
	private static final int BACK_SLOT = 43;
	
	private static final int INGREDIENTS_ROW = 1;
	private static final int INGREDIENTS_COLUMN = 1;
	
	private final RecipeType type;
	private final Material result;
	private final int amount;
	private final Ingredient[][] ingredients;
	private final boolean isReplacing;
	
	private final String title;
	
	public Recipe(final RecipeType type, final Material result, final int amount, final Ingredient[][] ingredients, final boolean isReplacing) {
		this.type = type;
		this.result = result;
		this.amount = amount;
		this.ingredients = ingredients;
		this.isReplacing = isReplacing;
		
		this.title = ItemUtils.getItemName(result);
	}

	@Override
	protected Inventory getInventory(final Player player) {
		final Inventory inventory = Bukkit.createInventory(null, 54, this.title);

		final int row = Recipe.INGREDIENTS_ROW + (ingredients.length == 1 ? 1 : 0);
		final int column = Recipe.INGREDIENTS_COLUMN + (ingredients[0].length == 1 ? 1 : 0);
		
		for(int i = 0; i < ingredients.length; i++) {
			for(int j = 0; j < ingredients[0].length; j++) {
				inventory.setItem((row + i) * 9 + (column + j), new ItemStack(this.ingredients[i][j].get()));
			}
		}
		
		inventory.setItem(Recipe.ARROW_SLOT, ItemUtils.setName(MHFHead.ARROW_RIGHT.item(), "yields"));
		inventory.setItem(Recipe.RESULT_SLOT, new ItemStack(this.result, this.amount));
		inventory.setItem(Recipe.BACK_SLOT, ItemUtils.newItem(Material.OAK_DOOR, "back", "§7return to the previous menu"));
		
		return inventory;
	}
	
	@Override
	protected void onClick(final ClickEvent event) {
		if(event.getSlot() == Recipe.BACK_SLOT) {
			super.back(event.getPlayer());
		}
	}
	
	public RecipeType getType() {
		return this.type;
	}
	
	public ItemStack getIcon() {
		if(this.isReplacing) {
			return ItemUtils.enchantAndSetLore(new ItemStack(this.result, this.amount), "§7replaces vanilla recipe");
		} else {
			return new ItemStack(this.result, this.amount);
		}
	}

}
