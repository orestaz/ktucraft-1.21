package net.orestas.ktucraft;

import net.fabricmc.api.ModInitializer;

import net.orestas.ktucraft.block.ModBlocks;
import net.orestas.ktucraft.item.ModItemGroups;
import net.orestas.ktucraft.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KTUCRAFT implements ModInitializer {
	public static final String MOD_ID = "ktucraft";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModItemGroups.registerItemGroups();
	}
}