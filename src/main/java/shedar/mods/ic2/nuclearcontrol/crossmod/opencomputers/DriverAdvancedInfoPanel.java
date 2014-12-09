package shedar.mods.ic2.nuclearcontrol.crossmod.opencomputers;

import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanel;
import net.minecraft.world.World;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverTileEntity;
import li.cil.oc.integration.ManagedTileEntityEnvironment;

public class DriverAdvancedInfoPanel extends DriverTileEntity {
	public static final String NAME = "advanced_info_panel";
	
	@Override
	public ManagedEnvironment createEnvironment(World world, int x, int y, int z) {
		return new Environment((TileEntityAdvancedInfoPanel)world.getTileEntity(x, y, z));
	}

	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityAdvancedInfoPanel.class;
	}

	public static final class Environment extends ManagedTileEntityEnvironment<TileEntityAdvancedInfoPanel> implements NamedBlock{
		public Environment(final TileEntityAdvancedInfoPanel tileentity) {
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
		
		@Callback(doc = "function():number -- gets the thickness of the panel")
		public Object[] getThickness(final Context context, final Arguments args){
			return new Object[]{((int)tileEntity.getThickness())};
		}
		
		public Object[] setThickness(final Context context, final Arguments args){
			final int newThickness = args.checkInteger(0);
			if(newThickness == tileEntity.getThickness()) return null;
			//TODO
			return null;
		}
	}
}
