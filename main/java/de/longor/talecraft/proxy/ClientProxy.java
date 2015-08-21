package de.longor.talecraft.proxy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import de.longor.talecraft.TaleCraft;
import de.longor.talecraft.TaleCraftBlocks;
import de.longor.talecraft.TaleCraftItems;
import de.longor.talecraft.blocks.util.tileentity.BlockUpdateDetectorTileEntity;
import de.longor.talecraft.blocks.util.tileentity.ClockBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.CollisionTriggerBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.DelayBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.EmitterBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.ImageHologramBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.InverterBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.LightBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.MemoryBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.MessageBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.RedstoneTriggerBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.RelayBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.ScriptBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.StorageBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.SummonBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.TriggerFilterBlockTileEntity;
import de.longor.talecraft.blocks.util.tileentity.URLBlockTileEntity;
import de.longor.talecraft.client.ClientSettings;
import de.longor.talecraft.client.InfoBar;
import de.longor.talecraft.client.InvokeTracker;
import de.longor.talecraft.client.commands.TaleCraftClientCommands;
import de.longor.talecraft.client.gui.misc.GuiEntityEditor;
import de.longor.talecraft.client.gui.misc.GuiEntityEditor.RemoteEntityDataLink;
import de.longor.talecraft.client.gui.misc.GuiMapControl;
import de.longor.talecraft.client.network.PlayerDataMergeMessageHandler;
import de.longor.talecraft.client.network.StringNBTCommandMessageHandler;
import de.longor.talecraft.client.render.IRenderable;
import de.longor.talecraft.client.render.ITemporaryRenderable;
import de.longor.talecraft.client.render.PushRenderableFactory;
import de.longor.talecraft.client.render.RenderModeHelper;
import de.longor.talecraft.client.render.entity.PointEntityRenderer;
import de.longor.talecraft.client.render.renderables.SelectionBoxRenderer;
import de.longor.talecraft.client.render.renderers.CustomSkyRenderer;
import de.longor.talecraft.client.render.renderers.EXTFontRenderer;
import de.longor.talecraft.client.render.renderers.ItemMetaWorldRenderer;
import de.longor.talecraft.client.render.tileentity.GenericTileEntityRenderer;
import de.longor.talecraft.client.render.tileentity.ImageHologramBlockTileEntityEXTRenderer;
import de.longor.talecraft.client.render.tileentity.StorageBlockTileEntityEXTRenderer;
import de.longor.talecraft.client.render.tileentity.SummonBlockTileEntityEXTRenderer;
import de.longor.talecraft.clipboard.ClipboardItem;
import de.longor.talecraft.entities.EntityPoint;
import de.longor.talecraft.items.CopyItem;
import de.longor.talecraft.network.PlayerNBTDataMerge;
import de.longor.talecraft.network.StringNBTCommand;

/**
 * WARNING: God-Class
 **/
public class ClientProxy extends CommonProxy {
	// All the singletons!
	public static final Minecraft mc = Minecraft.getMinecraft();
	public static final ClientSettings settings = new ClientSettings();
	public static ClientProxy proxy = (ClientProxy) TaleCraft.proxy;
	
	// tc internals
	private int visualizationMode = 0;
	private float partialTicks = 0f;
	private EXTFontRenderer fontRenderer;
	private ClipboardItem currentClipboardItem;
	private InfoBar infoBarInstance;
	private InvokeTracker invokeTracker;
	// queues
	private final ConcurrentLinkedDeque<Runnable> clientTickQeue = new ConcurrentLinkedDeque<Runnable>();
	private final ConcurrentLinkedDeque<ITemporaryRenderable> clientRenderTemporary = new ConcurrentLinkedDeque<ITemporaryRenderable>();
	private final ConcurrentLinkedDeque<IRenderable> clientRenderStatic = new ConcurrentLinkedDeque<IRenderable>();
	
