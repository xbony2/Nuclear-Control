package shedar.mods.ic2.nuclearcontrol.crossmod.appeng;

import appeng.tile.crafting.TileCraftingMonitorTile;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
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


public class ItemCardAppeng extends ItemCardEnergySensorLocation {

    public ItemCardAppeng() {
        //this.setTextureName("nuclearcontrol:cardRFReactor");
        this.setUnlocalizedName("AppengCard");
    }


    public static final int DISPLAY_BYTES = 1;
    public static final int DISPLAY_ITEMS = 2;
    public static final int DISPLAY_CRAFTER = 4;
    public static final int DISPLAY_CRAFTSTACK = 8;
    //public static final int DISPLAY_TEMP = 16;
    public static final UUID CARD_TYPE1 = new UUID(0, 2);

    @Override
    public UUID getCardType() {
        return CARD_TYPE1;
    }

    @Override
    public CardState update(TileEntity panel, ICardWrapper card, int range) {
        ChunkCoordinates target = card.getTarget();
        int targetType = card.getInt("targetType");
        if(targetType == 1) {
            TileEntity check = panel.getWorldObj().getTileEntity(target.posX, target.posY, target.posZ);
            if (check instanceof TileEntityNetworkLink) {
                TileEntityNetworkLink tileNetworkLink = (TileEntityNetworkLink) check;
                card.setInt("ByteTotal", tileNetworkLink.getTOTALBYTES());
                card.setInt("UsedBytes", tileNetworkLink.getUSEDBYTES());
                card.setInt("ItemsTotal", tileNetworkLink.getITEMTYPETOTAL());
                card.setInt("UsedItems", tileNetworkLink.getUSEDITEMTYPE());
                return CardState.OK;
            } else {
                return CardState.NO_TARGET;
            }
        } else if(targetType == 2){
            TileEntity check = panel.getWorldObj().getTileEntity(target.posX, target.posY, target.posZ);
            if(check instanceof TileCraftingMonitorTile){
                TileCraftingMonitorTile monitorTile = (TileCraftingMonitorTile) check;
                Item crafter;
                int size;
                if(monitorTile.getJobProgress() != null){
                    crafter = monitorTile.getJobProgress().getItemStack().getItem();
                    size = (int) monitorTile.getJobProgress().getStackSize();
                }else{
                    crafter = CrossAppeng.cardAppeng;
                    size = 0;
                }
                card.setInt("ITEMSTACK", Item.getIdFromItem(crafter));
                card.setInt("STACKSIZE", size);
                return CardState.OK;
            }
        } else{
            return CardState.NO_TARGET;
        }
        return CardState.NO_TARGET;
    }

    @Override
    public List<PanelString> getStringData(int displaySettings, ICardWrapper card, boolean showLabels) {
        List<PanelString> result = new LinkedList<PanelString>();
        PanelString line;
        int TYPE = card.getInt("targetType");

        if(TYPE == 1) {
            int byteTotal = card.getInt("ByteTotal");
            int usedBytes = card.getInt("UsedBytes");
            int items = card.getInt("ItemsTotal");
            int itemsUsed = card.getInt("UsedItems");

            //Total Bytes
            if ((displaySettings & DISPLAY_BYTES) > 0) {
                line = new PanelString();
                line.textRight = String.format(StatCollector.translateToLocal("msg.nc.InfoPanelAE.DisplayBytes"), usedBytes, byteTotal);
                result.add(line);
            }

            //Used Items
            if ((displaySettings & DISPLAY_ITEMS) > 0) {
                line = new PanelString();
                line.textRight = String.format(StatCollector.translateToLocal("msg.nc.InfoPanelAE.DisplayItem"), itemsUsed, items);
                result.add(line);
            }
        } else if(TYPE == 2){
            int stackSize = card.getInt("STACKSIZE");
            Item item = Item.getItemById(card.getInt("ITEMSTACK"));
            String localName = StatCollector.translateToLocal(item.getUnlocalizedName() + ".name");
            if(localName == "item.null.name" || localName.equals("Applied Energistics Card")){
                localName = StatCollector.translateToLocal("msg.null.craft");}

            //Crafting item
            if ((displaySettings & DISPLAY_CRAFTER) > 0) {
                line = new PanelString();
                line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelAE.CraftItemMake", localName, showLabels);
                result.add(line);
            }

            //Crafting Stacks
            if ((displaySettings & DISPLAY_CRAFTSTACK) > 0) {
                line = new PanelString();
                line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelAE.CraftAMT", stackSize, showLabels);
                result.add(line);
            }
        }
        return result;
    }

    @Override
    public List<PanelSetting> getSettingsList() {
        List<PanelSetting> result = new ArrayList<PanelSetting>(4);
        result.add(new PanelSetting(LangHelper.translate("msg.nc.InfoPanelAE.Bytes"), DISPLAY_BYTES, CARD_TYPE));
        result.add(new PanelSetting(LangHelper.translate("msg.nc.InfoPanelAE.Items"), DISPLAY_ITEMS, CARD_TYPE));
        result.add(new PanelSetting(LangHelper.translate("msg.nc.InfoPanelAE.CraftItem"), DISPLAY_CRAFTER, CARD_TYPE));
        result.add(new PanelSetting(LangHelper.translate("msg.nc.InfoPanelAE.CraftStack"), DISPLAY_CRAFTSTACK, CARD_TYPE));
        return result;
    }
}