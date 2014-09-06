package shedar.mods.ic2.nuclearcontrol;

import ic2.api.item.IC2Items;
import ic2.api.recipe.Recipes;

import java.io.File;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.MinecraftForge;
import shedar.mods.ic2.nuclearcontrol.blocks.BlockNuclearControlMain;
import shedar.mods.ic2.nuclearcontrol.crossmod.buildcraft.CrossBuildcraft;
import shedar.mods.ic2.nuclearcontrol.crossmod.ic2.CrossIndustrialCraft2;
import shedar.mods.ic2.nuclearcontrol.crossmod.railcraft.CrossRailcraft;
import shedar.mods.ic2.nuclearcontrol.crossmod.thermalexpansion.CrossTE;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardEnergyArrayLocation;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardEnergySensorLocation;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardLiquidArrayLocation;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardMultipleSensorLocation;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardReactorSensorLocation;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardText;
import shedar.mods.ic2.nuclearcontrol.items.ItemKitEnergySensor;
import shedar.mods.ic2.nuclearcontrol.items.ItemKitMultipleSensor;
import shedar.mods.ic2.nuclearcontrol.items.ItemKitReactorSensor;
import shedar.mods.ic2.nuclearcontrol.items.ItemNuclearControlMain;
import shedar.mods.ic2.nuclearcontrol.items.ItemTimeCard;
import shedar.mods.ic2.nuclearcontrol.items.ItemToolDigitalThermometer;
import shedar.mods.ic2.nuclearcontrol.items.ItemToolThermometer;
import shedar.mods.ic2.nuclearcontrol.items.ItemUpgrade;
import shedar.mods.ic2.nuclearcontrol.network.ChannelHandler;
import shedar.mods.ic2.nuclearcontrol.panel.ScreenManager;
import shedar.mods.ic2.nuclearcontrol.recipes.RecipesNew;
import shedar.mods.ic2.nuclearcontrol.recipes.RecipesOld;
import shedar.mods.ic2.nuclearcontrol.utils.Damages;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "IC2NuclearControl2", name="Nuclear Control 2", version="A2.0.0", dependencies="required-after:IC2", guiFactory = "shedar.mods.ic2.nuclearcontrol.gui.GuiFactory")
public class IC2NuclearControl{
    
    public static final int COLOR_WHITE = 15;
    public static final int COLOR_ORANGE = 14;
    public static final int COLOR_MAGENTA = 13;
    public static final int COLOR_LIGHT_BLUE = 12;
    public static final int COLOR_YELLOW = 11;
    public static final int COLOR_LIME = 10;
    public static final int COLOR_PINK = 9;
    public static final int COLOR_GRAY = 8;
    public static final int COLOR_LIGHT_GRAY = 7;
    public static final int COLOR_CYAN = 6;
    public static final int COLOR_PURPLE = 5;
    public static final int COLOR_BLUE = 4;
    public static final int COLOR_BROWN = 3;
    public static final int COLOR_GREEN = 2;
    public static final int COLOR_RED = 1;
    public static final int COLOR_BLACK = 0;
    
    //The instance of your mod forge uses
    @Instance
    public static IC2NuclearControl instance;
    
    //Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide = "shedar.mods.ic2.nuclearcontrol.ClientProxy", serverSide = "shedar.mods.ic2.nuclearcontrol.CommonProxy")
  
    //The proxy to be used by client and server
    public static CommonProxy proxy;
  	
  	//Mod's creative tab
  	public static IC2NCCreativeTabs tabIC2NC = new IC2NCCreativeTabs();
    
  	//For logging purposes
  	public static Logger logger;
  	public static ConfigurationHandler config;
  	
    protected File configFile;
    protected File configDir;
    
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
    public static BlockNuclearControlMain blockNuclearControlMain;
    public int modelId;
    public int alarmRange;
    public int SMPMaxAlarmRange;
    public int maxAlarmRange;
    public boolean isHttpSensorAvailable;
    public String httpSensorKey;
    public List<String> availableAlarms;
    public int remoteThermalMonitorEnergyConsumption;
    public ScreenManager screenManager = new ScreenManager();
    public int screenRefreshPeriod;
    public int rangeTriggerRefreshPeriod;
    public String recipes;

    public CrossBuildcraft crossBC;
    public CrossIndustrialCraft2 crossIC2;
    //public CrossGregTech crossGregTech;
    public CrossRailcraft crossRailcraft;
    public CrossTE crossThermalEx;

    protected void initBlocks(){
    	blockNuclearControlMain = new BlockNuclearControlMain();
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
    }

    public void registerBlocks(){
    	GameRegistry.registerBlock(blockNuclearControlMain, ItemNuclearControlMain.class, "blockNuclearControlMain");
    	GameRegistry.registerItem(itemToolThermometer, "itemToolThermometer");
    	GameRegistry.registerItem(itemToolDigitalThermometer, "itemToolDigitalThermometer");
    	GameRegistry.registerItem(itemRemoteSensorKit, "itemRemoteSensorKit");
    	GameRegistry.registerItem(itemEnergySensorKit, "itemEnergySensorKit");
    	GameRegistry.registerItem(itemMultipleSensorKit, "itemMultipleSensorKit");
    	GameRegistry.registerItem(itemSensorLocationCard, "itemSensorLocationCard");
    	GameRegistry.registerItem(itemEnergySensorLocationCard, "itemEnergySensorLocationCard");
    	GameRegistry.registerItem(itemMultipleSensorLocationCard, "itemMultipleSensorLocationCard");
    	GameRegistry.registerItem(itemEnergyArrayLocationCard, "itemEnergyArrayLocationCard");
    	GameRegistry.registerItem(itemTimeCard, "itemTimeCard");
    	GameRegistry.registerItem(itemUpgrade, "itemUpgrade");
    	GameRegistry.registerItem(itemTextCard, "itemTextCard");
    	GameRegistry.registerItem(itemLiquidArrayLocationCard, "itemLiquidArrayLocationCard");
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
    	logger = event.getModLog();
    	//Loads configuration
    	config = new ConfigurationHandler();
    	FMLCommonHandler.instance().bus().register(config);
    	config.init(event.getSuggestedConfigurationFile());

		//registers channel handler
		ChannelHandler.init();

		//Register event handlers
		MinecraftForge.EVENT_BUS.register(ServerTickHandler.instance);
		FMLCommonHandler.instance().bus().register(ServerTickHandler.instance);
		if (event.getSide().isClient()){
			MinecraftForge.EVENT_BUS.register(ClientTickHandler.instance);
			FMLCommonHandler.instance().bus().register(ClientTickHandler.instance);
		}
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt){
        crossBC = new CrossBuildcraft();
        crossIC2 = new CrossIndustrialCraft2();
        //crossGregTech = new CrossGregTech();
        crossRailcraft = new CrossRailcraft();
        crossThermalEx = new CrossTE();
        
        if(recipes.equals("normal")){ 
      	  RecipesNew.addRecipes();
        }
         
        if(recipes.equals("old")){
          RecipesOld.addOldRecipes();
        }
         
        if(recipes.equals("gregtech")){
          //TODO
          logger.error("Gregtech recipes not complete yet, try again later!");
        }
         
    }

    @EventHandler
    public void init(FMLInitializationEvent evt){
    	IC2NuclearControl.instance.screenManager = new ScreenManager();
		initBlocks();
		registerBlocks();
		proxy.registerTileEntities();
    }
}
