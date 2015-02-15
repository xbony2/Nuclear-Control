package shedar.mods.ic2.nuclearcontrol.crossmod.BigReactors.recipes;

import ic2.api.item.IC2Items;
import ic2.api.recipe.Recipes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import shedar.mods.ic2.nuclearcontrol.crossmod.BigReactors.CrossBigReactors;

public class BigReactorsRecipesGregtech {
	public static void addRecipes(){
		//TODO BR doesn't have any GT recipes
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
				'R', "dustRedstone", 
				'Y', "ingotYellorium", 
				'C', Items.comparator});
	}
}
