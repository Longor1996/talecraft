package de.longor.talecraft.proxy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

import javax.vecmath.Vector3f;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.google.common.eventbus.Subscribe;

import de.longor.talecraft.Reference;
import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftBlocks;
import de.longor.talecraft.TaleCraftItems;
import de.longor.talecraft.blocks.ClockBlockTileEntity;
import de.longor.talecraft.blocks.RedstoneTriggerTileEntity;
import de.longor.talecraft.client.InfoBar;
import de.longor.talecraft.client.gui.TCGuiScreen;
import de.longor.talecraft.client.gui.vcui.VCUIRenderer;
import de.longor.talecraft.client.render.BoxRenderer;
import de.longor.talecraft.client.render.CustomSkyRenderer;
import de.longor.talecraft.client.render.EXTFontRenderer;
import de.longor.talecraft.client.render.WireframeMode;
import de.longor.talecraft.client.render.tileentity.ClockBlockTileEntityRenderer;
import de.longor.talecraft.client.render.tileentity.GenericTileEntityRenderer;
import de.longor.talecraft.network.PlayerNBTDataMerge;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.model.BuiltInModel;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.crash.CrashReport;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.RegistrySimple;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy extends CommonProxy
{
	// final
	public final Minecraft mc = Minecraft.getMinecraft();
	public final KeyBinding buildModeBinding = new KeyBinding("key.toggleBuildMode", Keyboard.KEY_B, "key.categories.misc");
	public final KeyBinding wireframeBinding = new KeyBinding("key.toggleWireframe", Keyboard.KEY_PERIOD, "key.categories.misc");
    
	// mc internals
	public ModelManager mc_modelManager;
	
	// public
	public int wireframeMode = 0;
    public InfoBar infoBarInstance = new InfoBar();
    public EXTFontRenderer fontRenderer;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		
		MinecraftForge.EVENT_BUS.register(this);
		
		ClientRegistry.registerKeyBinding(buildModeBinding);
		ClientRegistry.registerKeyBinding(wireframeBinding);
		
		ClientRegistry.bindTileEntitySpecialRenderer(ClockBlockTileEntity.class, new GenericTileEntityRenderer<ClockBlockTileEntity>("talecraft:textures/blocks/util/timer.png"));
		ClientRegistry.bindTileEntitySpecialRenderer(RedstoneTriggerTileEntity.class, new GenericTileEntityRenderer<RedstoneTriggerTileEntity>("talecraft:textures/blocks/util/redstoneTriggerOff.png"));
		
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		
		for(int i = 0; i < 7; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.killBlock), i, new ModelResourceLocation("talecraft:killblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.clockBlock), 0, new ModelResourceLocation("talecraft:clockblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.redstoneTrigger), 0, new ModelResourceLocation("talecraft:redstone_trigger", "inventory"));
		
		mesher.register(TaleCraftItems.wand, 0, new ModelResourceLocation("talecraft:wand", "inventory"));
		
		TaleCraft.instance.simpleNetworkWrapper.registerMessage(new IMessageHandler() {
			@Override
			public IMessage onMessage(IMessage message, MessageContext ctx) {
				if(message instanceof PlayerNBTDataMerge) {
					Minecraft mc = ClientProxy.this.mc;
					if(mc.thePlayer != null) {
						mc.thePlayer.getEntityData().merge(((PlayerNBTDataMerge) message).data);
					}
				}
				return null;
			}
		}, PlayerNBTDataMerge.class, 0x01, Side.CLIENT);
		
	}
	
	public void modelBake(ModelBakeEvent event) {
	    this.mc_modelManager = event.modelManager;
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		fontRenderer = new EXTFontRenderer(mc.fontRendererObj);
		
		/*
		FileWriter wrt = null;
		try {
			wrt = new FileWriter(new File("blockdump.csv"));
			
			Block block = null;
			Iterator<Block> iter = Block.blockRegistry.iterator();
			while(iter.hasNext()) {
				block = iter.next();
				wrt.write(block.getUnlocalizedName());
				wrt.write(", ");
				wrt.write(block.getLocalizedName());
				wrt.write("\r\n");
			}
			wrt.write("\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				wrt.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//*/
	}
	
	@SubscribeEvent
	public void worldPass(RenderWorldLastEvent event) {
		WireframeMode.DISABLE();
		
		if(mc.thePlayer != null) {
			GlStateManager.pushMatrix();
			
			
			double partialTicks = event.partialTicks;
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			
			double px = mc.thePlayer.prevPosX + (mc.thePlayer.posX - mc.thePlayer.prevPosX) * (double)partialTicks;
            double py = mc.thePlayer.prevPosY + (mc.thePlayer.posY - mc.thePlayer.prevPosY) * (double)partialTicks;
            double pz = mc.thePlayer.prevPosZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * (double)partialTicks;
			GL11.glTranslated(-px, -py, -pz);
			
			GlStateManager.disableCull();
			GlStateManager.disableTexture2D();
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			
			NBTTagCompound playerData = mc.thePlayer.getEntityData();
			if(playerData.hasKey("tcWand")) {
				NBTTagCompound tcWand = playerData.getCompoundTag("tcWand");
				
				if(tcWand.hasKey("cursor")) {
					final float E = 1f / 32f;
					int[] cursor = tcWand.getIntArray("cursor");
					BoxRenderer.renderBox(tessellator, worldrenderer, cursor[0]-E, cursor[1]-E, cursor[2]-E, cursor[0]+1+E, cursor[1]+1+E, cursor[2]+1+E,
							1f,1f,0f,0.5f);
				}
				
				if(tcWand.hasKey("boundsA") && tcWand.hasKey("boundsB")) {
					int[] a = tcWand.getIntArray("boundsA");
					int[] b = tcWand.getIntArray("boundsB");
					
					int ix = Math.min(a[0], b[0]);
					int iy = Math.min(a[1], b[1]);
					int iz = Math.min(a[2], b[2]);
					int ax = Math.max(a[0], b[0]);
					int ay = Math.max(a[1], b[1]);
					int az = Math.max(a[2], b[2]);
					
					final float E = 0;
					
					BoxRenderer.renderSelectionBox(tessellator, worldrenderer, ix-E, iy-E, iz-E, ax+1+E, ay+1+E, az+1+E, 0.5f);
				}
			}
			
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			GlStateManager.popMatrix();
		}
		
		GlStateManager.enableTexture2D();
		
		
		
		double fade = 0;
		int color = 0x000000;
		
		if(fade > 0 && mc.ingameGUI != null) {
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GLU.gluOrtho2D(0, 2, 2, 0);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			{
				int alpha = MathHelper.clamp_int((int) (fade * 256), 0, 255);
				int mixed = (alpha & 0xFF) << 24 | (color);
				mc.ingameGUI.drawRect(-1, -1, 4, 4, mixed);
			}
		}
	}
	
	@SubscribeEvent
	public void worldPassPre(RenderWorldEvent.Pre event) {
		
	}
	
	public void unloadWorld(World world) {
		if(world instanceof WorldClient) {
			// the client is either changing dimensions or leaving the server.
			// reset all temporary world related settings
			wireframeMode = 0;
		}
	}
	
	public boolean isBuildMode() {
		return mc.playerController.isInCreativeMode();
	}
	
	public void tick(TickEvent event) {
		super.tick(event);
		
		if(event instanceof ClientTickEvent) {
			
		}
		
		if(event instanceof RenderTickEvent) {
			RenderTickEvent revt = (RenderTickEvent) event;
			
			if(revt.phase == Phase.START) {
				if(mc.theWorld != null && mc.theWorld.provider != null) {
					boolean wireframeModeActive = isBuildMode() ? (wireframeMode != 0) : false;
					
					if(wireframeModeActive) {
						CustomSkyRenderer.instance.setDebugSky(true);
						mc.theWorld.provider.setSkyRenderer(CustomSkyRenderer.instance);
					} else {
						mc.theWorld.provider.setSkyRenderer(null);
					}
				}
				
				if(mc.thePlayer != null)
					WireframeMode.ENABLE(mc.thePlayer.capabilities.isCreativeMode ? wireframeMode : 0);
				
				if(wireframeMode == 3) {
					GlStateManager.disableTexture2D();
				}
			}
			
			if(revt.phase == Phase.END) {
				if(mc.ingameGUI != null && mc.theWorld != null && infoBarInstance.canDisplayInfoBar(mc, this) ) {
					infoBarInstance.display(mc, mc.thePlayer, mc.theWorld, this);
				}
			}
		}// RenderTickEvent {}
	}// tick {}
	
	public void keyEvent(KeyInputEvent event) {
		if(wireframeBinding.isPressed() && wireframeBinding.isKeyDown()) {
			// toggle
			wireframeMode = (wireframeMode + 1) & 3;
			System.out.println("Toggled wireframe: " + wireframeMode);
		}
		
		if(buildModeBinding.isPressed() && buildModeBinding.isKeyDown() && mc.theWorld != null && mc.thePlayer != null && !mc.isGamePaused()) {
			taleCraft.logger.info("Switching GameMode using the BUILDMODe-key.");
			mc.thePlayer.sendChatMessage("/gamemode " + (isBuildMode() ? "2" : "1"));
			
			taleCraft.timedExecutor.executeLater(new Runnable() {
				@Override public void run() {
					mc.addScheduledTask(new Runnable(){
						public void run() {
							mc.renderGlobal.deleteAllDisplayLists();
						}
					});
				}
			}, 250);
		}
	}
	
}
