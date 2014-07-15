/**
 * 
 * @author Zuxelus (I copied him)
 * 
 */
package shedar.mods.ic2.nuclearcontrol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Packet {
	byte[] data;

	public Packet () {}

	public Packet(byte[] packet) 
	{
		this.data = packet.clone();
	}
}

