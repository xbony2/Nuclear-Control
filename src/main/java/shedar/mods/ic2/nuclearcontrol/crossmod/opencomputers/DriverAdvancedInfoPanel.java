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

/**
 * OC Driver for the advanced info panel.
 * 
 * @author xbony2
 */
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
		
		@Callback(doc = "function():number -- gets the horizonal rotation of the panel")
		public Object[] getRotationHorizonally(final Context context, final Arguments args){
			return new Object[]{((int)tileEntity.getRotationHor())};
		}
		
		@Callback(doc = "function():number -- gets the vertical rotation of the panel")
		public Object[] getRotationVertically(final Context context, final Arguments args){
			return new Object[]{((int)tileEntity.getRotationVert())};
		}
		
		@Callback(doc = "function():number -- gets the background color of the panel")
		public Object[] getBackgroundColor(final Context context, final Arguments args){
			return new Object[]{(tileEntity.getColorBackground())};
		}
		
		/* TODO: this is broken
		@Callback(doc = "function(value:number) -- sets the background color of the panel (0-16)")
		public Object[] setBackgroundColor(final Context context, final Arguments args){
			final int newColor = args.checkInteger(0);
			if(newColor < 0 || newColor > 16) return null; //invalid input
			if(newColor == tileEntity.getColorBackground()) return null; //same as the current background
			tileEntity.setColorBackground(newColor);
			return null;
		}*/
		
		@Callback(doc = "function(value:number) -- sets the thickness of the panel. Only input 1-16, otherwise nothing will happen")
		public Object[] setThickness(final Context context, final Arguments args){
			final int newThickness = args.checkInteger(0);
			if(newThickness <= 0 || newThickness > 16) return null; //invalid input
			if(newThickness == tileEntity.getThickness()) return null; //same as the current thickness
			tileEntity.setThickness((byte) newThickness);
			return null;
		}
		
		@Callback(doc = "function(value:number) -- sets the horizonal rotation of the panel. Only input 1-16, otherwise nothing will happen")
		public Object[] rotateHorizonally(final Context context, final Arguments args){
			final int newRotation = args.checkInteger(0);
			if(newRotation <= 0 || newRotation > 16) return null; //invalid input
			if(newRotation == tileEntity.getRotationHor()) return null; //same as the current rotation
			tileEntity.setRotateHor((byte) newRotation);
			return null;
		}
		
		@Callback(doc = "function(value:number) -- sets the vertical rotation of the panel. Only input 1-16, otherwise nothing will happen")
		public Object[] rotateVertically(final Context context, final Arguments args){
			final int newRotation = args.checkInteger(0);
			if(newRotation <= 0 || newRotation > 16) return null; //invalid input
			if(newRotation == tileEntity.getRotationVert()) return null; //same as the current rotation
			tileEntity.setRotateVert((byte) newRotation);
			return null;
		}
	}
}
