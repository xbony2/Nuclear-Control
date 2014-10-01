package shedar.mods.ic2.nuclearcontrol.api;

import java.util.List;
import java.util.UUID;

public interface IPanelMultiCard {

	List<PanelSetting> getSettingsList(ICardWrapper card);

	UUID getCardType(ICardWrapper card);
}
