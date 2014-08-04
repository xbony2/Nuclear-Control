package shedar.mods.ic2.nuclearcontrol.tileentities;

import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.network.NetworkHelper;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.network.NetworkManager;

import java.util.List;
import java.util.Vector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.IRedstoneConsumer;
import shedar.mods.ic2.nuclearcontrol.utils.Damages;
import shedar.mods.ic2.nuclearcontrol.utils.RedstoneHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

public class TileEntityHowlerAlarm extends TileEntity implements 
	INetworkDataProvider, INetworkUpdateListener, IWrenchable, IRedstoneConsumer, INetworkClientTileEntityEventListener{
	private static final String DEFAULT_SOUND_NAME = "default";
	private static final float BASE_SOUND_RANGE = 16F;
	private static final String SOUND_PREFIX = "nuclearcontrol:alarm-";

	private boolean init;
	private boolean soundReceived;

	private short prevFacing;
	public short facing;

	public int range;
	private int prevRange;

	public boolean powered;
	private boolean prevPowered;

	public String soundName;
	private String prevSoundName;

	private int updateTicker;
	protected int tickRate;
	private String soundId;

	public TileEntityHowlerAlarm(){
		facing = 0;
		prevFacing = 0;
		init = false;
		tickRate = 2;
		updateTicker = 0;
		powered = false;
		prevPowered = false;
		soundName = ""; 
		range = IC2NuclearControl.instance.alarmRange;
		soundReceived = false;
	}

	private void initData(){
		if (!worldObj.isRemote){
			RedstoneHelper.checkPowered(worldObj, this);
		}
		if (FMLCommonHandler.instance().getEffectiveSide().isServer() && "".equals(soundName)){
			setSoundName(DEFAULT_SOUND_NAME);
		}
		init = true;
	}

	public int getRange(){
		return range;
	}

	public void setRange(int r){
		range = r;
		if (prevRange != r){
			//NetworkHelper.updateTileEntityField(this, "range");
			((NetworkManager)IC2.network.get()).updateTileEntityField(this, "range");
		}
		prevRange = range;
	}    

	public String getSoundName(){
		return soundName;
	}

	public void setSoundName(String name){
		soundName = name;
		if (prevSoundName != name){
			//NetworkHelper.updateTileEntityField(this, "soundName");
			((NetworkManager)IC2.network.get()).updateTileEntityField(this, "soundName");
		}
		prevSoundName = name;
	}    

	public void onNetworkEvent(EntityPlayer entityplayer, int i){
		setRange(i);
	}

	@Override
	public short getFacing(){
		return (short)Facing.oppositeSide[facing];
	}

	@Override
	public void setFacing(short f){
		setSide((short)Facing.oppositeSide[f]);

	}

	private void setSide(short f){
		facing = f;

		if (prevFacing != f){
			//NetworkHelper.updateTileEntityField(this, "facing");
			((NetworkManager)IC2.network.get()).updateTileEntityField(this, "facing");
		}

		prevFacing = f;
	}

	@Override
	public boolean getPowered(){
		return powered;
	}

	@Override
	public void invalidate(){
		if(soundId != null){
			IC2NuclearControl.proxy.stopAlarm(soundId);
			soundId = null;
		}
		super.invalidate();
	}

	@Override
	public void setPowered(boolean value){
		powered = value;

		if (prevPowered != value){
			if (powered){
				if (soundId == null && soundReceived)
					soundId = IC2NuclearControl.proxy.playAlarm(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, 
							SOUND_PREFIX+soundName, getNormalizedRange());
			}else{
				if (soundId != null){
					IC2NuclearControl.proxy.stopAlarm(soundId);
					soundId = null;
				}
			}
			//NetworkHelper.updateTileEntityField(this, "powered");
			((NetworkManager)IC2.network.get()).updateTileEntityField(this, "powered");
		}
		prevPowered = value;
	}

	public void setPoweredNoNotify(boolean value){
		powered = value;

		if (prevPowered != value){
			if (powered){
				if(soundId == null && soundReceived)
					soundId = IC2NuclearControl.proxy.playAlarm(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, 
							SOUND_PREFIX+soundName, getNormalizedRange());
			}else{
				if (soundId != null){
					IC2NuclearControl.proxy.stopAlarm(soundId);
					soundId = null;
				}
			}
		}
		prevPowered = value;
	}

	private float getNormalizedRange(){
		if (worldObj.isRemote){
			return Math.min(range, IC2NuclearControl.instance.SMPMaxAlarmRange)/BASE_SOUND_RANGE;
		}
		return range/BASE_SOUND_RANGE;
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side){
		return false;
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer){
		return true;
	}

	@Override
	public float getWrenchDropRate(){
		return 1;
	}

	@Override
	public void onNetworkUpdate(String field){
		if (field.equals("facing") && prevFacing != facing){
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			prevFacing = facing;
		}
		if (field.equals("powered") && prevPowered != powered){
			setPoweredNoNotify(powered);
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		if (worldObj.isRemote && field.equals("soundName") && prevSoundName != soundName){
			if (IC2NuclearControl.instance.availableAlarms != null && !IC2NuclearControl.instance.availableAlarms.contains(soundName)){
				IC2NuclearControl.logger.info("Can't set sound '%s' at %d,%d,%d, using default", soundName, xCoord, yCoord, zCoord);
				soundName = DEFAULT_SOUND_NAME;
			}
			prevSoundName = soundName;
		}
		if (field.equals("soundName")){
			soundReceived = true;
		}
	}

	@Override
	public List<String> getNetworkedFields(){
		Vector<String> vector = new Vector<String>(2);
		vector.add("facing");
		vector.add("powered");
		vector.add("range");
		vector.add("soundName");
		return vector;
	}

	@Override
	public void updateEntity(){
		if (!init){
			initData();
		}
		super.updateEntity();
		if (FMLCommonHandler.instance().getEffectiveSide().isClient()){
			if (tickRate != -1 && updateTicker-- > 0)
				return;
			updateTicker = tickRate;
			checkStatus();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);
		prevFacing = facing =  nbttagcompound.getShort("facing");
		if (nbttagcompound.hasKey("soundName")){
			prevSoundName = soundName = nbttagcompound.getString("soundName");
			prevRange = range = nbttagcompound.getInteger("range");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound){
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setShort("facing", facing);
		nbttagcompound.setString("soundName", soundName);
		nbttagcompound.setInteger("range", range);
	}

	protected void checkStatus(){
		if (powered && soundReceived && (soundId == null || !IC2NuclearControl.proxy.isPlaying(soundId))){
			soundId = IC2NuclearControl.proxy.playAlarm(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, 
					SOUND_PREFIX + soundName, getNormalizedRange());
		}
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer){
		return new ItemStack(IC2NuclearControl.instance.blockNuclearControlMain, 1, Damages.DAMAGE_HOWLER_ALARM);
	}
}