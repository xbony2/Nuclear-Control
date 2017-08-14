package shedar.mods.ic2.nuclearcontrol.blocks;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.tile.IWrenchable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ic2.core.item.tool.ItemToolPainter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.blocks.subblocks.*;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityHowlerAlarm;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityThermo;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityIndustrialAlarm;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanel;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityInfoPanelExtender;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityRangeTrigger;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityRemoteThermo;
import shedar.mods.ic2.nuclearcontrol.utils.*;

public class BlockNuclearControlMain extends BlockContainer {
	public static Map<Integer, Subblock> subblocks;

	public BlockNuclearControlMain() {
		super(Material.iron);
		setHardness(0.5F);
		setCreativeTab(IC2NuclearControl.tabIC2NC);
		subblocks = new HashMap<Integer, Subblock>();
		register(new ThermalMonitor());
		register(new IndustrialAlarm());
		register(new HowlerAlarm());
		register(new RemoteThermo());
		register(new InfoPanel());
		register(new InfoPanelExtender());
		register(new EnergyCounter());
		register(new AverageCounter());
		register(new RangeTrigger());
		register(new AdvancedInfoPanel());
		register(new AdvancedInfoPanelExtender());
	}

	public void register(Subblock block) {
		subblocks.put(block.getDamage(), block);
	}

	@Override
	public int getRenderType() {
		return IC2NuclearControl.instance.modelId;
	}

	@Override
	public boolean isBlockNormalCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	private boolean isSolidBlockRequired(int metadata) {
		return subblocks.containsKey(metadata) && subblocks.get(metadata).isSolidBlockRequired();
	}

	/**
	 * Checks to see if its valid to put this block at the specified
	 * coordinates. Args: world, x, y, z
	 */
	public boolean canPlaceBlockAtlocal(World world, int x, int y, int z) {
		for (int face = 0; face < 6; face++) {
			int side = Facing.oppositeSide[face];
			if (world.isSideSolid(x + Facing.offsetsXForSide[side], y + Facing.offsetsYForSide[side], z 
					+ Facing.offsetsZForSide[side], ForgeDirection.getOrientation(face)))
				return true;
		}
		return false;
	}

