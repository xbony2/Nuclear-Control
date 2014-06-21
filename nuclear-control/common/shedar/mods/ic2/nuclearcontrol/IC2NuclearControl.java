package shedar.mods.ic2.nuclearcontrol;


import ic2.api.item.Items;
import ic2.api.recipe.Recipes;

import java.io.File;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

import org.modstats.ModstatInfo;
import org.modstats.Modstats;

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
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod( modid = "IC2NuclearControl", name="Nuclear Control", version="1.6.2d", dependencies = "after:IC2")
@NetworkMod(channels = { "NuclearControl" }, clientSideRequired = true, serverSideRequired = false, 
            packetHandler = PacketHandler.class, connectionHandler = ConnectionHandler.class)
@ModstatInfo(prefix="nc")
public class IC2NuclearControl
{
    
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
    public BlockNuclearControlMain blockNuclearControlMain;
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
    protected void addRecipes()
    {
        ItemStack thermalMonitor = new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_THERMAL_MONITOR);
        Recipes.advRecipes.addRecipe(thermalMonitor, new Object[]
                {
                    "GGG", "GCG", "GRG", 
                        Character.valueOf('G'), Items.getItem("reinforcedGlass"), 
                        Character.valueOf('R'), Item.redstone, 
                        Character.valueOf('C'), Items.getItem("advancedCircuit")
                });
        ItemStack howler = new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_HOWLER_ALARM);
        Recipes.advRecipes.addRecipe(howler, new Object[]
                {
                    "NNN", "ICI", "IRI", 
                        Character.valueOf('I'), Item.ingotIron, 
                        Character.valueOf('R'), Item.redstone, 
                        Character.valueOf('N'), Block.music, 
                        Character.valueOf('C'), Items.getItem("electronicCircuit")
                });
        ItemStack industrialAlarm = new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_INDUSTRIAL_ALARM);
        Recipes.advRecipes.addRecipe(industrialAlarm, new Object[]
                {
                    "GOG", "GHG", "GRG", 
                        Character.valueOf('G'), Items.getItem("reinforcedGlass"), 
                        Character.valueOf('O'), "dyeOrange", 
                        Character.valueOf('R'), Item.redstone, 
                        Character.valueOf('H'), howler 
                });

        Recipes.advRecipes.addRecipe(new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_REMOTE_THERMO), new Object[] 
                {
                    "F", "M", "T", 
                        Character.valueOf('T'), thermalMonitor, 
                        Character.valueOf('M'), Items.getItem("machine"), 
                        Character.valueOf('F'), Items.getItem("frequencyTransmitter")
                });
        Recipes.advRecipes.addRecipe(new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_INFO_PANEL), new Object[] 
                {
                    "PPP", "LCL", "IRI", 
                        Character.valueOf('P'), Block.thinGlass, 
                        Character.valueOf('L'), "dyeLime", 
                        Character.valueOf('I'), "dyeBlack", 
                        Character.valueOf('R'), Item.redstone, 
                        Character.valueOf('C'), Items.getItem("electronicCircuit") 
                });
        Recipes.advRecipes.addRecipe(new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_INFO_PANEL_EXTENDER), new Object[] 
                {
                    "PPP", "WLW", "WWW", 
                        Character.valueOf('P'), Block.thinGlass, 
                        Character.valueOf('L'), "dyeLime", 
                        Character.valueOf('W'), Block.planks, 
                });
        Recipes.advRecipes.addRecipe(new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_ADVANCED_PANEL), new Object[] 
                {
                    "PPP", "GLG", "CAC", 
                        Character.valueOf('P'), Block.thinGlass, 
                        Character.valueOf('L'), "dyeLime", 
                        Character.valueOf('G'), Items.getItem("goldCableItem"),
                        Character.valueOf('A'), Items.getItem("advancedCircuit"), 
                        Character.valueOf('C'), Items.getItem("carbonPlate") 
                });
        Recipes.advRecipes.addRecipe(new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_ADVANCED_EXTENDER), new Object[] 
                {
                    "PPP", "GLG", "GCG", 
                        Character.valueOf('P'), Block.thinGlass, 
                        Character.valueOf('L'), "dyeLime", 
                        Character.valueOf('G'), Items.getItem("goldCableItem"),
                        Character.valueOf('C'), Items.getItem("carbonPlate") 
                });
        Recipes.advRecipes.addRecipe(new ItemStack(itemToolThermometer, 1), new Object[] 
                {
                    "IG ", "GWG", " GG", 
                        Character.valueOf('G'), Block.glass, 
                        Character.valueOf('I'), Item.ingotIron, 
                        Character.valueOf('W'), Items.getItem("waterCell")
                });
        ItemStack digitalThermometer = new ItemStack(itemToolDigitalThermometer, 1);
        Recipes.advRecipes.addRecipe(digitalThermometer, new Object[] 
                {
                    "I  ", "IC ", " GI", 
                        Character.valueOf('G'), Item.glowstone, 
                        Character.valueOf('I'), Item.ingotIron, 
                        Character.valueOf('C'), Items.getItem("electronicCircuit")
                });
        Recipes.advRecipes.addRecipe(new ItemStack(itemRemoteSensorKit, 1), new Object[] 
                {
                    "  F", " D ", "P  ", 
                        Character.valueOf('P'), Item.paper, 
                        Character.valueOf('D'), digitalThermometer, 
                        Character.valueOf('F'), Items.getItem("frequencyTransmitter")
                });
        Recipes.advRecipes.addRecipe(new ItemStack(itemEnergySensorKit, 1), new Object[] 
                {
                    "  F", " D ", "P  ", 
                        Character.valueOf('P'), Item.paper, 
                        Character.valueOf('D'), Items.getItem("ecMeter"), 
                        Character.valueOf('F'), Items.getItem("frequencyTransmitter")
                });
        Recipes.advRecipes.addRecipe(new ItemStack(itemUpgrade, 1, ItemUpgrade.DAMAGE_RANGE), new Object[] 
                {
                    "CFC", 
                        Character.valueOf('C'), Items.getItem("insulatedCopperCableItem"), 
                        Character.valueOf('F'), Items.getItem("frequencyTransmitter")
                });
        Recipes.advRecipes.addRecipe(new ItemStack(itemUpgrade, 1, ItemUpgrade.DAMAGE_COLOR), new Object[] 
                {
                    "RYG","WCM","IAB", 
                        Character.valueOf('R'), "dyeRed",  
                        Character.valueOf('Y'), "dyeYellow",  
                        Character.valueOf('G'), "dyeGreen",  
                        Character.valueOf('W'), "dyeWhite",  
                        Character.valueOf('C'), Items.getItem("insulatedCopperCableItem"), 
                        Character.valueOf('M'), "dyeMagenta",  
                        Character.valueOf('I'), "dyeBlack",  
                        Character.valueOf('A'), "dyeCyan",  
                        Character.valueOf('B'), "dyeBlue"  
                });
        if(isHttpSensorAvailable)
        {
            Recipes.advRecipes.addRecipe(new ItemStack(itemUpgrade, 1, ItemUpgrade.DAMAGE_WEB), new Object[] 
                    {
                        "CFC","CAC","CFC", 
                            Character.valueOf('C'), new ItemStack(itemUpgrade, 1, ItemUpgrade.DAMAGE_RANGE), 
                            Character.valueOf('A'), Items.getItem("advancedCircuit"),
                            Character.valueOf('F'), Items.getItem("glassFiberCableItem")
                    });
        }
        
        ItemStack energyCounter = new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_ENERGY_COUNTER);
        Recipes.advRecipes.addRecipe(energyCounter, new Object[]
                {
                    " A ", "FTF", 
                        Character.valueOf('A'), Items.getItem("advancedCircuit"), 
                        Character.valueOf('F'), Items.getItem("glassFiberCableItem"), 
                        Character.valueOf('T'), Items.getItem("mvTransformer")
                });
        ItemStack averageCounter = new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_AVERAGE_COUNTER);
        Recipes.advRecipes.addRecipe(averageCounter, new Object[]
                {
                "FTF", " A ",  
                        Character.valueOf('A'), Items.getItem("advancedCircuit"), 
                        Character.valueOf('F'), Items.getItem("glassFiberCableItem"), 
                        Character.valueOf('T'), Items.getItem("mvTransformer")
                });
        ItemStack rangeTrigger = new ItemStack(blockNuclearControlMain, 1, Damages.DAMAGE_RANGE_TRIGGER);
        Recipes.advRecipes.addRecipe(rangeTrigger, new Object[]
                {
                "EFE", "AMA",  " R ",
                        
                        Character.valueOf('E'), Items.getItem("detectorCableItem"), 
                        Character.valueOf('F'), Items.getItem("frequencyTransmitter"),
                        Character.valueOf('A'), Items.getItem("advancedCircuit"), 
                        Character.valueOf('M'), Items.getItem("machine"), 
                        Character.valueOf('R'), Item.redstone 
                });
        Recipes.advRecipes.addRecipe(new ItemStack(itemMultipleSensorKit, 1, ItemKitMultipleSensor.TYPE_COUNTER), new Object[] 
                {
                    "  F", " C ", "P  ", 
                        Character.valueOf('P'), Item.paper, 
                        Character.valueOf('C'), Items.getItem("electronicCircuit"), 
                        Character.valueOf('F'), Items.getItem("frequencyTransmitter")
                });
        Recipes.advRecipes.addRecipe(new ItemStack(itemMultipleSensorKit, 1, ItemKitMultipleSensor.TYPE_LIQUID), new Object[] 
                {
                    "  F", " C ", "P  ", 
                        Character.valueOf('P'), Item.paper, 
                        Character.valueOf('C'), Item.bucketEmpty, 
                        Character.valueOf('F'), Items.getItem("frequencyTransmitter")
                });
        Recipes.advRecipes.addRecipe(new ItemStack(itemTextCard, 1), new Object[] 
                {
                    " C ", "PFP", " C ", 
                        Character.valueOf('P'), Item.paper, 
                        Character.valueOf('C'), Items.getItem("electronicCircuit"), 
                        Character.valueOf('F'), Items.getItem("insulatedCopperCableItem")
                });
        Recipes.advRecipes.addShapelessRecipe(new ItemStack(itemTimeCard, 1),  
                Items.getItem("electronicCircuit"), Item.pocketSundial);
        Recipes.advRecipes.addShapelessRecipe(new ItemStack(Items.getItem("electronicCircuit").getItem(), 2),  
                itemSensorLocationCard );
        Recipes.advRecipes.addShapelessRecipe(new ItemStack(Items.getItem("electronicCircuit").getItem(), 2),  
                itemEnergySensorLocationCard );
        Recipes.advRecipes.addShapelessRecipe(new ItemStack(Items.getItem("electronicCircuit").getItem(), 2),  
                new  ItemStack(itemMultipleSensorLocationCard, 1, ItemKitMultipleSensor.TYPE_COUNTER));
        Recipes.advRecipes.addShapelessRecipe(new ItemStack(Items.getItem("electronicCircuit").getItem(), 1),  
                new  ItemStack(itemMultipleSensorLocationCard, 1, ItemKitMultipleSensor.TYPE_LIQUID));
        CraftingManager.getInstance().getRecipeList().add(new StorageArrayRecipe());
    }
    
    protected static int getIdFor(Configuration configuration, String name, int i, boolean block)
    {
        try
        {
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
    }

    protected void initBlocks(Configuration configuration)
    {
        blockNuclearControlMain = new BlockNuclearControlMain(getIdFor(configuration, "blockNuclearControlMain", 192, true));
        blockNuclearControlMain.setUnlocalizedName("blockThermalMonitor");
        itemToolThermometer = new ItemToolThermometer(
                getIdFor(configuration, "itemToolThermometer", 31000, false))
                .setUnlocalizedName("ItemToolThermometer");
        itemToolDigitalThermometer = new ItemToolDigitalThermometer(
                getIdFor(configuration, "itemToolDigitalThermometer", 31001, false), 1, 80, 80)
                .setUnlocalizedName("ItemToolDigitalThermometer");
        itemSensorLocationCard = new ItemCardReactorSensorLocation(
                getIdFor(configuration, "itemSensorLocationCard", 31003, false))
                .setUnlocalizedName("ItemSensorLocationCard");
        itemUpgrade = new ItemUpgrade(
                getIdFor(configuration, "itemRangeUpgrade", 31004, false));
        itemTimeCard = new ItemTimeCard(
                getIdFor(configuration, "itemTimeCard", 31005, false))
                .setUnlocalizedName("ItemTimeCard");
        itemTextCard = new ItemCardText(
                getIdFor(configuration, "itemTextCard", 31011, false))
                .setUnlocalizedName("ItemTextCard");
        itemEnergySensorLocationCard = new ItemCardEnergySensorLocation(
                getIdFor(configuration, "itemEnergySensorLocationCard", 31007, false))
                .setUnlocalizedName("ItemEnergySensorLocationCard");
        itemEnergyArrayLocationCard = new ItemCardEnergyArrayLocation(
                getIdFor(configuration, "itemEnergyArrayLocationCard", 31008, false))
                .setUnlocalizedName("ItemEnergyArrayLocationCard");
        itemMultipleSensorLocationCard = new ItemCardMultipleSensorLocation(
                getIdFor(configuration, "itemCounterSensorLocationCard", 31010, false));
        itemMultipleSensorKit = new ItemKitMultipleSensor(
                getIdFor(configuration, "itemCounterSensorKit", 31009, false))
                .setUnlocalizedName("ItemCounterSensorKit");
        itemEnergySensorKit = new ItemKitEnergySensor(
                getIdFor(configuration, "itemEnergySensorKit", 31006, false))
                .setUnlocalizedName("ItemEnergySensorKit");
        itemRemoteSensorKit = new ItemKitReactorSensor(
                getIdFor(configuration, "itemRemoteSensorKit", 31002, false))
                .setUnlocalizedName("ItemRemoteSensorKit");
    }
    
    @EventHandler
    public void modsLoaded(FMLPostInitializationEvent evt)
    {
        addRecipes();
    }    

    public void registerBlocks()
    {
        GameRegistry.registerBlock(blockNuclearControlMain, ItemNuclearControlMain.class, "blockNuclearControlMain");
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) 
    {
        configFile = event.getSuggestedConfigurationFile();
        configDir = event.getModConfigurationDirectory();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(proxy);
        TickRegistry.registerScheduledTickHandler(proxy, Side.SERVER);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt)
    {
        crossBC = new CrossBuildcraft();
        crossIC2 = new CrossIndustrialCraft2();
        crossGregTech = new CrossGregTech();
        crossRailcraft = new CrossRailcraft();
    }

    @EventHandler
    public void init(FMLInitializationEvent evt)
    {
        Modstats.instance().getReporter().registerMod(this);
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
        isHttpSensorAvailable = configuration.get(Configuration.CATEGORY_GENERAL, "isHttpSensorAvailable", true).getBoolean(true);
        httpSensorKey = configuration.get(Configuration.CATEGORY_GENERAL, "httpSensorKey", UUID.randomUUID().toString().replace("-", "")).getString();
        proxy.registerTileEntities();
        NetworkRegistry.instance().registerGuiHandler(instance, proxy);
        configuration.save();
    }
}
