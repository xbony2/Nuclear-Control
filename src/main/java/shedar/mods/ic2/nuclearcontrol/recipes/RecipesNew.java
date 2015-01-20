package shedar.mods.ic2.nuclearcontrol.recipes;

import ic2.api.item.IC2Items;
import ic2.api.recipe.Recipes;
import ic2.core.util.StackUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.StorageArrayRecipe;
import shedar.mods.ic2.nuclearcontrol.items.ItemKitMultipleSensor;
import shedar.mods.ic2.nuclearcontrol.items.ItemUpgrade;
import shedar.mods.ic2.nuclearcontrol.utils.BlockDamages;
import shedar.mods.ic2.nuclearcontrol.utils.LightDamages;

public class RecipesNew {

	public static void addRecipes() {
		ItemStack thermalMonitor = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_THERMAL_MONITOR);
		Recipes.advRecipes.addRecipe(thermalMonitor, new Object[]{ 
				"LLL", "LCL", "LRL", 
					'L', "plateLead", 
					'R', "dustRedstone", 
					'C', "circuitAdvanced" });

		ItemStack howler = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_HOWLER_ALARM);
		Recipes.advRecipes.addRecipe(howler, new Object[]{ 
				"NNN", "ICI", "IRI", 
					'I', "plateIron", 
					'R', "dustRedstone", 
					'N', Blocks.noteblock, 
					'C', "circuitBasic"});

		ItemStack industrialAlarm = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_INDUSTRIAL_ALARM);
		Recipes.advRecipes.addRecipe(industrialAlarm, new Object[]{ 
				"BBB", "BHB", "BRB", 
					'B', "plateBronze", 
					'R', "dustRedstone", 
					'H', howler });

		ItemStack lampWhite = new ItemStack(IC2NuclearControl.blockNuclearControlLight, 1, LightDamages.DAMAGE_WHITE_OFF);
		Recipes.advRecipes.addRecipe(lampWhite, new Object[] { 
				"GGG", "GWG", "GLG", 
					'G', "paneGlass", 
					'W', "dyeWhite", 
					'L', Blocks.redstone_lamp});
		
		ItemStack lampOrange = new ItemStack(IC2NuclearControl.blockNuclearControlLight, 1, LightDamages.DAMAGE_ORANGE_OFF);
		Recipes.advRecipes.addRecipe(lampOrange, new Object[] { 
				"GGG", "GWG", "GLG", 
					'G', "paneGlass", 
					'W', "dyeOrange", 
					'L', Blocks.redstone_lamp});

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_REMOTE_THERMO), new Object[]{ 
			"IFI", "IMI", "ITI", 
				'T', thermalMonitor, 
				'M', IC2Items.getItem("machine"), 
				'F', IC2Items.getItem("frequencyTransmitter"), 
				'I', "plateIron" });

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_INFO_PANEL), new Object[]{ 
			"PPP", "CMC", "IRI",
				'P', "paneGlassLime", 'C', "circuitBasic", 'I', "dyeBlack",
				'R', "dustRedstone", 'M', IC2Items.getItem("machine") });

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_INFO_PANEL_EXTENDER), new Object[] { 
			"PPP", "WRW", "WWW", 
				'P', "paneGlassLime", 
				'R', "dustRedstone", 
				'W', "plankWood" });

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_ADVANCED_PANEL), new Object[] { 
			"PPP", "GLG", "CAC", 
				'P', "paneGlassLime", 
				'L', "dyeLime", 
				'G', IC2Items.getItem("goldCableItem"), 
				'A', "circuitAdvanced", 
				'C', IC2Items.getItem("carbonPlate")});

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_ADVANCED_EXTENDER), new Object[] { 
			"PPP", "GMG", "GCG", 
				'P', "paneGlassLime", 
				'M', IC2Items.getItem("machine"), 
				'G', IC2Items.getItem("goldCableItem"), 
				'C', IC2Items.getItem("carbonPlate")});

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemToolThermometer), new Object[]{
			"IG ", "GWG", " GG", 
				'G', "blockGlass", 
				'I', "plateIron", 
				'W', IC2Items.getItem("waterCell")});

		ItemStack digitalThermometer = new ItemStack(IC2NuclearControl.itemToolDigitalThermometer);
		Recipes.advRecipes.addRecipe(digitalThermometer, new Object[] { 
			"T ", " P", 
				'T', IC2NuclearControl.itemToolThermometer, 
				'P', IC2Items.getItem("powerunitsmall")});

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemRemoteSensorKit), new Object[] { 
			"DF", "PW", 
				'P', Items.paper, 
				'D', StackUtil.copyWithWildCard(digitalThermometer), 
				'F', IC2Items.getItem("frequencyTransmitter"), 
				'W', "dyeYellow" });

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemEnergySensorKit), new Object[] { 
			"RF", "PO", 
				'P', Items.paper, 
				'R', "dustRedstone", 
				'F', IC2Items.getItem("frequencyTransmitter"), 
				'O', "dyeOrange"});

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemUpgrade, 1, ItemUpgrade.DAMAGE_RANGE), new Object[] { 
			"CCC", "IFI", 
				'I', IC2Items.getItem("insulatedCopperCableItem"), 
				'F', IC2Items.getItem("frequencyTransmitter"), 
				'C', IC2Items.getItem("reactorCoolantSimple")});

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemUpgrade, 1, ItemUpgrade.DAMAGE_COLOR), new Object[] { 
			"RYG", "WCM", "IAB", 
				'R', "dyeRed", 
				'Y', "dyeYellow", 
				'G', "dyeGreen", 
				'W', "dyeWhite", 
				'C', "circuitBasic", 
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

		ItemStack energyCounter = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_ENERGY_COUNTER);
		Recipes.advRecipes.addRecipe(energyCounter, new Object[] { 
			"IAI", "FTF", 
				'A', "circuitAdvanced", 
				'F', IC2Items.getItem("glassFiberCableItem"), 
				'T', IC2Items.getItem("mvTransformer"), 
				'I', "plateIron"});

		ItemStack averageCounter = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_AVERAGE_COUNTER);
		Recipes.advRecipes.addRecipe( averageCounter, new Object[] { 
			"LAL", "FTF", 
				'A', "circuitAdvanced", 
				'F', IC2Items.getItem("glassFiberCableItem"), 
				'T', IC2Items.getItem("mvTransformer"), 
				'L', "plateLead"});

		ItemStack rangeTrigger = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_RANGE_TRIGGER);
		Recipes.advRecipes.addRecipe(rangeTrigger, new Object[] { 
			"EFE", "AMA", " R ", 
				'E', IC2Items.getItem("detectorCableItem"), 
				'F', IC2Items.getItem("frequencyTransmitter"), 
				'A', "circuitAdvanced", 
				'M', IC2Items.getItem("machine"), 
				'R', "dustRedstone" });

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
				'F', IC2Items.getItem("insulatedTinCableItem")});

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemTimeCard), new Object[]{ 
			" C ", "PWP", " C ", 
				'C', Items.clock, 
				'P', Items.paper, 
				'W', IC2Items.getItem("insulatedTinCableItem")});

		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemMultipleSensorKit, 1, ItemKitMultipleSensor.TYPE_GENERATOR), new Object[] { 
			"CF", "PL", 
				'P', Items.paper, 
				'C', IC2Items.getItem("energyStorageUpgrade"), 
				'F', IC2Items.getItem("frequencyTransmitter"), 
				'L', "dyeLightBlue" });
		
		Recipes.advRecipes.addShapelessRecipe(IC2Items.getItem("electronicCircuit"), IC2NuclearControl.itemSensorLocationCard);
		Recipes.advRecipes.addShapelessRecipe(IC2Items.getItem("electronicCircuit"), IC2NuclearControl.itemEnergySensorLocationCard);	 
		Recipes.advRecipes.addShapelessRecipe(IC2Items.getItem("electronicCircuit"), new ItemStack(IC2NuclearControl.itemMultipleSensorLocationCard, 1, ItemKitMultipleSensor.TYPE_COUNTER));
		Recipes.advRecipes.addShapelessRecipe(IC2Items.getItem("electronicCircuit"), new ItemStack(IC2NuclearControl.itemMultipleSensorLocationCard, 1, ItemKitMultipleSensor.TYPE_LIQUID));
		
		CraftingManager.getInstance().getRecipeList().add(new StorageArrayRecipe());
	}
}
