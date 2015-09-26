package shedar.mods.ic2.nuclearcontrol.crossmod.opencomputers;

import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.integration.ManagedTileEntityEnvironment;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter;
import li.cil.oc.api.network.ManagedEnvironment;
import net.minecraft.world.World;
import li.cil.oc.api.prefab.DriverTileEntity;

/**
 * OC Driver for the average counter.
 * 
 * @author xbony2
 */
public class DriverAverageCounter extends DriverTileEntity{
	public static final String NAME = "average_counter";

	@Override
	public ManagedEnvironment createEnvironment(World world, int x, int y, int z){
		return new Environment((TileEntityAverageCounter)world.getTileEntity(x, y, z));
	}

	@Override
	public Class<?> getTileEntityClass(){
		return TileEntityAverageCounter.class;
	}
	
	public static final class Environment extends ManagedTileEntityEnvironment<TileEntityAverageCounter> implements NamedBlock{
		public Environment(final TileEntityAverageCounter tileentity) {
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
		
		@Callback(doc = "function():number -- gets the energy type (0 for EU, 1 for RF, -1 for unknown/nil)")
		public Object[] getEnergyType(final Context context, final Arguments args){
			return new Object[]{((int)tileEntity.powerType)};
		}
	}
}
