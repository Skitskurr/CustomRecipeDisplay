package com.skitskurr.customrecipedisplay.recipe.ingredient;

import java.util.Random;

import org.bukkit.Material;

public class TagIngredient implements Ingredient{
	
	private static final Random RANDOM = new Random();
	
	private final Material[] types;
	
	public TagIngredient(final Material[] types) {
		this.types = types;
	}
	
	@Override
	public Material get() {
		return types[TagIngredient.RANDOM.nextInt(types.length)];
	}

}
