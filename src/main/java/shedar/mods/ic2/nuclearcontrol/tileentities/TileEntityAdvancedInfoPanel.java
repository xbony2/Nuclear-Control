package shedar.mods.ic2.nuclearcontrol.tileentities;

import ic2.api.network.NetworkHelper;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.items.ItemUpgrade;

public class TileEntityAdvancedInfoPanel extends TileEntityInfoPanel{
    private byte prevPowerMode;
    public byte powerMode;

    private byte prevThickness;
    public byte thickness;
    
    private byte prevRotateHor;
    public byte rotateHor;
    
    private byte prevRotateVert;
    public byte rotateVert;
    
    public ItemStack card2;
    public ItemStack card3;
    
    private static final byte SLOT_CARD1 = 0;
    private static final byte SLOT_CARD2 = 1;
    private static final byte SLOT_CARD3 = 2;
    private static final byte SLOT_UPGRADE_RANGE = 3;
    private static final byte SLOT_UPGRADE_WEB = 3;
    
    public static final int POWER_REDSTONE = 0;
    public static final int POWER_INVERTED = 1;
    public static final int POWER_ON = 2;
    public static final int POWER_OFF = 3;
    
    public static final int OFFSET_THICKNESS = 100;
    public static final int OFFSET_ROTATE_HOR = 200;
    public static final int OFFSET_ROTATE_VERT = 300;

    
    public TileEntityAdvancedInfoPanel(){
        super(4);//3 cards + range/web upgrade
        colored = true;
        thickness = 16;
    }
    
    @Override
    public int getCardSlotsCount(){
        return 3;
    }

    public byte getPowerMode(){
        return powerMode;
    }

    public void setPowerMode(byte p){
        powerMode = p;
        if (prevPowerMode != p)
        {
            NetworkHelper.updateTileEntityField(this, "powerMode");
            
        }
        prevPowerMode = powerMode;
    }
    
    public void setThickness(byte p){
        thickness = p;
        if (prevThickness != p)
        {
            NetworkHelper.updateTileEntityField(this, "thickness");
            
        }
        prevThickness = thickness;
    }
    
    public void setRotateHor(byte p){
        rotateHor = p;
        if (prevRotateHor != p)
        {
            NetworkHelper.updateTileEntityField(this, "rotateHor");
            
        }
        prevRotateHor = rotateHor;
    }
    
    public void setRotateVert(byte p){
        rotateVert = p;
        if (prevRotateVert != p)
        {
            NetworkHelper.updateTileEntityField(this, "rotateVert");
            
        }
        prevRotateVert = rotateVert;
    }
    
    @Override
    public List<String> getNetworkedFields(){
        List<String> list = super.getNetworkedFields();
        list.add("card2");
        list.add("card3");
        list.add("powerMode");
        list.add("thickness");
        list.add("rotateHor");
        list.add("rotateVert");
        return list;
    }

    @Override
    public void onNetworkUpdate(String field){
        super.onNetworkUpdate(field);
        if (field.equals("card2"))
        {
            inventory[SLOT_CARD2] = card2;
        }
        else if (field.equals("card3"))
        {
            inventory[SLOT_CARD3] = card3;
        } 
        else if (field.equals("powerMode") && prevPowerMode != powerMode)
        {
            if(screen!=null)
            {
                screen.turnPower(getPowered(), worldObj);
            }
            else
            {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                worldObj.updateAllLightTypes(xCoord, yCoord, zCoord);
            }
            prevPowerMode = powerMode;
        }
        else  if (field.equals("thickness") || field.equals("rotateHor") || field.equals("rotateVert"))
        {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }

    }
    
    @Override
    public boolean isItemValid(int slotIndex, ItemStack itemstack){
        switch (slotIndex){
            case SLOT_CARD1:
            case SLOT_CARD2:
            case SLOT_CARD3:
                return itemstack.getItem() instanceof IPanelDataSource;
            case SLOT_UPGRADE_RANGE:
                return itemstack.getItem() instanceof ItemUpgrade && 
                        (itemstack.getItemDamage() == ItemUpgrade.DAMAGE_RANGE ||
                        itemstack.getItemDamage() == ItemUpgrade.DAMAGE_WEB); 
            default: return false;
        }
    }
    
    @Override
    protected boolean isColoredEval()
    {
        return true;
    }

