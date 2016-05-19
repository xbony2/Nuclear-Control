package shedar.mods.ic2.nuclearcontrol.crossmod.vanilla;


import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.items.ItemSensorKitBase;

public class ItemVanillaKit extends ItemSensorKitBase{

    public ItemVanillaKit() {
        super("kitVanilla");
    }

    @Override
    protected ChunkCoordinates getTargetCoordinates(World world, int x, int y, int z, ItemStack stack) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if(tile instanceof TileEntityBrewingStand || tile instanceof TileEntityFurnace) {
            stack.setItemDamage(1);
            return new ChunkCoordinates(x, y, z);
        }else if(tile instanceof IInventory){
            if(((IInventory) tile).getSizeInventory() > 0) {
                stack.setItemDamage(0);
                return new ChunkCoordinates(x, y, z);
            }
        }
        return null;
    }

    @Override
    protected ItemStack getItemStackByDamage(int damage) {
        switch (damage){
            case 0:
                return new ItemStack(Vanilla.inventoryCard);
            case 1:
                return new ItemStack(Vanilla.machineCard);
            default:
                return null;
        }
    }
}
