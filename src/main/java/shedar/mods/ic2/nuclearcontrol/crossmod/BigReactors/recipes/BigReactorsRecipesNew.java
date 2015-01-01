package shedar.mods.ic2.nuclearcontrol.crossmod.BigReactors.recipes;

import shedar.mods.ic2.nuclearcontrol.crossmod.BigReactors.CrossBigReactors;
import ic2.api.item.IC2Items;
import ic2.api.recipe.Recipes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class BigReactorsRecipesNew {
	public static void addRecipes(){
		Recipes.advRecipes.addRecipe(new ItemStack(CrossBigReactors.kitRFsensor), new Object[]{
			"IT ", "PD ", " R ", 
				'I', "ingotYellorium", 
				'T', IC2Items.getItem("frequencyTransmitter"), 
				'P', Items.paper, 
				'D', "dyeRed", 
				'R', "dustRedstone"});
		Recipes.advRecipes.addRecipe(new ItemStack(CrossBigReactors.ReactorInfoFetch), new Object[]{
			"BRB", "YCY", "BRB", 
				'B', "reactorCasing", 
				'R', Items.redstone, 
				'Y', "ingotYellorium", 
				'C', Items.comparator});
	}
}
