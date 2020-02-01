package com.example.examplemod.init;

import java.util.ArrayList;
import java.util.List;

import com.example.examplemod.blocks.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks 
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();

	public static final Block OBSIDIAN_GLASS_BLOCK = new StandardGlassBlock("obsidian_glass_block", Material.GLASS);
	public static final Block TEST_GLASS_BLOCK = new StandardGlassBlock("test_glass_block", Material.GLASS);
	public static final Block BRICK_FURNACE = new BlockBrickFurnace("brick_furnace", Material.ROCK);
}