	/**
	 * called before onBlockPlacedBy by ItemBlock and ItemReed
	 */
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		super.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, metadata);
		ForgeDirection dir = ForgeDirection.getOrientation(side);
		ForgeDirection opposite = dir.getOpposite();
		if (metadata > BlockDamages.DAMAGE_MAX) {
			metadata = 0;
		}

		if(isSolidBlockRequired(metadata) && !world.isSideSolid(x + opposite.offsetX, y + opposite.offsetY, z + opposite.offsetZ, dir)){
			side = 1;
		}
		return metadata + (side << 8);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack item){
		int metadata = item.getItemDamage();
		onBlockPlacedBy(world, x, y, z, player, item, metadata);
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack item, int metadata) {
		TileEntity block = world.getTileEntity(x, y, z);
		int side = metadata >> 8;
		metadata = metadata & 0xff;
		if (metadata > BlockDamages.DAMAGE_MAX) {
			metadata = 0;
		}

		if (block instanceof IWrenchable) {
			IWrenchable wrenchable = (IWrenchable) block;
			wrenchable.setFacing((short) side);
			if (player != null && !isSolidBlockRequired(metadata)) {
				int rotationSegment = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
				if (player.rotationPitch >= 65) {
					wrenchable.setFacing((short) 1);
				} else if (player.rotationPitch <= -65) {
					wrenchable.setFacing((short) 0);
				} else {
					switch (rotationSegment) {
					case 0:
						wrenchable.setFacing((short) 2);
						break;
					case 1:
						wrenchable.setFacing((short) 5);
						break;
					case 2:
						wrenchable.setFacing((short) 3);
						break;
					case 3:
						wrenchable.setFacing((short) 4);
						break;
					default:
						wrenchable.setFacing((short) 0);
						break;
					}
				}
			}
		}
	}

	/**
	 * Called whenever the block is added into the world.
	 */
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		int metadata = world.getBlockMetadata(x, y, z);
		if (metadata > BlockDamages.DAMAGE_MAX) {
			metadata = 0;
		}

		if (isSolidBlockRequired(metadata))
			for (int face = 0; face < 6; face++) {
				int side = Facing.oppositeSide[face];
				if (world.isSideSolid(x + Facing.offsetsXForSide[side], y
						+ Facing.offsetsYForSide[side], z
						+ Facing.offsetsZForSide[side],
						ForgeDirection.getOrientation(face))) {
					TileEntity tileentity = world.getTileEntity(x, y, z);
					if (tileentity instanceof IWrenchable) {
						((IWrenchable) tileentity).setFacing((short) face);
					}
					break;
				}
			}
		dropBlockIfCantStay(world, x, y, z);
	}

	/*@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityHowlerAlarm) {
			((TileEntityHowlerAlarm) tileEntity).setPowered(false);
		}
		if (!world.isRemote && tileEntity instanceof IInventory) {
			IInventory inventory = (IInventory) tileEntity;
			float range = 0.7F;

			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				ItemStack itemStack = inventory.getStackInSlot(i);

				if (itemStack != null) {
					double dx = world.rand.nextFloat() * range
							+ (1.0F - range) * 0.5D;
					double dy = world.rand.nextFloat() * range
							+ (1.0F - range) * 0.5D;
					double dz = world.rand.nextFloat() * range
							+ (1.0F - range) * 0.5D;
					EntityItem item = new EntityItem(world, x + dx,
							y + dy, z + dz, itemStack);
					item.delayBeforeCanPickup = 10;
					world.spawnEntityInWorld(item);
					inventory.setInventorySlotContents(i, null);
				}
			}
		}
		super.breakBlock(world, x, y, z, this, metadata);
	}*/

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
		int side = 0;
		TileEntity tileentity = world.getTileEntity(x, y, z);
		if (tileentity instanceof IWrenchable) {
			side = Facing.oppositeSide[((IWrenchable) tileentity).getFacing()];
		}
		int metadata = world.getBlockMetadata(x, y, z);

		if (isSolidBlockRequired(metadata)
				&& !world.isSideSolid(x + Facing.offsetsXForSide[side], y
						+ Facing.offsetsYForSide[side], z
						+ Facing.offsetsZForSide[side], ForgeDirection
						.getOrientation(side).getOpposite())) {
			if (!world.isRemote) {
				dropBlockAsItem(world, x, y, z, metadata, 0);
			}
			world.setBlock(x, y, z, Blocks.air, 0, 3);
		} else {
			RedstoneHelper.checkPowered(world, tileentity);
		}
		super.onNeighborBlockChange(world, x, y, z, neighbor);
	}

	public boolean canPlaceBlockOnSide(World world, int x, int y, int z,
			int side, int metadata) {
		ForgeDirection dir = ForgeDirection.getOrientation(side);
		if (!isSolidBlockRequired(metadata)) {
			return true;
		}
		return (dir == ForgeDirection.DOWN && world.isSideSolid(x, y + 1, z,
				ForgeDirection.DOWN))
				|| (dir == ForgeDirection.UP && world.isSideSolid(x, y - 1, z,
						ForgeDirection.UP))
				|| (dir == ForgeDirection.NORTH && world.isSideSolid(x, y,
						z + 1, ForgeDirection.NORTH))
				|| (dir == ForgeDirection.SOUTH && world.isSideSolid(x, y,
						z - 1, ForgeDirection.SOUTH))
				|| (dir == ForgeDirection.WEST && world.isSideSolid(x + 1, y,
						z, ForgeDirection.WEST))
				|| (dir == ForgeDirection.EAST && world.isSideSolid(x - 1, y,
						z, ForgeDirection.EAST));
	}

	/**
	 * Tests if the block can remain at its current location and will drop as an
	 * item if it is unable to stay. Returns True if it can stay and False if it
	 * drops. Args: world, x, y, z
	 */
	private boolean dropBlockIfCantStay(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		if (!isSolidBlockRequired(metadata)) {
			return true;
		}
		if (!canPlaceBlockAtlocal(world, x, y, z)) {
			if (world.getBlock(x, y, z) == world.getBlock(x, y, z)) {
				dropBlockAsItem(world, x, y, z, metadata, 0);
				world.setBlockToAir(x, y, z);
			}
			return false;
		} else {
			return true;
		}
	}

	public static float[] getBlockBounds(int damage) {
		if (subblocks.containsKey(damage)) {
			return subblocks.get(damage).getBlockBounds(null);
		}
		return new float[] { 0, 0, 0, 1, 1, 1 };
	}

	/**
	 * Updates the blocks bounds based on its current state.
	 */
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
		int blockType = blockAccess.getBlockMetadata(x, y, z);

		float baseX1 = 0;
		float baseY1 = 0;
		float baseZ1 = 0;

		float baseX2 = 1;
		float baseY2 = 1;
		float baseZ2 = 1;

		TileEntity tileentity = blockAccess.getTileEntity(x, y, z);

		if (subblocks.containsKey(blockType)) {
			float[] bounds = subblocks.get(blockType).getBlockBounds(tileentity);
			baseX1 = bounds[0];
			baseY1 = bounds[1];
			baseZ1 = bounds[2];

			baseX2 = bounds[3];
			baseY2 = bounds[4];
			baseZ2 = bounds[5];
		}

		float tmp;

		int side = 0;
		if (tileentity instanceof IWrenchable) {
			side = Facing.oppositeSide[((IWrenchable) tileentity).getFacing()];
		}
		switch (side) {
		case 1:
			baseY1 = 1 - baseY1;
			baseY2 = 1 - baseY2;
			break;

		case 2:
			tmp = baseY1;
			baseY1 = baseZ1;
			baseZ1 = tmp;

			tmp = baseY2;
			baseY2 = baseZ2;
			baseZ2 = tmp;
			break;

		case 3:
			tmp = baseY1;
			baseY1 = baseZ1;
			baseZ1 = 1 - tmp;

			tmp = baseY2;
			baseY2 = baseZ2;
			baseZ2 = 1 - tmp;
			break;

		case 4:
			tmp = baseY1;
			baseY1 = baseX1;
			baseX1 = tmp;

			tmp = baseY2;
			baseY2 = baseX2;
			baseX2 = tmp;
			break;

		case 5:
			tmp = baseY1;
			baseY1 = baseX1;
			baseX1 = 1 - tmp;

			tmp = baseY2;
			baseY2 = baseX2;
			baseX2 = 1 - tmp;
			break;
		}
		setBlockBounds(Math.min(baseX1, baseX2), Math.min(baseY1, baseY2),
				Math.min(baseZ1, baseZ2), Math.max(baseX1, baseX2),
				Math.max(baseY1, baseY2), Math.max(baseZ1, baseZ2));
	}

	public String getInvName() {
		return "IC2 Thermo";
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float f1, float f2, float f3) {
		int blockType = world.getBlockMetadata(x, y, z);
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if(tileEntity instanceof TileEntityHowlerAlarm){
			if(player.getCurrentEquippedItem() != null && DyeUtil.isADye(player.getCurrentEquippedItem())){
				((TileEntityHowlerAlarm) tileEntity).setColor(ItemDye.field_150922_c[DyeUtil.getDyeId(player.getCurrentEquippedItem())]);
				world.markBlockForUpdate(x,y,z);
				if(!player.capabilities.isCreativeMode) {
					if (player.inventory.getCurrentItem().stackSize == 1) {
						player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
					} else {
						player.inventory.getCurrentItem().stackSize--;
					}
				}
				return true;
			} else if(player.getCurrentEquippedItem() != null){
				if(player.getCurrentEquippedItem().getItem() instanceof ItemToolPainter){
					ItemToolPainter p = (ItemToolPainter) player.getCurrentEquippedItem().getItem();
					((TileEntityHowlerAlarm) tileEntity).setColor(ItemDye.field_150922_c[p.color]);
					world.markBlockForUpdate(x,y,z);
					player.getCurrentEquippedItem().damageItem(1, player);
					return true;
				}
			}
		}
		
		if (WrenchHelper.isWrenchClicked(tileEntity, player, side)) {
			return true;
		}
		if (player != null && player.isSneaking()) {
			return false;
		}
		if (subblocks.containsKey(blockType) && subblocks.get(blockType).hasGui()) {
			if (player instanceof EntityPlayerMP) {
				BlockSnapshot blockSnapshot = new BlockSnapshot(world, x, y, z, Blocks.air, 0);
				PlaceEvent e = ForgeEventFactory.onPlayerBlockPlace(player, blockSnapshot, ForgeDirection.getOrientation(side));
				if (!e.isCanceled()) {
					player.openGui(IC2NuclearControl.instance, blockType, world, x, y, z);
				}
			}
			return true;
		}
		return false;
	}

	public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l) {
		return false;
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this
	 * box can change after the pool has been cleared to be reused)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess iblockaccess, int x, int y, int z, int direction) {
		return isProvidingStrongPower(iblockaccess, x, y, z, direction);
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess iblockaccess, int x, int y, int z, int direction) {
		TileEntity tileentity = iblockaccess.getTileEntity(x, y, z);
		if (!(tileentity instanceof TileEntityThermo) && !(tileentity instanceof TileEntityRangeTrigger))
			return 0;

		int targetX = x;
		int targetY = y;
		int targetZ = z;
		switch (direction) {
		case 0:
			targetY++;
			break;
		case 1:
			targetY--;
			break;
		case 2:
			targetZ++;
			break;
		case 3:
			targetZ--;
			break;
		case 4:
			targetX++;
			break;
		case 5:
			targetX--;
			break;
		}
		TileEntity targetEntity = iblockaccess.getTileEntity(targetX, targetY, targetZ);
		if (tileentity instanceof TileEntityThermo && targetEntity != null
				&& (NuclearHelper.getReactorAt(tileentity.getWorldObj(), targetX, targetY, targetZ) != null 
				|| NuclearHelper.getReactorChamberAt(tileentity.getWorldObj(), targetX, targetY, targetZ) != null)) {
			return 0;
		}
		if (tileentity instanceof TileEntityRemoteThermo) {
			TileEntityRemoteThermo thermo = (TileEntityRemoteThermo) tileentity;
			if(thermo.getEnergy() > 0) {
				return thermo.getOnFire() >= thermo.getHeatLevel() ^ thermo.isInvertRedstone() ? 15 : 0;
			}else{
				return 0;
			}
		}
		if (tileentity instanceof TileEntityRangeTrigger)
			return ((TileEntityRangeTrigger) tileentity).getOnFire() > 0
					^ ((TileEntityRangeTrigger) tileentity).isInvertRedstone() ? 15 : 0;
		return ((TileEntityThermo) tileentity).getOnFire() > 0 ^ ((TileEntityThermo) tileentity).isInvertRedstone() ? 15 : 0;
	}

	@Override
	public IIcon getIcon(int side, int metadata) {
		if (subblocks.containsKey(metadata))
			return subblocks.get(metadata).getBlockTextureFromSide(side);
		return null;
	}

	@Override
	public IIcon getIcon(IBlockAccess blockaccess, int x, int y, int z, int side) {
		int blockType = blockaccess.getBlockMetadata(x, y, z);
		if (subblocks.containsKey(blockType))
			return subblocks.get(blockType).getBlockTexture(blockaccess, x, y, z, side);
		return null;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		for (Subblock subblock : subblocks.values()) {
			subblock.registerIcons(iconRegister);
		}
	}

	public Subblock getSubblock(int metadata) {
		if (subblocks.containsKey(metadata))
			return subblocks.get(metadata);
		return null;
	}

	@Override
	public int damageDropped(int i) {
		if (i > 0 && i <= BlockDamages.DAMAGE_MAX) {
			return i;
		} else {
			return 0;
		}
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		int metadata = world.getBlockMetadata(x, y, z);
		return !isSolidBlockRequired(metadata);
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);
		if (entity instanceof TileEntityIndustrialAlarm) {
			return ((TileEntityIndustrialAlarm) entity).lightLevel;
		} else if (entity instanceof TileEntityInfoPanel) {
			if (((TileEntityInfoPanel) entity).getPowered())
				return 7;
			else
				return 0;
		} else if (entity instanceof TileEntityInfoPanelExtender) {
			TileEntityInfoPanelExtender extender = (TileEntityInfoPanelExtender) entity;
			if (extender.getScreen() != null) {
				TileEntityInfoPanel core = extender.getScreen().getCore(extender.getWorldObj());
				if (core != null && core.getPowered())
					return 7;
				else
					return 0;
			}
		}

		return getLightValue();
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void getSubBlocks(Item id, CreativeTabs tab, List itemList) {
		for (int i = 0; i <= BlockDamages.DAMAGE_MAX; i++) {
			itemList.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return null;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		if (subblocks.containsKey(metadata))
			return subblocks.get(metadata).getTileEntity();
		return null;
	}

	 @Override
	 public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
		dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
	 }
	private void dropItems(World world, int x, int y, int z){
		Random rand = new Random();

		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (!(tileEntity instanceof IInventory)) {
			return;
		}
		IInventory inventory = (IInventory) tileEntity;

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);

			if (item != null && item.stackSize > 0) {
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;

				EntityItem entityItem = new EntityItem(world,
						x + rx, y + ry, z + rz,
						new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

				if (item.hasTagCompound()) {
					entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
				}

				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
		//EntityItem e = new EntityItem(world, x, y, z, new ItemStack(this, 1, this.damageDropped(world.getBlockMetadata(x,y,z))));
		//world.spawnEntityInWorld(e);

	}

	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		if(world.getBlock(x,y,z).equals(this) && world.getBlockMetadata(x,y,z) == BlockDamages.DAMAGE_HOWLER_ALARM && world.getTileEntity(x,y,z) instanceof TileEntityHowlerAlarm) {
			return ((TileEntityHowlerAlarm) world.getTileEntity(x, y, z)).getColor();
		}else{
			return 16777215;
		}
	}
	//Color testing code - Unused
	private int RGBToInt(final int r, final int g, final int b) {
		int color = 0;
		color = color | b;
		color = color | g << 8;
		color = color | r << 16;

		return color;
	}

}
