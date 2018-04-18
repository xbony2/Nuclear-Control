package shedar.mods.ic2.nuclearcontrol;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import java.io.File;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.Logger;

import shedar.mods.ic2.nuclearcontrol.blocks.BlockNuclearControlLight;
import shedar.mods.ic2.nuclearcontrol.blocks.BlockNuclearControlMain;
import shedar.mods.ic2.nuclearcontrol.crossmod.CrossModLoader;
import shedar.mods.ic2.nuclearcontrol.crossmod.ModLib;
import shedar.mods.ic2.nuclearcontrol.crossmod.RF.CrossBuildcraft;
import shedar.mods.ic2.nuclearcontrol.crossmod.RF.CrossRF;
import shedar.mods.ic2.nuclearcontrol.crossmod.gregtech.CrossGregTech;
import shedar.mods.ic2.nuclearcontrol.crossmod.gregtech.GregtechRecipes;
import shedar.mods.ic2.nuclearcontrol.crossmod.ic2.IC2Cross;
import shedar.mods.ic2.nuclearcontrol.crossmod.ic2.IC2Type;
import shedar.mods.ic2.nuclearcontrol.crossmod.opencomputers.CrossOpenComputers;
import shedar.mods.ic2.nuclearcontrol.crossmod.railcraft.CrossRailcraft;
import shedar.mods.ic2.nuclearcontrol.items.ItemCard55Reactor;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardEnergyArrayLocation;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardEnergySensorLocation;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardLiquidArrayLocation;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardMultipleSensorLocation;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardReactorSensorLocation;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardText;
import shedar.mods.ic2.nuclearcontrol.items.ItemKitEnergySensor;
import shedar.mods.ic2.nuclearcontrol.items.ItemKitMultipleSensor;
import shedar.mods.ic2.nuclearcontrol.items.ItemKitReactorSensor;
import shedar.mods.ic2.nuclearcontrol.items.ItemNuclearControlLight;
import shedar.mods.ic2.nuclearcontrol.items.ItemNuclearControlMain;
import shedar.mods.ic2.nuclearcontrol.items.ItemRemoteMonitor;
import shedar.mods.ic2.nuclearcontrol.items.ItemTimeCard;
import shedar.mods.ic2.nuclearcontrol.items.ItemToolDigitalThermometer;
import shedar.mods.ic2.nuclearcontrol.items.ItemToolThermometer;
import shedar.mods.ic2.nuclearcontrol.items.ItemUpgrade;
import shedar.mods.ic2.nuclearcontrol.network.ChannelHandler;
import shedar.mods.ic2.nuclearcontrol.panel.ScreenManager;
import shedar.mods.ic2.nuclearcontrol.recipes.RecipesNew;
import shedar.mods.ic2.nuclearcontrol.recipes.RecipesOld;



@Mod(modid = "IC2NuclearControl", name = "Nuclear Control 2", version = "@VERSION@", dependencies = "required-after:IC2; after:gregtech;", guiFactory = "shedar.mods.ic2.nuclearcontrol.gui.GuiFactory")
public class IC2NuclearControl {

