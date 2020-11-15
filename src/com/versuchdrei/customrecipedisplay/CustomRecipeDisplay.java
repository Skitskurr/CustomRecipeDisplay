package com.versuchdrei.customrecipedisplay;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Tag;

/**
 * a class that creates a map of all the tags in the default minecraft datapack
 * @author VersuchDrei
 * @version 1.0
 */
public class CustomRecipeDisplay {
	
	public static Map<String, Set<Material>> getTags() {
		final Map<String, Tag<Material>> tags = new HashMap<String, Tag<Material>>();
		
		tags.put("minecraft:acacia_logs", Tag.ACACIA_LOGS);
		tags.put("minecraft:anvil", Tag.ANVIL);
		tags.put("minecraft:arrows", Tag.ITEMS_ARROWS);
		tags.put("minecraft:banners", Tag.BANNERS);
		tags.put("minecraft:beacon_payment_items", Tag.ITEMS_BEACON_PAYMENT_ITEMS);
		tags.put("minecraft:beds", Tag.BEDS);
		tags.put("minecraft:birch_logs", Tag.BIRCH_LOGS);
		tags.put("minecraft:boats", Tag.ITEMS_BOATS);
		tags.put("minecraft:buttons", Tag.BUTTONS);
		tags.put("minecraft:carpets", Tag.CARPETS);
		tags.put("minecraft:coals", Tag.ITEMS_COALS);
		tags.put("minecraft:creeper_drop_music_discs", Tag.ITEMS_CREEPER_DROP_MUSIC_DISCS);
		tags.put("minecraft:crimson_stems", Tag.CRIMSON_STEMS);
		tags.put("minecraft:dark_oak_logs", Tag.DARK_OAK_LOGS);
		tags.put("minecraft:doors", Tag.DOORS);
		tags.put("minecraft:fences", Tag.FENCES);
		tags.put("minecraft:fishes", Tag.ITEMS_FISHES);
		tags.put("minecraft:flowers", Tag.FLOWERS);
		tags.put("minecraft:furnace_materials", Tag.ITEMS_FURNACE_MATERIALS);
		tags.put("minecraft:gold_ores", Tag.GOLD_ORES);
		tags.put("minecraft:jungle_logs", Tag.JUNGLE_LOGS);
		tags.put("minecraft:leaves", Tag.LEAVES);
		tags.put("minecraft:lectern_books", Tag.ITEMS_LECTERN_BOOKS);
		tags.put("minecraft:logs", Tag.LOGS);
		tags.put("minecraft:logs_that_burn", Tag.LOGS_THAT_BURN);
		tags.put("minecraft:music_discs", Tag.ITEMS_MUSIC_DISCS);
		tags.put("minecraft:non_flammable_wood", Tag.NON_FLAMMABLE_WOOD);
		tags.put("minecraft:oak_logs", Tag.OAK_LOGS);
		tags.put("minecraft:piglin_loved", Tag.ITEMS_PIGLIN_LOVED);
		tags.put("minecraft:piglin_repellents", Tag.PIGLIN_REPELLENTS);
		tags.put("minecraft:planks", Tag.PLANKS);
		tags.put("minecraft:rails", Tag.RAILS);
		tags.put("minecraft:sand", Tag.SAND);
		tags.put("minecraft:saplings", Tag.SAPLINGS);
		tags.put("minecraft:signs", Tag.SIGNS);
		tags.put("minecraft:slabs", Tag.SLABS);
		tags.put("minecraft:small_flowers", Tag.SMALL_FLOWERS);
		tags.put("minecraft:soul_fire_base_blocks", Tag.SOUL_FIRE_BASE_BLOCKS);
		tags.put("minecraft:spruce_logs", Tag.SPRUCE_LOGS);
		tags.put("minecraft:stairs", Tag.STAIRS);
		tags.put("minecraft:stone_bricks", Tag.STONE_BRICKS);
		tags.put("minecraft:stone_tool_materials", Tag.ITEMS_STONE_TOOL_MATERIALS);
		tags.put("minecraft:tall_flowers", Tag.TALL_FLOWERS);
		tags.put("minecraft:trapdoors", Tag.TRAPDOORS);
		tags.put("minecraft:walls", Tag.WALLS);
		tags.put("minecraft:warped_stems", Tag.WARPED_STEMS);
		tags.put("minecraft:wooden_buttons", Tag.WOODEN_BUTTONS);
		tags.put("minecraft:wooden_doors", Tag.WOODEN_DOORS);
		tags.put("minecraft:wooden_fences", Tag.WOODEN_FENCES);
		tags.put("minecraft:wooden_pressure_plates", Tag.WOODEN_PRESSURE_PLATES);
		tags.put("minecraft:wooden_slabs", Tag.WOODEN_SLABS);
		tags.put("minecraft:wooden_stairs", Tag.WOODEN_STAIRS);
		tags.put("minecraft:wooden_trapdoors", Tag.WOODEN_TRAPDOORS);
		tags.put("minecraft:wool", Tag.WOOL);
		
		final Map<String, Set<Material>> typeMaps = new HashMap<>();
		for(final Entry<String, Tag<Material>> entry: tags.entrySet()) {
			typeMaps.put(entry.getKey(), entry.getValue().getValues());
		}
		
		return typeMaps;
	}
}
