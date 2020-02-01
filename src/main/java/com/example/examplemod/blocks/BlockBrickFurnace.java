//TODO put stuff in order

package com.example.examplemod.blocks;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.blocks.tileEntities.TileEntityBrickFurnace;
import com.example.examplemod.util.Reference;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class BlockBrickFurnace extends StandardIngotBlock implements ITileEntityProvider
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyBool BURNING = PropertyBool.create("burning");
	
	public BlockBrickFurnace(String name, Material material)
	{
		super(name, material);
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(BURNING, false));
		setHardness(3.0F);
		setResistance(3.0F);
		setHarvestLevel("pickaxe", 2);
		
	}
	
	//Set direction facing when placing (looking at player)
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
	
	// Manage META data and State (mainly conversion between the two)
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getIndex();
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing facing = EnumFacing.getFront(meta);
		if(facing.getAxis() == EnumFacing.Axis.Y) //if block for some damn reason is facing in non permitted direction (up or down), reset
		{
			facing = EnumFacing.NORTH;
		}
		return this.getDefaultState().withProperty(FACING, facing);
	}
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { BURNING, FACING} );
	}

	// Change furnace burning property on or off
    public static void setState(boolean active, World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity != null)
        {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }
    

	//TODO create GUI
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		if(!worldIn.isRemote)
		{
			playerIn.openGui(ExampleMod.instance, Reference.GUI_BRICK_FURNACE, worldIn, pos.getX(), pos.getY(), pos.getZ());
            playerIn.addStat(StatList.CRAFTING_TABLE_INTERACTION);
		}
		
		return true;
	}

	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) 
	{
		if (!worldIn.isRemote) 
        {
            IBlockState north = worldIn.getBlockState(pos.north());
            IBlockState south = worldIn.getBlockState(pos.south());
            IBlockState west = worldIn.getBlockState(pos.west());
            IBlockState east = worldIn.getBlockState(pos.east());
            EnumFacing face = (EnumFacing)state.getValue(FACING);

            if (face == EnumFacing.NORTH && north.isFullBlock() && !south.isFullBlock()) face = EnumFacing.SOUTH;
            else if (face == EnumFacing.SOUTH && south.isFullBlock() && !north.isFullBlock()) face = EnumFacing.NORTH;
            else if (face == EnumFacing.WEST && west.isFullBlock() && !east.isFullBlock()) face = EnumFacing.EAST;
            else if (face == EnumFacing.EAST && east.isFullBlock() && !west.isFullBlock()) face = EnumFacing.WEST;
            worldIn.setBlockState(pos, state.withProperty(FACING, face), 2);
        }
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) 
	{
		return true;
	}
    
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) 
	{
		return new TileEntityBrickFurnace();
	}

    // TODO figure out what this does (probably server synch, bc of the 2)
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
    	TileEntityBrickFurnace tileentity = (TileEntityBrickFurnace)worldIn.getTileEntity(pos);
    	ItemStackHandler handler = (ItemStackHandler) tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    	for(int i = 0; i < handler.getSlots(); i++)
    	{
    		worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i)));
    	}
        super.breakBlock(worldIn, pos, state);
    }
    
    //defines what it used to render block (tesr or not, affect performance, contains option for liquid)
    public EnumBlockRenderType getRenderType(IBlockState state) 
    {
        return EnumBlockRenderType.MODEL;
    }
    
    //rotate or mirror block, not all that useful...
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityBrickFurnace();
	}
}
