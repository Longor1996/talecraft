package de.longor.talecraft.clipboard;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.util.GObjectTypeHelper;
import de.longor.talecraft.util.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.UniqueIdentifier;
import net.minecraftforge.fml.relauncher.Side;

public class ClipboardItem {
	private NBTTagCompound data;
	
	public NBTTagCompound getData() {
		return data;
	}
	
	public static void pasteRegion(ClipboardItem item, BlockPos pos, World world, ICommandSender player) {
		NBTTagCompound tagCompound = item.data;
		
		NBTTagCompound blocks = NBTHelper.getOrNull(tagCompound, "blocks");
		if(blocks != null) {
			int regionWidth = blocks.getInteger("regionWidth");
			int regionHeight = blocks.getInteger("regionHeight");
			int regionLength = blocks.getInteger("regionLength");
			
			NBTTagList pallet = blocks.getTagList("pallet", new NBTTagString().getId());
			IBlockState[] palletRaw = new IBlockState[pallet.tagCount()];
			
			for(int i = 0; i < pallet.tagCount(); i++) {
				String typeString = pallet.getStringTagAt(i);
				IBlockState state = palletRaw[i] = GObjectTypeHelper.findBlockState(typeString);
				
				if(state != null) {
					// Dont do a thing.
				} else {
					System.out.println("Could not locate block type: " + typeString + " -> " + i);
				}
				
			}
			
			int[] blockData = blocks.getIntArray("tiles");
			
			for(int Yx = 0; Yx < regionHeight; Yx++) {
				for(int Zx = 0; Zx < regionLength; Zx++) {
					for(int Xx = 0; Xx < regionWidth; Xx++) {
						int index = (Yx*regionWidth*regionLength) + (Zx*regionWidth) + (Xx);
						int type = blockData[index];
						IBlockState state = palletRaw[type];
						
						world.setBlockState(pos.add(Xx, Yx, Zx), state);
						// BlockPos pos = new BlockPos(X, Y, Z);
					}
				}
			}
			
			NBTTagList tes = blocks.getTagList("tileentities", blocks.getId());
			
			for(int i = 0; i < tes.tagCount(); i++) {
				NBTTagCompound compound = (NBTTagCompound) tes.getCompoundTagAt(i).copy();
				System.out.println("TE : " + compound);
				
				int newPosX = pos.getX() + compound.getInteger("x");
				int newPosY = pos.getY() + compound.getInteger("y");
				int newPosZ = pos.getZ() + compound.getInteger("z");
				compound.setInteger("x", newPosX);
				compound.setInteger("y", newPosY);
				compound.setInteger("z", newPosZ);
				
				TileEntity entity = world.getTileEntity(new BlockPos(newPosX, newPosY, newPosZ));
				if(entity != null) {
					entity.readFromNBT(compound);
				}
			}
			
		}
		
	}
	
	public static ClipboardItem copyRegion(int[] bounds, World world, String name, ICommandSender player) {
		int regionWidth = bounds[3] - bounds[0] + 1;
		int regionHeight = bounds[4] - bounds[1] + 1;
		int regionLength = bounds[5] - bounds[2] + 1;
		int regionVolume = regionWidth*regionHeight*regionLength;
		
		// TaleCraft.logger.info("Copy Region: ");
		// TaleCraft.logger.info("-      Name: " + name);
		// TaleCraft.logger.info("-      Size: " + regionWidth + " " + regionHeight + " " + regionLength);
		// TaleCraft.logger.info("-     World: " + world.toString());
		// TaleCraft.logger.info("-    Player: " + player.toString());
		
		NBTTagCompound tagCompound = new NBTTagCompound();
		
		// Copy Blocks
		
		NBTTagCompound blocksCompound = NBTHelper.getOrCreate(tagCompound, "blocks");
		NBTTagList tileentitiesList = new NBTTagList();
		
		IBlockState[] blocksRaw = new IBlockState[regionVolume];
		
		blocksCompound.setInteger("regionWidth", regionWidth);
		blocksCompound.setInteger("regionHeight", regionHeight);
		blocksCompound.setInteger("regionLength", regionLength);
		
		NBTTagCompound offsetCompound = NBTHelper.getOrCreate(tagCompound, "offset");
		offsetCompound.setFloat("x", -regionWidth/2);
		offsetCompound.setFloat("y", -regionHeight/2);
		offsetCompound.setFloat("z", -regionLength/2);
		
		// Create a snapshot of the region
		for(int Y = bounds[1], Yx = 0; Y <= bounds[4]; Y++, Yx++) {
			for(int Z = bounds[2], Zx = 0; Z <= bounds[5]; Z++, Zx++) {
				for(int X = bounds[0], Xx = 0; X <= bounds[3]; X++, Xx++) {
					int index = (Yx*regionWidth*regionLength) + (Zx*regionWidth) + (Xx);
					BlockPos pos = new BlockPos(X, Y, Z);
					blocksRaw[index] = world.getBlockState(pos);
					TileEntity entity = world.getTileEntity(pos);
					
					if(entity != null) {
						NBTTagCompound tileEntityCompound = new NBTTagCompound();
						entity.writeToNBT(tileEntityCompound);
						
						tileEntityCompound.setInteger("x", tileEntityCompound.getInteger("x") - bounds[0]);
						tileEntityCompound.setInteger("y", tileEntityCompound.getInteger("y") - bounds[1]);
						tileEntityCompound.setInteger("z", tileEntityCompound.getInteger("z") - bounds[2]);
						
						tileentitiesList.appendTag(tileEntityCompound);
					}
				}
			}
		}
		
		// Visit all blocks, convert them to numbers, and create a Pallet
		int[] blocks = new int[regionVolume];
		Map<String,Integer> pallet_map = Maps.newHashMap();
		List<String> pallet_list = Lists.newArrayList();
		int palletIndexCounter = 0;
		
		for(int ix = 0; ix < blocksRaw.length; ix++) {
			IBlockState state = blocksRaw[ix];
			Block block = state.getBlock();
			
			UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(block);
			
			String typeName = identifier.modId + ":" + identifier.name;
			int typeMeta = block.getMetaFromState(state);
			
			String typeString = typeName + "/" + typeMeta;
			
			if(pallet_map.containsKey(typeString)) {
				blocks[ix] = pallet_map.get(typeString);
			} else {
				pallet_map.put(typeString, Integer.valueOf(palletIndexCounter));
				pallet_list.add(typeString);
				blocks[ix] = palletIndexCounter;
				palletIndexCounter++;
			}
			
			// System.out.println("copy: " + typeString + " = " + blocks[ix]);
		}
		
		// System.out.println("Pallet Built: " + pallet_map.size() + " different block types found");
		// System.out.println("Tile Entities: " + tileentitiesList.tagCount());
		// System.out.println("Blocks: " + blocks.length);
		
		NBTTagList palletTagList = new NBTTagList();
		for(String string : pallet_list) {
			palletTagList.appendTag(new NBTTagString(string));
		}
		
		blocksCompound.setIntArray("tiles", blocks);
		blocksCompound.setTag("tileentities", tileentitiesList);
		blocksCompound.setTag("pallet", palletTagList);
		
		// System.out.println("BLOCKS = " + blocksCompound);
		
		if(!world.isRemote) {
			StringBuilder builder = new StringBuilder();
			builder.append(EnumChatFormatting.GREEN);
			builder.append("Copied ").append(regionVolume).append(" blocks to the clipboard. ");
			builder.append("(").append(pallet_list.size()).append(" types)");
			
			player.addChatMessage(new ChatComponentText(builder.toString()));
			player.addChatMessage(new ChatComponentText("Key: " + name));
		}
		
		ClipboardItem item = new ClipboardItem();
		item.data = tagCompound;
		return item;
	}
	
