package shedar.mods.ic2.nuclearcontrol.network.message;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityHowlerAlarm;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearNetworkHelper;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketClientSound implements IMessage,
		IMessageHandler<PacketClientSound, IMessage> {
	private int x;
	private int y;
	private int z;
	private byte slot;
	private String soundName;

	public PacketClientSound() {
	}

	public PacketClientSound(int x, int y, int z, byte slot, String soundName) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.slot = slot;
		this.soundName = soundName;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		slot = buf.readByte();
		soundName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeByte(slot);
		ByteBufUtils.writeUTF8String(buf, soundName);
	}

	@Override
	public IMessage onMessage(PacketClientSound message, MessageContext ctx) {
		TileEntity tileEntity = ctx.getServerHandler().playerEntity.worldObj
				.getTileEntity(message.x, message.y, message.z);
		if (tileEntity instanceof TileEntityHowlerAlarm) {
			((TileEntityHowlerAlarm) tileEntity)
					.setSoundName(message.soundName);
		} else if (tileEntity instanceof TileEntityInfoPanel) {
			ItemStack stack = ((TileEntityInfoPanel) tileEntity)
					.getStackInSlot(message.slot);
			if (stack == null || !(stack.getItem() instanceof IPanelDataSource)) {
				return null;
			}
			new CardWrapperImpl(stack, -1).setTitle(message.soundName);
			NuclearNetworkHelper.setSensorCardTitle(
					(TileEntityInfoPanel) tileEntity, message.slot,
					message.soundName);
		}
		return null;
	}
}