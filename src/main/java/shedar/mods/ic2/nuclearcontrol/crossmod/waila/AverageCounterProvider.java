package shedar.mods.ic2.nuclearcontrol.crossmod.waila;

import java.util.List;

import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter;
import shedar.mods.ic2.nuclearcontrol.utils.LangHelper;
import shedar.mods.ic2.nuclearcontrol.utils.StringUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

public class AverageCounterProvider implements IWailaDataProvider{
	
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
		if(accessor.getTileEntity() instanceof TileEntityAverageCounter){
			currenttip.add(LangHelper.translate("msg.nc.waila.getEnergyAverage") + 
					StringUtils.getFormatted("", ((TileEntityAverageCounter)accessor.getTileEntity()).getClientAverage(), false));
			currenttip.add(LangHelper.translate("msg.nc.waila.getPeriod") + 
					StringUtils.getFormatted("", ((TileEntityAverageCounter)accessor.getTileEntity()).period, false));
		}
		return currenttip;
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
