package de.longor.talecraft.proxy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

import javax.vecmath.Vector3f;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.glu.GLU;

import com.google.common.collect.ArrayListMultimap;
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
import de.longor.talecraft.client.render.ITemporaryRenderable;
import de.longor.talecraft.client.render.ItemMetaWorldRenderer;
import de.longor.talecraft.client.render.PushRenderableFactory;
import de.longor.talecraft.client.render.WireframeMode;
import de.longor.talecraft.client.render.entity.PointEntityRenderer;
import de.longor.talecraft.client.render.tileentity.ClockBlockTileEntityRenderer;
import de.longor.talecraft.client.render.tileentity.GenericTileEntityRenderer;
import de.longor.talecraft.client.render.tileentity.IEXTTileEntityRenderer;
import de.longor.talecraft.entities.EntityPoint;
import de.longor.talecraft.items.TCItem;
import de.longor.talecraft.network.PlayerNBTDataMerge;
import de.longor.talecraft.network.StringNBTCommand;
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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.RegistrySimple;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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
	public final Minecraft mc = Minecraft.getMinecraft();
	
	// mc internals (exposed)
	public ModelManager mc_modelManager;
	
	// tc internals
    private int visualizationMode = 0;
    private EXTFontRenderer fontRenderer;
    private InfoBar infoBarInstance = new InfoBar();
    private ConcurrentLinkedDeque<Runnable> clientTickQeue = new ConcurrentLinkedDeque<Runnable>();
    private ConcurrentLinkedDeque<ITemporaryRenderable> clientRenderQeue = new ConcurrentLinkedDeque<ITemporaryRenderable>();
    
    // tc internals (final / constants)
 	private final KeyBinding buildModeBinding = new KeyBinding("key.toggleBuildMode", Keyboard.KEY_B, "key.categories.misc");
 	private final KeyBinding visualizationBinding = new KeyBinding("key.toggleWireframe", Keyboard.KEY_PERIOD, "key.categories.misc");
 	private final ResourceLocation selectionBoxTextuResourceLocation = new ResourceLocation("talecraft:textures/wandselection.png");
 	private final ResourceLocation whitePixelTextureResourceLocation = new ResourceLocation("talecraft:textures/blocks/util/white.png");
 	
 	public static final ResourceLocation colorReslocWhite = new ResourceLocation("talecraft:textures/colors/white.png");
 	public static final ResourceLocation colorReslocBlack = new ResourceLocation("talecraft:textures/colors/black.png");
 	public static final ResourceLocation colorReslocRed = new ResourceLocation("talecraft:textures/colors/red.png");
 	public static final ResourceLocation colorReslocBlue = new ResourceLocation("talecraft:textures/colors/blue.png");
 	public static final ResourceLocation colorReslocGreen = new ResourceLocation("talecraft:textures/colors/green.png");
 	public static final ResourceLocation colorReslocYellow = new ResourceLocation("talecraft:textures/colors/yellow.png");
 	public static final ResourceLocation colorReslocOrange = new ResourceLocation("talecraft:textures/colors/orange.png");
 	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		
		MinecraftForge.EVENT_BUS.register(this);
		
		// register all keybindings
		ClientRegistry.registerKeyBinding(buildModeBinding);
		ClientRegistry.registerKeyBinding(visualizationBinding);
		
		// register all tileentity renderers
		ClientRegistry.bindTileEntitySpecialRenderer(ClockBlockTileEntity.class,
				new GenericTileEntityRenderer<ClockBlockTileEntity>("talecraft:textures/blocks/util/timer.png"));
		ClientRegistry.bindTileEntitySpecialRenderer(RedstoneTriggerTileEntity.class,
				new GenericTileEntityRenderer<RedstoneTriggerTileEntity>("talecraft:textures/blocks/util/redstoneTriggerOff.png"));
		
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		
		// get hte ModelMesher and register ALL item-models
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		
		for(int i = 0; i < 7; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.killBlock), i, new ModelResourceLocation("talecraft:killblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.clockBlock), 0, new ModelResourceLocation("talecraft:clockblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.redstoneTrigger), 0, new ModelResourceLocation("talecraft:redstone_trigger", "inventory"));
		
		mesher.register(TaleCraftItems.wand, 0, new ModelResourceLocation("talecraft:wand", "inventory"));
		mesher.register(TaleCraftItems.filler, 0, new ModelResourceLocation("talecraft:filler", "inventory"));
		mesher.register(TaleCraftItems.eraser, 0, new ModelResourceLocation("talecraft:eraser", "inventory"));
		mesher.register(TaleCraftItems.teleporter, 0, new ModelResourceLocation("talecraft:teleporter", "inventory"));
		mesher.register(TaleCraftItems.instakill, 0, new ModelResourceLocation("talecraft:instakill", "inventory"));
		
		RenderingRegistry.registerEntityRenderingHandler(EntityPoint.class, new PointEntityRenderer(mc.getRenderManager()));
		
		// register StringNBTCommand-Packet
		TaleCraft.instance.simpleNetworkWrapper.registerMessage(new IMessageHandler() {
			@Override public IMessage onMessage(IMessage message, MessageContext ctx) {
				if(message instanceof StringNBTCommand) {
					StringNBTCommand cmd = (StringNBTCommand) message;
					ClientProxy.this.handleClientCommand(cmd.command,cmd.data);
				}
				return null;
			}
		}, StringNBTCommand.class, 0x01, Side.CLIENT);
		
		// register PlayerNBTDataMerge-Packet
		TaleCraft.instance.simpleNetworkWrapper.registerMessage(new IMessageHandler() {
			@Override public IMessage onMessage(final IMessage message, MessageContext ctx) {
				if(message instanceof PlayerNBTDataMerge) {
					final Minecraft mc = ClientProxy.this.mc;
					
					clientTickQeue.add(new Runnable(){
						Minecraft micr = mc;
						PlayerNBTDataMerge mpakDataMerge = (PlayerNBTDataMerge) message;
						@Override public void run() {
							if(micr.thePlayer != null) {
								micr.thePlayer.getEntityData().merge((mpakDataMerge.data));
							}
						}
					});
				}
				return null;
			}
		}, PlayerNBTDataMerge.class, 0x02, Side.CLIENT);
	} // init(..){}
	
	protected void handleClientCommand(String command, NBTTagCompound data) {
		// TODO: Commands SERVER->CLIENT ?
		
		if(command.equals("pushRenderable")) {
			ITemporaryRenderable renderable = PushRenderableFactory.parsePushRenderableFromNBT(data);
			if(renderable != null) {
				clientRenderQeue.offer(renderable);
			}
			return;
		}
		
	}

	public void modelBake(ModelBakeEvent event) {
	    this.mc_modelManager = event.modelManager;
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		
		// Create the singleton instance of the EXTFontRenderer
		fontRenderer = new EXTFontRenderer(mc.fontRendererObj);
	}
	
	@SubscribeEvent
	public void worldPass(RenderWorldLastEvent event) {
		WireframeMode.DISABLE();
		
		// Iterate trough all ITemporaryRenderables and remove the ones that can be removed.
		Iterator<ITemporaryRenderable> iterator = clientRenderQeue.iterator();
		while(iterator.hasNext()) {
			ITemporaryRenderable itr = iterator.next();
			if(itr.canRemove()) {
				iterator.remove();
			}
		}
		
		// If the world and the player exist, call the worldPostRender-method.
		if(mc.theWorld != null && mc.thePlayer != null) {
			GlStateManager.pushMatrix();
			
			double partialTicks = event.partialTicks;
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			
			worldPostRender(partialTicks, tessellator, worldrenderer);
			
			GlStateManager.popMatrix();
		}
		
		// Enable textures again, since the GUI-prerender doesn't enable it again by itself.
		GlStateManager.enableTexture2D();
		
		// XXX: UNUSED EXPERIMENTAL FEATURE
		// If active, render a fade-effect (this makes the screen go dark).
		// This overlays everything except the hand and the GUI, which is wrong.
		double fade = 0f;
		int color = 0x000000;
		if(fade > 0 && mc.ingameGUI != null) {
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GLU.gluOrtho2D(0, 2, 2, 0);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			{
				int alpha = MathHelper.clamp_int((int) (fade * 255), 0, 255);
				int mixed = ((alpha & 0xFF) << 24) | (color);
				mc.ingameGUI.drawRect(-1, -1, 4, 4, mixed);
			}
		}
	}
	
	private void worldPostRender(double partialTicks, Tessellator tessellator, WorldRenderer worldrenderer) {
		// Translate into World-Space
		double px = mc.thePlayer.prevPosX + (mc.thePlayer.posX - mc.thePlayer.prevPosX) * (double)partialTicks;
        double py = mc.thePlayer.prevPosY + (mc.thePlayer.posY - mc.thePlayer.prevPosY) * (double)partialTicks;
        double pz = mc.thePlayer.prevPosZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * (double)partialTicks;
		GL11.glTranslated(-px, -py, -pz);
		
		GlStateManager.disableCull();
		GlStateManager.disableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ZERO);
		
		// Wand Selection Rendering
		NBTTagCompound playerData = mc.thePlayer.getEntityData();
		if(playerData.hasKey("tcWand")) {
			NBTTagCompound tcWand = playerData.getCompoundTag("tcWand");
			
			if(tcWand.hasKey("cursor")) {
				final float E = -1f / 64f;
				int[] cursor = tcWand.getIntArray("cursor");
				
				GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_POINT);
				GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_FILL);
				GlStateManager.enableTexture2D();
				mc.getTextureManager().bindTexture(whitePixelTextureResourceLocation);
				BoxRenderer.renderBox(tessellator, worldrenderer, cursor[0]-E, cursor[1]-E, cursor[2]-E, cursor[0]+1+E, cursor[1]+1+E, cursor[2]+1+E, 0f,1f,1f,1f);
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			}
			
			// If not null, render the cursor selections boundaries.
			if(tcWand.hasKey("boundsA") && tcWand.hasKey("boundsB")) {
				// get bounds
				int[] a = tcWand.getIntArray("boundsA");
				int[] b = tcWand.getIntArray("boundsB");
				
				// make sure its correctly sorted
				int ix = Math.min(a[0], b[0]);
				int iy = Math.min(a[1], b[1]);
				int iz = Math.min(a[2], b[2]);
				int ax = Math.max(a[0], b[0]);
				int ay = Math.max(a[1], b[1]);
				int az = Math.max(a[2], b[2]);
				
				// 'error' offset
				final float E = 1f / 32f;
				
				// Prepare state
				GlStateManager.disableLighting();
				GlStateManager.disableNormalize();
				GlStateManager.enableTexture2D();
				RenderHelper.disableStandardItemLighting();
				mc.getTextureManager().bindTexture(selectionBoxTextuResourceLocation);
				
				// Render primary (with-depth) box
				BoxRenderer.renderSelectionBox(tessellator, worldrenderer, ix-E, iy-E, iz-E, ax+1+E, ay+1+E, az+1+E, 1);
				
				// Render secondary (no-depth) box
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
				GlStateManager.disableDepth();
				BoxRenderer.renderSelectionBox(tessellator, worldrenderer, ix-E, iy-E, iz-E, ax+1+E, ay+1+E, az+1+E, 1);
				GlStateManager.enableDepth();
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			}
		}
		
		GlStateManager.disableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		// Render all the temporary renderables
		for(ITemporaryRenderable renderable : clientRenderQeue) {
			renderable.render(mc, this, tessellator, worldrenderer, partialTicks);
		}
		
		// Render Item Meta Renderables
		if(mc.thePlayer != null && mc.thePlayer.getCurrentEquippedItem() != null) {
			ItemStack itemStack = mc.thePlayer.getCurrentEquippedItem();
			Item itemType = itemStack.getItem();
			
			ItemMetaWorldRenderer.tessellator = tessellator;
			ItemMetaWorldRenderer.worldrenderer = worldrenderer;
			ItemMetaWorldRenderer.partialTicks = partialTicks;
			ItemMetaWorldRenderer.clientProxy = this;
			ItemMetaWorldRenderer.world = mc.theWorld;
			ItemMetaWorldRenderer.player = mc.thePlayer;
			ItemMetaWorldRenderer.playerPosition = new Vec3(px, py, pz);
			ItemMetaWorldRenderer.render(itemType, itemStack);
		}
		
	}
	
	@SubscribeEvent
	public void worldPassPre(RenderWorldEvent.Pre event) {
		// Nothing hre yet!
	}
	
	/**
	 * This method is only called when the world is unloaded.
	 **/
	public void unloadWorld(World world) {
		if(world instanceof WorldClient) {
			// the client is either changing dimensions or leaving the server.
			// reset all temporary world related settings
			visualizationMode = 0;
		}
	}
	
	public boolean isBuildMode() {
		return mc.playerController != null && mc.playerController.isInCreativeMode();
	}
	
	public void tick(TickEvent event) {
		super.tick(event);
		
		if(event instanceof ClientTickEvent) {
			while(!clientTickQeue.isEmpty())
				clientTickQeue.poll().run();
		}
		
		if(event instanceof RenderTickEvent) {
			RenderTickEvent revt = (RenderTickEvent) event;
			
			// Pre-Scene Render
			if(revt.phase == Phase.START) {
				// this takes care of the CUSTOM SKY RENDERING
				if(mc.theWorld != null && mc.theWorld.provider != null) {
					boolean wireframeModeActive = isBuildMode() ? (visualizationMode != 0) : false;
					
					if(wireframeModeActive) {
						CustomSkyRenderer.instance.setDebugSky(true);
						mc.theWorld.provider.setSkyRenderer(CustomSkyRenderer.instance);
					} else {
						CustomSkyRenderer.instance.setDebugSky(false);
						mc.theWorld.provider.setSkyRenderer(null);
					}
				}
				
				// this enables the WIREFRAME-MODE if we are ingame
				if(mc.theWorld != null && mc.thePlayer != null) {
					WireframeMode.ENABLE(mc.thePlayer.capabilities.isCreativeMode ? visualizationMode : 0);
				}
				
				// this is part of the LIGHTING visualization mode
				if(visualizationMode == 3) {
					GlStateManager.disableTexture2D();
				}
			}
			
			// Post-World >> Pre-HUD Render
			if(revt.phase == Phase.END) {
				if(mc.ingameGUI != null && mc.theWorld != null && infoBarInstance.canDisplayInfoBar(mc, this) ) {
					infoBarInstance.display(mc, mc.thePlayer, mc.theWorld, this);
				}
			}
		}// RenderTickEvent {}
	}// tick {}
	
	/****/
	public void keyEvent(KeyInputEvent event) {
		// this toggles between the various visualization modes
		if(visualizationBinding.isPressed() && visualizationBinding.isKeyDown()) {
			visualizationMode = (visualizationMode + 1) & 3;
		}
		
		// this toggles between buildmode and adventuremode
		if(buildModeBinding.isPressed() && buildModeBinding.isKeyDown() && mc.theWorld != null && mc.thePlayer != null && !mc.isGamePaused()) {
			taleCraft.logger.info("Switching GameMode using the buildmode-key.");
			mc.thePlayer.sendChatMessage("/gamemode " + (isBuildMode() ? "2" : "1"));
			
			// these bunch of lines delete all display lists,
			// thus forcing the renderer to reupload the world to the GPU
			// (this process only takes several milliseconds)
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
	
	/***********************************/
	/**                               **/
	/**                               **/
	/**                               **/
	/***********************************/
	
	/****/
	public void sheduleClientTickTask(Runnable runnable) {
		this.clientTickQeue.push(runnable);
	}
	
	/****/
	public void sheduleClientRenderTask(ITemporaryRenderable renderable) {
		this.clientRenderQeue.push(renderable);
	}
	
	public int getVisualizationmode() {
		return this.visualizationMode;
	}
	
}
