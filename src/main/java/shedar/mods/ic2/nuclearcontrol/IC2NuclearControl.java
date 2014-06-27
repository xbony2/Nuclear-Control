package shedar.mods.ic2.nuclearcontrol;


import ic2.api.item.IC2Items;
import ic2.api.recipe.Recipes;

import java.io.File;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.MinecraftForge;

/*
import org.modstats.ModstatInfo;
import org.modstats.Modstats;
*/
import shedar.mods.ic2.nuclearcontrol.crossmod.buildcraft.CrossBuildcraft;
import shedar.mods.ic2.nuclearcontrol.crossmod.gregtech.CrossGregTech;
import shedar.mods.ic2.nuclearcontrol.crossmod.ic2.CrossIndustrialCraft2;
import shedar.mods.ic2.nuclearcontrol.crossmod.railcraft.CrossRailcraft;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardEnergyArrayLocation;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardEnergySensorLocation;
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
import shedar.mods.ic2.nuclearcontrol.panel.ScreenManager;
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
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "IC2NuclearControl", name="Nuclear Control", version="1.6.2d", dependencies = "after:IC2")
/*@NetworkMod(channels = { "NuclearControl" }, clientSideRequired = true, serverSideRequired = false, 
            packetHandler = PacketHandler.class, connectionHandler = ConnectionHandler.class)
@ModstatInfo(prefix="nc")*/
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
    
    public static final String LOG_PREFIX = "[IC2NuclearControl] ";
    public static final String NETWORK_CHANNEL_NAME = "NuclearControl";
    
    public static final String[] builtInAlarms = {"alarm-default.ogg", "alarm-sci-fi.ogg"};
    
    @Instance
    public static IC2NuclearControl instance;
    
    @SidedProxy(clientSide = "shedar.mods.ic2.nuclearcontrol.ClientProxy", serverSide = "shedar.mods.ic2.nuclearcontrol.CommonProxy")
    public static CommonProxy proxy;
    
    protected File configFile;
    protected File configDir;
    
    public String allowedAlarms;
    public List<String> serverAllowedAlarms;
    public Item itemToolThermometer;
    public Item itemToolDigitalThermometer;
    public Item itemRemoteSensorKit;
    public Item itemEnergySensorKit;
    public Item itemMultipleSensorKit;
    public Item itemSensorLocationCard;
    public Item itemEnergySensorLocationCard;
    public Item itemMultipleSensorLocationCard;
    public Item itemEnergyArrayLocationCard;
    public Item itemTimeCard;
    public Item itemUpgrade;
    public Item itemTextCard;
    public Block blockNuclearControlMain;
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

    public CrossBuildcraft crossBC;
    public CrossIndustrialCraft2 crossIC2;
    public CrossGregTech crossGregTech;
    public CrossRailcraft crossRailcraft;
    
    @SuppressWarnings("unchecked")
    protected void addRecipes(){
        
    	ItemStack thermalMonitor = new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_THERMAL_MONITOR);
        Recipes.advRecipes.addRecipe(thermalMonitor, new Object[]{
                    "GGG", "GCG", "GRG", 
                        'G', IC2Items.getItem("reinforcedGlass"), 
                        'R', Items.redstone, 
                        'C', IC2Items.getItem("advancedCircuit")});
        
        ItemStack howler = new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_HOWLER_ALARM);
        Recipes.advRecipes.addRecipe(howler, new Object[]{
                    "NNN", "ICI", "IRI", 
                        'I', Items.iron_ingot, 
                        'R', Items.redstone, 
                        'N', Blocks.jukebox, 
                        'C', IC2Items.getItem("electronicCircuit")});
        
        ItemStack industrialAlarm = new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_INDUSTRIAL_ALARM);
        Recipes.advRecipes.addRecipe(industrialAlarm, new Object[]{
                    "GOG", "GHG", "GRG", 
                        'G', IC2Items.getItem("reinforcedGlass"), 
                        'O', "dyeOrange", 
                        'R', Items.redstone, 
                        'H', howler});

        Recipes.advRecipes.addRecipe(new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_REMOTE_THERMO), new Object[]{
                    "F", "M", "T", 
                        'T', thermalMonitor, 
                        'M', IC2Items.getItem("machine"), 
                        'F', IC2Items.getItem("frequencyTransmitter")});
        
        Recipes.advRecipes.addRecipe(new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_INFO_PANEL), new Object[]{
                    "PPP", "LCL", "IRI", 
                        'P', Blocks.glass_pane, 
                        'L', "dyeLime", 
                        'I', "dyeBlack", 
                        'R', Items.redstone, 
                        'C', IC2Items.getItem("electronicCircuit")});
        
        Recipes.advRecipes.addRecipe(new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_INFO_PANEL_EXTENDER), new Object[] {
                    "PPP", "WLW", "WWW", 
                        'P', Blocks.glass_pane, 
                        'L', "dyeLime", 
                        'W', Blocks.planks});
        
        Recipes.advRecipes.addRecipe(new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_ADVANCED_PANEL), new Object[] {
                    "PPP", "GLG", "CAC", 
                        'P', Blocks.glass_pane, 
                        'L', "dyeLime", 
                        'G', IC2Items.getItem("goldCableItem"),
                        'A', IC2Items.getItem("advancedCircuit"), 
                        'C', IC2Items.getItem("carbonPlate")});
        
        Recipes.advRecipes.addRecipe(new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_ADVANCED_EXTENDER), new Object[] {
                    "PPP", "GLG", "GCG", 
                        'P', Blocks.glass_pane, 
                        'L', "dyeLime", 
                        'G', IC2Items.getItem("goldCableItem"),
                        'C', IC2Items.getItem("carbonPlate")});
        
        Recipes.advRecipes.addRecipe(new ItemStack(itemToolThermometer, 1), new Object[] {
                    "IG ", "GWG", " GG", 
                        'G', Blocks.glass, 
                        'I', Items.iron_ingot, 
                        'W', IC2Items.getItem("waterCell")});
        
        ItemStack digitalThermometer = new ItemStack(itemToolDigitalThermometer, 1);
        Recipes.advRecipes.addRecipe(digitalThermometer, new Object[] {
                    "I  ", "IC ", " GI", 
                        'G', Items.glowstone_dust, 
                        'I', Items.iron_ingot, 
                        'C', IC2Items.getItem("electronicCircuit")});
        
        Recipes.advRecipes.addRecipe(new ItemStack(itemRemoteSensorKit, 1), new Object[]{
                    "  F", " D ", "P  ", 
                        'P', Items.paper, 
                        'D', digitalThermometer, 
                        'F', IC2Items.getItem("frequencyTransmitter")});
        
        Recipes.advRecipes.addRecipe(new ItemStack(itemEnergySensorKit, 1), new Object[]{
                    "  F", " D ", "P  ", 
                        'P', Items.paper, 
                        'D', IC2Items.getItem("ecMeter"), 
                        'F', IC2Items.getItem("frequencyTransmitter")});
        
        Recipes.advRecipes.addRecipe(new ItemStack(itemUpgrade, 1, ItemUpgrade.DAMAGE_RANGE), new Object[]{
                    "CFC", 
                        'C', IC2Items.getItem("insulatedCopperCableItem"), 
                        'F', IC2Items.getItem("frequencyTransmitter")});
        
        Recipes.advRecipes.addRecipe(new ItemStack(itemUpgrade, 1, ItemUpgrade.DAMAGE_COLOR), new Object[] {
                    "RYG","WCM","IAB", 
                        'R', "dyeRed",  
                        'Y', "dyeYellow",  
                        'G', "dyeGreen",  
                        'W', "dyeWhite",  
                        'C', IC2Items.getItem("insulatedCopperCableItem"), 
                        'M', "dyeMagenta",  
                        'I', "dyeBlack",  
                        'A', "dyeCyan",  
                        'B', "dyeBlue"});
        
        if(isHttpSensorAvailable){
            Recipes.advRecipes.addRecipe(new ItemStack(itemUpgrade, 1, ItemUpgrade.DAMAGE_WEB), new Object[]{
                        "CFC","CAC","CFC", 
                            'C', new ItemStack(itemUpgrade, 1, ItemUpgrade.DAMAGE_RANGE), 
                            'A', IC2Items.getItem("advancedCircuit"),
                            'F', IC2Items.getItem("glassFiberCableItem")});
        }
        
        ItemStack energyCounter = new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_ENERGY_COUNTER);
        Recipes.advRecipes.addRecipe(energyCounter, new Object[]{
                    " A ", "FTF", 
                        'A', IC2Items.getItem("advancedCircuit"), 
                        'F', IC2Items.getItem("glassFiberCableItem"), 
                        'T', IC2Items.getItem("mvTransformer")});
        
        ItemStack averageCounter = new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_AVERAGE_COUNTER);
        Recipes.advRecipes.addRecipe(averageCounter, new Object[]{
                "FTF", " A ",  
                        'A', IC2Items.getItem("advancedCircuit"), 
                        'F', IC2Items.getItem("glassFiberCableItem"), 
                        'T', IC2Items.getItem("mvTransformer")});
        
        ItemStack rangeTrigger = new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_RANGE_TRIGGER);
        Recipes.advRecipes.addRecipe(rangeTrigger, new Object[]{
                "EFE", "AMA",  " R ",
                        'E', IC2Items.getItem("detectorCableItem"), 
                        'F', IC2Items.getItem("frequencyTransmitter"),
                        'A', IC2Items.getItem("advancedCircuit"), 
                        'M', IC2Items.getItem("machine"), 
                        'R', Items.redstone});
        
        Recipes.advRecipes.addRecipe(new ItemStack(itemMultipleSensorKit, 1, ItemKitMultipleSensor.TYPE_COUNTER), new Object[] {
                    "  F", " C ", "P  ", 
                        'P', Items.paper, 
                        'C', IC2Items.getItem("electronicCircuit"), 
                        'F', IC2Items.getItem("frequencyTransmitter")});
        
        Recipes.advRecipes.addRecipe(new ItemStack(itemMultipleSensorKit, 1, ItemKitMultipleSensor.TYPE_LIQUID), new Object[] {
                    "  F", " C ", "P  ", 
                        'P', Items.paper, 
                        'C', Items.bucket, 
                        'F', IC2Items.getItem("frequencyTransmitter")});
        
        Recipes.advRecipes.addRecipe(new ItemStack(itemTextCard, 1), new Object[] {
                    " C ", "PFP", " C ", 
                        'P', Items.paper, 
                        'C', IC2Items.getItem("electronicCircuit"), 
                        'F', IC2Items.getItem("insulatedCopperCableItem")});
        
        Recipes.advRecipes.addShapelessRecipe(new ItemStack(itemTimeCard, 1),  
        		IC2Items.getItem("electronicCircuit"), Items.clock);
        
        Recipes.advRecipes.addShapelessRecipe(new ItemStack(IC2Items.getItem("electronicCircuit").getItem(), 2),  
                itemSensorLocationCard );
        
        Recipes.advRecipes.addShapelessRecipe(new ItemStack(IC2Items.getItem("electronicCircuit").getItem(), 2),  
                itemEnergySensorLocationCard );
        
        Recipes.advRecipes.addShapelessRecipe(new ItemStack(IC2Items.getItem("electronicCircuit").getItem(), 2),  
                new  ItemStack(itemMultipleSensorLocationCard, 1, ItemKitMultipleSensor.TYPE_COUNTER));
        
        Recipes.advRecipes.addShapelessRecipe(new ItemStack(IC2Items.getItem("electronicCircuit").getItem(), 1),  
                new  ItemStack(itemMultipleSensorLocationCard, 1, ItemKitMultipleSensor.TYPE_LIQUID));
        
        CraftingManager.getInstance().getRecipeList().add(new StorageArrayRecipe());
    }
    /*
    protected static int getIdFor(Configuration configuration, String name, int i, boolean block){
        try{
            if (block)
                return configuration.getBlock(name, i).getInt();
            else
                return configuration.getItem(name, i).getInt();
        } 
        catch (Exception exception)
        {
            FMLLog.warning(LOG_PREFIX + "Can't get id for:" + name);
        }

        return i;
    }*/

    protected void initBlocks(Configuration configuration){
        blockNuclearControlMain = new BlockNuclearControlMain().setBlockName("blockThermalMonitor").setCreativeTab(CreativeTabs.tabRedstone);
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
    }
    
    @EventHandler
    public void modsLoaded(FMLPostInitializationEvent evt){
       addRecipes();
    }    

    public void registerBlocks(){
        //GameRegistry.registerBlock(blockNuclearControlMain, ItemNuclearControlMain.class, "blockNuclearControlMain");
    	GameRegistry.registerBlock(blockNuclearControlMain, "Pizza");
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
       configFile = event.getSuggestedConfigurationFile();
       configDir = event.getModConfigurationDirectory();
       MinecraftForge.EVENT_BUS.register(this);
       //MinecraftForge.EVENT_BUS.register(proxy);
       //FMLCommonHandler.instance().bus().register(proxy);
       //    registerScheduledTickHandler(proxy, Side.SERVER);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt){
        crossBC = new CrossBuildcraft();
        crossIC2 = new CrossIndustrialCraft2();
        crossGregTech = new CrossGregTech();
        crossRailcraft = new CrossRailcraft();
    }

    @EventHandler
    public void init(FMLInitializationEvent evt)
    {
        //Modstats.instance().getReporter().registerMod(this);
        IC2NuclearControl.instance.screenManager = new ScreenManager();
        Configuration configuration;
        configuration = new Configuration(configFile);
        configuration.load();
        initBlocks(configuration);
        registerBlocks();
        alarmRange = configuration.get(Configuration.CATEGORY_GENERAL, "alarmRange", 64).getInt();
        maxAlarmRange = configuration.get(Configuration.CATEGORY_GENERAL, "maxAlarmRange", 128).getInt();
        allowedAlarms = configuration.get(Configuration.CATEGORY_GENERAL, "allowedAlarms", "default,sci-fi").getString().replaceAll(" ", "");
        remoteThermalMonitorEnergyConsumption = configuration.get(Configuration.CATEGORY_GENERAL, "remoteThermalMonitorEnergyConsumption", 1).getInt();
        screenRefreshPeriod = configuration.get(Configuration.CATEGORY_GENERAL, "infoPanelRefreshPeriod", 20).getInt();
        rangeTriggerRefreshPeriod = configuration.get(Configuration.CATEGORY_GENERAL, "rangeTriggerRefreshPeriod", 20).getInt();
        SMPMaxAlarmRange = configuration.get(Configuration.CATEGORY_GENERAL, "SMPMaxAlarmRange", 256).getInt();
        //isHttpSensorAvailable = configuration.get(Configuration.CATEGORY_GENERAL, "isHttpSensorAvailable", true).getBoolean(true);
        //httpSensorKey = configuration.get(Configuration.CATEGORY_GENERAL, "httpSensorKey", UUID.randomUUID().toString().replace("-", "")).getString();
        proxy.registerTileEntities();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
        configuration.save();
    }
}
