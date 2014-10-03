package shedar.mods.ic2.nuclearcontrol.blocks;

import java.util.List;
import java.util.Map;

import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.blocks.subblocks.Subblock;
import shedar.mods.ic2.nuclearcontrol.utils.LightDamages;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockNuclearControlLight extends Block{
	public boolean isOn;
	public int damageOff;

	public BlockNuclearControlLight(Material material, String unlocal, boolean izOn, int damageOfff) {
		super(material);
		setHardness(0.3F);
		setStepSound(soundTypeGlass);
		setBlockName(unlocal);
		setCreativeTab(IC2NuclearControl.tabIC2NC);
		isOn = izOn;
		damageOff = damageOfff;
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z){
		if(isOn) return 15;
		return 0;
	}
	
	@Override
	public int damageDropped(int i) {
		return damageOff;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register){
		if(isOn) blockIcon = register.registerIcon("nuclearcontrol:light/on/" + this.getUnlocalizedName());
		else blockIcon = register.registerIcon("nuclearcontrol:light/off/" + this.getUnlocalizedName());
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		Block block = world.getBlock(x, y, z);
		if(this.isOn){
			if(!world.isRemote){
				
			}
		}
	}
}
