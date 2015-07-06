package shedar.mods.ic2.nuclearcontrol.network.message;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.api.IRemoteSensor;
import shedar.mods.ic2.nuclearcontrol.containers.ContainerRemoteMonitor;
import shedar.mods.ic2.nuclearcontrol.crossmod.RF.ItemCardRFEnergyLocation;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardBase;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;


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
                this.processCard(stack, 10, 0, ContainerRemoteMonitor.panel);
                return new PacketClientRemoteMonitor(helper.getUpdateSet());
            }
            return null;
        }

        private void processCard(ItemStack card, int upgradeCountRange, int slot, TileEntity panel) {
            if (card == null) {
                return;
            }
            Item item = card.getItem();
            if (item instanceof IPanelDataSource) {
                boolean needUpdate = true;
                if (upgradeCountRange > 7) {
                    upgradeCountRange = 7;
                }
                int range = 100 * (int) Math.pow(2, upgradeCountRange);
                CardWrapperImpl cardHelper = new CardWrapperImpl(card, slot);

                if (item instanceof IRemoteSensor) {
                    ChunkCoordinates target = cardHelper.getTarget();
                    if (target == null) {
                        needUpdate = false;
                        cardHelper.setState(CardState.INVALID_CARD);
                    } else {
                            cardHelper.setState(CardState.OUT_OF_RANGE);
                    }
                }
                if (needUpdate) {
                    CardState state = null;
                    if(item instanceof ItemCardRFEnergyLocation){
                        state = ((ItemCardRFEnergyLocation) item).update(MinecraftServer.getServer().worldServers[0], cardHelper, range);
                    }else {
                        state = ((IPanelDataSource) item).update(panel, cardHelper, range);
                    }
                    cardHelper.setInt("state", state.getIndex());
                }
            }
        }
    }
}
