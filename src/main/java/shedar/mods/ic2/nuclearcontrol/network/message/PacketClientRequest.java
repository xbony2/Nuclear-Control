package shedar.mods.ic2.nuclearcontrol.network.message;

import shedar.mods.ic2.nuclearcontrol.utils.NuclearNetworkHelper;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketClientRequest implements IMessage,
		IMessageHandler<PacketClientRequest, IMessage> {
	private int x;
	private int y;
	private int z;

	public PacketClientRequest() {
	}

	public PacketClientRequest(int x, int y, int z) {
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

	@Override
	public IMessage onMessage(PacketClientRequest message, MessageContext ctx) {
		NuclearNetworkHelper.sendDisplaySettingsToPlayer(message.x, message.y,
				message.z, ctx.getServerHandler().playerEntity);
		return null;
	}
}
