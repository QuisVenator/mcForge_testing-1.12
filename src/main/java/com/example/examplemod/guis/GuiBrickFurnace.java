package com.example.examplemod.guis;

import com.example.examplemod.blocks.containers.ContainerBrickFurnace;
import com.example.examplemod.blocks.tileEntities.TileEntityBrickFurnace;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiBrickFurnace extends GuiContainer
{
	private static final String MESSAGE_CLEAR_INVENTORY = "Clear Inventory!";
	private static final int TICKS_PER_ERROR = 20;
	private static final ResourceLocation TEXTURES = new ResourceLocation("qv_testing:textures/gui/brick_furnace.png");
	private final InventoryPlayer player;
	private final TileEntityBrickFurnace tileentity;
	private int errorMessageInventoryNotEmptyTicksRemaining = 0;
	
	public GuiBrickFurnace(InventoryPlayer player, TileEntityBrickFurnace tileentity, World worldIn) 
	{
		super(new ContainerBrickFurnace(player, tileentity, worldIn));
		this.player = player;
		this.tileentity = tileentity;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
	
		this.buttonList.add(new GuiButton( 1, this.guiLeft + 124, this.guiTop + 50, 42, 16, "Craft"));
	
	}
	
	@Override
	protected void actionPerformed(GuiButton b)
	{
		if(b.id == 1)
		{
			if(tileentity.getCraftingVisibillity() && this.itemsInCraftingGrid())
			{
				this.errorMessageInventoryNotEmptyTicksRemaining = TICKS_PER_ERROR;
			}
			else
			{
				tileentity.toggleCraftingVisible();
			}
		}
	}

	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		String tileName = this.tileentity.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(tileName, (this.xSize / 2 - this.fontRenderer.getStringWidth(tileName) / 2) + 3, 8, 4210752);
		this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 122, this.ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
		if(TileEntityBrickFurnace.isBurning(tileentity))
		{
			int k = this.getBurnLeftScaled(13);
			this.drawTexturedModalRect(this.guiLeft + 85, this.guiTop + 50 - k, 176, 13 - k, 13, k + 1);
		}
		if(tileentity.getCraftingVisibillity())
		{
			this.drawTexturedModalRect(this.guiLeft + 176, this.guiTop + 32, 176, 32, 80, 103);
		}
		
		int l = this.getCookProgressScaled(24);
		this.drawTexturedModalRect(this.guiLeft + 79, this.guiTop + 16, 176, 14, l + 1, 16);
		if(errorMessageInventoryNotEmptyTicksRemaining > 0)
		{
			this.fontRenderer.drawString(MESSAGE_CLEAR_INVENTORY, guiLeft + 179, guiTop + 61, 16714519);
			errorMessageInventoryNotEmptyTicksRemaining--;
		}
	}
	
	private int getBurnLeftScaled(int pixels)
	{
		int i = this.tileentity.getField(1);
		if(i == 0) i = 200;
		return this.tileentity.getField(0) * pixels / i;
	}
	
	private int getCookProgressScaled(int pixels)
	{
		int i = this.tileentity.getField(2);
		int j = this.tileentity.getField(3);
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}
	
	private boolean itemsInCraftingGrid()
	{
		for(int i = 40; i < 50; i++)
		{
			if(this.inventorySlots.inventorySlots.get(i).getHasStack()) return true;
		}
		return false;
	}
}
