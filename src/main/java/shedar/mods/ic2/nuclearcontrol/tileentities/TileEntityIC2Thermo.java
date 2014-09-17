package shedar.mods.ic2.nuclearcontrol.tileentities;

import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.network.NetworkHelper;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorChamber;
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
import shedar.mods.ic2.nuclearcontrol.ITextureHelper;
import shedar.mods.ic2.nuclearcontrol.blocks.subblocks.ThermalMonitor;
import shedar.mods.ic2.nuclearcontrol.utils.Damages;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearHelper;

public class TileEntityIC2Thermo extends TileEntity implements INetworkDataProvider, INetworkUpdateListener, INetworkClientTileEntityEventListener, IWrenchable,ITextureHelper{
	protected boolean init;
	private int prevHeatLevel;
	public int heatLevel;
	private int mappedHeatLevel;
	private int prevOnFire;
	public int onFire;
	private short prevFacing;
	public short facing;
	private boolean prevInvertRedstone;
	private boolean invertRedstone;

	protected int updateTicker;
	protected int tickRate;

	public TileEntityIC2Thermo(){
		init = false;
		onFire = 0;
		prevOnFire = 0;
		facing = 0;
		prevFacing = 0;
		mappedHeatLevel = 500;
		prevHeatLevel = 500;
		heatLevel = 500;
		updateTicker = 0;
		tickRate = -1;
		prevInvertRedstone = false;
		invertRedstone = false;
	}

	protected void initData(){
		if(!worldObj.isRemote){
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
		}
		init = true;
	}

	public boolean isInvertRedstone(){
		return invertRedstone;
	}

	public void setInvertRedstone(boolean value){
		invertRedstone = value;
		if(prevInvertRedstone !=value){
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
			((NetworkManager)IC2.network.get()).updateTileEntityField(this, "invertRedstone");
		}
		prevInvertRedstone = value;
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
	public List<String> getNetworkedFields(){
		Vector<String> vector = new Vector<String>(3);
		vector.add("heatLevel");
		vector.add("onFire");
		vector.add("facing");
		vector.add("invertRedstone");
		return vector;
	}

	@Override
	public void onNetworkUpdate(String field){
		if (field.equals("heatLevel") && prevHeatLevel != heatLevel){
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
			prevHeatLevel = heatLevel;
		}
		if (field.equals("facing") && prevFacing != facing){
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			prevFacing = facing;
		}
		if (field.equals("onFire") && prevOnFire != onFire){
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
			prevOnFire = onFire;
		}
		if (field.equals("invertRedstone") && prevInvertRedstone != invertRedstone){
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
			prevInvertRedstone = invertRedstone;
		}
	}

	public void onNetworkEvent(EntityPlayer entityplayer, int i){
		if (i < 0){
			switch (i){
			case -1:
				setInvertRedstone(false);
				break;
			case -2:
				setInvertRedstone(true);
				break;
			default:
				break;
			}
		}else{
			setHeatLevel(i);
		}

	}

	public void setOnFire(int f){
		onFire = f;
		if (prevOnFire != f){
			//NetworkHelper.updateTileEntityField(this, "onFire");
			((NetworkManager)IC2.network.get()).updateTileEntityField(this, "onFire");
		}
		prevOnFire = onFire;
	}

	public int getOnFire(){
		return onFire;
	}

	public void setHeatLevel(int h){
		heatLevel = h;
		if (prevHeatLevel != h){
			((NetworkManager)IC2.network.get()).updateTileEntityField(this, "heatLevel");
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
		}
		prevHeatLevel = heatLevel;
		mappedHeatLevel = h;
	}

	public void setHeatLevelWithoutNotify(int h){
		heatLevel = h;
		prevHeatLevel = heatLevel;
		mappedHeatLevel = h;
	}

	public Integer getHeatLevel(){
		return heatLevel;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);
		if (nbttagcompound.hasKey("heatLevel")){
			int heat = nbttagcompound.getInteger("heatLevel");
			setHeatLevelWithoutNotify(heat);
			prevFacing = facing =  nbttagcompound.getShort("facing");
			prevInvertRedstone = invertRedstone = nbttagcompound.getBoolean("invert"); 
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound){
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("heatLevel", getHeatLevel());
		nbttagcompound.setShort("facing", facing);
		nbttagcompound.setBoolean("invert", isInvertRedstone());
	}

	protected void checkStatus(){
		byte fire;
		IReactorChamber chamber = NuclearHelper.getReactorChamberAroundCoord(worldObj, xCoord, yCoord, zCoord);
		IReactor reactor = null;
		if (chamber != null){
			reactor = chamber.getReactor();
		}
		if (reactor == null){
			reactor = NuclearHelper.getReactorAroundCoord(worldObj, xCoord, yCoord, zCoord);
		}
		if (reactor != null){
			if (tickRate == -1){
				tickRate = reactor.getTickRate() / 2;
				if (tickRate == 0)
					tickRate = 1;
				updateTicker = tickRate;
			}
			int reactorHeat = reactor.getHeat();
			if (reactorHeat >= mappedHeatLevel){
				fire = 1;
			}else{
				fire = 0;
			}
		}else{
			fire = -1;
		}
		if (fire != getOnFire()){
			setOnFire(fire);
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
		}
	}

	@Override
	public void updateEntity(){
		if (!init){
			initData();
		}
		super.updateEntity();
		if (!worldObj.isRemote){
			if (tickRate != -1 && updateTicker-- > 0)
				return;
			updateTicker = tickRate;
			checkStatus();
		}

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
	public int modifyTextureIndex(int texture){
		if (texture != ThermalMonitor.I_FACE_GREEN)
			return texture;
		int fireState = getOnFire();
		switch (fireState){
		case 1:
			texture = ThermalMonitor.I_FACE_RED;
			break;
		case 0:
			texture = ThermalMonitor.I_FACE_GREEN;
			break;
		default:
			texture = ThermalMonitor.I_FACE_GRAY;
			break;
		}
		return texture;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer){
		return new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, Damages.DAMAGE_THERMAL_MONITOR);
	}
}
