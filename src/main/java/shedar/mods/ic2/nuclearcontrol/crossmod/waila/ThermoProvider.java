package shedar.mods.ic2.nuclearcontrol.crossmod.waila;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.api.BonyDebugger;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityThermo;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityRemoteThermo;
import shedar.mods.ic2.nuclearcontrol.utils.LangHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

/**
 * Provider for the thermal monitor.
 * 
 * @author xbony2
 *
 */
public class ThermoProvider implements IWailaDataProvider{
	
	@Override
	public List<String> getWailaBody(ItemStack itemstack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler handler) {
		if(accessor.getTileEntity() instanceof TileEntityThermo){
			currenttip.add(LangHelper.translate("msg.nc.waila.getHeatLevel") + 
					((TileEntityThermo)accessor.getTileEntity()).getHeatLevel().toString());
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
	
	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tileentity, NBTTagCompound tag, World world, int x, int y, int z){
		return null;
	}
}
