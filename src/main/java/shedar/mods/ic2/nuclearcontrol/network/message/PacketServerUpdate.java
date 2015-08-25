package shedar.mods.ic2.nuclearcontrol.network.message;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.containers.ContainerRemoteMonitor;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardBase;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;
import shedar.mods.ic2.nuclearcontrol.utils.NCLog;


public class PacketServerUpdate implements IMessage{

    //private Map<String, Object> tag;
    public ItemStack itemstack;

    public PacketServerUpdate(){
        //DO NOTHING...
    }

    public PacketServerUpdate(ItemStack card){
        this.itemstack = card;
        //NCLog.error(card);
    }
    @Override
    public void fromBytes(ByteBuf buf) {
       itemstack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, itemstack);

    }

    public static class Handler implements IMessageHandler<PacketServerUpdate, IMessage> {

        @Override
        public IMessage onMessage(PacketServerUpdate message, MessageContext ctx) {
            ItemStack stack = message.itemstack;
           // NCLog.error(stack);
            if(stack != null && stack.getItem() instanceof ItemCardBase){
                CardWrapperImpl helper = new CardWrapperImpl(stack, 0);
                CardState state = ((IPanelDataSource) stack.getItem()).update(ContainerRemoteMonitor.panel, helper, 100);
                return new PacketClientRemoteMonitor(helper.getUpdateSet());
            }
            return null;
        }
    }
}
