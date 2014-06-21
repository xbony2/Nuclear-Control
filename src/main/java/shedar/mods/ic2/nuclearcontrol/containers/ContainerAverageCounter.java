package shedar.mods.ic2.nuclearcontrol.containers;

import shedar.mods.ic2.nuclearcontrol.SlotFilter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearNetworkHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAverageCounter extends Container
{
    public TileEntityAverageCounter averageCounter;
    private EntityPlayer player;
    private long lastAverage = -1;

    public ContainerAverageCounter(EntityPlayer player, TileEntityAverageCounter averageCounter)
    {
        super();
        
        this.averageCounter = averageCounter;
        this.player = player; 
        
        //transformer upgrades
        addSlotToContainer(new SlotFilter(averageCounter, 0, 8, 18));
        
        //inventory
        for (int i = 0; i < 3; i++)
        {
            for (int k = 0; k < 9; k++)
            {
                addSlotToContainer(new Slot(player.inventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }
        }

        for (int j = 0; j < 9; j++)
        {
            addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 142));
        }
    }

    
    public void addCraftingToCrafters(ICrafting crafting)
    {
        super.addCraftingToCrafters(crafting);
        NuclearNetworkHelper.sendAverageCounterValue(averageCounter, crafting, averageCounter.getClientAverage());
    }    

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        int average = averageCounter.getClientAverage();
        for (int i = 0; i < crafters.size(); i++)
        {
            ICrafting crafting = (ICrafting)crafters.get(i);

            if (lastAverage != average)
            {
                NuclearNetworkHelper.sendAverageCounterValue(averageCounter, crafting, average);
            }
        }
        lastAverage = average;
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer var1)
    {
        return averageCounter.isUseableByPlayer(player);
    }
    
    @Override
    public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer player)
    {
        ItemStack stack = super.slotClick(par1, par2, par3, player);
        averageCounter.onInventoryChanged();
        return stack;
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer p, int slotId)
    {
        Slot slot = (Slot)this.inventorySlots.get(slotId);
        if(slot!=null)
        {
            ItemStack items = slot.getStack();
            if(items!=null)
            {
                int initialCount = items.stackSize;
                if(slotId < averageCounter.getSizeInventory())//moving from counter to inventory
                {
                    mergeItemStack(items, averageCounter.getSizeInventory(), inventorySlots.size(), false);
                    if (items.stackSize == 0)
                    {
                        slot.putStack((ItemStack)null);
                    }
                    else
                    {
                        slot.onSlotChanged();
                        if(initialCount!=items.stackSize)
                            return items;
                    }
                }
                else//moving from inventory to counter
                {
                    for(int i=0;i<averageCounter.getSizeInventory();i++)
                    {
                        if(!averageCounter.isItemValid(i, items))
                        {
                            continue;
                        }
                        ItemStack targetStack = averageCounter.getStackInSlot(i);
                        if(targetStack == null)
                        {
                            Slot targetSlot = (Slot)this.inventorySlots.get(i);
                            targetSlot.putStack(items);
                            slot.putStack((ItemStack)null);
                            break;
                        }
                        else if(items.isStackable() && items.isItemEqual(targetStack))
                        {
                            mergeItemStack(items, i, i+1, false);
                            if (items.stackSize == 0)
                            {
                                slot.putStack((ItemStack)null);
                            }
                            else
                            {
                                slot.onSlotChanged();
                                if(initialCount!=items.stackSize)
                                    return items;
                            }
                            break;
                        }
                        
                    }
                }
            }
        }
        return null;
    }
    
}
