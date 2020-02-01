package com.example.examplemod.items;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.ModItems;
import com.example.examplemod.util.IHasModel;

import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {

	public ItemBase(String name)
	{
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(ModItems.tabTutorialMod);
		
		ModItems.ITEMS.add(this);
	}
	@Override
	public void registerModels() {
		ExampleMod.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
