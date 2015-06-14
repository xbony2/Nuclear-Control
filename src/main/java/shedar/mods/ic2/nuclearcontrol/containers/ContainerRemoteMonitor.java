package shedar.mods.ic2.nuclearcontrol.containers;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.InventoryItem;
import shedar.mods.ic2.nuclearcontrol.SlotFilter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;

public class ContainerRemoteMonitor extends Container{

    protected ItemStack is;
    protected InventoryItem item;
    public static TileEntityInfoPanel panel;

    public ContainerRemoteMonitor(InventoryPlayer inv, ItemStack stack, InventoryItem iItem, TileEntityInfoPanel tile){
        this(inv, stack, iItem, tile, MinecraftServer.getServer().getEntityWorld());
    }
    public ContainerRemoteMonitor(InventoryPlayer inv, ItemStack stack, InventoryItem iItem, TileEntityInfoPanel tile, World world){
        this.is = stack;
        this.item = iItem;
        this.panel = tile;
        if(IC2NuclearControl.isServer){
            this.panel.setWorldObj(world);
        }else{
            this.panel.setWorldObj(MinecraftServer.getServer().getEntityWorld());
        }

        this.addSlotToContainer(new SlotFilter(this.item, 0, 177, 21));
        bindPlayerInventory(inv);
    }
    public static void updateTile(){
        panel.updateEntity();
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
    public ItemStack slotClick(int slot, int button, int flag, EntityPlayer player) {
        if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == player.getHeldItem()) {
            return null;
        }
        return super.slotClick(slot, button, flag, player);
    }
}
