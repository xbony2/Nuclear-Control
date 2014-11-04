package shedar.mods.ic2.nuclearcontrol.crossmod.waila;

import java.util.List;

import net.minecraft.item.ItemStack;
import shedar.mods.ic2.nuclearcontrol.api.BonyDebugger;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityIC2Thermo;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityRemoteThermo;
import shedar.mods.ic2.nuclearcontrol.utils.LangHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

public class IC2ThermoProvider implements IWailaDataProvider{
	
	@Override
	public List<String> getWailaBody(ItemStack itemstack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler handler) {
		if(accessor.getTileEntity() instanceof TileEntityIC2Thermo){
			currenttip.add(LangHelper.translate("msg.nc.waila.getHeatLevel") + 
					((TileEntityIC2Thermo)accessor.getTileEntity()).getHeatLevel().toString());
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
}
