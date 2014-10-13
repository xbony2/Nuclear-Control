package shedar.mods.ic2.nuclearcontrol.utils;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;

public class LiquidStorageHelper {

	public static FluidTankInfo getStorageAt(World world, int x, int y, int z) {
		if (world == null)
			return null;
		TileEntity entity = world.getTileEntity(x, y, z);
		if (entity != null && entity instanceof IFluidHandler) {
			FluidTankInfo[] tanks = ((IFluidHandler) entity).getTankInfo(ForgeDirection.UNKNOWN);
			if (tanks == null || tanks.length == 0)
				return null;
			return tanks[0];
		}
		return IC2NuclearControl.instance.crossRailcraft.getIronTank(entity);
	}

}
