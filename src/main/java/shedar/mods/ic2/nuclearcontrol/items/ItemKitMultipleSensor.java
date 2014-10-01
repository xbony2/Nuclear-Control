package shedar.mods.ic2.nuclearcontrol.items;

import ic2.core.block.generator.tileentity.TileEntityBaseGenerator;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTankInfo;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityEnergyCounter;
import shedar.mods.ic2.nuclearcontrol.utils.LiquidStorageHelper;
import shedar.mods.ic2.nuclearcontrol.utils.TextureResolver;

public class ItemKitMultipleSensor extends ItemSensorKitBase {
	public static final int TYPE_COUNTER = 0;
	public static final int TYPE_LIQUID = 1;
	public static final int TYPE_GENERATOR = 2;

	private static final String TEXTURE_KIT_COUNTER = "kitCounter";
	private static final String TEXTURE_KIT_LIQUID = "kitLiquid";
	private static final String TEXTURE_KIT_GENERATOR = "kitGenerator";

	private IIcon iconCounter;
	private IIcon iconLiquid;
	private IIcon iconGenerator;

	public ItemKitMultipleSensor() {
		super("");
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int damage = stack.getItemDamage();
		switch (damage) {
		case TYPE_COUNTER:
			return "item.ItemCounterSensorKit";
		case TYPE_LIQUID:
			return "item.ItemLiquidSensorKit";
		case TYPE_GENERATOR:
			return "item.ItemGeneratorSensorKit";
		}
		return "";
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		iconCounter = iconRegister.registerIcon(TextureResolver
				.getItemTexture(TEXTURE_KIT_COUNTER));
		iconLiquid = iconRegister.registerIcon(TextureResolver
				.getItemTexture(TEXTURE_KIT_LIQUID));
		iconGenerator = iconRegister.registerIcon(TextureResolver
				.getItemTexture(TEXTURE_KIT_GENERATOR));
	}

	@Override
	public IIcon getIconFromDamage(int damage) {
		switch (damage) {
		case TYPE_COUNTER:
			return iconCounter;
		case TYPE_LIQUID:
			return iconLiquid;
		case TYPE_GENERATOR:
			return iconGenerator;
		}
		return null;
	}

	@Override
	protected ChunkCoordinates getTargetCoordinates(World world, int x, int y,
			int z, ItemStack stack) {
		int damage = stack.getItemDamage();

		switch (damage) {
		case TYPE_COUNTER:
			TileEntity entity = world.getTileEntity(x, y, z);
			if (entity != null
					&& (entity instanceof TileEntityEnergyCounter || entity instanceof TileEntityAverageCounter)) {
				return new ChunkCoordinates(x, y, z);
			}
			break;
		case TYPE_LIQUID:
			FluidTankInfo tank = LiquidStorageHelper.getStorageAt(world, x, y,
					z);
			if (tank != null) {
				return new ChunkCoordinates(x, y, z);
			}
			break;
		case TYPE_GENERATOR:
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity != null
					&& tileentity instanceof TileEntityBaseGenerator) {
				return new ChunkCoordinates(x, y, z);
			}
			break;
		default:
			break;
		}
		return null;
	}

	@Override
	protected ItemStack getItemStackByDamage(int damage) {
		switch (damage) {
		case TYPE_COUNTER:
			return new ItemStack(
					IC2NuclearControl.itemMultipleSensorLocationCard,
					1, TYPE_COUNTER);
		case TYPE_LIQUID:
			return new ItemStack(
					IC2NuclearControl.itemMultipleSensorLocationCard,
					1, TYPE_LIQUID);
		case TYPE_GENERATOR:
			return new ItemStack(
					IC2NuclearControl.itemMultipleSensorLocationCard,
					1, TYPE_GENERATOR);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List list) {
		list.add(new ItemStack(item, 1, TYPE_COUNTER));
		list.add(new ItemStack(item, 1, TYPE_LIQUID));
		list.add(new ItemStack(item, 1, TYPE_GENERATOR));
	}

}