	public static ClipboardItem copyEntity(World worldObj, Entity entity, String keyString) {
    	System.out.println("Copy Entity: " + entity);
    	
		// Write entity to compound.
		NBTTagCompound tagCompound = new NBTTagCompound();
		entity.writeToNBT(tagCompound);
		
		// Remove UUID.
		tagCompound.removeTag("UUIDMost");
        tagCompound.removeTag("UUIDLeast");
		
        // Remove others.
        tagCompound.removeTag("Pos");
        tagCompound.removeTag("Dimension");
		
        // Add ID (if missing!)
        if(!tagCompound.hasKey("id")) {
        	tagCompound.setString("id", EntityList.getEntityString(entity));
        }
        
        tagCompound.setFloat("tc_width", entity.width);
        tagCompound.setFloat("tc_height", entity.height);
        
        ClipboardItem item = new ClipboardItem();
        item.data = new NBTTagCompound();
        item.data.setTag("entity", tagCompound);
        
        System.out.println("Created copy of entity: " + item.data);
        
		return item;
	}

	public static void pasteEntity(ClipboardItem item, Vec3 plantPos, World worldIn, EntityPlayer playerIn) {
		double posX = plantPos.xCoord;
		double posY = plantPos.yCoord;
		double posZ = plantPos.zCoord;
		
		
		// Create the entity, merge the existing NBT into it, then spawn the entity.
		NBTTagCompound entityNBT = item.getData().getCompoundTag("entity");
		String typeStr = entityNBT.getString("id");
		Entity entity = EntityList.createEntityFromNBT(entityNBT, worldIn);
		entity.setLocationAndAngles(posX, posY, posZ, entity.rotationYaw, entity.rotationPitch);
		worldIn.spawnEntityInWorld(entity);
		
		int spawnCount = 1;
		
		// This takes care of 'riding' entities.
		{
			Entity mountEntity = entity;
			
			for (
				NBTTagCompound mountEntityNBT = entityNBT;
				mountEntity != null && mountEntityNBT.hasKey("Riding", 10);
				mountEntityNBT = mountEntityNBT.getCompoundTag("Riding")
			) {
				Entity ridingEntity = EntityList.createEntityFromNBT(mountEntityNBT.getCompoundTag("Riding"), worldIn);
				
				if (ridingEntity != null) {
					ridingEntity.setLocationAndAngles(posX, posY, posZ, ridingEntity.rotationYaw, ridingEntity.rotationPitch);
					worldIn.spawnEntityInWorld(ridingEntity);
					mountEntity.mountEntity(ridingEntity);
					spawnCount++;
				}
				
				mountEntity = ridingEntity;
			}
		}
		
		playerIn.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN+"Spawned " + spawnCount + " entities from clipboard."));
		
	}
	
	public static ClipboardItem fromNBT(NBTTagCompound compoundTag) {
		ClipboardItem item = new ClipboardItem();
		item.data = compoundTag;
		return item;
	}
	
}
