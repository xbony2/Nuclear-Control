package shedar.mods.ic2.nuclearcontrol.crossmod.BigReactors;

import ic2.api.item.IC2Items;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
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
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(kitRFsensor), new Object[] {"it ", "pd ", " r ", 'i', "ingotYellorium", 't', IC2Items.getItem("frequencyTransmitter"), 'p', Items.paper, 'd', "dyeRed", 'r', Items.redstone}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ReactorInfoFetch), new Object[] {"brb", "ycy", "brb", 'b', "reactorCasing", 'r', Items.redstone, 'y', "ingotYellorium", 'c', Items.comparator}));
	}

}
