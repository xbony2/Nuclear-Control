package shedar.mods.ic2.nuclearcontrol.crossmod.waila;

import java.util.List;

import shedar.mods.ic2.nuclearcontrol.api.BonyDebugger;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityIC2Thermo;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class CrossWaila implements IWailaDataProvider{

	@Override
	public List<String> getWailaBody(ItemStack itemstack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler handler) {
		BonyDebugger.debug();
		if(accessor.getTileEntity() instanceof TileEntityIC2Thermo){
			currenttip.add(((TileEntityIC2Thermo)accessor.getTileEntity()).getHeatLevel().toString());
		}
		return currenttip;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemstack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler handler) {
		return currenttip;
	}

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler handler) {
		return null;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemstack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler handler) {
		return currenttip;
	}
	
	public static void callbackRegister(IWailaRegistrar register){
		register.addConfig("IC2NuclearControl", "msg.nc.waila.showBody");
		/*register.registerHeadProvider(new CrossWaila(), 4000);
		register.registerBodyProvider(new CrossWaila(), Block.class);*/
	}

}
