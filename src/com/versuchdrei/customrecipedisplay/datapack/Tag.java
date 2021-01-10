package com.versuchdrei.customrecipedisplay.datapack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;

import com.versuchdrei.customrecipedisplay.recipe.ingredient.Ingredient;
import com.versuchdrei.customrecipedisplay.recipe.ingredient.TagIngredient;

public class Tag {

	private final Set<Material> types = new HashSet<>();
	private final Set<String> subTags = new HashSet<>();
	
	public void addType(final Material type) {
		this.types.add(type);
	}
	
	public void addTypes(final Collection<Material> types) {
		this.types.addAll(types);
	}
	
	public void addTag(final String tag) {
		this.subTags.add(tag);
	}
	
	public Collection<Material> getTypes(){
		return this.types;
	}
	
	public Collection<String> getSubTags(){
		return this.subTags;
	}
	
	public Ingredient asIngredient() {
		return new TagIngredient(this.types.stream().toArray(Material[]::new));
	}
	
}
