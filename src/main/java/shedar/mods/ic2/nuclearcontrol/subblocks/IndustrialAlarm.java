package shedar.mods.ic2.nuclearcontrol.subblocks;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import shedar.mods.ic2.nuclearcontrol.containers.ContainerEmpty;
import shedar.mods.ic2.nuclearcontrol.gui.GuiIndustrialAlarm;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityHowlerAlarm;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityIndustrialAlarm;
import shedar.mods.ic2.nuclearcontrol.utils.Damages;

public class IndustrialAlarm extends Subblock
{
    private static final int DAMAGE = Damages.DAMAGE_INDUSTRIAL_ALARM;
    private static final float[] BOUNDS = {0.125F, 0, 0.125F, 0.875F, 0.4375F, 0.875F};
    
    public static final byte I_BACK = 0;
    
    public static final byte I_SIDES_HOR_DARK = 1;
    public static final byte I_SIDES_HOR_MID = 2;
    public static final byte I_SIDES_HOR_BRIGHT = 3;
    
    public static final byte I_SIDES_VERT_DARK = 4;
    public static final byte I_SIDES_VERT_MID = 5;
    public static final byte I_SIDES_VERT_BRIGHT = 6;

    public static final byte I_FACE_DARK= 7;
    public static final byte I_FACE_MID = 8;
    public static final byte I_FACE_BRIGHT = 9;
    
    private Icon[] icons = new Icon[10];
    
    private static final byte[][] mapping =
    {
        {I_BACK, I_FACE_DARK, I_SIDES_HOR_DARK, I_SIDES_HOR_DARK, I_SIDES_HOR_DARK, I_SIDES_HOR_DARK},
        {I_FACE_DARK, I_BACK, I_SIDES_HOR_DARK, I_SIDES_HOR_DARK, I_SIDES_HOR_DARK, I_SIDES_HOR_DARK},
        {I_SIDES_HOR_DARK, I_SIDES_HOR_DARK, I_BACK, I_FACE_DARK, I_SIDES_VERT_DARK, I_SIDES_VERT_DARK},
        {I_SIDES_HOR_DARK, I_SIDES_HOR_DARK, I_FACE_DARK, I_BACK, I_SIDES_VERT_DARK, I_SIDES_VERT_DARK},
        {I_SIDES_VERT_DARK, I_SIDES_VERT_DARK, I_SIDES_VERT_DARK, I_SIDES_VERT_DARK, I_BACK, I_FACE_DARK},
        {I_SIDES_VERT_DARK, I_SIDES_VERT_DARK, I_SIDES_VERT_DARK, I_SIDES_VERT_DARK, I_FACE_DARK, I_BACK}
    };
    
    public IndustrialAlarm()
    {
        super(DAMAGE, "tile.blockIndustrialAlarm");
    }

    @Override
    public TileEntity getTileEntity()
    {
        return new TileEntityIndustrialAlarm();
    }

    @Override
    public boolean isSolidBlockRequired()
    {
        return true;
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    public float[] getBlockBounds(TileEntity tileEntity)
    {
        return BOUNDS;
    }

    @Override
    public Container getServerGuiElement(TileEntity tileEntity, EntityPlayer player)
    {
        return new ContainerEmpty(tileEntity);
    }

    @Override
    public Object getClientGuiElement(TileEntity tileEntity, EntityPlayer player)
    {
        return new GuiIndustrialAlarm((TileEntityHowlerAlarm)tileEntity);
    }

    @Override
    public Icon getIcon(int index)
    {
        return icons[index];
    }

    @Override
    protected byte[][] getMapping()
    {
        return mapping;
    }

    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        icons[I_BACK] = iconRegister.registerIcon("nuclearcontrol:industrialAlarm/back");

        icons[I_SIDES_HOR_DARK] = iconRegister.registerIcon("nuclearcontrol:industrialAlarm/sidesHor0");
        icons[I_SIDES_HOR_MID] = iconRegister.registerIcon("nuclearcontrol:industrialAlarm/sidesHor1");
        icons[I_SIDES_HOR_BRIGHT] = iconRegister.registerIcon("nuclearcontrol:industrialAlarm/sidesHor2");

        icons[I_SIDES_VERT_DARK] = iconRegister.registerIcon("nuclearcontrol:industrialAlarm/sidesVert0");
        icons[I_SIDES_VERT_MID] = iconRegister.registerIcon("nuclearcontrol:industrialAlarm/sidesVert1");
        icons[I_SIDES_VERT_BRIGHT] = iconRegister.registerIcon("nuclearcontrol:industrialAlarm/sidesVert2");

        icons[I_FACE_DARK] = iconRegister.registerIcon("nuclearcontrol:industrialAlarm/face0");
        icons[I_FACE_MID] = iconRegister.registerIcon("nuclearcontrol:industrialAlarm/face1");
        icons[I_FACE_BRIGHT] = iconRegister.registerIcon("nuclearcontrol:industrialAlarm/face2");
    }

}
