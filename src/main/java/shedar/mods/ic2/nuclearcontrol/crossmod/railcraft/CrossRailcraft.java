package shedar.mods.ic2.nuclearcontrol.crossmod.railcraft;

import java.lang.reflect.Method;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;

public class CrossRailcraft {
	private boolean _isApiAvailable = false;
	private Class _tankTile;

	public CrossRailcraft() {
		try{
			_tankTile = Class.forName("mods.railcraft.common.blocks.machine.ITankTile", false, this.getClass().getClassLoader());
			_isApiAvailable = true;
		}catch(ClassNotFoundException e){
			_isApiAvailable = false;
		}
	}

	public FluidTankInfo getIronTank(TileEntity entity) {
		if (!_isApiAvailable || entity == null)
			return null;
		try {
			if (_tankTile.isAssignableFrom(entity.getClass())) {
				Method method = entity.getClass().getMethod("getTank");
				FluidTank tank = (FluidTank) method.invoke(entity);
				if (tank != null)
					return tank.getInfo();
			}
			return null;
		}catch (Exception e){
			return null;
		}
	}

}
