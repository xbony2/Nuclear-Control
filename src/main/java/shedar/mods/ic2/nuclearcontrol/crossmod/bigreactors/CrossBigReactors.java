package shedar.mods.ic2.nuclearcontrol.crossmod.bigreactors;

import ic2.api.item.IC2Items;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.crossmod.bigreactors.recipes.BigReactorsRecipesNew;
import shedar.mods.ic2.nuclearcontrol.crossmod.bigreactors.recipes.BigReactorsRecipesGregtech;
import shedar.mods.ic2.nuclearcontrol.utils.NCLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import erogenousbeef.bigreactors.common.BigReactors;

public class CrossBigReactors {
	
	public static Block ReactorInfoFetch;
	public static Item reactorCard;
	public static Item kitRFsensor;
	
	public static void isRegistrationInOrder(){
		if(Loader.isModLoaded("BigReactors")){
			NCLog.fatal("We know that there is a foreign reactor installed...");
			activateOtherModStuff();
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
		
		if(IC2NuclearControl.instance.recipes.toLowerCase().equals("normal")){
			BigReactorsRecipesNew.addRecipes();
		}
		
		if(IC2NuclearControl.instance.recipes.toLowerCase().equals("gregtech")){
			BigReactorsRecipesGregtech.addRecipes();
		}
	}

}
