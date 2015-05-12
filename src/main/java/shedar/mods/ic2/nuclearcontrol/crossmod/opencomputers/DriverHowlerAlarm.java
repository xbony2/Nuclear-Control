package shedar.mods.ic2.nuclearcontrol.crossmod.opencomputers;

import li.cil.oc.api.machine.Callback;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverTileEntity;
import li.cil.oc.integration.ManagedTileEntityEnvironment;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityHowlerAlarm;

/**
 * OC Driver for the howler alarm.
 * 
 * @author xbony2
 */
public class DriverHowlerAlarm extends DriverTileEntity{
	public static final String NAME = "howler_alarm";

	@Override
	public ManagedEnvironment createEnvironment(World world, int x, int y, int z) {
		return new Environment((TileEntityHowlerAlarm)world.getTileEntity(x, y, z));
	}

	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityHowlerAlarm.class;
	}
	
	public static final class Environment extends ManagedTileEntityEnvironment<TileEntityHowlerAlarm> implements NamedBlock{
		public Environment(final TileEntityHowlerAlarm tileentity){
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
		
		@Callback(doc = "function():string -- gets the sound name.")
		public Object[] getSoundName(final Context context, final Arguments args){
			return new Object[]{tileEntity.getSoundName()};
		}
		
		@Callback(doc = "function():number -- gets the range set.")
		public Object[] getRange(final Context context, final Arguments args){
			return new Object[]{tileEntity.getRange()};
		}
	}
}
