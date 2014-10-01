package shedar.mods.ic2.nuclearcontrol.items;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.tileentity.TileEntity;

import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;
import shedar.mods.ic2.nuclearcontrol.api.PanelSetting;
import shedar.mods.ic2.nuclearcontrol.api.PanelString;
import shedar.mods.ic2.nuclearcontrol.utils.LanguageHelper;
import shedar.mods.ic2.nuclearcontrol.utils.StringUtils;
import cpw.mods.fml.client.FMLClientHandler;

public class ItemTimeCard extends ItemCardBase {

	public static final UUID CARD_TYPE = new UUID(0, 1);
	public static final int MODE_24H = 1;

	public ItemTimeCard() {
		super("cardTime");
	}

	@Override
	public CardState update(TileEntity panel, ICardWrapper card, int range) {
		return CardState.OK;
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public List<PanelString> getStringData(int displaySettings,
			ICardWrapper card, boolean showLabels) {
		List<PanelString> result = new ArrayList<PanelString>(1);
		PanelString item = new PanelString();
		result.add(item);
		int time = (int) ((FMLClientHandler.instance().getClient().theWorld
				.getWorldTime() + 6000) % 24000);
		int hours = time / 1000;
		int minutes = (time % 1000) * 6 / 100;
		String suffix = "";

		if ((displaySettings & MODE_24H) == 0) {
			suffix = hours < 12 ? "AM" : "PM";
			hours %= 12;
			if (hours == 0)
				hours += 12;
		}

		item.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelTime",
				String.format("%02d:%02d%s", hours, minutes, suffix),
				showLabels);
		return result;
	}

	@Override
	public List<PanelSetting> getSettingsList() {
		List<PanelSetting> result = new ArrayList<PanelSetting>(1);
		result.add(new PanelSetting(LanguageHelper.translate("msg.nc.cb24h"),
				MODE_24H, CARD_TYPE));
		return result;
	}

	@Override
	public UUID getCardType() {
		return CARD_TYPE;
	}

}
