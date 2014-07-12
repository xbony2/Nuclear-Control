package shedar.mods.ic2.nuclearcontrol;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
//import net.minecraft.network.INetworkManager;
//import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
//import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;
import shedar.mods.ic2.nuclearcontrol.panel.http.HttpCardSender;
import shedar.mods.ic2.nuclearcontrol.subblocks.Subblock;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityHowlerAlarm;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityRangeTrigger;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearNetworkHelper;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
//import cpw.mods.fml.common.IScheduledTickHandler;
//import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.IGuiHandler;
//import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy implements IGuiHandler/*, IScheduledTickHandler*/{
    public boolean isPlaying(String soundId){
        return false;
    }
    
    public void stopAlarm(String soundId){}
    
    public String playAlarm(double x, double y, double z, String name, float volume){
        return null;
    }
    
    public void registerTileEntities(){
        GameRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityIC2Thermo.class, "IC2Thermo");
        GameRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityHowlerAlarm.class, "IC2HowlerAlarm");
        GameRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityIndustrialAlarm.class, "IC2IndustrialAlarm");
        GameRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityRemoteThermo.class, "IC2RemoteThermo");
        GameRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel.class, "IC2NCInfoPanel");
        GameRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanelExtender.class, "IC2NCInfoPanelExtender");
        GameRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanel.class, "IC2NCAdvancedInfoPanel");
        GameRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanelExtender.class, "IC2NCAdvancedInfoPanelExtender");
        GameRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityEnergyCounter.class, "IC2NCEnergyCounter");
        GameRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter.class, "IC2NCAverageCounter");
        GameRegistry.registerTileEntity(shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityRangeTrigger.class, "IC2NCRangeTrigger");
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        Subblock block = IC2NuclearControl.instance.blockNuclearControlMain.getSubblock(ID);
        if(block == null)
            return null;
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return block.getServerGuiElement(tileEntity, player);
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event){
        IC2NuclearControl.instance.screenManager.clearWorld(event.world);
    }
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, EntityPlayer player){
        if (player instanceof EntityPlayerMP){//server
            // used to set sound alarm from client's GUI
            ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
            byte packetId = dat.readByte();
            int x = dat.readInt();
            int y = dat.readInt();
            int z = dat.readInt();
            switch (packetId){
            case PacketHandler.PACKET_CLIENT_SOUND:
                byte slot = dat.readByte();
                String soundName = dat.readUTF();
                TileEntity tileEntity = ((EntityPlayerMP) player).worldObj.getTileEntity(x, y, z);
                if (tileEntity instanceof TileEntityHowlerAlarm){
                    ((TileEntityHowlerAlarm) tileEntity).setSoundName(soundName);
                }else if (tileEntity instanceof TileEntityInfoPanel){
                    ItemStack stack = ((TileEntityInfoPanel) tileEntity).getStackInSlot(slot);
                    if (stack == null || !(stack.getItem() instanceof IPanelDataSource)){
                        return;
                    }
                    new CardWrapperImpl(stack, -1).setTitle(soundName);
                    NuclearNetworkHelper.setSensorCardTitle((TileEntityInfoPanel)tileEntity, slot, soundName);
                }
                break;
            case PacketHandler.PACKET_CLIENT_REQUEST:
                NuclearNetworkHelper.sendDisplaySettingsToPlayer(x, y, z, (EntityPlayerMP)player);
                break;
            case PacketHandler.PACKET_CLIENT_RANGE_TRIGGER:
                long value = dat.readLong();
                boolean isEnd = dat.readBoolean();
                tileEntity = ((EntityPlayerMP) player).worldObj.getTileEntity(x, y, z);
                if (tileEntity instanceof TileEntityRangeTrigger){
                    if(isEnd){
                        ((TileEntityRangeTrigger)tileEntity).setLevelEnd(value);
                    }
                    else{
                        ((TileEntityRangeTrigger)tileEntity).setLevelStart(value);
                    }
                }
                break;
            case PacketHandler.PACKET_CLIENT_COLOR:
                int colors= dat.readInt();
                tileEntity = ((EntityPlayerMP) player).worldObj.getTileEntity(x, y, z);
                if (tileEntity instanceof TileEntityInfoPanel){
                    int back = colors >> 4;
                    int text = colors & 0xf;
                    ((TileEntityInfoPanel)tileEntity).setColorBackground(back);
                    ((TileEntityInfoPanel)tileEntity).setColorText(text);
                }
                break;
            case PacketHandler.PACKET_CLIENT_DISPLAY_SETTINGS:
                slot = dat.readByte();
                int settings = dat.readInt();
                tileEntity = ((EntityPlayerMP) player).worldObj.getTileEntity(x, y, z);
                if (tileEntity instanceof TileEntityInfoPanel){
                    ((TileEntityInfoPanel)tileEntity).setDisplaySettings(slot, settings);
                }
                break;
            case PacketHandler.PACKET_CLIENT_SENSOR:
                tileEntity = ((EntityPlayerMP) player).worldObj.getTileEntity(x, y, z);
                if (tileEntity instanceof TileEntityInfoPanel){
                    TileEntityInfoPanel panel = (TileEntityInfoPanel) tileEntity;
                    slot = dat.readByte();
                    ItemStack stack = panel.getStackInSlot(slot);
                    if (stack == null || !(stack.getItem() instanceof IPanelDataSource)){
                        return;
                    }
                    String className = dat.readUTF();
                    if(!stack.getItem().getClass().getName().equals(className)){
                        FMLLog.warning("Class mismatch: '%s'!='%s'", className, stack.getItem().getClass().getName());
                        return;
                    }
                    CardWrapperImpl helper = new CardWrapperImpl(stack, slot);
                    int fieldCount =  dat.readShort();
                    for(int i=0; i<fieldCount; i++){
                        String name = dat.readUTF();
                        byte type = dat.readByte();
                        switch (type){
                        case NuclearNetworkHelper.FIELD_INT:
                            helper.setInt(name, dat.readInt());
                            break;
                        case NuclearNetworkHelper.FIELD_BOOLEAN:
                            helper.setBoolean(name, dat.readBoolean());
                            break;
                        case NuclearNetworkHelper.FIELD_LONG:
                            helper.setLong(name, dat.readLong());
                            break;
                        case NuclearNetworkHelper.FIELD_STRING:
                            helper.setString(name, dat.readUTF());
                            break;
                        default:
                            FMLLog.warning("Invalid field type: %d", type);
                            break;
                        }
                    }
                    helper.commit(panel);
                }
                break;

            default:
                break;
            }
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        // null on server
        return null;
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData){
        // do nothing
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData){
        if(IC2NuclearControl.instance.isHttpSensorAvailable)
            HttpCardSender.instance.send();
    }

    @Override
    public EnumSet<TickType> ticks(){
        return EnumSet.of(TickType.SERVER);
    }

    @Override
    public String getLabel(){
        return "Nuclear Control sensor sender";
    }

    @Override
    public int nextTickSpacing(){
        return 20*20;
    }

}
