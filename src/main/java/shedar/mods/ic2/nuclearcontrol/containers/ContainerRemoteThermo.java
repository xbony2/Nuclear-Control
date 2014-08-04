package shedar.mods.ic2.nuclearcontrol.containers;

import shedar.mods.ic2.nuclearcontrol.SlotFilter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityRemoteThermo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerRemoteThermo extends Container
{
    public TileEntityRemoteThermo remoteThermo;
    private EntityPlayer player;
    private double lastEnergy = -1;

    public ContainerRemoteThermo(EntityPlayer player, TileEntityRemoteThermo remoteThermo)
    {
        super();
        
        this.remoteThermo = remoteThermo;
        this.player = player; 
        
        //energy charger
        addSlotToContainer(new SlotFilter(remoteThermo, 0, 13, 53));
        
        //upgrades
        addSlotToContainer(new SlotFilter(remoteThermo, 1, 190, 8));
        addSlotToContainer(new SlotFilter(remoteThermo, 2, 190, 26));
        addSlotToContainer(new SlotFilter(remoteThermo, 3, 190, 44));
        addSlotToContainer(new SlotFilter(remoteThermo, 4, 190, 62));

        //inventory
        for (int i = 0; i < 3; i++)
        {
            for (int k = 0; k < 9; k++)
            {
                addSlotToContainer(new Slot(player.inventory, k + i * 9 + 9, 27 + k * 18, 84 + i * 18));
            }
        }

        for (int j = 0; j < 9; j++)
        {
            addSlotToContainer(new Slot(player.inventory, j, 27 + j * 18, 142));
        }
    }


    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        int energy = (int)remoteThermo.energy; // ? (says Zuxelus)
        for (int i = 0; i < crafters.size(); i++)
        {
            ICrafting crafting = (ICrafting)crafters.get(i);

            if (lastEnergy != energy)
            {
                crafting.sendProgressBarUpdate(this, 0, energy);
            }
        }
        lastEnergy = energy;
    }
    
    public void updateProgressBar(int type, int value) 
    {
        if(type == 0)
        {
            remoteThermo.setEnergy(value);
        }
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer var1)
    {
        return remoteThermo.isUseableByPlayer(player);
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
                if(slotId < remoteThermo.getSizeInventory())//moving from thermo to inventory
                {
                    mergeItemStack(items, remoteThermo.getSizeInventory(), inventorySlots.size(), false);
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
                else//moving from inventory to thermo
                {
                    for(int i=0;i<remoteThermo.getSizeInventory();i++)
                    {
                        if(!remoteThermo.isItemValid(i, items))
                        {
                            continue;
                        }
                        ItemStack targetStack = remoteThermo.getStackInSlot(i);
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
