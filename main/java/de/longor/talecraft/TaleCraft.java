package de.longor.talecraft;

import org.apache.logging.log4j.Logger;

import sun.net.NetworkClient;
import de.longor.talecraft.managers.TCWorldsManager;
import de.longor.talecraft.network.StringNBTCommand;
import de.longor.talecraft.proxy.CommonProxy;
import de.longor.talecraft.proxy.ServerHandler;
import de.longor.util.TimedExecutor;
import net.minecraft.block.Block;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.NetworkSystem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistrySimple;
import net.minecraft.world.World;
import net.minecraftforge.client.gui.ForgeGuiFactory.ForgeConfigGui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
public class TaleCraft
{
    @Mod.Instance(Reference.MOD_ID)
    public static TaleCraft instance;
    public static ModContainer container;
    public static TCWorldsManager coremanager;
    public static TaleCraftCommonEventHandler fmlEventHandler;
    public static TaleCraftForgeEventHandler forgeEventHandler;
    public static SimpleNetworkWrapper simpleNetworkWrapper;
    public static TimedExecutor timedExecutor;
    public static Logger logger;
    
    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY, modId = Reference.MOD_ID)
    public static CommonProxy proxy;
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	logger = event.getModLog();
    	container = FMLCommonHandler.instance().findContainerFor(instance);
    	
    	logger.info("TaleCraft initialization...");
    	logger.info("TaleCraft Version: " + Reference.MOD_VERSION);
    	logger.info("TaleCraft ModContainer: " + container);
    	
    	MinecraftForge.EVENT_BUS.register(this);
    	
    	coremanager = new TCWorldsManager(this);
    	timedExecutor = new TimedExecutor();
    	simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("TaleCraftNet");
		
    	// this does NOT belong here!
    	// Register the handler for server-side StringNBT-commands.
		TaleCraft.instance.simpleNetworkWrapper.registerMessage(new IMessageHandler() {
			@Override public IMessage onMessage(IMessage message, MessageContext ctx) {
				if(message instanceof StringNBTCommand) {
					StringNBTCommand cmd = (StringNBTCommand) message;
					ServerHandler.handleSNBTCommand(ctx.getServerHandler(), cmd);
				}
				return null;
			}
		}, StringNBTCommand.class, 0x01, Side.SERVER);
    	
		/// print debug information
    	logger.info("TaleCraft CoreManager @" + coremanager.hashCode());
    	logger.info("TaleCraft TimedExecutor @" + timedExecutor.hashCode());
    	logger.info("TaleCraft NET SimpleNetworkWrapper @" + simpleNetworkWrapper.hashCode());
    	
    	// create and register the event handlers for COMMON
    	fmlEventHandler = new TaleCraftCommonEventHandler(this);
    	FMLCommonHandler.instance().bus().register(fmlEventHandler);
    	logger.info("TaleCraft CME Handler @" + fmlEventHandler.hashCode());
    	
    	// create and register the event handlers for FORGE
    	forgeEventHandler = new TaleCraftForgeEventHandler(this);
    	MinecraftForge.EVENT_BUS.register(forgeEventHandler);
    	logger.info("TaleCraft EVTB Handler @" + forgeEventHandler.hashCode());
    	
    	// Initialize all the Tabs/Blocks/Items/Commands etc.etc.
    	logger.info("Loading Tabs, Blocks, Items... (In that order)");
    	TaleCraftTabs.init();
    	TaleCraftBlocks.init();
    	TaleCraftItems.init();
    	TaleCraftCommands.init();
    	
    	// Initialize the Proxy(s)
    	logger.info("Initializing Proxy...");
    	proxy.taleCraft = this;
    	proxy.preInit(event);
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.init(event);
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	proxy.postInit(event);
    	logger.info("TaleCraft initialized, all systems ready.");
    }
	
	@Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event){
		ICommandManager cmdmng = MinecraftServer.getServer().getCommandManager();
		if (cmdmng instanceof ServerCommandManager && cmdmng instanceof CommandHandler) {
			CommandHandler cmdhnd = (CommandHandler) cmdmng;
			TaleCraftCommands.register(cmdhnd);
		}
	}
}
