package shedar.mods.ic2.nuclearcontrol.crossmod.appeng;

import appeng.api.networking.events.MENetworkCellArrayUpdate;
import appeng.api.networking.events.MENetworkEventSubscribe;

/**
 * Created by David on 4/22/2015.
 */
public class MEupdateEvent {

    @MENetworkEventSubscribe
    public void updateEvent(MENetworkCellArrayUpdate e){
        TileEntityNetworkLink.updateNetworkCache();
    }
}
