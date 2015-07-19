package de.longor.talecraft.blocks.deco;

import java.util.List;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import de.longor.talecraft.TaleCraftTabs;
import de.longor.talecraft.blocks.TCBlock;

public class BlankBlock extends TCBlock {
	public static final IProperty SUB = PropertyInteger.create("sub", 0, 15);
	
	public BlankBlock() {
		this.setCreativeTab(TaleCraftTabs.tab_TaleCraftDecorationTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(SUB, Integer.valueOf(0)));
	}
	
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        for (int j = 0; j < 16; ++j)
        {
            list.add(new ItemStack(itemIn, 1, j));
        }
    }
	
	@Override
    public int damageDropped(IBlockState state)
    {
        return ((Integer)state.getValue(SUB)).intValue();
    }
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(SUB, Integer.valueOf(meta));
    }
	
	@Override
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(SUB)).intValue();
    }
	
	@Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {SUB});
    }
	
}
