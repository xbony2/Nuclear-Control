package shedar.mods.ic2.nuclearcontrol.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTankInfo;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityEnergyCounter;
import shedar.mods.ic2.nuclearcontrol.utils.LiquidStorageHelper;
import shedar.mods.ic2.nuclearcontrol.utils.TextureResolver;

public class ItemKitMultipleSensor extends ItemSensorKitBase{
    public static final int TYPE_COUNTER = 0;
    public static final int TYPE_LIQUID = 1;

    private static final String TEXTURE_KIT_COUNTER = "kitCounter";
    private static final String TEXTURE_KIT_LIQUID = "kitLiquid";
    
    private IIcon iconCounter;
    private IIcon iconLiquid;
    
    public ItemKitMultipleSensor(){
        super("");
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) 
    {
        int damage = stack.getItemDamage();
        switch (damage)
        {
        case TYPE_COUNTER:
            return "item.ItemCounterSensorKit";
        case TYPE_LIQUID:
            return "item.ItemLiquidSensorKit";
        }
        return "";
    }

    @Override
    public void registerIcons(IIconRegister iconRegister)
    {
        iconCounter = iconRegister.registerIcon(TextureResolver.getItemTexture(TEXTURE_KIT_COUNTER));
        iconLiquid = iconRegister.registerIcon(TextureResolver.getItemTexture(TEXTURE_KIT_LIQUID));
    }    
    
    @Override
    public IIcon getIconFromDamage(int damage){
        switch (damage){
        case TYPE_COUNTER:
            return iconCounter;
        case TYPE_LIQUID:
            return iconLiquid;
        }
        return null;
    }
    
    @Override
    protected ChunkCoordinates getTargetCoordinates(World world, int x, int y, int z, ItemStack stack){
        int damage = stack.getItemDamage();
        
        switch (damage){
        case TYPE_COUNTER:
            TileEntity entity = world.getTileEntity(x, y, z);
            if (entity != null && 
                (entity instanceof TileEntityEnergyCounter ||
                 entity instanceof TileEntityAverageCounter))
            {
                return new ChunkCoordinates(x, y, z);
            }
            break;
        case TYPE_LIQUID:
            FluidTankInfo tank = LiquidStorageHelper.getStorageAt(world, x, y, z);
            if(tank!=null)
            {
                return new ChunkCoordinates(x, y, z);
            }
            break;
        default:
            break;
        }
        return null;
    }

    @Override
    protected ItemStack getItemStackByDamage(int damage)
    {
        switch (damage)
        {
        case TYPE_COUNTER:
            return new ItemStack(IC2NuclearControl.instance.itemMultipleSensorLocationCard, 1, TYPE_COUNTER);
        case TYPE_LIQUID:
            return new ItemStack(IC2NuclearControl.instance.itemMultipleSensorLocationCard, 1, TYPE_LIQUID);
        }
        return null;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, TYPE_COUNTER));
        par3List.add(new ItemStack(par1, 1, TYPE_LIQUID));
    }

}
