package shedar.mods.ic2.nuclearcontrol.items;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTankInfo;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;
import shedar.mods.ic2.nuclearcontrol.api.PanelSetting;
import shedar.mods.ic2.nuclearcontrol.api.PanelString;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;
import shedar.mods.ic2.nuclearcontrol.utils.LangHelper;
import shedar.mods.ic2.nuclearcontrol.utils.LiquidStorageHelper;
import shedar.mods.ic2.nuclearcontrol.utils.StringUtils;

public class ItemCardLiquidArrayLocation extends ItemCardBase {
	public static final int DISPLAY_NAME = 1;
	public static final int DISPLAY_AMOUNT = 2;
	public static final int DISPLAY_FREE = 4;
	public static final int DISPLAY_CAPACITY = 8;
	public static final int DISPLAY_PERCENTAGE = 16;
	public static final int DISPLAY_EACH = 32;
	public static final int DISPLAY_TOTAL = 64;

	private static final int STATUS_NOT_FOUND = Integer.MIN_VALUE;
	private static final int STATUS_OUT_OF_RANGE = Integer.MIN_VALUE + 1;

	public static final UUID CARD_TYPE = new UUID(0, 3);

	public ItemCardLiquidArrayLocation() {
		super("cardLiquidArray");
	}

	private int[] getCoordinates(ICardWrapper card, int cardNumber) {
		int cardCount = card.getInt("cardCount");
		if (cardNumber >= cardCount)
			return null;
		int[] coordinates = new int[] {
				card.getInt(String.format("_%dx", cardNumber)),
				card.getInt(String.format("_%dy", cardNumber)),
				card.getInt(String.format("_%dz", cardNumber)) };
		return coordinates;
	}

	public static int getCardCount(ICardWrapper card) {
		return card.getInt("cardCount");
	}

	public static void initArray(CardWrapperImpl card, Vector<ItemStack> cards) {
		int cardCount = getCardCount(card);
		for (ItemStack subCard : cards) {
			CardWrapperImpl wrapper = new CardWrapperImpl(subCard, -1);
			ChunkCoordinates target = wrapper.getTarget();
			if (target == null)
				continue;
			card.setInt(String.format("_%dx", cardCount), target.posX);
			card.setInt(String.format("_%dy", cardCount), target.posY);
			card.setInt(String.format("_%dz", cardCount), target.posZ);
			card.setInt(String.format("_%dtargetType", cardCount),
					wrapper.getInt("targetType"));
			cardCount++;
		}
		card.setInt("cardCount", cardCount);
	}

	@Override
	public CardState update(TileEntity panel, ICardWrapper card, int range) {
		int cardCount = getCardCount(card);
		double totalAmount = 0.0;
		if (cardCount == 0) {
			return CardState.INVALID_CARD;
		} else {
			boolean foundAny = false;
			boolean outOfRange = false;
			int liquidId = 0;
			for (int i = 0; i < cardCount; i++) {
				int[] coordinates = getCoordinates(card, i);
				int dx = coordinates[0] - panel.xCoord;
				int dy = coordinates[1] - panel.yCoord;
				int dz = coordinates[2] - panel.zCoord;
				if (Math.abs(dx) <= range && Math.abs(dy) <= range
						&& Math.abs(dz) <= range) {
					FluidTankInfo storage = LiquidStorageHelper.getStorageAt(
							panel.getWorldObj(), coordinates[0],
							coordinates[1], coordinates[2]);
					if (storage != null) {
						if (storage.fluid != null) {
							totalAmount += storage.fluid.amount;
							card.setInt(String.format("_%damount", i),
									storage.fluid.amount);

							if (storage.fluid.getFluidID() != 0
									&& storage.fluid.amount > 0) {
								liquidId = storage.fluid.getFluidID();
							}
							if (liquidId == 0)
								card.setString(String.format("_%dname", i), LangHelper.translate("msg.nc.None"));
							else
								card.setString(String.format("_%dname", i), FluidRegistry.getFluidName(storage.fluid));
						}
						card.setInt(String.format("_%dcapacity", i),
								storage.capacity);
						foundAny = true;
					} else {
						card.setInt(String.format("_%damount", i),
								STATUS_NOT_FOUND);
					}
				} else {
					card.setInt(String.format("_%damount", i),
							STATUS_OUT_OF_RANGE);
					outOfRange = true;
				}
			}
			card.setDouble("energyL", totalAmount);
			if (!foundAny) {
				if (outOfRange)
					return CardState.OUT_OF_RANGE;
				else
					return CardState.NO_TARGET;
			}
			return CardState.OK;
		}
	}

	@Override
	public UUID getCardType() {
		return CARD_TYPE;
	}

