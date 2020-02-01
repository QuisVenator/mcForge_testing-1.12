package com.example.examplemod.util.interceptors;

import com.example.examplemod.init.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class InterceptEvents 
{
	@SubscribeEvent
	public static void onObsidianSwordAttack(AttackEntityEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		Entity target = event.getTarget();
		ItemStack stack = player.getHeldItemMainhand();
		
		if (stack.getItem() == ModItems.OBSIDIAN_SWORD) 
		{
			if(player.experienceLevel <= 20)
			{
				stack.damageItem(3, player);
				event.setCanceled(true);
				target.attackEntityFrom(DamageSource.causePlayerDamage(player), 0.5f);
			}
		}
	}
}
