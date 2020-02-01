package com.example.examplemod.init;

import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes 
{
	public static void init() 
	{
		GameRegistry.addSmelting(ModBlocks.OBSIDIAN_GLASS_BLOCK, new ItemStack(Blocks.GLASS, 1), 0.0f);
	}
}
