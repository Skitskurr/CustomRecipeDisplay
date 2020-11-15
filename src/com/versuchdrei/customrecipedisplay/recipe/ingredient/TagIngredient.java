package com.versuchdrei.customrecipedisplay.recipe.ingredient;

import java.util.Random;

import org.bukkit.Material;

/**
 * a class representing a group of ingredients specified by a tag
 * @author VersuchDrei
 * @version 1.0
 */
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
