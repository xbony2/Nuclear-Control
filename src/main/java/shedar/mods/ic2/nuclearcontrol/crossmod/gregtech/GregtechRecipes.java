package shedar.mods.ic2.nuclearcontrol.crossmod.gregtech;

import net.minecraft.item.ItemStack;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.utils.Damages;
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
		
		ItemStack thermalMonitor = new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, Damages.DAMAGE_THERMAL_MONITOR);
		Recipes.advRecipes.addRecipe(thermalMonitor, new Object[] { 
				"LLL", "LRL", "CPC",
					'L', "plateLead",
					'P', "plateSteel",
					'C', "circuitAdvanced",
					'R', "plateRedstone"});
		//TODO
	}
}
