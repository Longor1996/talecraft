package de.longor.talecraft.blocks;

import de.longor.talecraft.TCAdminiumMaterial;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftTabs;
import de.longor.talecraft.client.gui.TCGuiScreen;
import de.longor.talecraft.client.gui.blocks.GuiClockBlock;
import de.longor.talecraft.invoke.ITriggerableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClockBlock extends BlockContainer implements ITriggerableBlock {

	public ClockBlock() {
		super(TCAdminiumMaterial.instance);
		setDefaultState(getBlockState().getBaseState());
		setCreativeTab(TaleCraftTabs.tab_TaleCraftTab);
		setBlockUnbreakable();
        setResistance(6000001.0F);
		setStepSound(Block.soundTypeStone);
		setTickRandomly(false);
		setLightOpacity(0);
        disableStats();
        translucent = true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new ClockBlockTileEntity();
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
    	
		System.out.println("HELLO "+playerIn.getName().toUpperCase()+", THIS IS CLOCK");
		
		Minecraft mc = Minecraft.getMinecraft();
		mc.displayGuiScreen(new GuiClockBlock((ClockBlockTileEntity)worldIn.getTileEntity(pos)));
		
		return true;
    }
	
	@Override
	public void trigger(World world, BlockPos position, int data) {
		ClockBlockTileEntity tEntity = (ClockBlockTileEntity)world.getTileEntity(position);
		if(tEntity != null) {
			if(tEntity.isClockRunning()) {
				tEntity.clockStop();
			}else{
				tEntity.clockStart();
			}
		}
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
	
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return false;
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
    
    @SideOnly(Side.CLIENT)
    public float getAmbientOcclusionLightValue()
    {
        return 1.0F;
    }
    
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {}
    

}
