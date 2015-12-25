package shedar.mods.ic2.nuclearcontrol.utils;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorChamber;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.crossmod.ic2.IC2Cross;

public class NuclearHelper {

	private static final double STEAM_PER_EU = 3.2D;

	public static IReactor getReactorAt(World world, int x, int y, int z) {
		if (world == null)
			return null;
		TileEntity entity = world.getTileEntity(x, y, z);
		if (entity instanceof IReactor)
			return (IReactor) entity;
		return null;
	}
	
	
	public static boolean isSteam(IReactor reactor) {
		return IC2NuclearControl.instance.crossIc2.isSteamReactor((TileEntity) reactor);
	}

	public static int euToSteam(int eu) {
		return (int) Math.floor((eu) * STEAM_PER_EU);
	}

	public static IReactorChamber getReactorChamberAt(World world, int x, int y, int z) {
		if (world == null)
			return null;
		TileEntity entity = world.getTileEntity(x, y, z);
		if (entity instanceof IReactorChamber) {
			return (IReactorChamber) entity;
		}
		return null;
	}

	public static IReactor getReactorAroundCoord(World world, int x, int y, int z) {
		if (world == null)
			return null;
		IReactor reactor = null;
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			reactor = getReactorAt(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
			if(reactor != null)
			{
				break;
			}
		}
		return reactor;
	}

	public static IReactorChamber getReactorChamberAroundCoord(World world, int x, int y, int z) {
		if (world == null)
			return null;
		IReactorChamber chamber = null;
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			chamber = getReactorChamberAt(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
			if(chamber != null)
			{
				break;
			}
		}
		
		return chamber;
	}

	public static boolean isProducing(IReactor reactor) {
		ChunkCoordinates position = reactor.getPosition();
		return reactor.getWorld().isBlockIndirectlyGettingPowered(position.posX, position.posY, position.posZ);
	}

	public static int getNuclearCellTimeLeft(ItemStack rStack) {
		return IC2NuclearControl.instance.crossIc2.getNuclearCellTimeLeft(rStack);
	}

}
