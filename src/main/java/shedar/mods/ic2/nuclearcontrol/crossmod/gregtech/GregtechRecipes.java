package shedar.mods.ic2.nuclearcontrol.crossmod.gregtech;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.api.item.IC2Items;
import ic2.api.recipe.Recipes;
import ic2.core.util.StackUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.StorageArrayRecipe;
import shedar.mods.ic2.nuclearcontrol.items.ItemKitMultipleSensor;
import shedar.mods.ic2.nuclearcontrol.items.ItemUpgrade;
import shedar.mods.ic2.nuclearcontrol.utils.BlockDamages;
import shedar.mods.ic2.nuclearcontrol.utils.LightDamages;

public class GregtechRecipes {
	private static Item gtmeta1;
	private static ItemStack gtComputerMonitor;
	private static ItemStack gtSensor;
	private static ItemStack gtEmitter;
	
	/**
	 * Grabs all the nessary items/itemstacks from GT indirectly.
	 * 
	 * @author xbony2
	 */
	public static void grabItems(){
		gtmeta1 = GameRegistry.findItem("gregtech", "gt.metaitem.01");
		gtComputerMonitor = new ItemStack(gtmeta1, 1, 32740);
		gtSensor = new ItemStack(gtmeta1, 1, 32690);
		gtEmitter = new ItemStack(gtmeta1, 1, 32680);
	}
	
