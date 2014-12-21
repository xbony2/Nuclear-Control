package shedar.mods.ic2.nuclearcontrol.items;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorChamber;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearHelper;

public class ItemKitReactorSensor extends ItemSensorKitBase {

	public ItemKitReactorSensor() {
		super("kitReactor");
	}

	@Override
	protected ChunkCoordinates getTargetCoordinates(World world, int x, int y, int z, ItemStack stack) {
		IReactor reactor = NuclearHelper.getReactorAt(world, x, y, z);
		if (reactor == null) {
			IReactorChamber chamber = NuclearHelper.getReactorChamberAt(world, x, y, z);
			if (chamber != null) {
				reactor = chamber.getReactor();
			}
		}
		if (reactor != null)
			return reactor.getPosition();
		return null;
	}

	@Override
	protected ItemStack getItemStackByDamage(int damage) {
		return new ItemStack(IC2NuclearControl.itemSensorLocationCard,
				1, 0);
	}

}
