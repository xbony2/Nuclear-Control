package shedar.mods.ic2.nuclearcontrol.items;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;
import shedar.mods.ic2.nuclearcontrol.api.IRemoteSensor;
import shedar.mods.ic2.nuclearcontrol.api.PanelSetting;
import shedar.mods.ic2.nuclearcontrol.api.PanelString;
import shedar.mods.ic2.nuclearcontrol.crossmod.data.EnergyStorageData;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;
import shedar.mods.ic2.nuclearcontrol.utils.EnergyStorageHelper;
import shedar.mods.ic2.nuclearcontrol.utils.LanguageHelper;
import shedar.mods.ic2.nuclearcontrol.utils.StringUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCardEnergySensorLocation extends ItemCardBase implements
		IRemoteSensor {
	private static final String HINT_TEMPLATE = "x: %d, y: %d, z: %d";

	public static final int DISPLAY_ENERGY = 1;
	public static final int DISPLAY_FREE = 2;
	public static final int DISPLAY_STORAGE = 4;
	public static final int DISPLAY_PERCENTAGE = 8;

	public static final UUID CARD_TYPE = new UUID(0, 2);

	public ItemCardEnergySensorLocation() {
		super("cardEnergy");
	}

	@Override
	public CardState update(TileEntity panel, ICardWrapper card, int range) {
		ChunkCoordinates target = card.getTarget();
		int targetType = card.getInt("targetType");
		EnergyStorageData storage = EnergyStorageHelper.getStorageAt(
				panel.getWorldObj(), target.posX, target.posY, target.posZ,
				targetType);
		if (storage != null) {
			card.setDouble("energyL", storage.stored);
			card.setDouble("maxStorageL", storage.capacity);
			return CardState.OK;
		} else {
			return CardState.NO_TARGET;
		}
	}

	@Override
	public UUID getCardType() {
		return CARD_TYPE;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addInformation(ItemStack itemStack, EntityPlayer player,
			List info, boolean advanced) {
		CardWrapperImpl helper = new CardWrapperImpl(itemStack, -1);
		ChunkCoordinates target = helper.getTarget();
		if (target != null) {
			String title = helper.getTitle();
			if (title != null && !title.isEmpty()) {
				info.add(title);
			}
			String hint = String.format(HINT_TEMPLATE, target.posX,
					target.posY, target.posZ);
			info.add(hint);
		}
	}

	@Override
	public List<PanelString> getStringData(int displaySettings,
			ICardWrapper card, boolean showLabels) {
		List<PanelString> result = new LinkedList<PanelString>();
		PanelString line;

		double energy = card.getDouble("energyL");
		double storage = card.getDouble("maxStorageL");

		if ((displaySettings & DISPLAY_ENERGY) > 0) {
			line = new PanelString();
			line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelEnergy",
					energy, showLabels);
			result.add(line);
		}
		if ((displaySettings & DISPLAY_FREE) > 0) {
			line = new PanelString();
			line.textLeft = StringUtils.getFormatted(
					"msg.nc.InfoPanelEnergyFree", storage - energy, showLabels);
			result.add(line);
		}
		if ((displaySettings & DISPLAY_STORAGE) > 0) {
			line = new PanelString();
			line.textLeft = StringUtils.getFormatted(
					"msg.nc.InfoPanelEnergyStorage", storage, showLabels);
			result.add(line);
		}
		if ((displaySettings & DISPLAY_PERCENTAGE) > 0) {
			line = new PanelString();
			line.textLeft = StringUtils.getFormatted(
					"msg.nc.InfoPanelEnergyPercentage", storage == 0 ? 100
							: (energy * 100 / storage), showLabels);
			result.add(line);
		}
		return result;
	}

	@Override
	public List<PanelSetting> getSettingsList() {
		List<PanelSetting> result = new ArrayList<PanelSetting>(3);
		result.add(new PanelSetting(LanguageHelper
				.translate("msg.nc.cbInfoPanelEnergyCurrent"), DISPLAY_ENERGY,
				CARD_TYPE));
		result.add(new PanelSetting(LanguageHelper
				.translate("msg.nc.cbInfoPanelEnergyStorage"), DISPLAY_STORAGE,
				CARD_TYPE));
		result.add(new PanelSetting(LanguageHelper
				.translate("msg.nc.cbInfoPanelEnergyFree"), DISPLAY_FREE,
				CARD_TYPE));
		result.add(new PanelSetting(LanguageHelper
				.translate("msg.nc.cbInfoPanelEnergyPercentage"),
				DISPLAY_PERCENTAGE, CARD_TYPE));
		return result;
	}

}
