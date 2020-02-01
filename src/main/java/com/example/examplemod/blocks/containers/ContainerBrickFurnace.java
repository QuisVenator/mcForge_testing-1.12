package com.example.examplemod.blocks.containers;

import com.example.examplemod.blocks.tileEntities.TileEntityBrickFurnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBrickFurnace extends Container
{
	private final TileEntityBrickFurnace tileentity;
	private InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	private int cookTime, totalCookTime, burnTime, currentBurnTime;
	private int craftingVisible;
	private final World world;
	
	public ContainerBrickFurnace(InventoryPlayer playerInventory, TileEntityBrickFurnace tileentity, World worldIn) 
	{
		this.world = worldIn;
		this.tileentity = tileentity;
		IItemHandler handler = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		this.addSlotToContainer(new SlotItemHandler(handler, 0, 25, 17));
		this.addSlotToContainer(new SlotItemHandler(handler, 1, 56, 17));
		this.addSlotToContainer(new SlotItemHandler(handler, 2, 85, 54));
		this.addSlotToContainer(new SlotItemHandler(handler, 3, 114, 17));
		
		for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 9; x++)
			{
				this.addSlotToContainer(new Slot(playerInventory, x + y*9 + 9, 8 + x*18, 84 + y*18));
			}
		}

		for(int x = 0; x < 9; x++)
		{
			this.addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 142));
		}

		for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 3; x++)
			{
				this.addSlotToContainer(new SlotItemHandler(handler, x + y*3 + 4, 191 + x*18, 38 + y*18));
			}
		}
		
		this.addSlotToContainer(new SlotItemHandler(handler, 13, 208, 98));
	}
	
	//we cancel any action of this as we manage this in detectAndSendChanges
	@Override
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
		return;
    }
   
	
	@Override
	public void detectAndSendChanges() 
	{
		if(!inventoryItemStacks.get(49).isEmpty() && getSlot(49).getStack().isEmpty())
		{
			for(int i = 40; i < 49; i++)
			{
	        	getSlot(i).decrStackSize(1);
			}
		}

		for(int i = 40; i < 49; i++)
		{
			craftMatrix.setInventorySlotContents(i-40, this.getSlot(i).getStack());
		}
        IRecipe irecipe = CraftingManager.findMatchingRecipe(craftMatrix, world);
        if(irecipe != null)
        {
        	this.putStackInSlot(49, irecipe.getCraftingResult(craftMatrix));
        }
        else
        {
        	this.putStackInSlot(49, ItemStack.EMPTY);
        }
        for (int j = 0; j < this.listeners.size(); ++j)
        {
        	for(int i = 40; i < 49; i++)
        	{
        		((IContainerListener)this.listeners.get(j)).sendSlotContents(this, i, getSlot(i).getStack());
            }
        	((IContainerListener)this.listeners.get(j)).sendSlotContents(this, 49, irecipe != null ? irecipe.getCraftingResult(craftMatrix) : ItemStack.EMPTY);
        }
        
		super.detectAndSendChanges();
		for(int i = 0; i < this.listeners.size(); ++i) 
		{
			IContainerListener listener = (IContainerListener)this.listeners.get(i);
			
			if(this.cookTime != this.tileentity.getField(2)) listener.sendWindowProperty(this, 2, this.tileentity.getField(2));
			if(this.burnTime != this.tileentity.getField(0)) listener.sendWindowProperty(this, 0, this.tileentity.getField(0));
			if(this.currentBurnTime != this.tileentity.getField(1)) listener.sendWindowProperty(this, 1, this.tileentity.getField(1));
			if(this.totalCookTime != this.tileentity.getField(3)) listener.sendWindowProperty(this, 3, this.tileentity.getField(3));
			if(this.craftingVisible != this.tileentity.getField(4)) listener.sendWindowProperty(this, 4, this.tileentity.getField(4));
		}
		this.cookTime = this.tileentity.getField(2);
		this.burnTime = this.tileentity.getField(0);
		this.currentBurnTime = this.tileentity.getField(1);
		this.totalCookTime = this.tileentity.getField(3);
		this.craftingVisible = this.tileentity.getField(4);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) 
	{
		this.tileentity.setField(id, data);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) 
	{
		return this.tileentity.isUsableByPlayer(playerIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) 
	{
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = (Slot)this.inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack()) 
		{
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			
			if(index == 3) 
			{
				if(!this.mergeItemStack(stack1, 4, 40, true)) return ItemStack.EMPTY;
				slot.onSlotChange(stack1, stack);
			}
			else if(index >= 4 && index < 40) 
			{
				if(!this.mergeItemStack(stack1, 0, 3, true) && !this.mergeItemStack(stack1, 40, 50, true)) return ItemStack.EMPTY;
			} 
			else if(index == 49)
			{
				while(!stack1.isEmpty() && this.mergeItemStack(stack1, 4, 40, true))
				{
					detectAndSendChanges();
					stack1 = slot.getStack();
				}
			}
			else if(!this.mergeItemStack(stack1, 4, 40, false)) 
			{
				return ItemStack.EMPTY;
			}
			if(stack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();

			}
			if(stack1.getCount() == stack.getCount()) return ItemStack.EMPTY;
			slot.onTake(playerIn, stack1);
		}
		return stack;
	}
}