package shedar.mods.ic2.nuclearcontrol.crossmod.ic2;

import java.lang.reflect.Method;
import java.util.Arrays;

import ic2.api.item.IC2Items;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.crossmod.data.EnergyStorageData;

public class CrossIndustrialCraft2 {

	private int _IC2WrenchId;
	private int _IC2ElectricWrenchId;

	private Method _getMaxDamageEx;
	private Method _getDamageOfStack;
	private Class _gradItemInt;

	private boolean _isApiAvailable = false;
	private boolean _isIdInitialized = false;

	private int[] _fuelIds = null;

	private void initIds() {
		if (!_isApiAvailable || _isIdInitialized)
			return;
		_fuelIds = new int[6];
		_fuelIds[0] = Item.getIdFromItem(IC2Items.getItem(
				"reactorUraniumSimple").getItem());
		_fuelIds[1] = Item.getIdFromItem(IC2Items.getItem("reactorUraniumDual")
				.getItem());
		_fuelIds[2] = Item.getIdFromItem(IC2Items.getItem("reactorUraniumQuad")
				.getItem());

		_fuelIds[3] = Item.getIdFromItem(IC2Items.getItem("reactorMOXSimple")
				.getItem());
		_fuelIds[4] = Item.getIdFromItem(IC2Items.getItem("reactorMOXDual")
				.getItem());
		_fuelIds[5] = Item.getIdFromItem(IC2Items.getItem("reactorMOXQuad")
				.getItem());

		Arrays.sort(_fuelIds);

		_IC2WrenchId = Item.getIdFromItem(IC2Items.getItem("wrench").getItem());
		_IC2ElectricWrenchId = Item.getIdFromItem(IC2Items.getItem(
				"electricWrench").getItem());
		_isIdInitialized = true;
	}

	public boolean isApiAvailable() {
		return _isApiAvailable;
	}

	public boolean isWrench(ItemStack itemStack) {
		initIds();
		return _isApiAvailable
				&& (Item.getIdFromItem(itemStack.getItem()) == _IC2WrenchId || Item
						.getIdFromItem(itemStack.getItem()) == _IC2ElectricWrenchId);
	}

	@SuppressWarnings("unchecked")
	public CrossIndustrialCraft2() {
		try {
			Class.forName("ic2.api.tile.IEnergyStorage", false, this.getClass()
					.getClassLoader());
			_gradItemInt = Class.forName("ic2.core.item.ItemGradualInt", false,
					this.getClass().getClassLoader());
			_getMaxDamageEx = _gradItemInt.getMethod("getMaxDamageEx");
			_getDamageOfStack = _gradItemInt.getMethod("getDamageOfStack",
					ItemStack.class);
			_isApiAvailable = true;
		} catch (Exception e) {
			_isApiAvailable = false;
		}
	}

	public EnergyStorageData getStorageData(TileEntity target) {
		if (!_isApiAvailable || target == null)
			return null;
		if (target instanceof IEnergyStorage) {
			IEnergyStorage storage = (IEnergyStorage) target;
			EnergyStorageData result = new EnergyStorageData();
			result.capacity = storage.getCapacity();
			result.stored = storage.getStored();
			result.units = "EU";
			result.type = EnergyStorageData.TARGET_TYPE_IC2;
			return result;
		}
		return null;
	}

	public int getNuclearCellTimeLeft(ItemStack stack) {
		if (!_isApiAvailable || stack == null)
			return -1;
		initIds();
		if (Arrays.binarySearch(_fuelIds, Item.getIdFromItem(stack.getItem())) >= 0) {
			int delta;
			try {
				int maxDamage = (Integer) _getMaxDamageEx.invoke(stack
						.getItem());
				int damage = (Integer) _getDamageOfStack.invoke(
						stack.getItem(), stack);
				delta = maxDamage - damage;
			} catch (Exception e) {
				delta = stack.getMaxDamage() - stack.getItemDamage();
			}
			return delta;
		}
		return -1;
	}

}