	// tc internals (final / constants)
	private final KeyBinding mapSettingsBinding = new KeyBinding("key.mapSettings", Keyboard.KEY_M, "key.categories.misc");
	private final KeyBinding buildModeBinding = new KeyBinding("key.toggleBuildMode", Keyboard.KEY_B, "key.categories.misc");
	private final KeyBinding visualizationBinding = new KeyBinding("key.toggleWireframe", Keyboard.KEY_PERIOD, "key.categories.misc");
	
	// Resource Locations
	public static final ResourceLocation textureReslocSelectionBox = new ResourceLocation("talecraft:textures/wandselection.png");
	public static final ResourceLocation textureReslocSelectionBox2 = new ResourceLocation("textures/misc/forcefield.png");
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
    	
    	init_loadsettings();
		
		MinecraftForge.EVENT_BUS.register(this);
		
		init_logic_keybindings();
		
    	TaleCraftClientCommands.init();
    	
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

		// Get the ModelMesher and register ALL item-models
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		init_render_block(mesher);
		init_render_item(mesher);

		init_render_entity();
		init_render_tilentity();

		init_logic_network();

	} // init(..){}
	
	private void init_loadsettings() {
		settings.init();
	}
	
	private void init_logic_keybindings() {
		// register all keybindings
		ClientRegistry.registerKeyBinding(mapSettingsBinding);
		ClientRegistry.registerKeyBinding(buildModeBinding);
		ClientRegistry.registerKeyBinding(visualizationBinding);
	}

	private void init_logic_network() {
		SimpleNetworkWrapper net = TaleCraft.instance.network;
		net.registerMessage(new StringNBTCommandMessageHandler(), StringNBTCommand.class, 0x01, Side.CLIENT);
		net.registerMessage(new PlayerDataMergeMessageHandler(), PlayerNBTDataMerge.class, 0x02, Side.CLIENT);
	}
	
	private void init_render_tilentity() {
		ClientRegistry.bindTileEntitySpecialRenderer(ClockBlockTileEntity.class,
		new GenericTileEntityRenderer<ClockBlockTileEntity>("talecraft:textures/blocks/util/timer.png"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(RedstoneTriggerBlockTileEntity.class,
		new GenericTileEntityRenderer<RedstoneTriggerBlockTileEntity>("talecraft:textures/blocks/util/redstoneTrigger.png"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(RelayBlockTileEntity.class,
		new GenericTileEntityRenderer<RelayBlockTileEntity>("talecraft:textures/blocks/util/relay.png"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(ScriptBlockTileEntity.class,
		new GenericTileEntityRenderer<ScriptBlockTileEntity>("talecraft:textures/blocks/util/script.png"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(BlockUpdateDetectorTileEntity.class,
		new GenericTileEntityRenderer<BlockUpdateDetectorTileEntity>("talecraft:textures/blocks/util/bud.png"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(StorageBlockTileEntity.class,
		new GenericTileEntityRenderer<StorageBlockTileEntity>("talecraft:textures/blocks/util/storage.png",
		new StorageBlockTileEntityEXTRenderer()));
		
		ClientRegistry.bindTileEntitySpecialRenderer(EmitterBlockTileEntity.class,
		new GenericTileEntityRenderer<EmitterBlockTileEntity>("talecraft:textures/blocks/util/emitter.png"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(ImageHologramBlockTileEntity.class,
		new GenericTileEntityRenderer<ImageHologramBlockTileEntity>("talecraft:textures/blocks/util/texture.png",
		new ImageHologramBlockTileEntityEXTRenderer()));
		
		ClientRegistry.bindTileEntitySpecialRenderer(CollisionTriggerBlockTileEntity.class,
		new GenericTileEntityRenderer<CollisionTriggerBlockTileEntity>("talecraft:textures/blocks/util/trigger.png"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(LightBlockTileEntity.class,
		new GenericTileEntityRenderer<LightBlockTileEntity>("talecraft:textures/blocks/util/light.png"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(MessageBlockTileEntity.class,
		new GenericTileEntityRenderer<MessageBlockTileEntity>("talecraft:textures/blocks/util/message.png"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(InverterBlockTileEntity.class,
		new GenericTileEntityRenderer<InverterBlockTileEntity>("talecraft:textures/blocks/util/inverter.png"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(MemoryBlockTileEntity.class,
		new GenericTileEntityRenderer<MemoryBlockTileEntity>("talecraft:textures/blocks/util/memory.png"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(TriggerFilterBlockTileEntity.class,
		new GenericTileEntityRenderer<TriggerFilterBlockTileEntity>("talecraft:textures/blocks/util/filter.png"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(DelayBlockTileEntity.class,
		new GenericTileEntityRenderer<DelayBlockTileEntity>("talecraft:textures/blocks/util/delay.png"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(URLBlockTileEntity.class,
		new GenericTileEntityRenderer<URLBlockTileEntity>("talecraft:textures/blocks/util/url.png"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(SummonBlockTileEntity.class,
		new GenericTileEntityRenderer<SummonBlockTileEntity>("talecraft:textures/blocks/util/spawner.png",
		new SummonBlockTileEntityEXTRenderer()));
		
		//
	}
	
	private void init_render_item(ItemModelMesher mesher) {
		// items
		mesher.register(TaleCraftItems.wand, 0, new ModelResourceLocation("talecraft:wand", "inventory"));
		mesher.register(TaleCraftItems.filler, 0, new ModelResourceLocation("talecraft:filler", "inventory"));
		mesher.register(TaleCraftItems.eraser, 0, new ModelResourceLocation("talecraft:eraser", "inventory"));
		mesher.register(TaleCraftItems.teleporter, 0, new ModelResourceLocation("talecraft:teleporter", "inventory"));
		mesher.register(TaleCraftItems.instakill, 0, new ModelResourceLocation("talecraft:instakill", "inventory"));
		mesher.register(TaleCraftItems.voxelbrush, 0, new ModelResourceLocation("talecraft:voxelbrush", "inventory"));
		mesher.register(TaleCraftItems.nudger, 0, new ModelResourceLocation("talecraft:nudger", "inventory"));
		mesher.register(TaleCraftItems.copy, 0, new ModelResourceLocation("talecraft:copy", "inventory"));
		mesher.register(TaleCraftItems.paste, 0, new ModelResourceLocation("talecraft:paste", "inventory"));
		mesher.register(TaleCraftItems.cut, 0, new ModelResourceLocation("talecraft:cut", "inventory"));
		mesher.register(TaleCraftItems.metaswapper, 0, new ModelResourceLocation("talecraft:metaswapper", "inventory"));
		mesher.register(TaleCraftItems.spawnpoint, 0, new ModelResourceLocation("talecraft:spawnpoint", "inventory"));
	}
	
	private void init_render_block(ItemModelMesher mesher) {
		// killblock (why?!)
		for(int i = 0; i < 7; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.killBlock), i, new ModelResourceLocation("talecraft:killblock", "inventory"));
		
		// decoration blocks
			// blank block
			for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.blankBlock), i, new ModelResourceLocation("talecraft:blankblock", "inventory"));
			
			// stone block A
			for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_a), i, new ModelResourceLocation("talecraft:deco_stone/block"+i, "inventory"));
			ModelBakery.addVariantName(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_a), mkstrlfint("talecraft:deco_stone/block", 0));
			// stone block B
			for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_b), i, new ModelResourceLocation("talecraft:deco_stone/block"+(i+16), "inventory"));
			ModelBakery.addVariantName(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_b), mkstrlfint("talecraft:deco_stone/block", 16));
			// stone block C
			for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_c), i, new ModelResourceLocation("talecraft:deco_stone/block"+(i+32), "inventory"));
			ModelBakery.addVariantName(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_c), mkstrlfint("talecraft:deco_stone/block", 32));
			// stone block D
			for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_d), i, new ModelResourceLocation("talecraft:deco_stone/block"+(i+48), "inventory"));
			ModelBakery.addVariantName(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_d), mkstrlfint("talecraft:deco_stone/block", 48));
			// stone block E
			for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_e), i, new ModelResourceLocation("talecraft:deco_stone/block"+(i+64), "inventory"));
			ModelBakery.addVariantName(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_e), mkstrlfint("talecraft:deco_stone/block", 64));
			// stone block E
			for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_f), i, new ModelResourceLocation("talecraft:deco_stone/block"+(i+80), "inventory"));
			ModelBakery.addVariantName(Item.getItemFromBlock(TaleCraftBlocks.deco_stone_f), mkstrlfint("talecraft:deco_stone/block", 80));
			
			// wood block A
			for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.deco_wood_a), i, new ModelResourceLocation("talecraft:deco_wood/block"+i, "inventory"));
			ModelBakery.addVariantName(Item.getItemFromBlock(TaleCraftBlocks.deco_wood_a), mkstrlfint("talecraft:deco_wood/block", 0));
			
			// glass block A
			for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.deco_glass_a), i, new ModelResourceLocation("talecraft:deco_glass/block"+i, "inventory"));
			ModelBakery.addVariantName(Item.getItemFromBlock(TaleCraftBlocks.deco_glass_a), mkstrlfint("talecraft:deco_glass/block", 0));
			
			// cage block A
			for(int i = 0; i < 16; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.deco_cage_a), i, new ModelResourceLocation("talecraft:deco_cage/block"+i, "inventory"));
			ModelBakery.addVariantName(Item.getItemFromBlock(TaleCraftBlocks.deco_cage_a), mkstrlfint("talecraft:deco_cage/block", 0));
		
		// barrier-ext block
//		for(int i = 0; i < 7; i++) mesher.register(Item.getItemFromBlock(TaleCraftBlocks.barrierEXTBlock), i, new ModelResourceLocation("talecraft:barrierextblock", "inventory"));
		
		// blocks
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.clockBlock), 0, new ModelResourceLocation("talecraft:clockblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.redstoneTrigger), 0, new ModelResourceLocation("talecraft:redstone_trigger", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.redstoneActivator), 0, new ModelResourceLocation("talecraft:redstone_activator", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.relayBlock), 0, new ModelResourceLocation("talecraft:relayblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.scriptBlock), 0, new ModelResourceLocation("talecraft:scriptblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.updateDetectorBlock), 0, new ModelResourceLocation("talecraft:updatedetectorblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.storageBlock), 0, new ModelResourceLocation("talecraft:storageblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.emitterBlock), 0, new ModelResourceLocation("talecraft:emitterblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.imageHologramBlock), 0, new ModelResourceLocation("talecraft:imagehologramblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.collisionTriggerBlock), 0, new ModelResourceLocation("talecraft:collisiontriggerblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.lightBlock), 0, new ModelResourceLocation("talecraft:lightblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.hiddenBlock), 0, new ModelResourceLocation("talecraft:hiddenblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.messageBlock), 0, new ModelResourceLocation("talecraft:messageblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.inverterBlock), 0, new ModelResourceLocation("talecraft:inverterblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.memoryBlock), 0, new ModelResourceLocation("talecraft:memoryblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.triggerFilterBlock), 0, new ModelResourceLocation("talecraft:triggerfilterblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.delayBlock), 0, new ModelResourceLocation("talecraft:delayblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.urlBlock), 0, new ModelResourceLocation("talecraft:urlblock", "inventory"));
		mesher.register(Item.getItemFromBlock(TaleCraftBlocks.summonBlock), 0, new ModelResourceLocation("talecraft:summonblock", "inventory"));
		
	}
	
	private String[] mkstrlfint(String string, int j) {
		String[] ary = new String[16];
		
		for(int i = 0; i < 16; i++)
			ary[i] = string + (j+i);
		
		return ary;
	}
	
	private void init_render_entity() {
		RenderingRegistry.registerEntityRenderingHandler(EntityPoint.class, new PointEntityRenderer(mc.getRenderManager()));
	}
	
	public void handleClientCommand(String command, NBTTagCompound data) {
		if(command.equals("client.network.join")) {
			TaleCraft.logger.info("Sending TaleCraft data to server...");
			
			String tccommand = "join acknowledged";
			NBTTagCompound tcdata = new NBTTagCompound();
			TaleCraft.network.sendToServer(new StringNBTCommand(tccommand, tcdata));
			
			settings.send();
			return;
		}
		
		if(command.equals("client.debug.track.invoke")) {
			invokeTracker.trackInvoke(data);
			return;
		}
		
		if(command.equals("client.gui.editor.entity")) {
			final UUID uuid = UUID.fromString(data.getString("entityUUID"));
			final NBTTagCompound entity = data.getCompoundTag("entityData");
			
			// Open the GUI in the next tick.
			sheduleClientTickTask(new Runnable() {
				@Override
				public void run() {
					RemoteEntityDataLink dataLink = new RemoteEntityDataLink() {
						UUID entityUUID = uuid;
						@Override public void updateData(NBTTagCompound entityData) {
							NBTTagCompound compound = new NBTTagCompound();
							compound.setString("entityUUID", entityUUID.toString());
							compound.setTag("entityData", entityData);
							
							String cmd = "data.entity.merge";
							StringNBTCommand command = new StringNBTCommand(cmd, compound);
							TaleCraft.network.sendToServer(command);
						}
					};
					mc.displayGuiScreen(new GuiEntityEditor(entity, dataLink));
				}
			});
			return;
		}
		
		if(command.equals("client.gui.openurl")) {
			final String url = data.getString("url");
			
			// This is possibly a stupid idea...
			if(data.getBoolean("force")) {
                Sys.openURL(url);
				return;
			}
			
			// Open the GUI in the next tick.
			sheduleClientTickTask(new Runnable() {
				@Override
				public void run() {
					GuiConfirmOpenLink gui = new GuiConfirmOpenLink(new GuiYesNoCallback() {
						@Override public void confirmClicked(boolean result, int id) {
					        if (id == 31102009) {
					            if (result) {
					                Sys.openURL(url);
					            }
					            mc.displayGuiScreen(null);
					        }
						}
					}, url, 31102009, true);
					
					mc.displayGuiScreen(gui);
				}
			});
			return;
		}
		
		if(command.equals("item.copy.trigger")) {
			sheduleClientTickTask(new Runnable() {
				@Override
				public void run() {
					CopyItem copy = TaleCraftItems.copy;
					ItemStack stack = new ItemStack(copy);
					copy.onItemRightClick(stack, mc.theWorld, mc.thePlayer);
				}
			});
		}
		
		if(command.equals("client.render.renderable.push")) {
			ITemporaryRenderable renderable = PushRenderableFactory.parsePushRenderableFromNBT(data);
			if(renderable != null && isBuildMode()) {
				clientRenderTemporary.offer(renderable);
			}
			return;
		}
		
		if(command.equals("client.render.renderable.clear")) {
			clientRenderTemporary.clear();
			return;
		}
		
//		if(command.equals("switchShader") && Boolean.FALSE.booleanValue()) {
//			final String sh = data.getString("shaderName");
//			clientTickQeue.offer(new Runnable() {
//				String shader = sh;
//				@Override
//				public void run() {
//					System.out.println("SWITCH : " + shader);
//
//					Field[] fields = mc.entityRenderer.getClass().getDeclaredFields();
//					Field shaderResourceLocations = null;
//					for(Field field : fields) {
//						System.out.println("entityRenderer."+field.getName() + " : " + field.getType());
//					}
//				}
//			});
//			return;
//		}
		
		TaleCraft.logger.info("Received Command -> " + command + ", with data: " + data);
		// XXX: Implement more Server->Client commands.
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		
		// Create the singleton instance of the EXTFontRenderer
		fontRenderer = new EXTFontRenderer(mc.fontRendererObj);
		
		// Create the InfoBar Instance
		infoBarInstance = new InfoBar();
		
		// Create te invoke tracker instance
		invokeTracker = new InvokeTracker();
		
		clientRenderStatic.offer(new SelectionBoxRenderer());
	}

	@SubscribeEvent
	public void worldPass(RenderWorldLastEvent event) {
		RenderModeHelper.DISABLE();
		
		// Iterate trough all ITemporaryRenderables and remove the ones that can be removed.
		Iterator<ITemporaryRenderable> iterator = clientRenderTemporary.iterator();
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
	}

	@SubscribeEvent
	public void worldPostRenderHand(RenderHandEvent event) {
		
		// XXX: UNUSED EXPERIMENTAL FEATURE
		// If active, render a fade-effect (this makes the screen go dark).
		// This overlays everything except the hand and the GUI, which is wrong.
		double fade = 0.0f;
		int color = 0x000000;
		if(fade > 0 && mc.ingameGUI != null) {
			// Draw Overlay
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
			
			RenderHelper.disableStandardItemLighting();
			
			// Do NOT draw the hand!
			event.setCanceled(true);
		}
		
		// Enable for reasons stated in:
		// ClientProxy..worldPass() -> Last line of code.
		GlStateManager.enableTexture2D();
	}
	
	private void worldPostRender(double partialTicks, Tessellator tessellator, WorldRenderer worldrenderer) {
		this.partialTicks = (float) partialTicks;
		
		// Translate into World-Space
		double px = mc.thePlayer.prevPosX + (mc.thePlayer.posX - mc.thePlayer.prevPosX) * (double)partialTicks;
		double py = mc.thePlayer.prevPosY + (mc.thePlayer.posY - mc.thePlayer.prevPosY) * (double)partialTicks;
		double pz = mc.thePlayer.prevPosZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * (double)partialTicks;
		GL11.glTranslated(-px, -py, -pz);
		
		GlStateManager.disableCull();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.color(1, 1, 1, 1);
		RenderHelper.enableStandardItemLighting();
		
		// Render all the renderables
		for(IRenderable renderable : clientRenderStatic) {
			renderable.render(mc, this, tessellator, worldrenderer, partialTicks);
		}
		
		GlStateManager.disableCull();
		GlStateManager.enableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableStandardItemLighting();
		
		// Render all the temporary renderables
		for(ITemporaryRenderable renderable : clientRenderTemporary) {
			renderable.render(mc, this, tessellator, worldrenderer, partialTicks);
		}
		
		GlStateManager.disableCull();
		GlStateManager.enableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableStandardItemLighting();
		
		// Render Item Meta Renderables
		if(mc.thePlayer != null && mc.thePlayer.getCurrentEquippedItem() != null) {
			ItemStack itemStack = mc.thePlayer.getCurrentEquippedItem();
			Item itemType = itemStack.getItem();
			
			ItemMetaWorldRenderer.tessellator = tessellator;
			ItemMetaWorldRenderer.worldrenderer = worldrenderer;
			ItemMetaWorldRenderer.partialTicks = partialTicks;
			ItemMetaWorldRenderer.partialTicksF = (float) partialTicks;
			ItemMetaWorldRenderer.clientProxy = this;
			ItemMetaWorldRenderer.world = mc.theWorld;
			ItemMetaWorldRenderer.player = mc.thePlayer;
			ItemMetaWorldRenderer.playerPosition = new Vec3(px, py, pz);
			ItemMetaWorldRenderer.render(itemType, itemStack);
		}
		
		GlStateManager.enableCull();;
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.color(1, 1, 1, 1);
		RenderHelper.enableStandardItemLighting();
	}
	
	@SubscribeEvent
	public void worldPassPre(RenderWorldEvent.Pre event) {
		// Nothing hre yet!
	}
	
	public void loadWorld(World world) {
		
		// This only works on the server side!
		// System.out.println("World loaded: " + world);
		
	}
	
	/**
	 * This method is called when the world is unloaded.
	 **/
	public void unloadWorld(World world) {
		if(world instanceof WorldClient) {
			// the client is either changing dimensions or leaving the server.
			// reset all temporary world related settings here
			// delete all temporary world related objects here
			visualizationMode = 0;
			clientRenderTemporary.clear();
			
			// This is stupid but,
			// Save the TaleCraft settings on World unload.
			settings.save();
		}
	}
	
	/**
	 * @return TRUE, if the client is in build-mode (aka: creative-mode), FALSE if not.
	 **/
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
					RenderModeHelper.ENABLE(mc.thePlayer.capabilities.isCreativeMode ? visualizationMode : 0);
					
					if(visualizationMode == 0) {
						mc.gameSettings.clouds = true;
					} else {
						mc.gameSettings.clouds = false;
					}
					
					// this is part of the LIGHTING visualization mode
					if(visualizationMode == 3) {
						GlStateManager.disableTexture2D();
					}
					
					if(visualizationMode == 4) {
				        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
				        GlStateManager.disableTexture2D();
				        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
				        GlStateManager.disableFog();
					}
				}
			}
			
			// Post-World >> Pre-HUD Render
			if(revt.phase == Phase.END) {
				if(mc.ingameGUI != null && mc.theWorld != null) {
					if(infoBarInstance.canDisplayInfoBar(mc, this)) {
						infoBarInstance.display(mc, mc.thePlayer, mc.theWorld, this);
						
						// XXX: Move this to its own IF
						invokeTracker.display(mc, mc.thePlayer, mc.theWorld, this);
					}
				}
			}
		}// RenderTickEvent {}
	}// tick {}
	
	/****/
	public void keyEvent(KeyInputEvent event) {
		// this toggles between the various visualization modes
		if(visualizationBinding.isPressed() && visualizationBinding.isKeyDown()) {
			setVisualizationMode(visualizationMode+1);
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
		
		if(
			mapSettingsBinding.isPressed() && mapSettingsBinding.isKeyDown() &&
			isBuildMode() && mc.thePlayer != null && mc.theWorld != null
		) {
			// XXX: Disabled functionality.
			// mc.displayGuiScreen(new GuiMapControl());
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
		this.clientRenderTemporary.push(renderable);
	}

	/****/
	public int getVisualizationmode() {
		return this.visualizationMode;
	}

	/****/
	public int getTemporablesCount() {
		return clientRenderTemporary.size();
	}

	/****/
	public int getStaticCount() {
		return clientRenderStatic.size();
	}

	/****/
	public void setVisualizationMode(int mode) {
		visualizationMode = mode;
		
		if(visualizationMode < 0) {
			visualizationMode = 0;
		}
		
		if(visualizationMode > 4) {
			visualizationMode = 0;
		}
	}

	/****/
	public void setClipboard(ClipboardItem item) {
		currentClipboardItem = item;
	}

	/****/
	public ClipboardItem getClipboard() {
		return currentClipboardItem;
	}

	/****/
	public float getLastPartialTicks() {
		return partialTicks;
	}

	/****/
	public NBTTagCompound getSettings(EntityPlayer playerIn) {
		return getSettings().getNBT();
	}

	/****/
	public ClientSettings getSettings() {
		return settings;
	}

	/****/
	public static final boolean isInBuildMode() {
		if(proxy == null)
			proxy = TaleCraft.proxy.asClient();
		
		return proxy.isBuildMode();
	}

	public InfoBar getInfoBar() {
		return infoBarInstance;
	}
	
	public static void shedule(Runnable runnable) {
		proxy.sheduleClientTickTask(runnable);
	}
	
}
