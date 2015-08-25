package shedar.mods.ic2.nuclearcontrol.network.message;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.containers.ContainerRemoteMonitor;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardBase;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;

/**
 * Created by David on 8/24/2015.
 */
public class PacketClientUpdate implements IMessage {

    //private Map<String, Object> tag;
    public int x;
    public int y;
    public int z;

    public PacketClientUpdate(){
        //DO NOTHING...
    }

    public PacketClientUpdate(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);

    }

    public static class Handler implements IMessageHandler<PacketClientUpdate, IMessage> {

        @Override
        public IMessage onMessage(PacketClientUpdate message, MessageContext ctx) {
            World world = MinecraftServer.getServer().worldServers[0];
            TileEntity tile = world.getTileEntity(message.x, message.y, message.z);
            tile.getDescriptionPacket();
            tile.markDirty();
            return null;
        }
    }
}