	@Override
	public List<PanelString> getStringData(int displaySettings, ICardWrapper card, boolean showLabels) {
		List<PanelString> result = new LinkedList<PanelString>();
		PanelString line;
		double totalAmount = 0;
		double totalCapacity = 0;
		boolean showEach = (displaySettings & DISPLAY_EACH) > 0;
		boolean showSummary = (displaySettings & DISPLAY_TOTAL) > 0;
		boolean showName = (displaySettings & DISPLAY_NAME) > 0;
		boolean showAmount = true;// (displaySettings & DISPLAY_AMOUNT) > 0;
		boolean showFree = (displaySettings & DISPLAY_FREE) > 0;
		boolean showCapacity = (displaySettings & DISPLAY_CAPACITY) > 0;
		boolean showPercentage = (displaySettings & DISPLAY_PERCENTAGE) > 0;
		int cardCount = getCardCount(card);
		for (int i = 0; i < cardCount; i++) {
			int amount = card.getInt(String.format("_%damount", i));
			int capacity = card.getInt(String.format("_%dcapacity", i));
			boolean isOutOfRange = amount == STATUS_OUT_OF_RANGE;
			boolean isNotFound = amount == STATUS_NOT_FOUND;
			if (showSummary && !isOutOfRange && !isNotFound) {
				totalAmount += amount;
				totalCapacity += capacity;
			}

			if (showEach) {
				if (isOutOfRange) {
					line = new PanelString();
					line.textLeft = StringUtils.getFormattedKey("msg.nc.InfoPanelOutOfRangeN", i + 1);
					result.add(line);
				} else if (isNotFound) {
					line = new PanelString();
					line.textLeft = StringUtils.getFormattedKey("msg.nc.InfoPanelNotFoundN", i + 1);
					result.add(line);
				} else {
					if (showName) {
						line = new PanelString();
						if (showLabels)
							line.textLeft = StringUtils.getFormattedKey("msg.nc.InfoPanelLiquidNameN", 
									i + 1, card.getString(String.format("_%dname", i)));
						else
							line.textLeft = StringUtils.getFormatted("", amount, false);
						result.add(line);
					}
					if (showAmount) {
						line = new PanelString();
						if (showLabels)
							line.textLeft = StringUtils.getFormattedKey("msg.nc.InfoPanelLiquidN", 
									i + 1, StringUtils.getFormatted("", amount, false));
						else
							line.textLeft = StringUtils.getFormatted("", amount, false);
						result.add(line);
					}
					if (showFree) {
						line = new PanelString();
						if (showLabels)
							line.textLeft = StringUtils.getFormattedKey("msg.nc.InfoPanelLiquidFreeN", 
									i + 1, StringUtils.getFormatted("", capacity - amount, false));
						else
							line.textLeft = StringUtils.getFormatted("", capacity - amount, false);

						result.add(line);
					}
					if (showCapacity) {
						line = new PanelString();
						if (showLabels)
							line.textLeft = StringUtils.getFormattedKey("msg.nc.InfoPanelLiquidCapacityN", 
									i + 1, StringUtils.getFormatted("", capacity, false));
						else
							line.textLeft = StringUtils.getFormatted("", capacity, false);
						result.add(line);
					}
					if (showPercentage) {
						line = new PanelString();
						if (showLabels)
							line.textLeft = StringUtils.getFormattedKey("msg.nc.InfoPanelLiquidPercentageN", 
									i + 1, StringUtils.getFormatted("", 
											capacity == 0 ? 100 : (((double) amount / capacity) * 100), false));
						else
							line.textLeft = StringUtils.getFormatted("", 
									capacity == 0 ? 100 : (((double) amount / capacity) * 100), false);
						result.add(line);
					}
				}
			}
		}
		if (showSummary) {
			if (showAmount) {
				line = new PanelString();
				line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelLiquidAmount", totalAmount, showLabels);
				result.add(line);
			}
			if (showFree) {
				line = new PanelString();
				line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelLiquidFree", 
						totalCapacity - totalAmount, showLabels);
				result.add(line);
			}
			if (showName) {
				line = new PanelString();
				line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelLiquidCapacity", totalCapacity, showLabels);
				result.add(line);
			}
			if (showPercentage) {
				line = new PanelString();
				line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelLiquidPercentage", 
						totalCapacity == 0 ? 100 : ((totalAmount / totalCapacity) * 100), showLabels);
				result.add(line);
			}
		}
		return result;
	}

	@Override
	public List<PanelSetting> getSettingsList() {
		List<PanelSetting> result = new ArrayList<PanelSetting>(3);
		result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelLiquidName"), DISPLAY_NAME, CARD_TYPE));
		// result.add(new
		// PanelSetting(LanguageHelper.translate("msg.nc.cbInfoPanelLiquidAmount"),
		// DISPLAY_AMOUNT, CARD_TYPE));
		result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelLiquidFree"), DISPLAY_FREE, CARD_TYPE));
		result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelLiquidCapacity"), DISPLAY_CAPACITY, CARD_TYPE));
		result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelLiquidPercentage"), DISPLAY_PERCENTAGE, CARD_TYPE));
		result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelLiquidEach"), DISPLAY_EACH, CARD_TYPE));
		result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelLiquidTotal"), DISPLAY_TOTAL, CARD_TYPE));
		return result;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean advanced) {
		CardWrapperImpl card = new CardWrapperImpl(itemStack, -1);
		int cardCount = getCardCount(card);
		if (cardCount > 0) {
			String title = card.getTitle();
			if (title != null && !title.isEmpty()) {
				info.add(title);
			}
			String hint = String.format(LangHelper.translate("msg.nc.LiquidCardQuantity"), cardCount);
			info.add(hint);
		}
	}
}
