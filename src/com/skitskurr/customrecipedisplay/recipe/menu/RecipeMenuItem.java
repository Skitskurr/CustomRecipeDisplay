package com.skitskurr.customrecipedisplay.recipe.menu;

import com.skitskurr.customrecipedisplay.recipe.Recipe;
import com.skitskurr.customrecipedisplay.recipe.RecipeType;
import com.skitskurr.menumanager.implementations.FixedMenuItem;
import com.skitskurr.menumanager.implementations.MenuItemClickEvent;

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