	public static void addRecipes(){
		GregtechRecipes.grabItems();
		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemToolThermometer), new Object[]{
			"BG ", "GMG", " GI",
				'B', "boltIron",
				'G', "plateGlass",
				'M', "cellMercury",
				'I', "stickIron"});
		
		ItemStack digitalThermometer = new ItemStack(IC2NuclearControl.itemToolDigitalThermometer);
		Recipes.advRecipes.addRecipe(digitalThermometer, new Object[]{
			"BG ", "CMC", " G3",
				'B', "boltTungsten",
				'G', "plateGlass",
				'C', "circuitGood",
				'M', gtComputerMonitor,
				'3', IC2NuclearControl.itemToolThermometer});
		
		ItemStack thermalMonitor = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_THERMAL_MONITOR);
		Recipes.advRecipes.addRecipe(thermalMonitor, new Object[]{ 
				"LLL", "LRL", "CPC",
					'L', "plateLead",
					'P', gtComputerMonitor,
					'C', "circuitAdvanced",
					'R', "plateRedstone"});
		
		ItemStack remoteThermalMonitor = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_REMOTE_THERMO);
		Recipes.advRecipes.addRecipe(remoteThermalMonitor, new Object[]{ 
				"SGE", "CMC", "BTB",
					'S', gtSensor,
					'G', "glassReinforced",
					'E', gtEmitter,
					'C', gtComputerMonitor,
					'M', IC2Items.getItem("machine"),
					'B', "circuitBasic",
					'T', thermalMonitor});
		
		ItemStack howler = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_HOWLER_ALARM);
		Recipes.advRecipes.addRecipe(howler, new Object[]{
				"INI", "CRC", "GMG",
					'I', "plateIron",
					'N', Blocks.noteblock,
					'C', "circuitBasic",
					'R', IC2Items.getItem("elemotor"),
					'G', "cableGt01Gold",
					'M', IC2Items.getItem("machine")});
		
		ItemStack industrialAlarm = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_INDUSTRIAL_ALARM);
		Recipes.advRecipes.addRecipe(industrialAlarm, new Object[]{
				"GLG", "RHR", "CMC",
					'G', IC2Items.getItem("reinforcedGlass"),
					'L', IC2NuclearControl.blockNuclearControlLight,
					'R', Items.repeater,
					'H', howler,
					'C', "cableGt01Gold",
					'M', "plateDenseBronze"});
		
		ItemStack industrialInformationPanel = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_INFO_PANEL);
		Recipes.advRecipes.addRecipe(industrialInformationPanel, new Object[]{
				"APA", "CMC", "IWI",
					'A', gtComputerMonitor,
					'P', "paneGlassLime",
					'C', "circuitBasic",
					'M', IC2Items.getItem("machine"),
					'I', "plateIron",
					'W', "cableGt01RedAlloy"});
		
		ItemStack industrialInfoPanelExtender = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_INFO_PANEL_EXTENDER);
		Recipes.advRecipes.addRecipe(industrialInfoPanelExtender, new Object[]{
			"APA", "WWW", "WRW",
				'A', gtComputerMonitor,
				'P', "paneGlassLime",
				'W', "plankWood",
				'R', "cableGt01RedAlloy"});
		
		ItemStack energyCounter = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_ENERGY_COUNTER);
		Recipes.advRecipes.addRecipe(energyCounter, new Object[]{
			"IAI", "CMC", "IZI",
				'A', gtComputerMonitor,
				'I', "plateIron",
				'C', "cableGt01Platinum",
				'M', IC2Items.getItem("machine"),
				'Z', "circuitAdvanced"});
		
		ItemStack averageCounter = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_AVERAGE_COUNTER);
		Recipes.advRecipes.addRecipe(averageCounter, new Object[]{
			"LAL", "WMW", "LCL",
				'L', "plateLead",
				'A', gtComputerMonitor,
				'W', "cableGt01Platinum",
				'M', IC2Items.getItem("machine"),
				'C', "circuitAdvanced"});
		
		ItemStack rangeTrigger = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_RANGE_TRIGGER);
		Recipes.advRecipes.addRecipe(rangeTrigger, new Object[]{
			"SAS", "CMC", "ZIZ",
				'S', "plateSteel",
				'A', gtComputerMonitor,
				'C', "cableGt01Platinum",
				'M', IC2Items.getItem("machine"),
				'Z', "circuitAdvanced",
				'I', IC2Items.getItem("frequencyTransmitter")});
		
		ItemStack advancedIndustrialInformationPanel = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_ADVANCED_PANEL);
		Recipes.advRecipes.addRecipe(advancedIndustrialInformationPanel, new Object[]{
			"1P2", "ACA", "WHS",
				'1', new ItemStack(IC2NuclearControl.itemUpgrade, 1, 0),
				'2', new ItemStack(IC2NuclearControl.itemUpgrade, 1, 1),
				'P', industrialInformationPanel,
				'A', "plateAlloyCarbon",
				'C', "circuitAdvanced",
				'W', "craftingToolWrench",
				'H', "craftingToolHardHammer",
				'S', "craftingToolScrewdriver"});
		
		ItemStack advancedIndustrialInfoPanelExtender = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_ADVANCED_EXTENDER);
		Recipes.advRecipes.addRecipe(advancedIndustrialInfoPanelExtender, new Object[]{
			"1P2", "ASA", "WHD",
				'1', new ItemStack(IC2NuclearControl.itemUpgrade, 1, 0),
				'2', new ItemStack(IC2NuclearControl.itemUpgrade, 1, 1),
				'P', industrialInfoPanelExtender,
				'A', "plateAlloyCarbon",
				'S', "plateSteel",
				'W', "craftingToolWrench",
				'H', "craftingToolHardHammer",
				'D', "craftingToolScrewdriver"});
		
		ItemStack lampWhite = new ItemStack(IC2NuclearControl.blockNuclearControlLight, 1, LightDamages.DAMAGE_WHITE_OFF);
		Recipes.advRecipes.addRecipe(lampWhite, new Object[]{
			"GGG", "GRG", "GWG",
				'G', "paneGlassWhite",
				'R', Blocks.redstone_lamp,
				'W', "wireGt01RedAlloy"});
		
		ItemStack lampOrange = new ItemStack(IC2NuclearControl.blockNuclearControlLight, 1, LightDamages.DAMAGE_ORANGE_OFF);
		Recipes.advRecipes.addRecipe(lampOrange, new Object[]{
				"GGG", "GRG", "GWG",
					'G', "paneGlassOrange",
					'R', Blocks.redstone_lamp,
					'W', "wireGt01RedAlloy"});
		
		ItemStack thermometer = new ItemStack(IC2NuclearControl.itemToolThermometer);
		Recipes.advRecipes.addRecipe(thermometer, new Object[]{
			"IG ", "GMG", " GG",
				'I', "stickIron",
				'M', "cellMercury",
				'G', "plateGlass"});
		
		ItemStack digitalThermo = new ItemStack(IC2NuclearControl.itemToolDigitalThermometer, 1);
		Recipes.advRecipes.addRecipe(digitalThermo, new Object[]{
			"TG ", "CAC", " GP",
				'T', thermometer,
				'G', "plateGlass",
				'C', "circuitBasic",
				'A', gtComputerMonitor,
				'P', IC2Items.getItem("powerunitsmall")});
		
		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemRemoteSensorKit, 1), new Object[] { 
			"DF", "PW", 
				'P', Items.paper, 
				'D', StackUtil.copyWithWildCard(digitalThermometer), 
				'F', IC2Items.getItem("frequencyTransmitter"), 
				'W', "dyeYellow"});

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemEnergySensorKit, 1), new Object[] { 
			"RF", "PO", 
				'P', Items.paper, 
				'R', "dustRedstone", 
				'F', IC2Items.getItem("frequencyTransmitter"), 
				'O', "dyeOrange"});

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemUpgrade, 1, ItemUpgrade.DAMAGE_RANGE), new Object[] { 
			"CCC", "IFI", 
				'I', "cableGt01Tin", 
				'F', IC2Items.getItem("frequencyTransmitter"), 
				'C', IC2Items.getItem("reactorCoolantSimple")});

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemUpgrade, 1, ItemUpgrade.DAMAGE_COLOR), new Object[] { 
			"RYG", "WCM", "IAB", 
				'R', "dyeRed", 
				'Y', "dyeYellow", 
				'G', "dyeGreen", 
				'W', "dyeWhite", 
				'C', "circuitAdvanced", 
				'M', "dyeMagenta", 
				'I', "dyeBlack",
				'A', "dyeCyan", 
				'B', "dyeBlue" });

		/*if ((IC2NuclearControl.isHttpSensorAvailableClient && !IC2NuclearControl.isServer) 
				|| (IC2NuclearControl.isHttpSensorAvailableServer && IC2NuclearControl.isServer)) {
			Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemUpgrade, 1, ItemUpgrade.DAMAGE_WEB), new Object[]{
				"CGC", "CAC", "CGC",
					'C', new ItemStack(IC2NuclearControl.itemUpgrade, 1, ItemUpgrade.DAMAGE_RANGE), 
					'A', "circuitAdvanced", 
					'G', "blockGlass"});
		}*/
		
		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemMultipleSensorKit, 1, ItemKitMultipleSensor.TYPE_COUNTER), new Object[] { 
			"CF", "PR", 
				'P', Items.paper, 
				'C', "circuitBasic", 
				'F', IC2Items.getItem("frequencyTransmitter"), 
				'R', "dyeOrange" });

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemMultipleSensorKit, 1, ItemKitMultipleSensor.TYPE_LIQUID), new Object[] { 
			"CF", "PB", 
				'P', Items.paper, 
				'C', Items.bucket,
				'F', IC2Items.getItem("frequencyTransmitter"), 
				'B', "dyeBlue" });

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemTextCard), new Object[] { 
			" C ", "PFP", " C ", 
				'P', Items.paper, 
				'C', "circuitBasic", 
				'F', "cableGt01Tin"});

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemTimeCard), new Object[]{ 
			" C ", "PWP", " C ", 
				'C', Items.clock, 
				'P', Items.paper, 
				'W', "cableGt01Tin"});

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemMultipleSensorKit, 1, ItemKitMultipleSensor.TYPE_GENERATOR), new Object[] { 
			"CF", "PL", 
				'P', Items.paper, 
				'C', IC2Items.getItem("energyStorageUpgrade"), 
				'F', IC2Items.getItem("frequencyTransmitter"), 
				'L', "dyeLightBlue" });
		
		CraftingManager.getInstance().getRecipeList().add(new StorageArrayRecipe());
	}
}
