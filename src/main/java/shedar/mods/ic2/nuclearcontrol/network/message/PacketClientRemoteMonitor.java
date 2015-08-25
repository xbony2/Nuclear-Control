package shedar.mods.ic2.nuclearcontrol.network.message;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.InventoryItem;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.items.ItemRemoteMonitor;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearNetworkHelper;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by David on 12/06/2015.
 */
public class PacketClientRemoteMonitor implements IMessage {

    private Map<String, Object> fields;

    public static final int FIELD_DOUBLE = 1;
    public static final int FIELD_INT = 2;
    public static final int FIELD_STRING = 3;
    public static final int FIELD_BOOLEAN = 4;
    public static final int FIELD_TAG = 5;
    public static final int FIELD_NULL = 6;
    public static final int FIELD_LONG = 7;

    public PacketClientRemoteMonitor() {
    }

    public PacketClientRemoteMonitor(Map<String, Object> updateSet) {
        this.fields = updateSet;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int fieldCount = buf.readShort();
        fields = new HashMap<String, Object>();
        for (int i = 0; i < fieldCount; i++) {
            String name = ByteBufUtils.readUTF8String(buf);
            byte type = buf.readByte();
            switch (type) {
                case NuclearNetworkHelper.FIELD_INT:
                    fields.put(name, buf.readInt());
                    break;
                case NuclearNetworkHelper.FIELD_BOOLEAN:
                    fields.put(name, buf.readBoolean());
                    break;
                case NuclearNetworkHelper.FIELD_LONG:
                    fields.put(name, buf.readLong());
                    break;
                case NuclearNetworkHelper.FIELD_DOUBLE:
                    fields.put(name, buf.readDouble());
                    break;
                case NuclearNetworkHelper.FIELD_STRING:
                    fields.put(name, ByteBufUtils.readUTF8String(buf));
                    break;
                case NuclearNetworkHelper.FIELD_TAG:
                    NBTTagCompound tag;
                    try {
                        int size = buf.readInt();
                        DataInputStream dat = new DataInputStream(
                                new ByteArrayInputStream(Arrays.copyOfRange(
                                        buf.array(), buf.readerIndex() + 1,
                                        buf.readerIndex() + 1 + size)));
                        tag = CompressedStreamTools.readCompressed(dat);
                        fields.put(name, tag);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case NuclearNetworkHelper.FIELD_NULL:
                    fields.put(name, null);
                    break;
                default:
                    IC2NuclearControl.logger.warn("Invalid field type: %d", type);
                    break;
            }
        }


    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeShort(fields.size());
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            ByteBufUtils.writeUTF8String(buf, entry.getKey());
            Object value = entry.getValue();
            if (value instanceof Long) {
                buf.writeByte(FIELD_LONG);
                buf.writeLong((Long) value);
            } else if (value instanceof Double) {
                buf.writeByte(FIELD_DOUBLE);
                buf.writeDouble((Double) value);
            } else if (value instanceof Integer) {
                buf.writeByte(FIELD_INT);
                buf.writeInt((Integer) value);
            } else if (value instanceof String) {
                buf.writeByte(FIELD_STRING);
                ByteBufUtils.writeUTF8String(buf, (String) value);
            } else if (value instanceof Boolean) {
                buf.writeByte(FIELD_BOOLEAN);
                buf.writeBoolean((Boolean) value);
            } else if (value instanceof NBTTagCompound) {
                buf.writeByte(FIELD_TAG);
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    DataOutputStream output = new DataOutputStream(stream);
                    CompressedStreamTools.writeCompressed(
                            (NBTTagCompound) value, output);
                    buf.writeInt(stream.size());
                    buf.writeBytes(stream.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (value == null) {
                buf.writeByte(FIELD_NULL);
            }
        }

    }
    public static class Handler implements IMessageHandler<PacketClientRemoteMonitor, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketClientRemoteMonitor message, MessageContext ctx) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if(player.getHeldItem().getItem() == IC2NuclearControl.itemRemoteMonitor){
                //ItemRemoteMonitor remote = (ItemRemoteMonitor) player.getHeldItem().getItem();
                InventoryItem itemInv = new InventoryItem(player.getHeldItem());
                if(itemInv.getStackInSlot(0) == null || !(itemInv.getStackInSlot(0).getItem() instanceof IPanelDataSource)) {
                    return null;
                }
                CardWrapperImpl helper = new CardWrapperImpl(itemInv.getStackInSlot(0), 0);
                for (Map.Entry<String, Object> entry : message.fields.entrySet()) {
                    String name = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof Long) {
                        helper.setLong(name, (Long) value);
                    } else if (value instanceof Double) {
                        helper.setDouble(name, (Double) value);
                    } else if (value instanceof Integer) {
                        helper.setInt(name, (Integer) value);
                    } else if (value instanceof String) {
                        helper.setString(name, (String) value);
                    } else if (value instanceof Boolean) {
                        helper.setBoolean(name, (Boolean) value);
                    } else if (value instanceof NBTTagCompound) {
                        helper.setTag(name, (NBTTagCompound) value);
                    } else if (value == null) {
                        helper.clearField(name);
                    }
                }
            }

            return null;
        }
    }
}
