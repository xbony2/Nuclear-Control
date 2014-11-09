package shedar.mods.ic2.nuclearcontrol.crossmod.gregtech;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.utils.BlockDamages;
import ic2.api.item.IC2Items;
import ic2.api.recipe.Recipes;

public class GregtechRecipes {
	
	public static void addRecipes(){
		Recipes.advRecipes.addRecipe(new ItemStack(IC2NuclearControl.itemToolThermometer), new Object[]{
			"BG ", "GMG", " GI",
				'B', "boltIron",
				'G', "plateGlass",
				'M', "cellMercury",
				'I', "stickIron"});
		
		ItemStack digitalThermometer = new ItemStack(IC2NuclearControl.itemToolDigitalThermometer, 1);
		Recipes.advRecipes.addRecipe(digitalThermometer, new Object[]{
			"BG ", "CMC", " G3",
				'B', "boltTungsten",
				'G', "plateGlass",
				'C', "circuitGood",
				'M', "plateAluminium"});
		
		ItemStack thermalMonitor = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_THERMAL_MONITOR);
		Recipes.advRecipes.addRecipe(thermalMonitor, new Object[]{ 
				"LLL", "LRL", "CPC",
					'L', "plateLead",
					'P', "plateSteel",
					'C', "circuitAdvanced",
					'R', "plateRedstone"});
		
		ItemStack howler = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_HOWLER_ALARM);
		Recipes.advRecipes.addRecipe(howler, new Object[]{
				"INI", "CRC", "GMG",
					'I', "plateIron",
					'N', Blocks.noteblock,
					'C', "circuitBasic",
					'R', IC2Items.getItem("elemotor"),
					'G', "cableGt01Gold",
					'M', IC2Items.getItem("machine")
		});
		
		ItemStack industrialAlarm = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_INDUSTRIAL_ALARM);
		Recipes.advRecipes.addRecipe(industrialAlarm, new Object[]{
				"GLG", "RHR", "CMC",
					'G', IC2Items.getItem("reinforcedGlass"),
					'L', IC2NuclearControl.blockNuclearControlLight,
					'R', Items.repeater,
					'H', howler,
					'C', "cableGt01Gold",
					'M', "plateDenseBronze"
		});
		//TODO
	}
}
