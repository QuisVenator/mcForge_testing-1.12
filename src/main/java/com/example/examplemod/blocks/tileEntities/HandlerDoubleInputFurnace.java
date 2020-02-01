package com.example.examplemod.blocks.tileEntities;

import javax.annotation.Nonnull;


import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class HandlerDoubleInputFurnace extends ItemStackHandler
{
	private final int fuelSlots[];
	private final int resultSlots[];
	

    public HandlerDoubleInputFurnace(int size, int fuelSlots[], int resultSlots[])
    {
        super(size);
    	if(resultSlots == null) throw new IllegalArgumentException("Furnace should always have an output");
    	if((fuelSlots == null ? 0 : fuelSlots.length) + resultSlots.length > size) throw new  IllegalArgumentException("Fuel slots and result slots combined exceed size!");
    	this.fuelSlots = fuelSlots;
    	this.resultSlots = resultSlots;
    }
    
	//disable putting items in slot 4 (output slot could otherwise be used for experience farming)
    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
        for(int i = 0; i < resultSlots.length; i++)
        {
        	if(resultSlots[i] == slot) return false;
        }
        for(int i = 0; i < fuelSlots.length; i++)
        {
        	if(fuelSlots[i] == slot && !TileEntityBrickFurnace.isItemFuel(stack)) return false;
        }
        return super.isItemValid(slot, stack);
    }
}
