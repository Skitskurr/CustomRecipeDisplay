package com.skitskurr.customrecipedisplay.recipe.ingredient;

import org.bukkit.Material;

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
