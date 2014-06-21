package shedar.mods.ic2.nuclearcontrol.items;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.api.CardHelper;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.IAdvancedCardSettings;
import shedar.mods.ic2.nuclearcontrol.api.ICardGui;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;
import shedar.mods.ic2.nuclearcontrol.api.PanelSetting;
import shedar.mods.ic2.nuclearcontrol.api.PanelString;
import shedar.mods.ic2.nuclearcontrol.gui.GuiCardText;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCardText extends ItemCardBase implements IAdvancedCardSettings
{
    public static final UUID CARD_TYPE = UUID.fromString("90e53ad2-0aae-4937-9078-02a4561259d8");
    
    public ItemCardText(int i)
    {
        super(i, "cardText");
    }

    @Override
    public CardState update(TileEntity panel, ICardWrapper card, int range)
    {
        return CardState.OK;
    }

    @Override
    public UUID getCardType()
    {
        return CARD_TYPE;
    }
    
    @Override
    public List<PanelString> getStringData(int displaySettings, ICardWrapper card, boolean showLabels)
    {
        List<PanelString> result = new LinkedList<PanelString>();
        boolean started = false;
        for(int i=9; i>=0; i--)
        {
            String text = card.getString("line_"+i);
            if(text.equals("") && !started)
            {
                continue;
            }
            started = true;
            PanelString line = new PanelString();
            line.textLeft = text;
            result.add(0, line);
        }
        return result;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean advanced) 
    {
        ICardWrapper helper = CardHelper.getWrapper(itemStack);
        String title = helper.getTitle();
        if(!"".equals(title))
            info.add(title);
    }    

    @Override
    public List<PanelSetting> getSettingsList()
    {
        return null;
    }

    @Override
    public ICardGui getSettingsScreen(ICardWrapper wrapper)
    {
        return new GuiCardText(wrapper);
    }
}
