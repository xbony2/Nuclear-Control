package shedar.mods.ic2.nuclearcontrol.crossmod.opencomputers;

import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanel;
import net.minecraft.world.World;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverTileEntity;
import li.cil.oc.integration.ManagedTileEntityEnvironment;

public class DriverAdvancedInfoPanel extends DriverTileEntity {

	@Override
	public ManagedEnvironment createEnvironment(World world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityAdvancedInfoPanel.class;
	}

	public static final class Environment extends ManagedTileEntityEnvironment<TileEntityAdvancedInfoPanel> implements NamedBlock{

		public Environment(TileEntityAdvancedInfoPanel tileEntity, String name) {
			super(tileEntity, name);
			// TODO Auto-generated constructor stub
		}

		@Override
		public String preferredName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int priority() {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}
}
