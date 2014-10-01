package shedar.mods.ic2.nuclearcontrol.tileentities;

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
import shedar.mods.ic2.nuclearcontrol.utils.Damages;
import cpw.mods.fml.common.FMLCommonHandler;

public class TileEntityAverageCounter extends TileEntity implements
		IEnergyConductor, IWrenchable, INetworkClientTileEntityEventListener,
		IInventory, ISlotItemFilter, INetworkDataProvider,
		INetworkUpdateListener {
	private static final int BASE_PACKET_SIZE = 32;
	private static final int DATA_POINTS = 11 * 20;

	public static final byte POWER_TYPE_EU = 0;
	public static final byte POWER_TYPE_MJ = 1;

	private boolean init;
	private ItemStack inventory[];

	// 0 - EU, 1- MJ
	private byte prevPowerType;
	public byte powerType;

	// private double prevTotal;

	private short prevFacing;
	public short facing;

	protected double[] data;
	protected int index;
	protected int updateTicker;
	protected int tickRate;
	protected short prevPeriod;
	public short period;
	private int clientAverage = -1;

	public int packetSize;

	private boolean addedToEnergyNet;

	public TileEntityAverageCounter() {
		super();
		inventory = new ItemStack[1];// transformers upgrade
		addedToEnergyNet = false;
		packetSize = BASE_PACKET_SIZE;
		prevFacing = facing = 0;
		data = new double[DATA_POINTS];
		index = 0;
		tickRate = 20;
		updateTicker = tickRate;
		prevPeriod = period = 1;
		powerType = POWER_TYPE_EU;
	}

	protected void initData() {
		init = true;
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

		if (prevFacing != f) {
			// NetworkHelper.updateTileEntityField(this, "facing");
			IC2.network.get().updateTileEntityField(this,
					"facing");
		}

		prevFacing = f;
	}

	private void setPeriod(short p) {
		period = p;

		if (prevPeriod != p) {
			// NetworkHelper.updateTileEntityField(this, "period");
			IC2.network.get().updateTileEntityField(this,
					"period");
		}
		prevPeriod = p;
	}

	public void setPowerType(byte p) {
		powerType = p;

		if (prevPowerType != p) {
			// NetworkHelper.updateTileEntityField(this, "powerType");
			IC2.network.get().updateTileEntityField(this,
					"powerType");
		}

		prevPowerType = p;
	}

	@Override
	public void updateEntity() {
		if (!init) {
			initData();
			markDirty();
		}
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			return;
		if (!worldObj.isRemote) {
			if (!addedToEnergyNet) {
				EnergyTileLoadEvent event = new EnergyTileLoadEvent(this);
				MinecraftForge.EVENT_BUS.post(event);
				// prevTotal = EnergyNet.instance.getTotalEnergyEmitted(this);
				addedToEnergyNet = true;
			}
			index = (index + 1) % DATA_POINTS;
			data[index] = 0;
			getAverage();
			double total = EnergyNet.instance.getTotalEnergyEmitted(this);

			data[index] = total;
			setPowerType(POWER_TYPE_EU);
		}
		super.updateEntity();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		facing = nbttagcompound.getShort("facing");
		index = nbttagcompound.getInteger("dataIndex");
		updateTicker = nbttagcompound.getInteger("updateTicker");
		prevPeriod = period = nbttagcompound.getShort("period");
		powerType = nbttagcompound.getByte("powerType");

		for (int i = 0; i < DATA_POINTS; i++)
			data[i] = nbttagcompound.getLong("point-" + i);

		NBTTagList nbttaglist = nbttagcompound.getTagList("Items",
				Constants.NBT.TAG_COMPOUND);
		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound compound = nbttaglist
					.getCompoundTagAt(i);
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
		nbttagcompound.setInteger("dataIndex", index);
		nbttagcompound.setInteger("updateTicker", updateTicker);
		nbttagcompound.setShort("period", period);
		nbttagcompound.setByte("powerType", powerType);

		for (int i = 0; i < DATA_POINTS; i++)
			nbttagcompound.setLong("point-" + i, (long) data[i]);

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
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this
				&& player.getDistanceSq(xCoord + 0.5D,
						yCoord + 0.5D, zCoord + 0.5D) <= 64D;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public void markDirty() {
		super.markDirty();
		int upgradeCountTransormer = 0;
		ItemStack itemStack = inventory[0];
		if (itemStack != null
				&& itemStack
						.isItemEqual(IC2Items.getItem("transformerUpgrade"))) {
			upgradeCountTransormer = itemStack.stackSize;
		}
		upgradeCountTransormer = Math.min(upgradeCountTransormer, 4);
		if (worldObj != null && !worldObj.isRemote) {
			packetSize = BASE_PACKET_SIZE
					* (int) Math.pow(4D, upgradeCountTransormer);

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
	public boolean acceptsEnergyFrom(TileEntity emitter,
			ForgeDirection direction) {
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
		Vector<String> vector = new Vector<String>(3);
		vector.add("facing");
		vector.add("period");
		vector.add("powerType");
		return vector;
	}

	public void setClientAverage(int value) {
		clientAverage = value;
	}

	public int getClientAverage() {
		if (clientAverage == -1)
			return getAverage();
		return clientAverage;
	}

	private int getAverage() {
		int start = DATA_POINTS + index - period * 20;
		double sum = 0;
		for (int i = 0; i < period * 20; i++) {
			sum += data[(start + i) % DATA_POINTS];
		}
		clientAverage = (int) Math.round(sum / period / 20);
		return clientAverage;
	}

	@Override
	public void onNetworkEvent(EntityPlayer player, int event) {
		if (event == 0) {
			for (int i = 0; i < DATA_POINTS; i++) {
				data[i] = 0;
			}

			updateTicker = tickRate;
			index = 0;
		} else {
			setPeriod((short) event);
		}
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(IC2NuclearControl.blockNuclearControlMain, 1,
				Damages.DAMAGE_AVERAGE_COUNTER);
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