package shedar.mods.ic2.nuclearcontrol.tileentities;

import shedar.mods.ic2.nuclearcontrol.crossmod.EnergyStorageData;

import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.item.IC2Items;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.network.NetworkManager;

import java.util.List;
import java.util.Vector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.ISlotItemFilter;
import shedar.mods.ic2.nuclearcontrol.utils.BlockDamages;
import cpw.mods.fml.common.FMLCommonHandler;

public class TileEntityEnergyCounter extends TileEntity implements
		IEnergyConductor, IWrenchable, INetworkClientTileEntityEventListener,
		IInventory, ISlotItemFilter, INetworkDataProvider,
		INetworkUpdateListener {
	
	private static final int BASE_PACKET_SIZE = 32;
	private boolean init;
	private ItemStack inventory[];

	protected int updateTicker;
	protected int tickRate;

	public double counter;

	private short prevFacing;
	public short facing;

	// check out shedar.mods.ic2.nuclearcontrol.crossmod.EnergyStorageData
	private byte prevPowerType;
	public byte powerType;

	public int packetSize;

	private boolean addedToEnergyNet;

	public TileEntityEnergyCounter() {
		super();
		inventory = new ItemStack[1];// transformers upgrade
		addedToEnergyNet = false;
		packetSize = BASE_PACKET_SIZE;
		prevFacing = facing = 0;
		counter = 0.0;
		tickRate = IC2NuclearControl.instance.screenRefreshPeriod;
		updateTicker = tickRate;
	}

	protected void initData() {
		init = true;
	}

	public void setPowerType(byte p) {
		powerType = p;

		if (prevPowerType != p)
			IC2.network.get().updateTileEntityField(this, "powerType");

		prevPowerType = p;
	}

	@Override
	public short getFacing() {
		return (short) Facing.oppositeSide[facing];
	}

	@Override
	public void setFacing(short f) {
		if (addedToEnergyNet) {
			EnergyTileUnloadEvent event = new EnergyTileUnloadEvent(this);
			MinecraftForge.EVENT_BUS.post(event);
		}
		setSide((short) Facing.oppositeSide[f]);
		addedToEnergyNet = false;
		if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
			EnergyTileLoadEvent event = new EnergyTileLoadEvent(this);
			MinecraftForge.EVENT_BUS.post(event);
			addedToEnergyNet = true;
		}
	}

	private void setSide(short f) {
		facing = f;

		if(init && prevFacing != f)
			((NetworkManager)IC2.network.get()).updateTileEntityField(this, "facing");

		prevFacing = f;
	}

	@Override
	public void updateEntity() {
		if (!init) {
			initData();
			markDirty();
		}
		if (!worldObj.isRemote) {
			if (!addedToEnergyNet) {
				EnergyTileLoadEvent event = new EnergyTileLoadEvent(this);
				MinecraftForge.EVENT_BUS.post(event);
				addedToEnergyNet = true;
			}
			if (updateTicker-- == 0) {
				updateTicker = tickRate - 1;
				counter += EnergyNet.instance.getTotalEnergyEmitted(this); //So sue me
				this.setPowerType((byte)EnergyStorageData.TARGET_TYPE_IC2);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		facing = nbttagcompound.getShort("facing");
		counter = nbttagcompound.getDouble("counter");
		powerType = nbttagcompound.getByte("powerType");

		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound compound = nbttaglist.getCompoundTagAt(i);
			byte slotNum = compound.getByte("Slot");

			if (slotNum >= 0 && slotNum < inventory.length) {
				inventory[slotNum] = ItemStack.loadItemStackFromNBT(compound);
			}
		}
		markDirty();
	}

	@Override
	public void onNetworkUpdate(String field) {
		if (field.equals("facing") && prevFacing != facing) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			prevFacing = facing;
		}
	}

	@Override
	public void invalidate() {
		if (!worldObj.isRemote && addedToEnergyNet) {
			EnergyTileUnloadEvent event = new EnergyTileUnloadEvent(this);
			MinecraftForge.EVENT_BUS.post(event);
			addedToEnergyNet = false;
		}
		super.invalidate();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setShort("facing", facing);
		nbttagcompound.setDouble("counter", counter);
		nbttagcompound.setByte("powerType", powerType);

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] != null) {
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(compound);
				nbttaglist.appendTag(compound);
			}
		}
		nbttagcompound.setTag("Items", nbttaglist);
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slotNum) {
		return inventory[slotNum];
	}

	@Override
	public ItemStack decrStackSize(int slotNum, int amount) {
		if (inventory[slotNum] != null) {
			if (inventory[slotNum].stackSize <= amount) {
				ItemStack itemStack = inventory[slotNum];
				inventory[slotNum] = null;
				markDirty();
				return itemStack;
			}

			ItemStack taken = inventory[slotNum].splitStack(amount);
			if (inventory[slotNum].stackSize == 0) {
				inventory[slotNum] = null;
			}
			markDirty();
			return taken;
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slotNum, ItemStack itemStack) {
		inventory[slotNum] = itemStack;

		if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
			itemStack.stackSize = getInventoryStackLimit();
		}
		markDirty();
	}

	@Override
	public String getInventoryName() {
		return "block.RemoteThermo";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && 
				player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
	}

	@Override
	public void openInventory(){}

	@Override
	public void closeInventory(){}

	@Override
	public void markDirty() {
		super.markDirty();
		int upgradeCountTransormer = 0;
		ItemStack itemStack = inventory[0];
		if (itemStack != null && itemStack.isItemEqual(IC2Items.getItem("transformerUpgrade"))) {
			upgradeCountTransormer = itemStack.stackSize;
		}
		upgradeCountTransormer = Math.min(upgradeCountTransormer, 4);
		if (worldObj != null && !worldObj.isRemote) {
			packetSize = BASE_PACKET_SIZE * (int) Math.pow(4D, upgradeCountTransormer);

			if (addedToEnergyNet) {
				EnergyTileUnloadEvent event = new EnergyTileUnloadEvent(this);
				MinecraftForge.EVENT_BUS.post(event);
			}
			addedToEnergyNet = false;
			EnergyTileLoadEvent event = new EnergyTileLoadEvent(this);
			MinecraftForge.EVENT_BUS.post(event);
			addedToEnergyNet = true;
		}
	};

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
		return direction.ordinal() == getFacing();
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
		return direction.ordinal() != getFacing();
	}

	@Override
	public boolean isItemValid(int slotIndex, ItemStack itemstack) {
		return itemstack.isItemEqual(IC2Items.getItem("transformerUpgrade"));
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int face) {
		return getFacing() != face;
	};

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return true;
	}

	@Override
	public float getWrenchDropRate() {
		return 1;
	}

	@Override
	public List<String> getNetworkedFields() {
		Vector<String> vector = new Vector<String>(2);
		vector.add("facing");
		vector.add("powerType");
		return vector;
	}

	@Override
	public void onNetworkEvent(EntityPlayer player, int event) {
		counter = 0;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1, BlockDamages.DAMAGE_ENERGY_COUNTER);
	}

	@Override
	public double getConductionLoss() {
		return 0.025D;
	}

	@Override
	public double getInsulationEnergyAbsorption() {
		return 16384;
	}

	@Override
	public double getInsulationBreakdownEnergy() {
		return packetSize + 1;
	}

	@Override
	public double getConductorBreakdownEnergy() {
		return packetSize + 1;
	}

	@Override
	public void removeInsulation() {
	}

	@Override
	public void removeConductor() {
		worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air, 0, 3);
		worldObj.createExplosion(null, xCoord, yCoord, zCoord, 0.8F, false);
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		return isItemValid(slot, itemstack);
	}
}