package shedar.mods.ic2.nuclearcontrol.crossmod.appeng;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;
import shedar.mods.ic2.nuclearcontrol.api.PanelSetting;
import shedar.mods.ic2.nuclearcontrol.api.PanelString;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardEnergySensorLocation;
import shedar.mods.ic2.nuclearcontrol.utils.LangHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


public class ItemCardAppeng extends ItemCardEnergySensorLocation {

    public ItemCardAppeng() {
        //this.setTextureName("nuclearcontrol:cardRFReactor");
    }


    public static final int DISPLAY_BYTES = 1;
    public static final int DISPLAY_ITEMS = 2;
    //public static final int DISPLAY_ENERGY = 4;
    //public static final int DISPLAY_PERCENTAGE = 8;
    //public static final int DISPLAY_TEMP = 16;
    public static final UUID CARD_TYPE1 = new UUID(0, 2);

    @Override
    public UUID getCardType() {
        return CARD_TYPE1;
    }

    @Override
    public CardState update(TileEntity panel, ICardWrapper card, int range) {
        ChunkCoordinates target = card.getTarget();
        //int targetType = card.getInt("targetType");
        TileEntity check = panel.getWorldObj().getTileEntity(target.posX, target.posY, target.posZ);
        if(check instanceof TileEntityNetworkLink){
            TileEntityNetworkLink tileNetworkLink = (TileEntityNetworkLink) check;
            card.setInt("ByteTotal", tileNetworkLink.getTOTALBYTES());
            card.setInt("UsedBytes", tileNetworkLink.getUSEDBYTES());
            card.setInt("ItemsTotal", tileNetworkLink.getITEMTYPETOTAL());
            card.setInt("UsedItems", tileNetworkLink.getUSEDITEMTYPE());
            return CardState.OK;
        }else{
            return CardState.NO_TARGET;
        }
    }

    @Override
    public List<PanelString> getStringData(int displaySettings, ICardWrapper card, boolean showLabels) {
        List<PanelString> result = new LinkedList<PanelString>();
        PanelString line;

        int byteTotal = card.getInt("ByteTotal");
        int usedBytes = card.getInt("UsedBytes");
        int items = card.getInt("ItemsTotal");
        int itemsUsed = card.getInt("UsedItems");

        //Total Bytes
        if ((displaySettings & DISPLAY_BYTES) > 0) {
            line = new PanelString();
            line.textLeft = String.format("Bytes: %s / %s", usedBytes, byteTotal);
            result.add(line);
        }

        //Stored Energy
        if ((displaySettings & DISPLAY_ITEMS) > 0) {
            line = new PanelString();
            line.textLeft = String.format("Items: %s / %s", itemsUsed, items);
            result.add(line);
        }

        return result;
    }

    @Override
    public List<PanelSetting> getSettingsList() {
        List<PanelSetting> result = new ArrayList<PanelSetting>(5);
        result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelOnOff"), DISPLAY_BYTES, CARD_TYPE));
        result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelEnergyCurrent"), DISPLAY_ITEMS, CARD_TYPE));
        //result.add(new PanelSetting(LangHelper.translate("msg.nc.InfoPanelRF.Output"), DISPLAY_OUTPUT, CARD_TYPE));
        //result.add(new PanelSetting(LangHelper.translate("msg.nc.InfoPanelRF.TempPROPER"), DISPLAY_TEMP, CARD_TYPE));
        //result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelEnergyPercentage"), DISPLAY_PERCENTAGE, CARD_TYPE));
        return result;
    }
}