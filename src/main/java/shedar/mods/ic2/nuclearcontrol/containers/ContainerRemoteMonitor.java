package shedar.mods.ic2.nuclearcontrol.containers;


import buildcraft.robots.ItemRobot;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.server.FMLServerHandler;
import li.cil.oc.api.driver.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.InventoryItem;
import shedar.mods.ic2.nuclearcontrol.SlotFilter;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.items.ItemRemoteMonitor;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import shedar.mods.ic2.nuclearcontrol.utils.NCLog;

public class ContainerRemoteMonitor extends Container{

    protected ItemStack is;
    protected InventoryItem item;

    public ContainerRemoteMonitor(InventoryPlayer inv, ItemStack stack, InventoryItem iItem){
        this.is = stack;
        this.item = iItem;

        this.addSlotToContainer(new SlotFilter(this.item, 0, 177, 21));
        bindPlayerInventory(inv);
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        /*for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }*/

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot){
        ItemStack stack = null;
        Slot slots = (Slot)this.inventorySlots.get(slot);

        if (slots.getStack() != null) {
            if(slots.getStack().getItem() == IC2NuclearControl.itemRemoteMonitor){
                return null;
            }
        }

        if (slots != null && slots.getHasStack()) {
            ItemStack itemstackR = slots.getStack();
            stack = itemstackR.copy();

            if (slot == 0) {
                boolean fixed = false;
               for(int h=1; h < 10; h++){
                   Slot know = (Slot)this.inventorySlots.get(h);
                   if(!know.getHasStack()){
                       know.putStack(slots.getStack());
                       slots.decrStackSize(1);
                       fixed = true;
                   }
               }
                if(!fixed){
                    return null;
                }
                slots.onSlotChange(itemstackR, stack);
            } else if(slots.getStack().getItem() instanceof IPanelDataSource && !((Slot)this.inventorySlots.get(0)).getHasStack()){
                ((Slot)this.inventorySlots.get(0)).putStack(itemstackR);
                slots.decrStackSize(1);
                slots.onSlotChange(itemstackR, stack);
                ((Slot)this.inventorySlots.get(0)).onSlotChanged();
            }else{
                return null;
            }
        }
        return stack;
    }


    @Override
    public ItemStack slotClick(int slot, int button, int flag, EntityPlayer player) {
        if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == player.getHeldItem()) {
            return null;
        }
        return super.slotClick(slot, button, flag, player);
    }
}

