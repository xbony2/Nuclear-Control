package shedar.mods.ic2.nuclearcontrol.crossmod.vanilla;


import cpw.mods.fml.common.registry.GameRegistry;
import ic2.api.item.IC2Items;
import ic2.api.recipe.Recipes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Vanilla {

    public static Item inventoryCard;
    public static Item vanillaKit;
    public static Item machineCard;


    public static void initVanilla(){

        inventoryCard = new ItemCardInventoryScanner().setUnlocalizedName("ItemInventoryScannerCard");
        vanillaKit = new ItemVanillaKit().setUnlocalizedName("ItemVanilliaKit");
        machineCard = new ItemVanillaMachineCard().setUnlocalizedName("ItemVanillaMachineCard");

        GameRegistry.registerItem(inventoryCard, "ItemInventoryScannerCard");
        GameRegistry.registerItem(vanillaKit, "ItemVanilliaKit");
        GameRegistry.registerItem(machineCard, "ItemVanillaMachineCard");

        Recipes.advRecipes.addRecipe(new ItemStack(vanillaKit), new Object[] {
                "PL ",
                "FC ",
                'P', Items.paper,
                'C', "circuitBasic",
                'F', IC2Items.getItem("frequencyTransmitter"),
                'L', Blocks.lever
        });

    }
}
