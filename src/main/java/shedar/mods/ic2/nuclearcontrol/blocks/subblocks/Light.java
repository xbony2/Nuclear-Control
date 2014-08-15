package shedar.mods.ic2.nuclearcontrol.blocks.subblocks;

import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityLight;
import shedar.mods.ic2.nuclearcontrol.utils.Damages;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

public class Light extends Subblock{

	private static final int DAMAGE = Damages.DAMAGE_LIGHT;
	private static final float[] BOUNDS = {0, 0, 0, 1, 1, 1};
	
	public static final byte I_SIDE = 0;
	
	 private static final byte[][] mapping = {
	        {I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE},
	        {I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE},
	        {I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE},
	        {I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE},
	        {I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE},
	        {I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_SIDE}
	    };
	
	private IIcon[] icons = new IIcon[1];
	
	public Light() {
		super(DAMAGE, "tile.blockLight");
		
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
		icons[0] = iconRegister.registerIcon("nuclearcontrol:light/on/whiteOn");
	}

	@Override
	public TileEntity getTileEntity(){
		TileEntity entity = new TileEntityLight();
		return entity;
	}

	@Override
	public boolean isSolidBlockRequired(){
		return false;
	}

	@Override
	public boolean hasGui(){
		return false;
	}

	@Override
	public float[] getBlockBounds(TileEntity tileEntity){
		return BOUNDS;
	}

	@Override
	public Container getServerGuiElement(TileEntity tileEntity, EntityPlayer player){
		return null;
	}

	@Override
	public Object getClientGuiElement(TileEntity tileEntity, EntityPlayer player){
		return null;
	}

}
