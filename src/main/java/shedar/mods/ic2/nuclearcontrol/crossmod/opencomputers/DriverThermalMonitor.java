package shedar.mods.ic2.nuclearcontrol.crossmod.opencomputers;

import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.crossmod.opencomputers.DriverInfoPanel.Environment;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityThermo;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverTileEntity;
import li.cil.oc.integration.ManagedTileEntityEnvironment;

public class DriverThermalMonitor extends DriverTileEntity {
	public static final String NAME = "thermal_monitor";
	
	@Override
	public ManagedEnvironment createEnvironment(World world, int x, int y, int z) {
		return new Environment((TileEntityThermo)world.getTileEntity(x, y, z));
	}

	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityThermo.class;
	}

	public static final class Environment extends ManagedTileEntityEnvironment<TileEntityThermo> implements NamedBlock{
		public Environment(final TileEntityThermo tileentity) {
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
		
		@Callback(doc = "function():boolean -- sees if the reactor is overheated")
		public Object[] isOverheated(final Context context, final Arguments args){
			if(tileEntity.getOnFire() == 1){
				return new Object[]{true};
			}
			return new Object[]{false};
		}
		
		@Callback(doc = "function():number -- returns 1 if overheated, 0 if not, -1 if reactor is not found")
		public Object[] getReactorStatus(final Context context, final Arguments args){
			return new Object[]{tileEntity.getOnFire()};
		}
		
		@Callback(doc = "function():number -- gets the set heat level of the reactor")
		public Object[] getHeatLevel(final Context context, final Arguments args){
			return new Object[]{tileEntity.getHeatLevel()};
		}
		
		@Callback(doc = "function(number) -- sets the heat level of the reactor")
		public Object[] setHeatLevel(final Context context, final Arguments args){
			final int newHeatLevel = args.checkInteger(0);
			if(newHeatLevel == tileEntity.getHeatLevel() || newHeatLevel < 0)
				return null;
			tileEntity.setHeatLevel(newHeatLevel);	
			return null;
		}
	}
}
