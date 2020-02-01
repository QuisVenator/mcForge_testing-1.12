package com.example.examplemod.items;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.ModItems;
import com.example.examplemod.util.IHasModel;
import net.minecraft.item.ItemSword;

public class AtackItem extends ItemSword implements IHasModel
{	
	public AtackItem(ToolMaterial material, String name) {
		super(material);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(ModItems.tabTutorialMod);
		ModItems.ITEMS.add(this);
	}
	@Override
	public void registerModels() {
		ExampleMod.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
