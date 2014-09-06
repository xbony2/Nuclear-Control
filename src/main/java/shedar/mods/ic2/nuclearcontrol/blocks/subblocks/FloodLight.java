package shedar.mods.ic2.nuclearcontrol.blocks.subblocks;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityFloodLightOff;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityFloodLightOn;
import shedar.mods.ic2.nuclearcontrol.utils.Damages;

public class FloodLight extends Subblock{

	private static final int DAMAGE1 = Damages.DAMAGE_FLOOD_LIGHT_ON;
	private static final int DAMAGE2 = Damages.DAMAGE_FLOOD_LIGHT_OFF;
	private static final float[] BOUNDS = {0, 0, 0, 1, 1, 1};
	
	boolean isOn;
	
	public static final byte I_FRONT = 0;
	public static final byte I_EXTENDER_BACK = 1;
    public static final byte I_EXTENDER_SIDE = 2;
	
    private static final byte[][] mapping ={
            {I_EXTENDER_BACK, I_FRONT, I_EXTENDER_SIDE, I_EXTENDER_SIDE, I_EXTENDER_SIDE, I_EXTENDER_SIDE},
            {I_FRONT, I_EXTENDER_BACK, I_EXTENDER_SIDE, I_EXTENDER_SIDE, I_EXTENDER_SIDE, I_EXTENDER_SIDE},
            {I_EXTENDER_SIDE, I_EXTENDER_SIDE, I_EXTENDER_BACK, I_FRONT, I_EXTENDER_SIDE, I_EXTENDER_SIDE},
            {I_EXTENDER_SIDE, I_EXTENDER_SIDE, I_FRONT, I_EXTENDER_BACK, I_EXTENDER_SIDE, I_EXTENDER_SIDE},
            {I_EXTENDER_SIDE, I_EXTENDER_SIDE, I_EXTENDER_SIDE, I_EXTENDER_SIDE, I_EXTENDER_BACK, I_FRONT},
            {I_EXTENDER_SIDE, I_EXTENDER_SIDE, I_EXTENDER_SIDE, I_EXTENDER_SIDE, I_FRONT, I_EXTENDER_BACK}
        };
	
	private IIcon[] icons = new IIcon[3];
	
	public FloodLight(boolean on) {
		super(on ? DAMAGE1 : DAMAGE2, on ? "tile.blockFloodLightOn" : "tile.blockFloodLightOff");
		
	}

	@Override
	public IIcon getIcon(int index) {
		return icons[index];
	}

	@Override
	protected byte[][] getMapping() {
		return mapping;
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		if(isOn){
			icons[0] = iconRegister.registerIcon("nuclearcontrol:floodlight/on");
		}else{
			icons[0] = iconRegister.registerIcon("nuclearcontrol:floodlight/off");
		}
		icons[1] = iconRegister.registerIcon("nuclearcontrol:floodlight/back");
		icons[2] = iconRegister.registerIcon("nuclearcontrol:floodlight/side");
	}

	@Override
	public TileEntity getTileEntity() {
		TileEntity entity = (isOn ? new TileEntityFloodLightOn() : new TileEntityFloodLightOff());
		return entity;
	}

	@Override
	public boolean isSolidBlockRequired() {
		return false;
	}

	@Override
	public boolean hasGui() {
		return false;
	}

	@Override
	public float[] getBlockBounds(TileEntity tileEntity) {
		return BOUNDS;
	}

	@Override
	public Container getServerGuiElement(TileEntity tileEntity, EntityPlayer player) {
		return null;
	}

	@Override
	public Object getClientGuiElement(TileEntity tileEntity, EntityPlayer player) {
		return null;
	}

}
