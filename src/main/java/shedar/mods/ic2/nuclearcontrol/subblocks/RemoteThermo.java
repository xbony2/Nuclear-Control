package shedar.mods.ic2.nuclearcontrol.subblocks;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import shedar.mods.ic2.nuclearcontrol.containers.ContainerRemoteThermo;
import shedar.mods.ic2.nuclearcontrol.gui.GuiRemoteThermo;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityRemoteThermo;
import shedar.mods.ic2.nuclearcontrol.utils.Damages;

public class RemoteThermo extends Subblock{
    private static final int DAMAGE = Damages.DAMAGE_REMOTE_THERMO;
    private static final float[] BOUNDS = {0, 0, 0, 1, 1, 1};

    public static final byte I_BACK = 0;
    public static final byte I_FACE = 1;
    public static final byte I_SCALE = 2;
    public static final byte I_SIDE = 3;
    
    private static final byte[][] mapping ={
        {I_BACK, I_FACE, I_SIDE, I_SIDE, I_SIDE, I_SIDE},
        {I_FACE, I_BACK, I_SIDE, I_SIDE, I_SIDE, I_SIDE},
        {I_SIDE, I_SIDE, I_BACK, I_FACE, I_SIDE, I_SIDE},
        {I_SIDE, I_SIDE, I_FACE, I_BACK, I_SIDE, I_SIDE},
        {I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_BACK, I_FACE},
        {I_SIDE, I_SIDE, I_SIDE, I_SIDE, I_FACE, I_BACK}
    };
    
    private IIcon[] icons = new IIcon[4];
    
    public RemoteThermo(){
        super(DAMAGE, "tile.blockRemoteThermo");
    }

    @Override
    public TileEntity getTileEntity(){
        return new TileEntityRemoteThermo();
    }

    @Override
    public boolean isSolidBlockRequired(){
        return false;
    }

    @Override
    public boolean hasGui(){
        return true;
    }

    @Override
    public float[] getBlockBounds(TileEntity tileEntity){
        return BOUNDS;
    }

    @Override
    public Container getServerGuiElement(TileEntity tileEntity, EntityPlayer player){
        return new ContainerRemoteThermo(player, (TileEntityRemoteThermo)tileEntity);
    }

    @Override
    public Object getClientGuiElement(TileEntity tileEntity, EntityPlayer player){
        ContainerRemoteThermo container = new ContainerRemoteThermo(player, (TileEntityRemoteThermo)tileEntity);
        return new GuiRemoteThermo(container);
    }

    @Override
    public IIcon getIcon(int index){
        return icons[index];
    }

    @Override
    protected byte[][] getMapping(){
        return mapping;
    }

    @Override
    public void registerIcons(IIconRegister iconRegister){
        icons[I_BACK] = iconRegister.registerIcon("nuclearcontrol:remoteThermo/back");
        icons[I_FACE] = iconRegister.registerIcon("nuclearcontrol:remoteThermo/face");
        icons[I_SIDE] = iconRegister.registerIcon("nuclearcontrol:remoteThermo/side");
        icons[I_SCALE] = iconRegister.registerIcon("nuclearcontrol:remoteThermo/scale");
    }

}