    @Override
    protected boolean isWebEval()
    {
        if(!IC2NuclearControl.instance.isHttpSensorAvailable)
            return false;
        ItemStack itemStack = inventory[SLOT_UPGRADE_WEB];
        return itemStack != null && itemStack.getItem() instanceof ItemUpgrade && itemStack.getItemDamage() == ItemUpgrade.DAMAGE_WEB;
    }
    
    @Override
    protected ItemStack getRangeUpgrade()
    {
        return inventory[SLOT_UPGRADE_RANGE];
    }

    @Override
    public List<ItemStack> getCards()
    {
        List<ItemStack> data = new ArrayList<ItemStack>(3);
        data.add(inventory[SLOT_CARD1]);
        data.add(inventory[SLOT_CARD2]);
        data.add(inventory[SLOT_CARD3]);
        return data;
    }
    
    @Override
    protected boolean isCardSlot(int slot)
    {
        return slot == SLOT_CARD1 || slot == SLOT_CARD2 || slot == SLOT_CARD3; 
    }

    @Override
    protected void saveDisplaySettings(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setTag("dSettings1", serializeSlotSettings(SLOT_CARD1));
        nbttagcompound.setTag("dSettings2", serializeSlotSettings(SLOT_CARD2));
        nbttagcompound.setTag("dSettings3", serializeSlotSettings(SLOT_CARD3));
        nbttagcompound.setByte("rotateHor", rotateHor);
        nbttagcompound.setByte("rotateVert", rotateVert);
        nbttagcompound.setByte("thickness", thickness);
        nbttagcompound.setByte("powerMode", powerMode);
    }
    
    @Override
    protected void readDisplaySettings(NBTTagCompound nbttagcompound)
    {
        deserializeDisplaySettings(nbttagcompound, "dSettings1", SLOT_CARD1);
        deserializeDisplaySettings(nbttagcompound, "dSettings2", SLOT_CARD2);
        deserializeDisplaySettings(nbttagcompound, "dSettings3", SLOT_CARD3);
        rotateHor = nbttagcompound.getByte("rotateHor");
        rotateVert = nbttagcompound.getByte("rotateVert");
        thickness = nbttagcompound.getByte("thickness");
        powerMode = nbttagcompound.getByte("powerMode");
    }

    @Override
    protected void postReadFromNBT()
    {
        if(inventory[SLOT_CARD1]!=null)
        {
            card = inventory[SLOT_CARD1];
        }
        if(inventory[SLOT_CARD2]!=null)
        {
            card2 = inventory[SLOT_CARD2];
        }
        if(inventory[SLOT_CARD3]!=null)
        {
            card3 = inventory[SLOT_CARD3];
        }
    }
    
    public byte getNextPowerMode()
    {
        switch (powerMode)
        {   
        case POWER_REDSTONE:
            return POWER_INVERTED;
        case POWER_INVERTED:
            return POWER_ON;
        case POWER_ON:
            return POWER_OFF;
        case POWER_OFF:
            return POWER_REDSTONE;
        }
        return POWER_REDSTONE;
    }
    
    @Override
    public boolean getPowered()
    {
        switch (powerMode)
        {   
        case POWER_ON:
            return true;
        case POWER_OFF:
            return false;
        case POWER_REDSTONE:
            return powered;
        case POWER_INVERTED:
            return !powered;
        }
        return false;
    }

    @Override
    public void onNetworkEvent(EntityPlayer entityplayer, int i)
    {
        super.onNetworkEvent(entityplayer, i);
        if(i>=0 && i<100)
        {
            switch (i)
            {   
            case POWER_ON:
            case POWER_OFF:
            case POWER_REDSTONE:
            case POWER_INVERTED:
                setPowerMode((byte)i);
            }
        }
        else if(i>=OFFSET_THICKNESS && i<OFFSET_THICKNESS+100)
        {
            i -= OFFSET_THICKNESS;
            setThickness((byte)i);
        }
        else if(i>=OFFSET_ROTATE_HOR && i<OFFSET_ROTATE_HOR+100)
        {
            i -= OFFSET_ROTATE_HOR + 8;
            i = -(i*7);
            setRotateHor((byte)i);
        }
        else if(i>=OFFSET_ROTATE_VERT && i<OFFSET_ROTATE_VERT+100)
        {
            i -= OFFSET_ROTATE_VERT + 8;
            i = -(i*7);
            setRotateVert((byte)i);
        }
    }    
}
