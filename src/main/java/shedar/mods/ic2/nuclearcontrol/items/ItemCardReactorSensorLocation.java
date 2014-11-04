package shedar.mods.ic2.nuclearcontrol.items;

import ic2.api.reactor.IReactor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import shedar.mods.ic2.nuclearcontrol.api.CardHelper;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;
import shedar.mods.ic2.nuclearcontrol.api.IRemoteSensor;
import shedar.mods.ic2.nuclearcontrol.api.PanelSetting;
import shedar.mods.ic2.nuclearcontrol.api.PanelString;
import shedar.mods.ic2.nuclearcontrol.utils.LangHelper;
import shedar.mods.ic2.nuclearcontrol.utils.NuclearHelper;
import shedar.mods.ic2.nuclearcontrol.utils.StringUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCardReactorSensorLocation extends ItemCardBase implements
		IRemoteSensor {
	protected static final String HINT_TEMPLATE = "x: %d, y: %d, z: %d";

	public static final int DISPLAY_ONOFF = 1;
	public static final int DISPLAY_HEAT = 2;
	public static final int DISPLAY_MAXHEAT = 4;
	public static final int DISPLAY_OUTPUT = 8;
	public static final int DISPLAY_TIME = 16;
	public static final int DISPLAY_MELTING = 32;

	public static final UUID CARD_TYPE = new UUID(0, 0);

	public ItemCardReactorSensorLocation() {
		super("cardReactor");
	}

	@Override
	public CardState update(TileEntity panel, ICardWrapper card, int range) {
		ChunkCoordinates target = card.getTarget();
		IReactor reactor = NuclearHelper.getReactorAt(panel.getWorldObj(),
				target.posX, target.posY, target.posZ);
		if (reactor != null) {
			card.setInt("heat", reactor.getHeat());
			card.setInt("maxHeat", reactor.getMaxHeat());
			card.setBoolean("reactorPoweredB",
					NuclearHelper.isProducing(reactor));
			card.setInt("output",
					(int) Math.round(reactor.getReactorEUEnergyOutput()));
			card.setBoolean("isSteam", NuclearHelper.isSteam(reactor));

			IInventory inventory = (IInventory) reactor;
			int slotCount = inventory.getSizeInventory();
			int timeLeft = 0;
			for (int i = 0; i < slotCount; i++) {
				ItemStack rStack = inventory.getStackInSlot(i);
				if (rStack != null) {
					timeLeft = Math.max(timeLeft,
							NuclearHelper.getNuclearCellTimeLeft(rStack));
				}
			}
			card.setInt("timeLeft", timeLeft * reactor.getTickRate() / 20);
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
		ICardWrapper helper = CardHelper.getWrapper(itemStack);
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
		String text;
		PanelString line;
		if ((displaySettings & DISPLAY_HEAT) > 0) {
			line = new PanelString();
			line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelHeat",
					card.getInt("heat"), showLabels);
			result.add(line);
		}
		if ((displaySettings & DISPLAY_MAXHEAT) > 0) {
			line = new PanelString();
			line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelMaxHeat",
					card.getInt("maxHeat"), showLabels);
			result.add(line);
		}
		if ((displaySettings & DISPLAY_MELTING) > 0) {
			line = new PanelString();
			line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelMelting",
					card.getInt("maxHeat") * 85 / 100, showLabels);
			result.add(line);
		}
		if ((displaySettings & DISPLAY_OUTPUT) > 0) {
			line = new PanelString();
			if (card.getBoolean("isSteam")) {
				line.textLeft = StringUtils.getFormatted(
						"msg.nc.InfoPanelOutputSteam",
						NuclearHelper.euToSteam(card.getInt("output")),
						showLabels);
			} else {
				line.textLeft = StringUtils.getFormatted(
						"msg.nc.InfoPanelOutput", card.getInt("output"),
						showLabels);
			}
			result.add(line);
		}
		int timeLeft = card.getInt("timeLeft");
		if ((displaySettings & DISPLAY_TIME) > 0) {
			int hours = timeLeft / 3600;
			int minutes = (timeLeft % 3600) / 60;
			int seconds = timeLeft % 60;
			line = new PanelString();

			String time = String
					.format("%d:%02d:%02d", hours, minutes, seconds);
			line.textLeft = StringUtils.getFormatted(
					"msg.nc.InfoPanelTimeRemaining", time, showLabels);
			result.add(line);
		}

		int txtColor = 0;
		if ((displaySettings & DISPLAY_ONOFF) > 0) {
			boolean reactorPowered = card.getBoolean("reactorPoweredB");
			if (reactorPowered) {
				txtColor = 0x00ff00;
				text = LangHelper.translate("msg.nc.InfoPanelOn");
			} else {
				txtColor = 0xff0000;
				text = LangHelper.translate("msg.nc.InfoPanelOff");
			}
			if (result.size() > 0) {
				PanelString firstLine = result.get(0);
				firstLine.textRight = text;
				firstLine.colorRight = txtColor;
			} else {
				line = new PanelString();
				line.textLeft = text;
				line.colorLeft = txtColor;
				result.add(line);
			}
		}
		return result;
	}

	@Override
	public List<PanelSetting> getSettingsList() {
		List<PanelSetting> result = new ArrayList<PanelSetting>(6);
		result.add(new PanelSetting(LangHelper
				.translate("msg.nc.cbInfoPanelOnOff"), DISPLAY_ONOFF, CARD_TYPE));
		result.add(new PanelSetting(LangHelper
				.translate("msg.nc.cbInfoPanelHeat"), DISPLAY_HEAT, CARD_TYPE));
		result.add(new PanelSetting(LangHelper
				.translate("msg.nc.cbInfoPanelMaxHeat"), DISPLAY_MAXHEAT,
				CARD_TYPE));
		result.add(new PanelSetting(LangHelper
				.translate("msg.nc.cbInfoPanelMelting"), DISPLAY_MELTING,
				CARD_TYPE));
		result.add(new PanelSetting(LangHelper
				.translate("msg.nc.cbInfoPanelOutput"), DISPLAY_OUTPUT,
				CARD_TYPE));
		result.add(new PanelSetting(LangHelper
				.translate("msg.nc.cbInfoPanelTimeRemaining"), DISPLAY_TIME,
				CARD_TYPE));
		return result;
	}

}
