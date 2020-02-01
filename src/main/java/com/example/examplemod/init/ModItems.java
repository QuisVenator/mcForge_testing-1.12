package com.example.examplemod.init;

import java.util.ArrayList;
import java.util.List;

import com.example.examplemod.items.AtackItem;
import com.example.examplemod.items.ItemBase;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;

public class ModItems {	
	
	public static final CreativeTabs tabTutorialMod = new CreativeTabs("Better Obsidian") 
	{
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(OBSIDIAN_GLASS_SHARD);
		}
	
		@Override
		public boolean hasSearchBar() {
			return true;
		}
	}.setBackgroundImageName("item_search.png");
	
	public static final List<Item> ITEMS = new ArrayList<Item>();

	public static final Item OBSIDIAN_GLASS_SHARD = new ItemBase("obsidian_glass_shard")
	{
		@Override
	    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	    {
			entity.attackEntityFrom(DamageSource.GENERIC, 5);
			player.attackEntityFrom(DamageSource.CACTUS, 1);
	        return false;
	    }
	};
	public static final Item OBSIDIAN_GLASS_TRAP = new ItemBase("obsidian_glass_trap");
	public static ToolMaterial obsidian_material = EnumHelper.addToolMaterial("obsidian_glass", 3, 7, 100000, 100, 0);
	public static final ItemSword OBSIDIAN_SWORD = new AtackItem(obsidian_material, "obsidian_sword");
	public static final Item RUBT_ITEM = new ItemBase("ruby");
}
