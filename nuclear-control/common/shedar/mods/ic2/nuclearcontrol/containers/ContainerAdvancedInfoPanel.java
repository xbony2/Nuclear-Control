package shedar.mods.ic2.nuclearcontrol.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import shedar.mods.ic2.nuclearcontrol.SlotFilter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanel;

public class ContainerAdvancedInfoPanel extends ContainerInfoPanel
{
    public ContainerAdvancedInfoPanel(EntityPlayer player, TileEntityAdvancedInfoPanel panel)
    {
        super();
        
        this.panel = panel;
        this.player = player; 
        
        //cards
        addSlotToContainer(new SlotFilter(panel, 0, 8, 24+18));
        addSlotToContainer(new SlotFilter(panel, 1, 8+18, 24+18));
        addSlotToContainer(new SlotFilter(panel, 2, 8+36, 24+18));
        
        //range upgrade
        addSlotToContainer(new SlotFilter(panel, 3, 8+54, 24+18));

        //inventory
        for (int i = 0; i < 3; i++)
        {
            for (int k = 0; k < 9; k++)
            {
                addSlotToContainer(new Slot(player.inventory, k + i * 9 + 9, 8 + k * 18, 130 + i * 18));
            }
        }

        for (int j = 0; j < 9; j++)
        {
            addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 188));
        }
    }
    
}
