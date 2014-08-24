package shedar.mods.ic2.nuclearcontrol.network.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.util.ChatComponentText;
import shedar.mods.ic2.nuclearcontrol.ClientProxy;
import shedar.mods.ic2.nuclearcontrol.utils.LanguageHelper;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketChat implements IMessage, IMessageHandler<PacketChat, IMessage>
{
	private String message;
	
	public PacketChat() {}
	
	public PacketChat(String message)
	{
		this.message = message;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		message = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, message);
	}
	
    @Override
    public IMessage onMessage(PacketChat messages, MessageContext ctx)
    {
		String[] chunks = messages.message.split(":");
		messages.message = LanguageHelper.translate("msg.nc." + chunks[0]);
		if (chunks.length > 1)
		{
			List<String> list = new ArrayList<String>(Arrays.asList(chunks));
			list.remove(0);
			chunks = list.toArray(chunks);
			messages.message = String.format(messages.message, (Object[])chunks);
		}
		ClientProxy.getPlayer().addChatComponentMessage(new ChatComponentText(messages.message));
    	return null;
    }
}
