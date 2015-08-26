package shedar.mods.ic2.nuclearcontrol.items;

import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.crossmod.EnergyStorageData;

import ic2.api.energy.EnergyNet;
import ic2.core.block.generator.tileentity.TileEntityBaseGenerator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTankInfo;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;
import shedar.mods.ic2.nuclearcontrol.api.IPanelMultiCard;
import shedar.mods.ic2.nuclearcontrol.api.IRangeTriggerable;
import shedar.mods.ic2.nuclearcontrol.api.IRemoteSensor;
import shedar.mods.ic2.nuclearcontrol.api.PanelSetting;
import shedar.mods.ic2.nuclearcontrol.api.PanelString;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAverageCounter;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityEnergyCounter;
import shedar.mods.ic2.nuclearcontrol.utils.LangHelper;
import shedar.mods.ic2.nuclearcontrol.utils.LiquidStorageHelper;
import shedar.mods.ic2.nuclearcontrol.utils.StringUtils;
import shedar.mods.ic2.nuclearcontrol.utils.TextureResolver;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCardMultipleSensorLocation extends ItemCardBase implements IRemoteSensor, IPanelMultiCard, IRangeTriggerable {
	private static final String HINT_TEMPLATE = "x: %d, y: %d, z: %d";

	public static final int DISPLAY_ENERGY = 1;

	public static final int DISPLAY_LIQUID_NAME = 1;
	public static final int DISPLAY_LIQUID_AMOUNT = 2;
	public static final int DISPLAY_LIQUID_FREE = 4;
	public static final int DISPLAY_LIQUID_CAPACITY = 8;
	public static final int DISPLAY_LIQUID_PERCENTAGE = 16;

	private static final UUID CARD_TYPE_COUNTER = new UUID(0, 4);
	private static final UUID CARD_TYPE_LIQUID = UUID.fromString("210dc1f0-118c-48ee-9d08-42bfbee1ea15");
	private static final UUID CARD_TYPE_GENERATOR = UUID.fromString("210dc1f0-118c-48ee-9d08-42bfbee1ea16");

	private static final String TEXTURE_CARD_COUNTER = "cardCounter";
	private static final String TEXTURE_CARD_LIQUID = "cardLiquid";
	private static final String TEXTURE_CARD_GENERATOR = "cardGenerator";

	private IIcon iconCounter;
	private IIcon iconLiquid;
	private IIcon iconGenerator;

	public ItemCardMultipleSensorLocation() {
		super("");
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		iconCounter = iconRegister.registerIcon(TextureResolver.getItemTexture(TEXTURE_CARD_COUNTER));
		iconLiquid = iconRegister.registerIcon(TextureResolver.getItemTexture(TEXTURE_CARD_LIQUID));
		iconGenerator = iconRegister.registerIcon(TextureResolver.getItemTexture(TEXTURE_CARD_GENERATOR));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int damage = stack.getItemDamage();
		switch (damage) {
		case ItemKitMultipleSensor.TYPE_COUNTER:
			return "item.itemCounterSensorLocationCard";
		case ItemKitMultipleSensor.TYPE_LIQUID:
			return "item.ItemLiquidSensorLocationCard";
		case ItemKitMultipleSensor.TYPE_GENERATOR:
			return "item.ItemGeneratorSensorLocationCard";
		}
		return "";
	}

	@Override
	public CardState update(TileEntity panel, ICardWrapper card, int range) {
		int damage = card.getItemStack().getItemDamage();
		switch (damage) {
		case ItemKitMultipleSensor.TYPE_COUNTER:
			return updateCounter(panel.getWorldObj(), card, range);
		case ItemKitMultipleSensor.TYPE_LIQUID:
			return updateLiquid(panel.getWorldObj(), card, range);
		case ItemKitMultipleSensor.TYPE_GENERATOR:
			return updateGenerator(panel.getWorldObj(), card, range);
		}
		return CardState.INVALID_CARD;
	}

	@Override
	public CardState update(World world, ICardWrapper card, int range) {
		int damage = card.getItemStack().getItemDamage();
		switch (damage) {
			case ItemKitMultipleSensor.TYPE_COUNTER:
				return updateCounter(world, card, range);
			case ItemKitMultipleSensor.TYPE_LIQUID:
				return updateLiquid(world, card, range);
			case ItemKitMultipleSensor.TYPE_GENERATOR:
				return updateGenerator(world, card, range);
		}
		return CardState.INVALID_CARD;
	}

	public CardState updateLiquid(World world, ICardWrapper card, int range) {
		ChunkCoordinates target = card.getTarget();
		FluidTankInfo storage = LiquidStorageHelper.getStorageAt(world, target.posX, target.posY, target.posZ);
		if (storage != null) {
			int capacity = storage.capacity;
			int amount = 0;
			int liquidId = 0;
			NBTTagCompound liquidTag = null;
			if (storage.fluid != null) {
				amount = storage.fluid.amount;
				if (storage.fluid.getFluidID() != 0 && amount > 0) {
					liquidId = storage.fluid.getFluidID();
					liquidTag = storage.fluid.tag;
				}
			}
			card.setInt("capacity", capacity);
			card.setInt("amount", amount);
			card.setInt("liquidId", liquidId);
			card.setTag("liquidTag", liquidTag);
			card.setDouble("range_trigger_amount", (double)amount);
			return CardState.OK;
		} else {
			return CardState.NO_TARGET;
		}
	}

	public CardState updateCounter(World world, ICardWrapper card, int range) {
		ChunkCoordinates target = card.getTarget();
		TileEntity tileEntity = world.getTileEntity(target.posX, target.posY, target.posZ);
		if (tileEntity != null && tileEntity instanceof TileEntityEnergyCounter) {
			TileEntityEnergyCounter counter = (TileEntityEnergyCounter) tileEntity;
			card.setDouble("energy", counter.counter);
			card.setDouble("range_trigger_amount", counter.counter);
			card.setInt("powerType", (int) counter.powerType);
			return CardState.OK;
		} else if (tileEntity != null && tileEntity instanceof TileEntityAverageCounter) {
			TileEntityAverageCounter avgCounter = (TileEntityAverageCounter) tileEntity;
			card.setInt("average", avgCounter.getClientAverage());
			card.setDouble("range_trigger_amount", (double)avgCounter.getClientAverage());
			card.setInt("powerType", (int) avgCounter.powerType);
			return CardState.OK;
		} else {
			return CardState.NO_TARGET;
		}
	}

	public CardState updateGenerator(World world, ICardWrapper card, int range) {
		ChunkCoordinates target = card.getTarget();
		TileEntity entity = world.getTileEntity(target.posX, target.posY, target.posZ);
		if (entity instanceof TileEntityBaseGenerator) {
			// int production = ((TileEntityBaseGenerator)entity).production;
			int production = (int) EnergyNet.instance.getTotalEnergyEmitted(entity);// TODO deprecated
			card.setInt("production", production);
			card.setDouble("range_trigger_amount", (double)production);
			return CardState.OK;
		} else {
			return CardState.NO_TARGET;
		}
	}

	@Override
	public List<PanelSetting> getSettingsList(ICardWrapper card) {
		int damage = card.getItemStack().getItemDamage();
		switch (damage) {
		case ItemKitMultipleSensor.TYPE_COUNTER:
			return getSettingsListCounter();
		case ItemKitMultipleSensor.TYPE_LIQUID:
			return getSettingsListLiquid();
		case ItemKitMultipleSensor.TYPE_GENERATOR:
			return getSettingsListGenerator();
		}
		return null;
	}

	@Override
	public UUID getCardType(ICardWrapper card) {
		int damage = card.getItemStack().getItemDamage();
		switch (damage) {
		case ItemKitMultipleSensor.TYPE_COUNTER:
			return CARD_TYPE_COUNTER;
		case ItemKitMultipleSensor.TYPE_LIQUID:
			return CARD_TYPE_LIQUID;
		case ItemKitMultipleSensor.TYPE_GENERATOR:
			return CARD_TYPE_GENERATOR;
		}
		return null;
	}

	@Override
	public List<PanelString> getStringData(int displaySettings, ICardWrapper card, boolean showLabels) {
		int damage = card.getItemStack().getItemDamage();
		switch (damage) {
		case ItemKitMultipleSensor.TYPE_COUNTER:
			return getStringDataCounter(displaySettings, card, showLabels);
		case ItemKitMultipleSensor.TYPE_LIQUID:
			return getStringDataLiquid(displaySettings, card, showLabels);
		case ItemKitMultipleSensor.TYPE_GENERATOR:
			return getStringDataGenerator(displaySettings, card, showLabels);
		}
		return null;
	}

	public List<PanelString> getStringDataLiquid(int displaySettings, ICardWrapper card, boolean showLabels) {
		List<PanelString> result = new LinkedList<PanelString>();
		PanelString line;

		int capacity = card.getInt("capacity");
		int amount = card.getInt("amount");

		if ((displaySettings & DISPLAY_LIQUID_NAME) > 0) {
			int liquidId = card.getInt("liquidId");
			String name;
			if (liquidId == 0)
				name = LangHelper.translate("msg.nc.None");
			else
				name = FluidRegistry.getFluidName(liquidId);
			line = new PanelString();
			line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelLiquidName", name, showLabels);
			result.add(line);
		}
		if ((displaySettings & DISPLAY_LIQUID_AMOUNT) > 0) {
			line = new PanelString();
			line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelLiquidAmount", amount, showLabels);
			result.add(line);
		}
		if ((displaySettings & DISPLAY_LIQUID_FREE) > 0) {
			line = new PanelString();
			line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelLiquidFree", capacity - amount, showLabels);
			result.add(line);
		}
		if ((displaySettings & DISPLAY_LIQUID_CAPACITY) > 0) {
			line = new PanelString();
			line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelLiquidCapacity", capacity, showLabels);
			result.add(line);
		}
		if ((displaySettings & DISPLAY_LIQUID_PERCENTAGE) > 0) {
			line = new PanelString();
			line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelLiquidPercentage", 
					capacity == 0 ? 100 : (amount * 100 / capacity), showLabels);
			result.add(line);
		}
		return result;
	}

	public List<PanelString> getStringDataCounter(int displaySettings, ICardWrapper card, boolean showLabels) {
		List<PanelString> result = new LinkedList<PanelString>();
		PanelString line;
		if (card.hasField("average")) {// average counter
			if ((displaySettings & DISPLAY_ENERGY) > 0) {
				line = new PanelString();
				String key = card.getInt("powerType") == EnergyStorageData.TARGET_TYPE_IC2 ? 
						"msg.nc.InfoPanelOutput" : "msg.nc.InfoPanelOutputMJ";
				line.textLeft = StringUtils.getFormatted(key, card.getInt("average"), showLabels);
				result.add(line);
			}
		} else {// energy counter
			if ((displaySettings & DISPLAY_ENERGY) > 0) {
				double energy = card.getDouble("energy");
				line = new PanelString();
				String key = card.getInt("powerType") == EnergyStorageData.TARGET_TYPE_IC2 ? 
						"msg.nc.InfoPanelEnergyCounter" : "msg.nc.InfoPanelEnergyCounterMJ";
				line.textLeft = StringUtils.getFormatted(key, energy, showLabels);
				result.add(line);
			}
		}
		return result;
	}

	public List<PanelString> getStringDataGenerator(int displaySettings, ICardWrapper card, boolean showLabels) {
		List<PanelString> result = new LinkedList<PanelString>();
		PanelString line = new PanelString();
		line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelOutput", card.getInt("production"), showLabels);
		result.add(line);
		return result;
	}

	@Override
	public IIcon getIconFromDamage(int damage) {
		switch (damage) {
		case ItemKitMultipleSensor.TYPE_COUNTER:
			return iconCounter;
		case ItemKitMultipleSensor.TYPE_LIQUID:
			return iconLiquid;
		case ItemKitMultipleSensor.TYPE_GENERATOR:
			return iconGenerator;
		}
		return null;
	}

	public List<PanelSetting> getSettingsListCounter() {
		List<PanelSetting> result = new ArrayList<PanelSetting>(3);
		result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelEnergyCurrent"), 
				DISPLAY_ENERGY, CARD_TYPE_COUNTER));
		return result;
	}

	public List<PanelSetting> getSettingsListLiquid() {
		List<PanelSetting> result = new ArrayList<PanelSetting>(3);
		result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelLiquidName"), 
				DISPLAY_LIQUID_NAME, CARD_TYPE_LIQUID));
		result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelLiquidAmount"), 
				DISPLAY_LIQUID_AMOUNT, CARD_TYPE_LIQUID));
		result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelLiquidFree"),
				DISPLAY_LIQUID_FREE, CARD_TYPE_LIQUID));
		result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelLiquidCapacity"),
				DISPLAY_LIQUID_CAPACITY, CARD_TYPE_LIQUID));
		result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelLiquidPercentage"),
				DISPLAY_LIQUID_PERCENTAGE, CARD_TYPE_LIQUID));
		return result;
	}

	public List<PanelSetting> getSettingsListGenerator() {
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean advanced) {
		CardWrapperImpl helper = new CardWrapperImpl(itemStack, -1);
		ChunkCoordinates target = helper.getTarget();
		if (target != null) {
			String title = helper.getTitle();
			if (title != null && !title.isEmpty()) {
				info.add(title);
			}
			String hint = String.format(HINT_TEMPLATE, target.posX, target.posY, target.posZ);
			info.add(hint);
		}
	}

	@Override
	public List<PanelSetting> getSettingsList() {
		return null;
	}

	@Override
	public UUID getCardType() {
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		list.add(new ItemStack(item, 1, ItemKitMultipleSensor.TYPE_COUNTER));
		list.add(new ItemStack(item, 1, ItemKitMultipleSensor.TYPE_LIQUID));
		list.add(new ItemStack(item, 1, ItemKitMultipleSensor.TYPE_GENERATOR));
	}

}
