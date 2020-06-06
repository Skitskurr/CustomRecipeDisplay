package com.skitskurr.customrecipedisplay.recipe.menu;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.skitskurr.customrecipedisplay.recipe.RecipeType;
import com.skitskurr.menumanager.ClickEvent;
import com.skitskurr.menumanager.Menu;
import com.skitskurr.menumanager.implementations.FixedScrollableMenu;
import com.skitskurr.menumanager.utils.ItemUtils;

public class RecipeMenu extends Menu{
	
	private static final int CRAFTING_SHAPED_SLOT = 10;
	private static final int CRAFTING_SHAPELESS_SLOT = 12;
	private static final int SMELTING_SLOT = 14;
	private static final int BLASTING_SLOT = 16;
	private static final int CAMPFIRE_COOKING_SLOT = 28;
	private static final int SMOKING_SLOT = 30;
	private static final int STONECUTTING_SLOT = 32;
	
	private final Menu craftingShapedMenu;
	private final Menu craftingShapelessMenu;
	private final Menu smeltingMenu;
	private final Menu blastingMenu;
	private final Menu campfireCookingMenu;
	private final Menu smokingMenu;
	private final Menu stonecuttingMenu;
	
	private final Inventory inventory;
	
	public RecipeMenu(final Map<RecipeType, List<RecipeMenuItem>> items) {
		this.inventory = Bukkit.createInventory(null, 54, "custom recipes");
		
		if(items.containsKey(RecipeType.CRAFTING_SHAPED)) {
			final List<RecipeMenuItem> subItems = items.get(RecipeType.CRAFTING_SHAPED);
			this.craftingShapedMenu = new FixedScrollableMenu("shaped recipes", subItems);
			this.inventory.setItem(RecipeMenu.CRAFTING_SHAPED_SLOT,
					ItemUtils.newEnchantedItem(Material.CRAFTING_TABLE, "shaped recipes", "§7" + subItems.size() + " new recipes"));
		} else {
			this.craftingShapedMenu = null;
			this.inventory.setItem(RecipeMenu.CRAFTING_SHAPED_SLOT, ItemUtils.newItem(Material.CRAFTING_TABLE, "shaped recipes", "§70 new recipes"));
		}
		
		if(items.containsKey(RecipeType.CRAFTING_SHAPELESS)) {
			final List<RecipeMenuItem> subItems = items.get(RecipeType.CRAFTING_SHAPELESS);
			this.craftingShapelessMenu = new FixedScrollableMenu("shapeless recipes", subItems);
			this.inventory.setItem(RecipeMenu.CRAFTING_SHAPELESS_SLOT,
					ItemUtils.newEnchantedItem(Material.CRAFTING_TABLE, "shapeless recipes", "§7" + subItems.size() + " new recipes"));
		} else {
			this.craftingShapelessMenu = null;
			this.inventory.setItem(RecipeMenu.CRAFTING_SHAPELESS_SLOT, ItemUtils.newItem(Material.CRAFTING_TABLE, "shapeless recipes", "§70 new recipes"));
		}
		
		if(items.containsKey(RecipeType.SMELTING)) {
			final List<RecipeMenuItem> subItems = items.get(RecipeType.SMELTING);
			this.smeltingMenu = new FixedScrollableMenu("smelting recipes", subItems);
			this.inventory.setItem(RecipeMenu.SMELTING_SLOT,
					ItemUtils.newEnchantedItem(Material.FURNACE, "smelting recipes", "§7" + subItems.size() + " new recipes"));
		} else {
			this.smeltingMenu = null;
			this.inventory.setItem(RecipeMenu.SMELTING_SLOT, ItemUtils.newItem(Material.FURNACE, "smelting recipes", "§70 new recipes"));
		}
		
		if(items.containsKey(RecipeType.BLASTING)) {
			final List<RecipeMenuItem> subItems = items.get(RecipeType.BLASTING);
			this.blastingMenu = new FixedScrollableMenu("blasting recipes", subItems);
			this.inventory.setItem(RecipeMenu.BLASTING_SLOT,
					ItemUtils.newEnchantedItem(Material.BLAST_FURNACE, "blasting recipes", "§7" + subItems.size() + " new recipes"));
		} else {
			this.blastingMenu = null;
			this.inventory.setItem(RecipeMenu.BLASTING_SLOT, ItemUtils.newItem(Material.BLAST_FURNACE, "blasting recipes", "§70 new recipes"));
		}
		
		if(items.containsKey(RecipeType.CAMPFIRE_COOKING)) {
			final List<RecipeMenuItem> subItems = items.get(RecipeType.CAMPFIRE_COOKING);
			this.campfireCookingMenu = new FixedScrollableMenu("campfire recipes", subItems);
			this.inventory.setItem(RecipeMenu.CAMPFIRE_COOKING_SLOT,
					ItemUtils.newEnchantedItem(Material.CAMPFIRE, "campfire recipes", "§7" + subItems.size() + " new recipes"));
		} else {
			this.campfireCookingMenu = null;
			this.inventory.setItem(RecipeMenu.CAMPFIRE_COOKING_SLOT, ItemUtils.newItem(Material.CAMPFIRE, "campfire recipes", "§70 new recipes"));
		}
		
		if(items.containsKey(RecipeType.SMOKING)) {
			final List<RecipeMenuItem> subItems = items.get(RecipeType.SMOKING);
			this.smokingMenu = new FixedScrollableMenu("smoking recipes", subItems);
			this.inventory.setItem(RecipeMenu.SMOKING_SLOT,
					ItemUtils.newEnchantedItem(Material.SMOKER, "smoking recipes", "§7" + subItems.size() + " new recipes"));
		} else {
			this.smokingMenu = null;
			this.inventory.setItem(RecipeMenu.SMOKING_SLOT, ItemUtils.newItem(Material.SMOKER, "smoking recipes", "§70 new recipes"));
		}
		
		if(items.containsKey(RecipeType.STONECUTTING)) {
			final List<RecipeMenuItem> subItems = items.get(RecipeType.STONECUTTING);
			this.stonecuttingMenu = new FixedScrollableMenu("stonecutting recipes", subItems);
			this.inventory.setItem(RecipeMenu.STONECUTTING_SLOT,
					ItemUtils.newEnchantedItem(Material.STONECUTTER, "stonecutting recipes", "§7" + subItems.size() + " new recipes"));
		} else {
			this.stonecuttingMenu = null;
			this.inventory.setItem(RecipeMenu.STONECUTTING_SLOT, ItemUtils.newItem(Material.STONECUTTER, "stonecutting recipes", "§70 new recipes"));
		}
	}
	
