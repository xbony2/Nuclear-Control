package shedar.mods.ic2.nuclearcontrol.crossmod.opencomputers;

import shedar.mods.ic2.nuclearcontrol.crossmod.opencomputers.DriverAdvancedInfoPanel.Environment;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanel;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import net.minecraft.world.World;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverTileEntity;
import li.cil.oc.integration.ManagedTileEntityEnvironment;

/**
 * OC Driver for the info panel.
 * 
 * @author xbony2
 */
public class DriverInfoPanel extends DriverTileEntity {
	public static final String NAME = "info_panel";

	@Override
	public ManagedEnvironment createEnvironment(World world, int x, int y, int z) {
		return new Environment((TileEntityInfoPanel)world.getTileEntity(x, y, z));
	}

	@Override
	public Class<?> getTileEntityClass() {
		return TileEntityInfoPanel.class;
	}

	public static final class Environment extends ManagedTileEntityEnvironment<TileEntityInfoPanel> implements NamedBlock{
		public Environment(final TileEntityInfoPanel tileentity) {
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
		
		@Callback(doc = "function():number -- gets the background color of the panel")
		public Object[] getBackgroundColor(final Context context, final Arguments args){
			return new Object[]{tileEntity.getColorBackground()};
		}
		
		@Callback(doc = "function():number -- gets the text color of the panel")
		public Object[] getTextColor(final Context context, final Arguments args){
			return new Object[]{tileEntity.getColorText()};
		}
		
		@Callback(doc = "function():boolean -- checks if the color upgrade is installed")
		public Object[] hasColorUpgrade(final Context context, final Arguments args){
			return new Object[]{tileEntity.getColored()};
		}
		
		@Callback(doc = "function():boolean -- checks if the web upgrade is installed (should be false most of the time)")
		public Object[] hasWebUpgrade(final Context context, final Arguments args){
			return new Object[]{tileEntity.getIsWeb()};
		}
	}
}
