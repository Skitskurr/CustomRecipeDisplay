package com.versuchdrei.customrecipedisplay.recipe.menu;

import com.versuchdrei.customrecipedisplay.recipe.Recipe;
import com.versuchdrei.customrecipedisplay.recipe.RecipeType;
import com.versuchdrei.menumanager.implementations.FixedMenuItem;
import com.versuchdrei.menumanager.implementations.MenuItemClickEvent;

/**
 * a menu item opening the menu for a single recipe
 * @author VersuchDrei
 * @version 1.0
 */
public class RecipeMenuItem extends FixedMenuItem{
	
	private final Recipe recipe;
	
	public RecipeMenuItem(final Recipe recipe) {
		super(recipe.getIcon());
		
		this.recipe = recipe;
	}
	
	@Override
	public void onClick(final MenuItemClickEvent event) {
		this.recipe.open(event.getPlayer());
	}
	
	public RecipeType getType() {
		return this.recipe.getType();
	}

}
