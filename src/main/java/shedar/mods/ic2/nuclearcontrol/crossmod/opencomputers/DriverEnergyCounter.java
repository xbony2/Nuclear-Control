package shedar.mods.ic2.nuclearcontrol.crossmod.opencomputers;

import li.cil.oc.api.network.ManagedEnvironment;

import li.cil.oc.api.machine.Callback;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityEnergyCounter;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.integration.ManagedTileEntityEnvironment;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter;
import li.cil.oc.api.prefab.DriverTileEntity;
import net.minecraft.world.World;

public class DriverEnergyCounter extends DriverTileEntity {
	public static final String NAME = "energy_counter";
	
	@Override
	public ManagedEnvironment createEnvironment(World world, int x, int y, int z){
		return new Environment((TileEntityEnergyCounter)world.getTileEntity(x, y, z));
	}

	@Override
	public Class<?> getTileEntityClass(){
		return TileEntityEnergyCounter.class;
	}
	
	public static final class Environment extends ManagedTileEntityEnvironment<TileEntityEnergyCounter> implements NamedBlock{
		public Environment(final TileEntityEnergyCounter tileentity) {
			super(tileentity, NAME);
		}

		@Override
		public String preferredName() {
			return NAME;
		}

		@Override
		public int priority() {
			return 0;
		}
		
		@Callback(doc = "function():number -- gets the period (in seconds) of the average counter.")
		public Object[] getPeriod(final Context context, final Arguments args){
			return new Object[]{((int)tileEntity.period)};
		}
		
		@Callback(doc = "function():number -- gets the average of the counter.")
		public Object[] getAverage(final Context context, final Arguments args){
			return new Object[]{((int)tileEntity.getClientAverage())};
		}
	}
}
