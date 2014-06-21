package shedar.mods.ic2.nuclearcontrol.crossmod.gregtech;

import java.lang.reflect.Field;

import gregtechmod.api.GregTech_API;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CrossGregTech
{
    private boolean _isApiAvailable = false;
    private boolean _isIdInitialized = false;

    private Class<?> _cellClass;
    private Field _maxDelay;
        
    private int _thoriumId1;
    private int _thoriumId2;
    private int _thoriumId4;
    
    private int _plutoniumId1;
    private int _plutoniumId2;
    private int _plutoniumId4;

    
    private void init()
    {
        if(!_isApiAvailable || _isIdInitialized)
            return;
        try
        {
            _cellClass = Class.forName("gregtechmod.api.items.GT_RadioactiveCell_Item");
            _maxDelay = _cellClass.getDeclaredField("maxDelay");
            _maxDelay.setAccessible(true);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    
        _thoriumId1 = GregTech_API.getGregTechItem(48, 1, 0).itemID;
        _thoriumId2 = GregTech_API.getGregTechItem(49, 1, 0).itemID;
        _thoriumId4 = GregTech_API.getGregTechItem(50, 1, 0).itemID;
        
        _plutoniumId1 = GregTech_API.getGregTechItem(51, 1, 0).itemID;
        _plutoniumId2 = GregTech_API.getGregTechItem(52, 1, 0).itemID;
        _plutoniumId4 = GregTech_API.getGregTechItem(53, 1, 0).itemID;
        
        _isIdInitialized = true;
    }
    
    public boolean isApiAvailable()
    {
        return _isApiAvailable;
    }
    
    public CrossGregTech()
    {
        try
        {
            Class.forName("gregtechmod.api.items.GT_RadioactiveCell_Item", false, this.getClass().getClassLoader());
            _maxDelay = null;
            _isApiAvailable = true;
        } catch (ClassNotFoundException e)
        {
            _isApiAvailable = false;
        }
    }
    
    public int getNuclearCellTimeLeft(ItemStack stack)
    {
        if(!_isApiAvailable || stack == null)
            return -1;
        init();
        if(stack.itemID == _thoriumId1 || stack.itemID == _thoriumId2 || stack.itemID == _thoriumId4 ||
                stack.itemID == _plutoniumId1 || stack.itemID == _plutoniumId2 || stack.itemID == _plutoniumId4)
        {
            if(_maxDelay!=null)
            {
                NBTTagCompound tags = stack.getTagCompound();

                int durability = tags == null?0:tags.getInteger("durability");
                
                try
                {
                    int max = _maxDelay.getInt(stack.getItem());
                    return max - durability;
                } catch (Exception e)
                {
                    return -1;
                }
            }
        }
        return -1;
    }
    
}