	// The instance of your mod forge uses
	@Instance
	public static IC2NuclearControl instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "shedar.mods.ic2.nuclearcontrol.ClientProxy", serverSide = "shedar.mods.ic2.nuclearcontrol.CommonProxy")
	// The proxy to be used by client and server
	public static CommonProxy proxy;

	// Mod's creative tab
	public static IC2NCCreativeTabs tabIC2NC = new IC2NCCreativeTabs();

	// For logging purposes
	public static Logger logger;
	public static ConfigurationHandler config;

	protected File configFile;
	protected File configDir;

	public static boolean isServer;
	public String allowedAlarms;
	public List<String> serverAllowedAlarms;
	public static Item itemToolThermometer;
	public static Item itemToolDigitalThermometer;
	public static Item itemRemoteSensorKit;
	public static Item itemEnergySensorKit;
	public static Item itemMultipleSensorKit;
	public static Item itemSensorLocationCard;
	public static Item itemEnergySensorLocationCard;
	public static Item itemMultipleSensorLocationCard;
	public static Item itemEnergyArrayLocationCard;
	public static Item itemTimeCard;
	public static Item itemUpgrade;
	public static Item itemTextCard;
	public static Item itemLiquidArrayLocationCard;
	public static Item itemWindCard;
	public static Item itemRemoteMonitor;
	public static Item item55ReactorCard;
	public static BlockNuclearControlMain blockNuclearControlMain;
	public static BlockNuclearControlLight blockNuclearControlLight;
	public int modelId;
	public int alarmRange;
	public int SMPMaxAlarmRange;
	public int maxAlarmRange;
	//public static boolean isHttpSensorAvailableClient;
	//public static boolean isHttpSensorAvailableServer;
	public String httpSensorKey;
	public List<String> availableAlarms;
	public int remoteThermalMonitorEnergyConsumption;
	public ScreenManager screenManager = new ScreenManager();
	public int screenRefreshPeriod;
	public int dataRefreshPeriod;
	public int rangeTriggerRefreshPeriod;
	public boolean disableCapes;
	public String recipes;

	public CrossBuildcraft crossBC;
	public CrossRailcraft crossRailcraft;
	public CrossRF crossRF;
	public CrossOpenComputers crossOC;
	public IC2Cross crossIc2;
	public CrossGregTech crossGT;

	protected void initBlocks() {
		blockNuclearControlMain = new BlockNuclearControlMain();
		blockNuclearControlLight = new BlockNuclearControlLight();
		itemToolThermometer = new ItemToolThermometer().setUnlocalizedName("ItemToolThermometer");
		itemToolDigitalThermometer = new ItemToolDigitalThermometer(1, 80, 80).setUnlocalizedName("ItemToolDigitalThermometer");
		itemSensorLocationCard = new ItemCardReactorSensorLocation().setUnlocalizedName("ItemSensorLocationCard");
		itemUpgrade = new ItemUpgrade();
		itemTimeCard = new ItemTimeCard().setUnlocalizedName("ItemTimeCard");
		itemTextCard = new ItemCardText().setUnlocalizedName("ItemTextCard");
		itemEnergySensorLocationCard = new ItemCardEnergySensorLocation().setUnlocalizedName("ItemEnergySensorLocationCard");
		itemEnergyArrayLocationCard = new ItemCardEnergyArrayLocation().setUnlocalizedName("ItemEnergyArrayLocationCard");
		itemMultipleSensorLocationCard = new ItemCardMultipleSensorLocation();
		itemMultipleSensorKit = new ItemKitMultipleSensor().setUnlocalizedName("ItemCounterSensorKit");
		itemEnergySensorKit = new ItemKitEnergySensor().setUnlocalizedName("ItemEnergySensorKit");
		itemRemoteSensorKit = new ItemKitReactorSensor().setUnlocalizedName("ItemRemoteSensorKit");
		itemLiquidArrayLocationCard = new ItemCardLiquidArrayLocation().setUnlocalizedName("ItemLiquidArrayLocationCard");
		item55ReactorCard = new ItemCard55Reactor().setUnlocalizedName("Item55ReactorCard");
        itemRemoteMonitor = new ItemRemoteMonitor().setUnlocalizedName("remoteMonitor");
	}

	protected void registerBlocks() {
		GameRegistry.registerBlock(blockNuclearControlMain, ItemNuclearControlMain.class, "blockNuclearControlMain");
		GameRegistry.registerBlock(blockNuclearControlLight, ItemNuclearControlLight.class,"blockNuclearControlLight");
		GameRegistry.registerItem(itemToolThermometer, "ItemToolThermometer");
		GameRegistry.registerItem(itemToolDigitalThermometer, "ItemToolDigitalThermometer");
		GameRegistry.registerItem(itemRemoteSensorKit, "ItemRemoteSensorKit");
		GameRegistry.registerItem(itemEnergySensorKit, "ItemEnergySensorKit");
		GameRegistry.registerItem(itemMultipleSensorKit, "ItemMultipleSensorKit");
		GameRegistry.registerItem(itemSensorLocationCard, "ItemSensorLocationCard");
		GameRegistry.registerItem(itemEnergySensorLocationCard, "ItemEnergySensorLocationCard");
		GameRegistry.registerItem(itemMultipleSensorLocationCard, "ItemMultipleSensorLocationCard");
		GameRegistry.registerItem(itemEnergyArrayLocationCard, "ItemEnergyArrayLocationCard");
		GameRegistry.registerItem(itemTimeCard, "ItemTimeCard");
		GameRegistry.registerItem(itemUpgrade, "ItemUpgrade");
		GameRegistry.registerItem(itemTextCard, "ItemTextCard");
		GameRegistry.registerItem(itemLiquidArrayLocationCard, "ItemLiquidArrayLocationCard");
		GameRegistry.registerItem(item55ReactorCard, "Item55ReactorCard");
        GameRegistry.registerItem(itemRemoteMonitor, "remoteMonitor");
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		if (event.getSide() == Side.CLIENT)
			isServer = false;
		else
			isServer = true;

		// Loads configuration
		config = new ConfigurationHandler();
		FMLCommonHandler.instance().bus().register(config);
		config.init(event.getSuggestedConfigurationFile());

		// registers channel handler
		ChannelHandler.init();

		// Register event handlers
		MinecraftForge.EVENT_BUS.register(ServerTickHandler.instance);
		FMLCommonHandler.instance().bus().register(ServerTickHandler.instance);
		if (!isServer) {
			MinecraftForge.EVENT_BUS.register(ClientTickHandler.instance);
			FMLCommonHandler.instance().bus().register(ClientTickHandler.instance);
		}
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
        CrossModLoader.preinit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		IC2NuclearControl.instance.screenManager = new ScreenManager();
		initBlocks();
		registerBlocks();
		proxy.registerTileEntities();
		CrossModLoader.init();
		if(Loader.isModLoaded("OpenComputers")) crossOC = new CrossOpenComputers();
		//Registers waila stuff
		//FMLInterModComms.sendMessage("Waila", "register", "shedar.mods.ic2.nuclearcontrol.crossmod.waila.CrossWaila.callbackRegister");
		//CrossBigReactors.doStuff();
		//CrossAppeng.RegistrationCheck();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if(!disableCapes) {
			proxy.cape();
		}
		CrossModLoader.postinit();
		crossBC = new CrossBuildcraft();
		crossRailcraft = new CrossRailcraft();
		crossRF = new CrossRF();
		crossIc2 = IC2Cross.getIC2Cross();
		if (crossIc2.getType() == IC2Type.SPEIGER) {
			if (recipes.equalsIgnoreCase("normal-force")) {
				logger.info("Loading normal recipes with IC2 Classic may prevent certain recipes working");
				RecipesNew.addRecipes();
			} else if (recipes.equalsIgnoreCase("gregtech-force")) {
				logger.info("Loading Gregtech recipes with IC2 Classic will prevent certain recipes working");
				GregtechRecipes.addRecipes();
			} else {
				RecipesOld.addOldRecipes();
			}
		} else if (recipes.equalsIgnoreCase("old")) {
			RecipesOld.addOldRecipes();
		} else if (recipes.equalsIgnoreCase("gregtech") || recipes.equalsIgnoreCase("gregtech5")) {
			GregtechRecipes.addRecipes();
			logger.info("Hard... I mean, FUN recipes turned on! Have fun!");
		} else {
			RecipesNew.addRecipes();
		}
		crossGT = new CrossGregTech();
		/*
		//I thought about doing this, but I didn't :P
		ItemStack dBlock = new ItemStack(Blocks.diamond_block);
		dBlock.setStackDisplayName("ERROR: report to skyboy!");
		Recipes.advRecipes.addRecipe(dBlock, new Object[]{
			"GGG", "GGG", "GGG",
				'G', "greggy_greg_do_please_kindly_stuff_a_sock_in_it"});*/
	}
}
