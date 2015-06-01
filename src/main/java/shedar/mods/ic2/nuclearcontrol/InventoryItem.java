package shedar.mods.ic2.nuclearcontrol;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardBase;
import shedar.mods.ic2.nuclearcontrol.items.ItemRemoteMonitor;

public class InventoryItem implements IInventory, ISlotItemFilter{
    private String name = "Inventory Item";


    public static final int INV_SIZE = 1;
    private ItemStack[] inventory = new ItemStack[INV_SIZE];

    /** Provides NBT Tag Compound to reference */
    private final ItemStack invItem;

    public InventoryItem(ItemStack stack) {
        this.invItem = stack;
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        readFromNBT(stack.getTagCompound());
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        ItemStack stack = getStackInSlot(slot);
        if(stack != null)
        {
            if(stack.stackSize > amount)
            {
                stack = stack.splitStack(amount);
                markDirty();
            }
            else
            {
                setInventorySlotContents(slot, null);
            }
        }
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if(stack != null)
        {
            setInventorySlotContents(slot, null);
        }
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        this.inventory[slot] = itemstack;

        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
        {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return name;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return name.length() > 0;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }


    @Override
    public void markDirty() {
        for (int i = 0; i < getSizeInventory(); ++i)
        {
            if (getStackInSlot(i) != null && getStackInSlot(i).stackSize == 0)
                inventory[i] = null;
        }
        writeToNBT(invItem.getTagCompound());
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getHeldItem() == invItem;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {
        markDirty();
    }

    @Override
    public boolean isItemValid(int slotIndex, ItemStack itemStack) {
        return !(itemStack.getItem() instanceof ItemRemoteMonitor) && (itemStack.getItem() instanceof ItemCardBase);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return !(itemstack.getItem() instanceof ItemRemoteMonitor) && (itemstack.getItem() instanceof ItemCardBase);
    }

    public void readFromNBT(NBTTagCompound compound) {
        NBTTagList items = compound.getTagList("ItemInventory", compound.getId());
        for (int i = 0; i < items.tagCount(); ++i) {
            NBTTagCompound item = items.getCompoundTagAt(i);
            byte slot = item.getByte("Slot");
            if (slot >= 0 && slot < getSizeInventory()) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(item);
            }
        }
    }


    public void writeToNBT(NBTTagCompound tagcompound)
    {
        NBTTagList items = new NBTTagList();

        for (int i = 0; i < getSizeInventory(); ++i)
        {
            if (getStackInSlot(i) != null)
            {
                NBTTagCompound item = new NBTTagCompound();
                item.setInteger("Slot", i);
                getStackInSlot(i).writeToNBT(item);
                items.appendTag(item);
            }
        }
        tagcompound.setTag("ItemInventory", items);
    }


}