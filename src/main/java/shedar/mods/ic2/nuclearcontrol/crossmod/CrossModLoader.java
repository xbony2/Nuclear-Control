package shedar.mods.ic2.nuclearcontrol.crossmod;


import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import shedar.mods.ic2.nuclearcontrol.crossmod.RF.CrossTE;
import shedar.mods.ic2.nuclearcontrol.crossmod.appeng.CrossAppeng;
import shedar.mods.ic2.nuclearcontrol.crossmod.bigreactors.CrossBigReactors;
import shedar.mods.ic2.nuclearcontrol.crossmod.mekanism.CrossMekanism;
import shedar.mods.ic2.nuclearcontrol.crossmod.vanilla.Vanilla;

public class CrossModLoader {

    public static CrossMekanism crossMekanism;

    public static void preinit(){

    }

    public static void init(){
        //Registers waila stuff
        FMLInterModComms.sendMessage("Waila", "register", "shedar.mods.ic2.nuclearcontrol.crossmod.waila.CrossWaila.callbackRegister");
        CrossBigReactors.doStuff();
        CrossAppeng.registrationCheck();
        Vanilla.initVanilla();
    }

    public static void postinit(){
        CrossTE.intergrateTE();
        crossMekanism = new CrossMekanism();
        if(Loader.isModLoaded(ModLib.MEKANISM)){CrossMekanism.LoadItems();}
    }
}
