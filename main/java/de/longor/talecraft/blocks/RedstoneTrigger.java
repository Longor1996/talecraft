package de.longor.talecraft.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import de.longor.talecraft.TCAdminiumMaterial;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftTabs;
import de.longor.talecraft.client.gui.TCGuiScreen;
import de.longor.talecraft.client.gui.blocks.GuiClockBlock;
import de.longor.talecraft.client.gui.blocks.GuiRedstoneTriggerBlock;

public class RedstoneTrigger extends BlockContainer {
    public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");
	
	public RedstoneTrigger() {
		super(TCAdminiumMaterial.instance);
        setDefaultState(this.blockState.getBaseState().withProperty(TRIGGERED, Boolean.valueOf(false)));
		setBlockUnbreakable();
        setResistance(6000001.0F);
		setCreativeTab(TaleCraftTabs.tab_TaleCraftTab);
		setStepSound(Block.soundTypeStone);
		setTickRandomly(false);
		setLightOpacity(0);
        disableStats();
        translucent = true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new RedstoneTriggerTileEntity();
	}
	
	@Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!worldIn.isRemote)
        {
            boolean flag = worldIn.isBlockPowered(pos);
            boolean flag1 = ((Boolean)state.getValue(TRIGGERED)).booleanValue();
            
            if (flag && !flag1)
            {
                worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(true)), 4);
                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
            }
            else if (!flag && flag1)
            {
                worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(false)), 4);
            }
        }
    }
	
    public int tickRate(World worldIn)
    {
        return 1;
    }
	
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	if (worldIn.isRemote)
    		return;
    	
        TileEntity tileentity = worldIn.getTileEntity(pos);
        
        if (tileentity instanceof RedstoneTriggerTileEntity)
        {
            ((RedstoneTriggerTileEntity)tileentity).invokeFromUpdateTick(worldIn, pos, state, rand);
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
    	if(!worldIn.isRemote)
    		return true;
    	if(!TaleCraft.proxy.isBuildMode())
    		return false;
    	if(playerIn.isSneaking())
			return true;
    	
		Minecraft mc = Minecraft.getMinecraft();
		mc.displayGuiScreen(new GuiRedstoneTriggerBlock());
		
		return true;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
    {
    	return TaleCraft.proxy.isBuildMode() ? super.collisionRayTrace(worldIn, pos, start, end) : null;
    }
	
	@Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }
	
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
    	if(TaleCraft.proxy.isBuildMode())
    		return new AxisAlignedBB(pos.getX(),pos.getY(),pos.getZ(),pos.getX()+1,pos.getY()+1,pos.getZ()+1);
    	else
    		return null;
    }
    
	@Override
    public int getRenderType()
    {
        return 2;
    }
    
	@Override
    public boolean isFullCube()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }
    
    // All sides are solid
	@Override
    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
    	return true;
    }
    
    @SideOnly(Side.CLIENT)
    public float getAmbientOcclusionLightValue()
    {
        return 1.0F;
    }
    
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {}
    
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TRIGGERED, Boolean.valueOf((meta & 1) > 0));
    }
    
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;

        if (((Boolean)state.getValue(TRIGGERED)).booleanValue())
        {
            i |= 1;
        }

        return i;
    }
    
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {TRIGGERED});
    }
    
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(TRIGGERED, Boolean.valueOf(false));
    }
}
