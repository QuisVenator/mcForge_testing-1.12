package com.example.examplemod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StandardGlassBlock extends BlockBase
{
	public StandardGlassBlock(String name, Material material)
	{
		super(name, material);
		setSoundType(SoundType.GLASS);
		setHardness(1.0F);
		setResistance(3.0F);
		//setHarvestLevel("pickaxe", 2);
		setLightOpacity(5);
	}
	
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    @SuppressWarnings("deprecation") //because minecraft decided to deprecate without alternative apparently
	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();

        if (!block.isOpaqueCube(iblockstate) && !block.isAir(iblockstate, null, null))
        {
            return false;
        }
        else
        {
        	return true;
        }


    }
}
