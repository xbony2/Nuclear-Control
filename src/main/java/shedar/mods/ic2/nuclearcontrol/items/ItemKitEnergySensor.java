package shedar.mods.ic2.nuclearcontrol.items;

import cpw.mods.fml.common.Loader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.crossmod.EnergyStorageData;
import shedar.mods.ic2.nuclearcontrol.crossmod.ModLib;
import shedar.mods.ic2.nuclearcontrol.crossmod.RF.CrossTE;
import shedar.mods.ic2.nuclearcontrol.crossmod.mekanism.CrossMekanism;
import shedar.mods.ic2.nuclearcontrol.utils.EnergyStorageHelper;
import shedar.mods.ic2.nuclearcontrol.utils.ItemStackUtils;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearNetworkHelper;

public class ItemKitEnergySensor extends ItemSensorKitBase {

	public ItemKitEnergySensor() {
		super("kitEnergy");
	}

	@Override
	protected ItemStack getItemStackByDamage(int damage) {
		return new ItemStack(IC2NuclearControl.itemEnergySensorLocationCard);
	}
    protected ItemStack getItemStackbyType(EnergyStorageData storageData, TileEntity tileEntity) {
			if (tileEntity != null) {
				if (CrossMekanism.isMekanismPresent() && CrossMekanism.classExists && tileEntity instanceof mekanism.api.energy.IStrictEnergyStorage) {
					return new ItemStack(CrossMekanism.mekCard);
				}
			} else if (Loader.isModLoaded(ModLib.TE) || storageData.type == EnergyStorageData.TARGET_TYPE_RF) {
				return new ItemStack(CrossTE.RFSensorCard);
			}
        return new ItemStack(IC2NuclearControl.itemEnergySensorLocationCard);
    }

	@Override
	protected ChunkCoordinates getTargetCoordinates(World world, int x, int y, int z, ItemStack stack) {
		return null;
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (player == null)
			return false;
		boolean isServer = player instanceof EntityPlayerMP;
		if (!isServer)
			return false;
		EnergyStorageData storage = EnergyStorageHelper.getStorageAt(world, x, y, z, EnergyStorageData.TARGET_TYPE_UNKNOWN);
		if (storage != null) {
			ItemStack sensorLocationCard = getItemStackbyType(storage, world.getTileEntity(x, y, z));

			NBTTagCompound nbtTagCompound = ItemStackUtils.getTagCompound(sensorLocationCard);
			nbtTagCompound.setInteger("x", x);
			nbtTagCompound.setInteger("y", y);
			nbtTagCompound.setInteger("z", z);
			nbtTagCompound.setInteger("targetType", storage.type);

			player.inventory.mainInventory[player.inventory.currentItem] = sensorLocationCard;
			if (!world.isRemote) {
				NuclearNetworkHelper.chatMessage(player, "SensorKit");
			}
			return true;
		}
		return false;
	}

}
