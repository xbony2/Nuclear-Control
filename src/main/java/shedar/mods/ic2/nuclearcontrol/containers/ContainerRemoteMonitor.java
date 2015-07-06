package shedar.mods.ic2.nuclearcontrol.containers;


import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.InventoryItem;
import shedar.mods.ic2.nuclearcontrol.SlotFilter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import shedar.mods.ic2.nuclearcontrol.utils.RedstoneHelper;

public class ContainerRemoteMonitor extends Container{

    protected ItemStack is;
    protected InventoryItem item;
    public static TileEntityInfoPanel panel;


    public ContainerRemoteMonitor(InventoryPlayer inv, ItemStack stack, InventoryItem iItem, TileEntityInfoPanel tile, World world){
        this.is = stack;
        this.item = iItem;
        this.panel = tile;
        this.panel.setWorldObj(world);


        this.addSlotToContainer(new SlotFilter(this.item, 0, 177, 21));
        bindPlayerInventory(inv);
    }

    protected static void initData() {
        if(panel.getWorldObj() != null) {
            if (!panel.getWorldObj().isRemote) {
                RedstoneHelper.checkPowered(panel.getWorldObj(), panel);
            }
        }
            if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
                if (panel.screenData == null) {
                    IC2NuclearControl.instance.screenManager.registerInfoPanel(panel);
                } else {
                    panel.setScreen(IC2NuclearControl.instance.screenManager.loadScreen(panel));
                    if (panel.getScreen() != null) {
                        panel.getScreen().init(true, panel.getWorldObj());
                    }
                }
            }
            panel.init = true;
    }
    public static void updateTile(){
        initData();
        panel.updateEntity();
        panel.init = false;
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
