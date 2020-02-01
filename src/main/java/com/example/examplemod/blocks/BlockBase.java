package com.example.examplemod.blocks;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.ModBlocks;
import com.example.examplemod.init.ModItems;
import com.example.examplemod.util.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IHasModel
{

	public BlockBase(String name, Material material)
	{
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(ModItems.tabTutorialMod);
		
		
		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public void registerModels() 
	{
		ExampleMod.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
