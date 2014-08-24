package shedar.mods.ic2.nuclearcontrol.network.message;

import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketClientDisplaySettings implements IMessage, IMessageHandler<PacketClientDisplaySettings, IMessage>
{
	private int x;
	private int y;
	private int z;
	private byte slot;
	private int settings;
	
	public PacketClientDisplaySettings() {}
	
	public PacketClientDisplaySettings(int x, int y, int z, byte slot, int settings)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.slot = slot;
		this.settings = settings;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		slot = buf.readByte();
		settings = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeByte(slot);
		buf.writeInt(settings);
	}
	
    @Override
    public IMessage onMessage(PacketClientDisplaySettings message, MessageContext ctx)
    {
		TileEntity tileEntity = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
		if (tileEntity instanceof TileEntityInfoPanel)
		{
			((TileEntityInfoPanel)tileEntity).setDisplaySettings(message.slot, message.settings);
		}
    	return null;
    }
}
