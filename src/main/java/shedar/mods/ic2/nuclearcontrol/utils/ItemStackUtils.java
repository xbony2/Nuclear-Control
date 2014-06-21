package shedar.mods.ic2.nuclearcontrol.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemStackUtils
{
    
    public static NBTTagCompound getTagCompound(ItemStack itemStack)
    {
        NBTTagCompound nbtTagCompound = itemStack.getTagCompound();
        if (nbtTagCompound == null)
        {
            nbtTagCompound = new NBTTagCompound();
            itemStack.setTagCompound(nbtTagCompound);
        }
        return nbtTagCompound;
    }
}
