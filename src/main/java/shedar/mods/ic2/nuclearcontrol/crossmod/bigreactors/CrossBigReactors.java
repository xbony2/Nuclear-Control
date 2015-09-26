package shedar.mods.ic2.nuclearcontrol.crossmod.bigreactors;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.crossmod.ModLib;
import shedar.mods.ic2.nuclearcontrol.crossmod.bigreactors.recipes.BigReactorsRecipesGregtech;
import shedar.mods.ic2.nuclearcontrol.crossmod.bigreactors.recipes.BigReactorsRecipesNew;
import shedar.mods.ic2.nuclearcontrol.utils.NCLog;

public class CrossBigReactors {
	
	public static Block ReactorInfoFetch;
	public static Item reactorCard;
	public static Item kitRFsensor;
	
	public static void doStuff(){
		if(Loader.isModLoaded(ModLib.BIG_REACTORS)){
			NCLog.fatal("We know that there is a foreign reactor installed...");
			activateOtherModStuff();
		}
	}

    @Optional.Method(modid = ModLib.BIG_REACTORS)
	private static void activateOtherModStuff(){
		ReactorInfoFetch = new BlockActiveReactorInfoFetch();
		reactorCard = new ItemCardRFSensor().setUnlocalizedName("RFreactorCard");
		kitRFsensor = new ItemKitRFSensor().setUnlocalizedName("RFreactorKit");
		GameRegistry.registerBlock(ReactorInfoFetch, "NC-BRinfoFetch");
		GameRegistry.registerItem(reactorCard, "NC-BRreactorCard");
		GameRegistry.registerItem(kitRFsensor, "NC-BRrfsensorKit");
		GameRegistry.registerTileEntity(TileEntityBlockFetcher.class, "NC-BRinfoFetch");
		
		if(IC2NuclearControl.instance.recipes.toLowerCase().equals("normal")){
			BigReactorsRecipesNew.addRecipes();
		}
		
		if(IC2NuclearControl.instance.recipes.toLowerCase().equals("gregtech")){
			BigReactorsRecipesGregtech.addRecipes();
		}
	}

}
