package shedar.mods.ic2.nuclearcontrol.crossmod.appeng;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import shedar.mods.ic2.nuclearcontrol.utils.NCLog;

/**
 * Created by Dmf444 on 4/4/2015.
 */
public class CrossAppeng {

    public static Block networklink;

    public static void isRegistrationInOrder(){
        if(Loader.isModLoaded("appliedenergistics2")){
            NCLog.fatal("Large Storage System? We can help to monitor that!");
            addBlocksItemsTiles();
        }
    }

    private static void addBlocksItemsTiles(){
        networklink = new BlockNetworkLink();
        GameRegistry.registerBlock(networklink, "networkLink");
        GameRegistry.registerTileEntity(TileEntityNetworkLink.class, "networkLink");
    }
}
