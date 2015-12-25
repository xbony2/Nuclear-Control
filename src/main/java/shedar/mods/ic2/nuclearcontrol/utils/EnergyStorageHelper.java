package shedar.mods.ic2.nuclearcontrol.utils;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.crossmod.EnergyStorageData;

public class EnergyStorageHelper {

	public static EnergyStorageData getStorageAt(World world, int x, int y, int z, int type) {
		if (world == null)
			return null;
		TileEntity entity = world.getTileEntity(x, y, z);
		switch (type) {
		case EnergyStorageData.TARGET_TYPE_IC2:
			return IC2NuclearControl.instance.crossIc2.getStorageData(entity);
		case EnergyStorageData.TARGET_TYPE_RF:
			return IC2NuclearControl.instance.crossRF.getStorageData(entity);
		case EnergyStorageData.TARGET_TYPE_UNKNOWN:
			EnergyStorageData data = IC2NuclearControl.instance.crossIc2.getStorageData(entity);
			if (data == null) { //TODO: there's probably a better way to do this for future integration
				data = IC2NuclearControl.instance.crossRF.getStorageData(entity);
			}
			return data;
		}
		return null;
	}

}
