package shedar.mods.ic2.nuclearcontrol.crossmod.RF;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;
import shedar.mods.ic2.nuclearcontrol.crossmod.EnergyStorageData;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityEnergyCounter;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.registry.GameRegistry;

public class CrossBuildcraft{
    private boolean _isApiAvailable = false;
    
    public boolean isApiAvailable(){
        return _isApiAvailable;
    }
    
    public CrossBuildcraft(){
        try {
            Class.forName("buildcraft.api.tools.IToolWrench", false, this.getClass().getClassLoader());
            _isApiAvailable = true;
        }catch (ClassNotFoundException e){
            _isApiAvailable = false;
        }
    }
    
    public void useWrench(ItemStack itemStack, TileEntity target, EntityPlayer player){
        if(_isApiAvailable)
            ((IToolWrench) itemStack.getItem()).wrenchUsed(player, target.xCoord, target.yCoord, target.zCoord);
    }
    
    public boolean isWrench(ItemStack itemStack, TileEntity target, EntityPlayer player){
        return _isApiAvailable && itemStack.getItem() instanceof IToolWrench && ((IToolWrench)itemStack.getItem()).canWrench(player, target.xCoord, target.yCoord, target.zCoord);
    }
    
    public boolean isTankContainer(Object obj){
        return _isApiAvailable && obj instanceof IFluidHandler;
    }
}
