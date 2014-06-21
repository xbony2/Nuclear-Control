package shedar.mods.ic2.nuclearcontrol.crossmod.nei;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import codechicken.nei.forge.IContainerTooltipHandler;

public class TooltipHandler implements IContainerTooltipHandler
{

    @Override
    public List<String> handleTooltipFirst(GuiContainer gui, int mousex, int mousey, List<String> currenttip)
    {
        return currenttip;
    }

    @Override
    public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, List<String> currenttip)
    {
        if(itemstack!=null && itemstack.hasTagCompound())
        {
            NBTTagCompound tags = itemstack.getTagCompound();
            if(tags.hasKey("_webSensorId")){
                long id = tags.getLong("_webSensorId");
                if(id>0)
                    currenttip.add("Web Id: "+id);
            }
        }
        return currenttip;
    }


}
