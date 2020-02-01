package com.example.examplemod.blocks.tileEntities;

import com.example.examplemod.blocks.BlockBrickFurnace;
import com.example.examplemod.recipes.RecipesBrickFurnace;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityBrickFurnace extends TileEntity implements ITickable
{
	private static final int FUEL_SLOT = 2;
	private static final int RESULT_FURNACE = 3;
	private static final int RESULT_CRAFTING = 14;
	private static final int INPUT_FURNACE_1 = 0;
	private static final int INPUT_FURNACE_2 = 1;
	private static final int INPUT_CRAFTING[] = {4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
	
	private ItemStackHandler handler = new HandlerDoubleInputFurnace(14, new int[] {FUEL_SLOT}, new int[] {RESULT_FURNACE, RESULT_CRAFTING});
	private String customName;
	private ItemStack currentlyProducing = ItemStack.EMPTY;
	
	private int burnTime;
	private int currentBurnTime;
	private int cookTime;
	private int totalCookTime = 50;
	private boolean craftingVisible;

	//tells rest of code that this class implements an item handler
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		else return false;
	}
	
	//give access to item handler to other classes (for example for dropping items on breaking)
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) this.handler;
		return super.getCapability(capability, facing);
	}
	
	public boolean hasCustomName() 
	{
		return this.customName != null && !this.customName.isEmpty();
	}
	
	public void setCustomName(String customName) 
	{
		this.customName = customName;
	}
	
	@Override
	public ITextComponent getDisplayName() 
	{
		return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.brick_furnace");
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.handler.deserializeNBT(compound.getCompoundTag("Inventory"));
		this.burnTime = compound.getInteger("BurnTime");
		this.cookTime = compound.getInteger("CookTime");
		this.totalCookTime = compound.getInteger("CookTimeTotal");
		this.currentBurnTime = getItemBurnTime((ItemStack)this.handler.getStackInSlot(2));
		
		if(compound.hasKey("CustomName", 8)) this.setCustomName(compound.getString("CustomName"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		super.writeToNBT(compound);
		compound.setInteger("BurnTime", (short)this.burnTime);
		compound.setInteger("CookTime", (short)this.cookTime);
		compound.setInteger("CookTimeTotal", (short)this.totalCookTime);
		compound.setTag("Inventory", this.handler.serializeNBT());
		
		if(this.hasCustomName()) compound.setString("CustomName", this.customName);
		return compound;
	}
	
	public boolean isBurning() 
	{
		return this.burnTime > 0;
	}
	
	@SideOnly(Side.CLIENT)
	public static boolean isBurning(TileEntityBrickFurnace te) 
	{
		return te.getField(0) > 0;
	}
	
	public void update() 
	{	
		if(this.isBurning())
		{
			--this.burnTime;
			BlockBrickFurnace.setState(true, world, pos);
		}
		
		ItemStack fuel = this.handler.getStackInSlot(FUEL_SLOT);
		
		if(this.canSmelt())
		{
			if(!this.isBurning())
			{
				if(!fuel.isEmpty())
				{
					cookTime++;
					this.burnTime = getItemBurnTime(handler.extractItem(FUEL_SLOT, 1, false));
				}
			}
			else
			{
				cookTime++;
				if(cookTime >= totalCookTime)
				{
					this.handler.insertItem(3, currentlyProducing, false);
					if(!currentlyProducing.isEmpty())
					{
						cookTime = 0;
						this.handler.extractItem(INPUT_FURNACE_1, 1, false);
						this.handler.extractItem(INPUT_FURNACE_2, 1, false);
					}
					else
					{
						cookTime--;
					}
				}
			}
		}
		else
		{
			cookTime = 0;
		}
	}
	
	//see if a smelt process can currently be started
	private boolean canSmelt() 
	{
		if(((ItemStack)this.handler.getStackInSlot(INPUT_FURNACE_1)).isEmpty() || ((ItemStack)this.handler.getStackInSlot(INPUT_FURNACE_2)).isEmpty()) return false;
		else 
		{
			currentlyProducing = RecipesBrickFurnace.getInstance().getBrickFurnaceResult((ItemStack)this.handler.getStackInSlot(INPUT_FURNACE_1), (ItemStack)this.handler.getStackInSlot(INPUT_FURNACE_2)).copy();	
			if(currentlyProducing.isEmpty()) return false;
			else
			{
				return canStackOutput();
			}
		}
	}
	
	//
	private boolean canStackOutput()
	{
		ItemStack produced = this.handler.getStackInSlot(3);
		if (currentlyProducing.isEmpty()) return false;
		else if(produced.isEmpty()) return true;
		else if (ItemStack.areItemsEqualIgnoreDurability(produced, currentlyProducing) && produced.getMaxStackSize() >= produced.getCount() + currentlyProducing.getCount()) return true;
		else return false;
	}
	
	public static int getItemBurnTime(ItemStack fuel) 
	{
		if(fuel.isEmpty()) return 0;
		else 
		{
			Item item = fuel.getItem();

			if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.AIR) 
			{
				Block block = Block.getBlockFromItem(item);

				if (block == Blocks.WOODEN_SLAB) return 150;
				if (block.getDefaultState().getMaterial() == Material.WOOD) return 300;
				if (block == Blocks.COAL_BLOCK) return 16000;
			}

			if (item instanceof ItemTool && "WOOD".equals(((ItemTool)item).getToolMaterialName())) return 200;
			if (item instanceof ItemSword && "WOOD".equals(((ItemSword)item).getToolMaterialName())) return 200;
			if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe)item).getMaterialName())) return 200;
			if (item == Items.STICK) return 100;
			if (item == Items.COAL) return 1600;
			if (item == Items.LAVA_BUCKET) return 20000;
			if (item == Item.getItemFromBlock(Blocks.SAPLING)) return 100;
			if (item == Items.BLAZE_ROD) return 2400;

			return ForgeEventFactory.getItemBurnTime(fuel);
		}
	}
		
	public static boolean isItemFuel(ItemStack fuel)
	{
		return getItemBurnTime(fuel) > 0;
	}
	
	public boolean isUsableByPlayer(EntityPlayer player) 
	{
		return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}

	public int getField(int id) 
	{
		switch(id) 
		{
		case 0:
			return this.burnTime;
		case 1:
			return this.currentBurnTime;
		case 2:
			return this.cookTime;
		case 3:
			return this.totalCookTime;
		case 4:
			return this.craftingVisible ? 1 : 0;
		default:
			return 0;
		}
	}

	public void setField(int id, int value) 
	{
		switch(id) 
		{
		case 0:
			this.burnTime = value;
			break;
		case 1:
			this.currentBurnTime = value;
			break;
		case 2:
			this.cookTime = value;
			break;
		case 3:
			this.totalCookTime = value;
			break;
		case 4:
			this.craftingVisible = value == 1;
		}
	}
	
	public boolean getCraftingVisibillity()
	{
		return this.craftingVisible;
	}
	public void toggleCraftingVisible()
	{
		this.markDirty();
		this.craftingVisible = !this.craftingVisible;
	}
	
	//TODO implement method to expose inventory on drop
}