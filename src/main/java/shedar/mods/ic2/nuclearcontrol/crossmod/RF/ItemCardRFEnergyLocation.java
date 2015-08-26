package shedar.mods.ic2.nuclearcontrol.crossmod.RF;

import cofh.api.energy.IEnergyHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;
import shedar.mods.ic2.nuclearcontrol.api.PanelSetting;
import shedar.mods.ic2.nuclearcontrol.api.PanelString;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardEnergySensorLocation;
import shedar.mods.ic2.nuclearcontrol.utils.LangHelper;
import shedar.mods.ic2.nuclearcontrol.utils.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


public class ItemCardRFEnergyLocation extends ItemCardEnergySensorLocation {

    public static final UUID CARD_TYPE = new UUID(0, 3);

    public ItemCardRFEnergyLocation() {
        this.setUnlocalizedName("RFenergyCard");

    }

    @Override
         public CardState update(TileEntity panel, ICardWrapper card, int range) {
        ChunkCoordinates target = card.getTarget();
        TileEntity tile = panel.getWorldObj().getTileEntity(target.posX, target.posY, target.posZ);
        //NCLog.fatal(tile instanceof IEnergyHandler);
        if(tile instanceof IEnergyHandler) {
            IEnergyHandler iEnergyStorage = (IEnergyHandler) tile;
            card.setInt("energyL", iEnergyStorage.getEnergyStored(ForgeDirection.UNKNOWN));
            card.setInt("maxStorageL", iEnergyStorage.getMaxEnergyStored(ForgeDirection.UNKNOWN));
            card.setInt("range_trigger_amount", iEnergyStorage.getEnergyStored(ForgeDirection.UNKNOWN));
            return CardState.OK;
        } else {
            return CardState.NO_TARGET;
        }
    }

    @Override
    public CardState update(World world, ICardWrapper card, int range) {
        ChunkCoordinates target = card.getTarget();
        TileEntity tile = world.getTileEntity(target.posX, target.posY, target.posZ);
        //NCLog.fatal(tile instanceof IEnergyHandler);
        if(tile instanceof IEnergyHandler) {
            IEnergyHandler iEnergyStorage = (IEnergyHandler) tile;
            card.setInt("energyL", iEnergyStorage.getEnergyStored(ForgeDirection.UNKNOWN));
            card.setInt("maxStorageL", iEnergyStorage.getMaxEnergyStored(ForgeDirection.UNKNOWN));
            card.setInt("range_trigger_amount", iEnergyStorage.getEnergyStored(ForgeDirection.UNKNOWN));
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
    public List<PanelString> getStringData(int displaySettings,
                                           ICardWrapper card, boolean showLabels) {
        List<PanelString> result = new LinkedList<PanelString>();
        PanelString line;

        double energy = card.getDouble("energyL");
        double storage = card.getDouble("maxStorageL");

        if ((displaySettings & DISPLAY_ENERGY) > 0) {
            line = new PanelString();
            line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelEnergy", energy, showLabels);
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
                    "msg.nc.InfoPanelEnergyPercentage", storage == 0 ? 100 : (energy * 100 / storage), showLabels);
            result.add(line);
        }
        return result;
    }

    @Override
    public List<PanelSetting> getSettingsList() {
        List<PanelSetting> result = new ArrayList<PanelSetting>(3);
        result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelEnergyCurrent"), DISPLAY_ENERGY, CARD_TYPE));
        result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelEnergyStorage"), DISPLAY_STORAGE, CARD_TYPE));
        result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelEnergyFree"), DISPLAY_FREE, CARD_TYPE));
        result.add(new PanelSetting(LangHelper.translate("msg.nc.cbInfoPanelEnergyPercentage"), DISPLAY_PERCENTAGE, CARD_TYPE));
        return result;
    }
}
