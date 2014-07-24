package shedar.mods.ic2.nuclearcontrol.blocks.subblocks;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.containers.ContainerEnergyCounter;
import shedar.mods.ic2.nuclearcontrol.gui.GuiEnergyCounter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityEnergyCounter;
import shedar.mods.ic2.nuclearcontrol.utils.Damages;

public class EnergyCounter extends Subblock{
    private static final int DAMAGE = Damages.DAMAGE_ENERGY_COUNTER;
    private static final float[] BOUNDS = {0, 0, 0, 1, 1, 1};

    public static final byte I_INPUT = 0;
    public static final byte I_OUTPUT = 1;
    
    private static final byte[][] mapping =
        {
        {I_OUTPUT, I_INPUT, I_OUTPUT, I_OUTPUT, I_OUTPUT, I_OUTPUT},
        {I_INPUT, I_OUTPUT, I_OUTPUT, I_OUTPUT, I_OUTPUT, I_OUTPUT},
        {I_OUTPUT, I_OUTPUT, I_OUTPUT, I_INPUT, I_OUTPUT, I_OUTPUT},
        {I_OUTPUT, I_OUTPUT, I_INPUT, I_OUTPUT, I_OUTPUT, I_OUTPUT},
        {I_OUTPUT, I_OUTPUT, I_OUTPUT, I_OUTPUT, I_OUTPUT, I_INPUT},
        {I_OUTPUT, I_OUTPUT, I_OUTPUT, I_OUTPUT, I_INPUT, I_OUTPUT}
    };
    
    private IIcon[] icons = new IIcon[2];

    public EnergyCounter(){
        super(DAMAGE, "tile.blockEnergyCounter");
    }

    @Override
    public TileEntity getTileEntity(){
        TileEntity instance = IC2NuclearControl.instance.crossBC.getEnergyCounter();
        if(instance == null)
            instance = new TileEntityEnergyCounter();
        return instance;
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
        return new ContainerEnergyCounter(player, (TileEntityEnergyCounter)tileEntity);
    }

    @Override
    public Object getClientGuiElement(TileEntity tileEntity, EntityPlayer player){
        ContainerEnergyCounter containerCounter = new ContainerEnergyCounter(player, (TileEntityEnergyCounter)tileEntity);
        return new GuiEnergyCounter(containerCounter);
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
        icons[I_INPUT] = iconRegister.registerIcon("nuclearcontrol:energyCounter/input");
        icons[I_OUTPUT] = iconRegister.registerIcon("nuclearcontrol:energyCounter/output");
        
    }

}