	@Override
	protected Inventory getInventory(final Player player) {
		return this.inventory;
	}
	
	@Override
	protected void onClick(final ClickEvent event) {
		switch(event.getSlot()) {
		case RecipeMenu.CRAFTING_SHAPED_SLOT:
			if(this.craftingShapedMenu != null) {
				this.craftingShapedMenu.open(event.getPlayer());
			}
			break;
		case RecipeMenu.CRAFTING_SHAPELESS_SLOT:
			if(this.craftingShapelessMenu != null) {
				this.craftingShapelessMenu.open(event.getPlayer());
			}
			break;
		case RecipeMenu.SMELTING_SLOT:
			if(this.smeltingMenu != null) {
				this.smeltingMenu.open(event.getPlayer());
			}
			break;
		case RecipeMenu.BLASTING_SLOT:
			if(this.blastingMenu != null) {
				this.blastingMenu.open(event.getPlayer());
			}
			break;
		case RecipeMenu.CAMPFIRE_COOKING_SLOT:
			if(this.campfireCookingMenu != null) {
				this.campfireCookingMenu.open(event.getPlayer());
			}
			break;
		case RecipeMenu.SMOKING_SLOT:
			if(this.smokingMenu != null) {
				this.smokingMenu.open(event.getPlayer());
			}
			break;
		case RecipeMenu.STONECUTTING_SLOT:
			if(this.stonecuttingMenu != null) {
				this.stonecuttingMenu.open(event.getPlayer());
			}
			break;
		default:
			break;
		}
	}

}
