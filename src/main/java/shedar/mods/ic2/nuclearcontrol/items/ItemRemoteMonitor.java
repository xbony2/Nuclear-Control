package shedar.mods.ic2.nuclearcontrol.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.gui.GuiRemoteMonitor;

public class ItemRemoteMonitor extends Item{

    public ItemRemoteMonitor(){
        this.setCreativeTab(IC2NuclearControl.tabIC2NC);

    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        player.openGui(IC2NuclearControl.instance, GuiRemoteMonitor.REMOTEMONITOR_GUI, world, 0, 0, 0);
        return stack;
    }
    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 1;
    }

}
