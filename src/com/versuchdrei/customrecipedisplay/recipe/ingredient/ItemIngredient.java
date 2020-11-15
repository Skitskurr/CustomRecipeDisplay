package com.versuchdrei.customrecipedisplay.recipe.ingredient;

import org.bukkit.Material;

/**
 * a class representing a fixed ingredient
 * @author VersuchDrei
 * @version 1.0
 */
public class ItemIngredient implements Ingredient{
	
	private final Material type;
	
	public ItemIngredient(final Material type) {
		this.type = type;
	}
	
	@Override
	public Material get() {
		return this.type;
	}

}
