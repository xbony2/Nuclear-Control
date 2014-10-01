package shedar.mods.ic2.nuclearcontrol.network.message;

import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketClientColor implements IMessage,
		IMessageHandler<PacketClientColor, IMessage> {
	private int x;
	private int y;
	private int z;
	private int colors;

	public PacketClientColor() {
	}

	public PacketClientColor(int x, int y, int z, int colors) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.colors = colors;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		colors = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(colors);
	}

	@Override
	public IMessage onMessage(PacketClientColor message, MessageContext ctx) {
		TileEntity tileEntity = ctx.getServerHandler().playerEntity.worldObj
				.getTileEntity(message.x, message.y, message.z);
		if (tileEntity instanceof TileEntityInfoPanel) {
			int back = message.colors >> 4;
			int text = message.colors & 0xf;
			((TileEntityInfoPanel) tileEntity).setColorBackground(back);
			((TileEntityInfoPanel) tileEntity).setColorText(text);
		}
		return null;
	}
}
