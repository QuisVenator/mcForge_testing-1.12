package com.example.examplemod.util.handlers;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.blocks.containers.ContainerBrickFurnace;
import com.example.examplemod.blocks.tileEntities.TileEntityBrickFurnace;
import com.example.examplemod.guis.GuiBrickFurnace;
import com.example.examplemod.util.Reference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if(ID == Reference.GUI_BRICK_FURNACE)
		{
			return new ContainerBrickFurnace(player.inventory, (TileEntityBrickFurnace)world.getTileEntity(new BlockPos(x, y, z)), world);
		}
		else
		{
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if(ID == Reference.GUI_BRICK_FURNACE)
		{
			return new GuiBrickFurnace(player.inventory, (TileEntityBrickFurnace)world.getTileEntity(new BlockPos(x, y, z)), world);
		}
		else
		{
			return null;
		}
	}
	
	public static void init()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(ExampleMod.instance, new GuiHandler());
	}
}
