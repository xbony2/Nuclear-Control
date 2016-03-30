package shedar.mods.ic2.nuclearcontrol.crossmod.mekanism;


import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import shedar.mods.ic2.nuclearcontrol.crossmod.ModLib;

public class CrossMekanism {

    public static boolean classExists = false;
    public static Item mekCard;

    public CrossMekanism(){
        try {
            Class.forName("mekanism.api.energy.IStrictEnergyStorage", false, this.getClass().getClassLoader());
            classExists = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean isMekanismPresent(){
        if (Loader.isModLoaded(ModLib.MEKANISM)) {
            return true;
        }
        return false;
    }

    public static void LoadItems(){
        mekCard = new MekRFCard();
        GameRegistry.registerItem(mekCard, "MekRFenergyCard");
    }

}
