package shedar.mods.ic2.nuclearcontrol.crossmod.BigReactors;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import shedar.mods.ic2.nuclearcontrol.utils.NCLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class CrossBigReactors {
	
	public static Block ReactorInfoFetch;
	public static Item reactorCard;
	public static Item kitRFsensor;
	
	public static void isRegistrationInOrder(){
		if(Loader.isModLoaded("BigReactors")){
			NCLog.fatal("We know that there is a foreign reactor installed...");
			activateOtherModStuff();
			//GameRegistry.addShapelessRecipe(new ItemStack(Items.diamond,  64), Blocks.dirt); 
		}
	}
	
	private static void activateOtherModStuff(){
		ReactorInfoFetch = new BlockActiveReactorInfoFetch();
		reactorCard = new ItemCardRFSensor().setUnlocalizedName("RFreactorCard");
		kitRFsensor = new ItemKitRFSensor().setUnlocalizedName("RFreactorKit");
		GameRegistry.registerBlock(ReactorInfoFetch, "NC-BRinfoFetch");
		GameRegistry.registerItem(reactorCard, "NC-BRreactorCard");
		GameRegistry.registerItem(kitRFsensor, "NC-BRrfsensorKit");
		GameRegistry.registerTileEntity(TileEntityBlockFetcher.class, "NC-BRinfoFetch");
	}

}
