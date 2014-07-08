package shedar.mods.ic2.nuclearcontrol.renderers;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.IRotation;
import shedar.mods.ic2.nuclearcontrol.blocks.BlockNuclearControlMain;
import shedar.mods.ic2.nuclearcontrol.renderers.model.ModelInfoPanel;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanel;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanelExtender;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MainBlockRenderer implements ISimpleBlockRenderingHandler{
    private int modelId;

    public MainBlockRenderer(int modelId){
        this.modelId = modelId;
    }
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int model, RenderBlocks renderer)
    {
        if(model == modelId)
        {
            float[] size = /*IC2NuclearControl.instance.*/BlockNuclearControlMain.getBlockBounds(metadata); 
            block.setBlockBounds(size[0], size[1], size[2], size[3], size[4], size[5]);
            renderer.setRenderBoundsFromBlock(block);
            Tessellator tesselator = Tessellator.instance;
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            tesselator.startDrawingQuads();
            tesselator.setNormal(0.0F, -1.0F, 0.0F);
            renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata));
            tesselator.draw();
            tesselator.startDrawingQuads();
            tesselator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata));
            tesselator.draw();
            tesselator.startDrawingQuads();
            tesselator.setNormal(0.0F, 0.0F, -1.0F);
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, metadata));
            tesselator.draw();
            tesselator.startDrawingQuads();
            tesselator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, metadata));
            tesselator.draw();
            tesselator.startDrawingQuads();
            tesselator.setNormal(-1.0F, 0.0F, 0.0F);
            renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, metadata));
            tesselator.draw();
            tesselator.startDrawingQuads();
            tesselator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, metadata));
            tesselator.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int model, RenderBlocks renderer)
    {
        if(model == modelId)
        {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if(tileEntity instanceof IRotation)
            {
                switch(((IRotation) tileEntity).getFacing())
                {
                    case 0:
                        renderer.uvRotateBottom = ((IRotation) tileEntity).getRotation();
                        break;
                    case 1:
                        renderer.uvRotateTop = ((IRotation) tileEntity).getRotation();
                        break;
                    case 2:
                        renderer.uvRotateEast = ((IRotation) tileEntity).getRotation();
                        break;
                    case 3:
                        renderer.uvRotateWest = ((IRotation) tileEntity).getRotation();
                        break;
                    case 4:
                        renderer.uvRotateNorth = ((IRotation) tileEntity).getRotation();
                        break;
                    case 5:
                        renderer.uvRotateSouth = ((IRotation) tileEntity).getRotation();
                        break;
                        
                }
            }
            if(tileEntity instanceof TileEntityAdvancedInfoPanel)
            {
                TileEntityAdvancedInfoPanel advancedCore = (TileEntityAdvancedInfoPanel)tileEntity;
                if(advancedCore.getScreen()!=null)
                    new ModelInfoPanel().renderScreen(block, advancedCore, x, y, z, renderer);
                else
                    renderer.renderStandardBlock(block, x, y, z);
            }
            else if(tileEntity instanceof TileEntityAdvancedInfoPanelExtender)
            {
                TileEntityAdvancedInfoPanelExtender advancedExtender = (TileEntityAdvancedInfoPanelExtender)tileEntity;
                if(advancedExtender.getScreen()==null || advancedExtender.getScreen().getCore(advancedExtender.getWorldObj())==null)
                    renderer.renderStandardBlock(block, x, y, z);
            }
            else
            {
                renderer.renderStandardBlock(block, x, y, z);
            }
                
            renderer.uvRotateBottom = 0;
            renderer.uvRotateEast = 0;
            renderer.uvRotateNorth= 0;
            renderer.uvRotateSouth = 0;
            renderer.uvRotateTop = 0;
            renderer.uvRotateWest = 0;
            return true;
        }
        return false;
    }

    @Override
    public int getRenderId()
    {
        return IC2NuclearControl.instance.modelId;
    }

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

}
