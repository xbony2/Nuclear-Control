package shedar.mods.ic2.nuclearcontrol.crossmod.vanilla;


import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

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

    }
}
