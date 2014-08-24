package shedar.mods.ic2.nuclearcontrol.network.message;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSensorTitle implements IMessage, IMessageHandler<PacketSensorTitle, IMessage>
{
	private int x;
	private int y;
	private int z;
	private byte slot;
	private String title;
	
	public PacketSensorTitle() {}
	
	public PacketSensorTitle(int x, int y, int z, byte slot, String title)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.slot = slot;
		this.title = title;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		slot = buf.readByte();
		title = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeByte(slot);
		ByteBufUtils.writeUTF8String(buf, title);
	}
	
    @Override
    public IMessage onMessage(PacketSensorTitle message, MessageContext ctx)
    {
		TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x, message.y, message.z);
		if (tileEntity == null || !(tileEntity instanceof TileEntityInfoPanel))
		{
			return null;
		}
		TileEntityInfoPanel panel = (TileEntityInfoPanel)tileEntity;
		ItemStack itemStack = panel.getStackInSlot(message.slot);
		if(itemStack == null || !(itemStack.getItem() instanceof IPanelDataSource))
		{
			return null;
		}
		new CardWrapperImpl(itemStack, message.slot).setTitle(message.title);
		panel.resetCardData();
    	return null;
    }
}