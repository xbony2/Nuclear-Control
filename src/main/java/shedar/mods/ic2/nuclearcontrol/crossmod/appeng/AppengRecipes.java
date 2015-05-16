package shedar.mods.ic2.nuclearcontrol.crossmod.appeng;


import appeng.api.AEApi;
import appeng.api.util.AEColor;
import ic2.api.item.IC2Items;
import ic2.api.recipe.Recipes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AppengRecipes {

    public static void addRecipesToRegistry(){

        //AE Kit
        Recipes.advRecipes.addRecipe(new ItemStack(CrossAppeng.kitAppeng), new Object[]{
                "IT", "PD",
                'I', AEApi.instance().definitions().materials().fluixCrystal().maybeStack(1).get(),
                'T', IC2Items.getItem("frequencyTransmitter"),
                'P', Items.paper,
                'D', "dyePurple"});
        //AE Monitor
        Recipes.advRecipes.addRecipe(new ItemStack(CrossAppeng.networklink), new Object[]{
                "BRB", "YCY", "BRB",
                'B', "ingotIron",
                'R', AEApi.instance().definitions().materials().calcProcessor().maybeStack(1).get(),
                'Y', AEApi.instance().definitions().parts().cableGlass().item(AEColor.Purple),
                'C', Items.comparator});
    }

    public static void addGregtechRecipes(){

        //AE Kit
        Recipes.advRecipes.addRecipe(new ItemStack(CrossAppeng.kitAppeng), new Object[]{
                "IT", "PD",
                'I', AEApi.instance().definitions().materials().fluixCrystal().maybeStack(1).get(),
                'T', IC2Items.getItem("frequencyTransmitter"),
                'P', Items.paper,
                'D', "dyePurple"});
        //AE Monitor
        Recipes.advRecipes.addRecipe(new ItemStack(CrossAppeng.networklink), new Object[]{
                "BRB", "YCY", "BRB",
                'B', "plateIron",
                'R', AEApi.instance().definitions().materials().calcProcessor().maybeStack(1).get(),
                'Y', AEApi.instance().definitions().parts().cableGlass().item(AEColor.Transparent),
                'C', Items.comparator});
    }
}
