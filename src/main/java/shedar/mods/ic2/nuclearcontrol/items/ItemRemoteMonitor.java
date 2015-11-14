package shedar.mods.ic2.nuclearcontrol.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.InventoryItem;
import shedar.mods.ic2.nuclearcontrol.gui.GuiRemoteMonitor;

public class ItemRemoteMonitor extends Item{

    private IIcon base, card;

    public ItemRemoteMonitor(){
        this.setCreativeTab(IC2NuclearControl.tabIC2NC);

    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if(!player.isSneaking()) {
            player.openGui(IC2NuclearControl.instance, GuiRemoteMonitor.REMOTEMONITOR_GUI, world, 0, 0, 0);
        }
        return stack;
    }
    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 1;
    }

    public boolean requiresMultipleRenderPasses(){
        return true;
    }

 	public int getRenderPasses(int k){
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister){
        card = iconRegister.registerIcon("nuclearcontrol:remoteMonitorCard");
        base = iconRegister.registerIcon("nuclearcontrol:remoteMonitor");
    }


    public IIcon getIcon(ItemStack stack, int pass){
        if (pass == 0){
            return base;
        }
        else {
            InventoryItem inventoryItem = new InventoryItem(stack);
            if (inventoryItem.getStackInSlot(0) != null){
                this.setCardColor(inventoryItem.getStackInSlot(0).getItem());
                return card;
            }
            return base;
        }
    }

    private void setCardColor(Item stack){
        if(stack instanceof ItemCardEnergySensorLocation){
            GL11.glColor4f(255, 0, 0, 1);
        } else if(stack instanceof ItemCardText){
            GL11.glColor4f(0, 255, 0, 1);
        }else if(stack instanceof ItemTimeCard){
            GL11.glColor4f(255,255,51,1);
        }else{
            GL11.glColor4f(0,0,255,1);
        }
    }
}
