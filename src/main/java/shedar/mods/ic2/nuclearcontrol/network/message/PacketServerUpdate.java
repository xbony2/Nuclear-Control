package shedar.mods.ic2.nuclearcontrol.network.message;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardBase;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;


public class PacketServerUpdate implements IMessage{

    //private Map<String, Object> tag;
    private ItemStack itemstack;

    PacketServerUpdate(){
        //DO NOTHING...
    }

    public PacketServerUpdate(ItemStack card){
        this.itemstack = card;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, itemstack);

    }

    public static class Handler implements IMessageHandler<PacketServerUpdate, IMessage> {

        @Override
        public IMessage onMessage(PacketServerUpdate message, MessageContext ctx) {
            ItemStack stack = message.itemstack;
            if(stack.getItem() instanceof ItemCardBase){
                CardWrapperImpl helper = new CardWrapperImpl(stack, -1);
                return new PacketClientRemoteMonitor(helper.getUpdateSet());
            }
            return null;
        }
    }
}
