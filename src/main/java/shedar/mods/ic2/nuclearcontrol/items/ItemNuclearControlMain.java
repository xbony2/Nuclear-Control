package shedar.mods.ic2.nuclearcontrol.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shedar.mods.ic2.nuclearcontrol.blocks.BlockNuclearControlMain;
import shedar.mods.ic2.nuclearcontrol.blocks.subblocks.Subblock;
import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemNuclearControlMain extends ItemBlock {
	public ItemNuclearControlMain(Block block) {
		super(block);
		setMaxDamage(0);
		setHasSubtypes(true);
		setCreativeTab(IC2NuclearControl.tabIC2NC);
	}

	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack item) {
		Subblock subblock = IC2NuclearControl.blockNuclearControlMain.getSubblock(item.getItemDamage());
		if (subblock == null)
			return "";
		return subblock.getName();
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		if (!world.setBlock(x, y, z, field_150939_a, metadata & 0xff, 3)) {
			return false;
		}

		if (world.getBlock(x, y, z) == field_150939_a) {
			if (field_150939_a instanceof BlockNuclearControlMain)
				((BlockNuclearControlMain) field_150939_a).onBlockPlacedBy(world, x, y, z, player, stack, metadata);
			else
				field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
			field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
		}
		return true;
	}

	@SideOnly(Side.CLIENT)
	/**
	 * Returns true if the given ItemBlock can be placed on the given side of the given block position.
	 */
	@Override
	public boolean func_150936_a(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack item) {
		Block block = world.getBlock(x, y, z);

		if (block == Blocks.snow_layer) {
			side = 1;
		} else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, x, y, z)) {
			if (side == 0) {
				--y;
			}

			if (side == 1) {
				++y;
			}

			if (side == 2) {
				--z;
			}

			if (side == 3) {
				++z;
			}

			if (side == 4) {
				--x;
			}

			if (side == 5) {
				++x;
			}
		}
		
		/*if((Math.floor(player.posX) == x || Math.ceil(player.posX) == x) && (Math.floor(player.posZ) == y || Math.ceil(player.posZ) == y)) {
			if((Math.floor(player.posY) + 2 == y || Math.ceil(player.posY) + 2 == y) || (Math.floor(player.posY) + 2 == y || Math.ceil(player.posY) + 2 == y)){
				return false;
			}
		}*/

		return IC2NuclearControl.blockNuclearControlMain.canPlaceBlockOnSide(world, x, y, z, item.getItemDamage());
	}

}