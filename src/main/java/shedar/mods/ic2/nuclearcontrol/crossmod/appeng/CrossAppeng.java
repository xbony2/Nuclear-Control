package shedar.mods.ic2.nuclearcontrol.crossmod.appeng;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.utils.NCLog;


public class CrossAppeng {

    public static Block networklink;
    public static Item kitAppeng;
    public static Item cardAppeng;

    public static void isRegistrationInOrder(){
        if(Loader.isModLoaded("appliedenergistics2")){
            NCLog.fatal("Large Storage System? We can help to monitor that!");
            addBlocksItemsTiles();
        }
    }

    @Optional.Method(modid = "appliedenergistics2")
    private static void addBlocksItemsTiles(){
        networklink = new BlockNetworkLink();
        kitAppeng = new ItemKitAppeng();
        cardAppeng = new ItemCardAppeng();
        GameRegistry.registerBlock(networklink, "networkLink");
        GameRegistry.registerItem(kitAppeng, "KitAppeng");
        GameRegistry.registerItem(cardAppeng, "CardAppeng");
        GameRegistry.registerTileEntity(TileEntityNetworkLink.class, "networkLink");

        if(IC2NuclearControl.instance.recipes.toLowerCase().equals("normal")){
            AppengRecipes.addRecipesToRegistry();
        }

        if(IC2NuclearControl.instance.recipes.toLowerCase().equals("gregtech")){
            AppengRecipes.addGregtechRecipes();
        }
    }
}
