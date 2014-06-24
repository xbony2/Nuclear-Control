package shedar.mods.ic2.nuclearcontrol.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.BlockNuclearControlMain;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.subblocks.Subblock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemNuclearControlMain extends ItemBlock
{
    public ItemNuclearControlMain()
    {
        super(field_150939_a);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack item)
    {
        Subblock subblock = IC2NuclearControl.instance.blockNuclearControlMain.getSubblock(item.getItemDamage());
        if(subblock == null)
            return "";
        return subblock.getName();
    }
    
    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
       if (!world.setBlock(x, y, z, getBlockId(), metadata & 0xff, 3))
       {
               return false;
       }

       if (world.getBlockId(x, y, z) == getBlockID())
       {
           if(Block.blocksList[getBlockID()] instanceof BlockNuclearControlMain)
               ((BlockNuclearControlMain)Block.blocksList[getBlockID()]).onBlockPlacedBy(world, x, y, z, player, stack, metadata);
           else
               Block.blocksList[getBlockID()].onBlockPlacedBy(world, x, y, z, player, stack);
           Block.blocksList[getBlockID()].onPostBlockPlaced(world, x, y, z, metadata);
       }

       return true;
    }


    @SideOnly(Side.CLIENT)
    /**
     * Returns true if the given ItemBlock can be placed on the given side of the given block position.
     */
    @Override
    public boolean canPlaceItemBlockOnSide(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack item){
        Block var8 = world.getBlock(x, y, z);

        if (var8 == Blocks.snow){
            side = 1;
        }
        else if (var8 != Blocks.vine && var8 != Blocks.tallgrass && var8 != Blocks.deadbush
                && (Block.blocksList[var8] == null || !Block.blocksList[var8].isBlockReplaceable(world, x, y, z)))
        {
            if (side == 0)
            {
                --y;
            }

            if (side == 1)
            {
                ++y;
            }

            if (side == 2)
            {
                --z;
            }

            if (side == 3)
            {
                ++z;
            }

            if (side == 4)
            {
                --x;
            }

            if (side == 5)
            {
                ++x;
            }
        }

        return IC2NuclearControl.instance.blockNuclearControlMain.canPlaceBlockOnSide(world, x, y, z, side, item.getItemDamage());
    }
    
}
