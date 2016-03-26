package shedar.mods.ic2.nuclearcontrol.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import shedar.mods.ic2.nuclearcontrol.api.ICardGui;
import shedar.mods.ic2.nuclearcontrol.api.ICardSettingsWrapper;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;

public class GuiCardText extends GuiScreen implements ICardGui {
	private static final String TEXTURE_FILE = "nuclearcontrol:textures/gui/GUITextCard.png";
	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);

	private ICardSettingsWrapper wrapper;
	private ICardWrapper helper;
	private GuiTextArea textArea;

	protected int xSize = 226;
	protected int ySize = 146;
	protected int guiLeft;
	protected int guiTop;

	private static final int lineCount = 10;

	public GuiCardText(ICardWrapper helper){
		this.helper = helper;
	}

	@Override
	public boolean doesGuiPauseGame(){
		return false;
	}

	@Override
	public void setCardSettingsHelper(ICardSettingsWrapper wrapper){
		this.wrapper = wrapper;
	}

	@SuppressWarnings("unchecked")
	private void initControls(){
		buttonList.clear();
		buttonList.add(new GuiButton(1, guiLeft + xSize - 60 - 8, guiTop + 120, 60, 20, "Ok"));
		textArea = new GuiTextArea(fontRendererObj, guiLeft + 8, guiTop + 5, xSize - 16, ySize - 35, lineCount);
		textArea.setFocused(true);
		String[] data = textArea.getText();
		for (int i = 0; i < lineCount; i++)
			data[i] = helper.getString("line_" + i);
	}

	@Override
	protected void mouseClicked(int x, int y, int par3) {
		super.mouseClicked(x, y, par3);
		if (textArea != null)
			textArea.mouseClicked(x, y, par3);
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (textArea != null && wrapper != null) {
			String[] lines = textArea.getText();
			
			if (lines != null)
				for (int i = 0; i < lines.length; i++)
					wrapper.setString("line_" + i, lines[i]);
				
		}
		wrapper.commit();
		wrapper.closeGui();
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(TEXTURE_LOCATION);
		int left = (width - xSize) / 2;
		int top = (height - ySize) / 2;
		drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
		if (textArea != null)
			textArea.drawTextBox();
		super.drawScreen(par1, par2, par3);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if (par2 == 1 || (par2 == this.mc.gameSettings.keyBindInventory.getKeyCode() && (textArea == null || !textArea.isFocused()))) 
			actionPerformed(null);
		else if(textArea != null && textArea.isFocused())
			textArea.textAreaKeyTyped(par1, par2);
		else
			super.keyTyped(par1, par2);
	}

	@Override
	public void initGui(){
		super.initGui();
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		initControls();
	}
}
