package shedar.mods.ic2.nuclearcontrol.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ChatAllowedCharacters;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiTextArea extends Gui {
	private final int lineCount;
	private int maxStringLength = 32;
	private int cursorCounter;
	private int cursorPosition = 0;
	private int cursorLine = 0;
	private boolean isFocused = false;
	private String[] text;

	private final FontRenderer fontRenderer;

	private final int xPos;
	private final int yPos;
	private final int width;
	private final int height;

	public GuiTextArea(FontRenderer fontRenderer, int xPos, int yPos,
			int width, int height, int lineCount) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.fontRenderer = fontRenderer;
		this.lineCount = lineCount;
		text = new String[lineCount];
		for (int i = 0; i < lineCount; i++) {
			text[i] = "";
		}
	}

	public String[] getText() {
		return text;
	}

	public void drawTextBox() {
		drawRect(xPos - 1, yPos - 1, xPos + width + 1, yPos + height + 1,
				0xFFA0A0A0);
		drawRect(xPos, yPos, xPos + width, yPos + height, 0xFF000000);
		int textColor = 0xE0E0E0;

		int textLeft = xPos + 4;
		int textTop = yPos
				+ (height - lineCount * (fontRenderer.FONT_HEIGHT + 1)) / 2;

		for (int i = 0; i < lineCount; i++) {
			fontRenderer.drawStringWithShadow(text[i], textLeft, textTop
					+ (fontRenderer.FONT_HEIGHT + 1) * i, textColor);
		}
		textTop += (fontRenderer.FONT_HEIGHT + 1) * cursorLine;
		int cursorPositionX = textLeft
				+ fontRenderer.getStringWidth(text[cursorLine].substring(0,
						Math.min(text[cursorLine].length(), cursorPosition)))
				- 1;
		boolean drawCursor = isFocused && cursorCounter / 6 % 2 == 0;
		if (drawCursor)
			drawCursorVertical(cursorPositionX, textTop - 1,
					cursorPositionX + 1, textTop + 1 + fontRenderer.FONT_HEIGHT);
	}

	private void drawCursorVertical(int left, int top, int right, int bottom) {
		int var5;

		if (left < right) {
			var5 = left;
			left = right;
			right = var5;
		}

		if (top < bottom) {
			var5 = top;
			top = bottom;
			bottom = var5;
		}

		Tessellator var6 = Tessellator.instance;
		GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glLogicOp(GL11.GL_OR_REVERSE);
		var6.startDrawingQuads();
		var6.addVertex(left, bottom, 0.0D);
		var6.addVertex(right, bottom, 0.0D);
		var6.addVertex(right, top, 0.0D);
		var6.addVertex(left, top, 0.0D);
		var6.draw();
		GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void setCursorPosition(int x, int y) {
		if (y >= text.length)
			y = text.length - 1;
		cursorPosition = x;
		cursorLine = y;
		int lineLength = text[y].length();

		if (cursorPosition < 0) {
			cursorPosition = 0;
		}

		if (this.cursorPosition > lineLength) {
			this.cursorPosition = lineLength;
		}
	}

	public void deleteFromCursor(int count) {
		if (text[cursorLine].length() != 0) {
			boolean back = count < 0;
			String curLine = text[cursorLine];
			int left = back ? cursorPosition + count : cursorPosition;
			int right = back ? cursorPosition : cursorPosition + count;
			String newLine = "";

			if (left >= 0) {
				newLine = curLine.substring(0, left);
			}

			if (right < curLine.length()) {
				newLine = newLine + curLine.substring(right);
			}
			text[cursorLine] = newLine;
			if (back) {
				setCursorPosition(cursorPosition + count, cursorLine);
			}
		}
	}

	public void writeText(String additionalText) {
		String newLine = "";
		String filteredText = ChatAllowedCharacters
				.filerAllowedCharacters(additionalText);
		int freeCharCount = this.maxStringLength - text[cursorLine].length();

		if (text[cursorLine].length() > 0) {
			newLine = newLine + text[cursorLine].substring(0, cursorPosition);
		}

		if (freeCharCount < filteredText.length()) {
			newLine = newLine + filteredText.substring(0, freeCharCount);
		} else {
			newLine = newLine + filteredText;
		}

		if (text[cursorLine].length() > 0
				&& cursorPosition < text[cursorLine].length()) {
			newLine = newLine + text[cursorLine].substring(cursorPosition);
		}

		text[cursorLine] = newLine;
		setCursorPosition(cursorPosition + filteredText.length(), cursorLine);
	}

	private void setCursorLine(int delta) {
		int newCursorLine = cursorLine + delta;
		if (newCursorLine < 0)
			newCursorLine = 0;
		if (newCursorLine >= lineCount)
			newCursorLine = lineCount - 1;
		cursorPosition = Math.min(cursorPosition, text[newCursorLine].length());
		cursorLine = newCursorLine;
	}

	public void mouseClicked(int x, int y, int par3) {
		isFocused = x >= xPos && x < xPos + width && y >= yPos
				&& y < yPos + height;
	}

	public boolean isFocused() {
		return isFocused;
	}

	public void setFocused(boolean focused) {
		isFocused = focused;
	}

	public boolean textAreaKeyTyped(char par1, int par2) {
		if (this.isFocused) {
			switch (par1) {
			case 1:
				setCursorPosition(text[cursorLine].length(), cursorLine);
				return true;
			case 13:
				setCursorLine(1);
				return true;
			default:
				switch (par2) {
				case 14:// backspace
					deleteFromCursor(-1);
					return true;
				case Keyboard.KEY_HOME:
					setCursorPosition(0, cursorLine);
					return true;
				case Keyboard.KEY_LEFT:
					setCursorPosition(cursorPosition - 1, cursorLine);
					return true;
				case Keyboard.KEY_RIGHT:
					setCursorPosition(cursorPosition + 1, cursorLine);
					return true;
				case Keyboard.KEY_UP:
					setCursorLine(-1);
					return true;
				case Keyboard.KEY_DOWN:
					setCursorLine(1);
					return true;
				case Keyboard.KEY_END:
					setCursorPosition(text[cursorLine].length(), cursorLine);
					return true;
				case Keyboard.KEY_DELETE:
					deleteFromCursor(1);
					return true;
				default:
					if (ChatAllowedCharacters.isAllowedCharacter(par1)) {
						this.writeText(Character.toString(par1));
						return true;
					} else {
						return false;
					}
				}
			}
		} else {
			return false;
		}
	}
}
