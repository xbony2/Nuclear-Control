package shedar.mods.ic2.nuclearcontrol.tileentities;

import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.IRotation;
import shedar.mods.ic2.nuclearcontrol.IScreenPart;
import shedar.mods.ic2.nuclearcontrol.ITextureHelper;
import shedar.mods.ic2.nuclearcontrol.blocks.subblocks.InfoPanel;
import shedar.mods.ic2.nuclearcontrol.panel.Screen;
import shedar.mods.ic2.nuclearcontrol.utils.Damages;
import cpw.mods.fml.common.FMLCommonHandler;

public class TileEntityInfoPanelExtender extends TileEntity implements
		INetworkDataProvider, INetworkUpdateListener, IWrenchable,
		ITextureHelper, IScreenPart, IRotation {

	protected boolean init;

	private Screen screen;
	private short prevFacing;
	public short facing;
	private boolean partOfScreen;

	private int coreX;
	private int coreY;
	private int coreZ;

	@Override
	public short getFacing() {
		return (short) Facing.oppositeSide[facing];
	}

	@Override
	public void setFacing(short f) {
		setSide((short) Facing.oppositeSide[f]);

	}

	private void setSide(short f) {
		facing = f;
		if (prevFacing != f) {
			if (FMLCommonHandler.instance().getEffectiveSide().isServer()
					&& init) {
				IC2NuclearControl.instance.screenManager
						.unregisterScreenPart(this);
				IC2NuclearControl.instance.screenManager
						.registerInfoPanelExtender(this);
			}
			// NetworkHelper.updateTileEntityField(this, "facing");
			IC2.network.get().updateTileEntityField(this,
					"facing");
		}

		prevFacing = f;
	}

	@Override
	public void onNetworkUpdate(String field) {
		if (field.equals("facing") && prevFacing != facing) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			prevFacing = facing;
		}
	}

	public TileEntityInfoPanelExtender() {
		super();
		init = false;
		facing = 0;
		prevFacing = 0;
		screen = null;
		partOfScreen = false;
	}

	@Override
	public List<String> getNetworkedFields() {
		List<String> list = new ArrayList<String>(1);
		list.add("facing");
		return list;
	}

	protected void initData() {
		if (FMLCommonHandler.instance().getEffectiveSide().isServer()
				&& !partOfScreen) {
			IC2NuclearControl.instance.screenManager
					.registerInfoPanelExtender(this);
		}
		if (partOfScreen && screen == null) {
			TileEntity core = worldObj.getTileEntity(coreX, coreY, coreZ);
			if (core != null && core instanceof TileEntityInfoPanel) {
				screen = ((TileEntityInfoPanel) core).getScreen();
				if (screen != null)
					screen.init(true, worldObj);
			}
		}
		init = true;
	}

	@Override
	public void updateEntity() {
		if (!init) {
			initData();
		}
		super.updateEntity();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		prevFacing = facing = nbttagcompound.getShort("facing");
		partOfScreen = nbttagcompound.getBoolean("partOfScreen");
		if (nbttagcompound.hasKey("coreX")) {
			coreX = nbttagcompound.getInteger("coreX");
			coreY = nbttagcompound.getInteger("coreY");
			coreZ = nbttagcompound.getInteger("coreZ");
		}
	}

	@Override
	public void invalidate() {
		super.invalidate();
		if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
			IC2NuclearControl.instance.screenManager.unregisterScreenPart(this);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setShort("facing", facing);
		nbttagcompound.setBoolean("partOfScreen", partOfScreen);
		if (screen != null) {
			nbttagcompound.setInteger("coreX", coreX);
			nbttagcompound.setInteger("coreY", coreY);
			nbttagcompound.setInteger("coreZ", coreZ);
		}
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int face) {
		return !entityPlayer.isSneaking() && getFacing() != face;
	};

	@Override
	public float getWrenchDropRate() {
		return 1;
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return !entityPlayer.isSneaking();
	}

	@Override
	public int modifyTextureIndex(int texture) {
		if (texture != InfoPanel.I_COLOR_DEFAULT)
			return texture;
		if (screen != null) {
			TileEntityInfoPanel core = screen.getCore(worldObj);
			if (core != null) {
				return core.modifyTextureIndex(texture, xCoord, yCoord, zCoord);
			}
		}
		return texture;
	}

	@Override
	public void setScreen(Screen screen) {
		this.screen = screen;
		partOfScreen = screen != null;
	}

	@Override
	public Screen getScreen() {
		return screen;
	}

	@Override
	public void rotate() {
		if (screen != null) {
			TileEntityInfoPanel core = screen.getCore(worldObj);
			if (core != null)
				core.rotate();
		}
	}

	@Override
	public int getRotation() {
		if (screen != null) {
			TileEntityInfoPanel core = screen.getCore(worldObj);
			if (core != null)
				return core.rotation;
		}
		return 0;
	}

	@Override
	public void setRotation(int rotation) {
		if (screen != null) {
			TileEntityInfoPanel core = screen.getCore(worldObj);
			if (core != null)
				core.setRotation(rotation);
		}
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(
				IC2NuclearControl.blockNuclearControlMain, 1,
				Damages.DAMAGE_INFO_PANEL_EXTENDER);
	}

	@Override
	public void updateData() {
	}
}