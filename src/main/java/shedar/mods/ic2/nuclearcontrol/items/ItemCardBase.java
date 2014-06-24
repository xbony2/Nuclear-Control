package shedar.mods.ic2.nuclearcontrol.items;

import java.util.List;
import java.util.UUID;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.api.PanelSetting;
import shedar.mods.ic2.nuclearcontrol.api.PanelString;
import shedar.mods.ic2.nuclearcontrol.utils.TextureResolver;

public abstract class ItemCardBase extends Item implements  IPanelDataSource{
    private String textureItemName;
    
    public ItemCardBase(String textureItemName){
        super();
        this.textureItemName = textureItemName;
        setMaxStackSize(1);
        canRepair = false;
    }
    
    @Override
    public void registerIcons(IIconRegister iconRegister){
             itemIcon = iconRegister.registerIcon(TextureResolver.getItemTexture(textureItemName));
    }    

    @Override
    public boolean isDamageable(){
        return true;
    }
    /*
    @SuppressWarnings("rawtypes")
    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List){
        //should not be created via creative inventory
    }*/

    @Override
    abstract public CardState update(TileEntity panel, ICardWrapper card, int range);

    @Override
    abstract public UUID getCardType();

    @Override
    abstract public List<PanelString> getStringData(int displaySettings, ICardWrapper card, boolean showLabels);

    @Override
    abstract public List<PanelSetting> getSettingsList();
}
