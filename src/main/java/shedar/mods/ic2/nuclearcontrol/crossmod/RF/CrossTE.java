package shedar.mods.ic2.nuclearcontrol.crossmod.RF;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import shedar.mods.ic2.nuclearcontrol.crossmod.ModLib;
import shedar.mods.ic2.nuclearcontrol.utils.NCLog;


public class CrossTE {

    public static Item RFSensorCard;

    public static void intergrateTE(){
        if(Loader.isModLoaded(ModLib.TE ) || Loader.isModLoaded(ModLib.Eio)){
            registerThermalExpansion();
            NCLog.fatal("Another Energy System is Loaded. Adding Intergration!");
        }
    }

    private static void registerThermalExpansion(){
        RFSensorCard = new ItemCardRFEnergyLocation();

        GameRegistry.registerItem(RFSensorCard, "RFSensorCard");
    }
}
