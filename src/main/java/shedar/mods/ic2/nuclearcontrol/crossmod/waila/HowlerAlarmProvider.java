package shedar.mods.ic2.nuclearcontrol.crossmod.waila;

import java.util.List;

import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityHowlerAlarm;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityIndustrialAlarm;
import shedar.mods.ic2.nuclearcontrol.utils.LangHelper;
import net.minecraft.item.ItemStack;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

/**
 * Provider for the howler alarm
 * 
 * @author xbony2
 *
 */
public class HowlerAlarmProvider implements IWailaDataProvider{

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler handler) {
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemstack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler handler) {
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemstack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler handler) {
		if(accessor.getTileEntity() instanceof TileEntityHowlerAlarm){
			if(!(accessor.getTileEntity() instanceof TileEntityIndustrialAlarm)){ //Howler alarm only
				currenttip.add(LangHelper.translate("msg.nc.waila.getSoundNames") + 
						((TileEntityHowlerAlarm)accessor.getTileEntity()).getSoundName());
			}
			currenttip.add(LangHelper.translate("msg.nc.waila.getRange") + 
					((TileEntityHowlerAlarm)accessor.getTileEntity()).getRange());
		}
		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemstack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler handler) {
		return currenttip;
	}

}
