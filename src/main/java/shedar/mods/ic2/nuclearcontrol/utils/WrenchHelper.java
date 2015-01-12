package shedar.mods.ic2.nuclearcontrol.utils;

import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.IRotation;
import cpw.mods.fml.common.FMLCommonHandler;

public class WrenchHelper {
	public static boolean isWrenchClicked(TileEntity tileEntity, EntityPlayer player, int side) {
		if (player != null && tileEntity != null) {
			ItemStack equipped = player.getCurrentEquippedItem();

			if (equipped != null) {
				boolean ic2Wrench = IC2NuclearControl.instance.crossIC2.isWrench(equipped);
				boolean bcWrench = IC2NuclearControl.instance.crossBC.isWrench(equipped, tileEntity, player);// TODO: DMF
				if (player.isSneaking() && tileEntity instanceof IRotation) {
					if (ic2Wrench || bcWrench) {
						if (bcWrench) {
							IC2NuclearControl.instance.crossBC.useWrench(equipped, tileEntity, player);
						}

						if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
							((IRotation) tileEntity).rotate();
						}
						return true;
					}
				}else if (bcWrench && tileEntity instanceof IWrenchable){
					IWrenchable wrenchable = (IWrenchable) tileEntity;

					if (player.isSneaking()) {
						side += side % 2 * -2 + 1;
					}

					if (wrenchable.wrenchCanSetFacing(player, side)) {
						IC2NuclearControl.instance.crossBC.useWrench(equipped, tileEntity, player);
						if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
							wrenchable.setFacing((short) side);
						}
						return true;
					}
				}
			}
		}
		return false;
	}
}
