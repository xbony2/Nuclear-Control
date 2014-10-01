package shedar.mods.ic2.nuclearcontrol.network.message;

import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityRangeTrigger;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketClientRangeTrigger implements IMessage,
		IMessageHandler<PacketClientRangeTrigger, IMessage> {
	private int x;
	private int y;
	private int z;
	private double value;
	private boolean isEnd;

	public PacketClientRangeTrigger() {
	}

	public PacketClientRangeTrigger(int x, int y, int z, double value,
			boolean isEnd) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.value = value;
		this.isEnd = isEnd;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		value = buf.readDouble();
		isEnd = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeDouble(value);
		buf.writeBoolean(isEnd);
	}

	@Override
	public IMessage onMessage(PacketClientRangeTrigger message,
			MessageContext ctx) {
		TileEntity tileEntity = ctx.getServerHandler().playerEntity.worldObj
				.getTileEntity(message.x, message.y, message.z);
		if (tileEntity instanceof TileEntityRangeTrigger) {
			if (message.isEnd) {
				((TileEntityRangeTrigger) tileEntity)
						.setLevelEnd(message.value);
			} else {
				((TileEntityRangeTrigger) tileEntity)
						.setLevelStart(message.value);
			}
		}
		return null;
	}
}
