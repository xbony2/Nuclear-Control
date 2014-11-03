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

public class CrossWaila {
	
	public static void callbackRegister(IWailaRegistrar register){
		register.registerBodyProvider(new IC2ThermoProvider(), TileEntityIC2Thermo.class);
	}

}
