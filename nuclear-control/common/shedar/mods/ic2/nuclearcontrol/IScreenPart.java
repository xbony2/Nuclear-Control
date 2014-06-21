package shedar.mods.ic2.nuclearcontrol;

import shedar.mods.ic2.nuclearcontrol.panel.Screen;

public interface IScreenPart
{
    public void setScreen(Screen screen);
    public Screen getScreen();
    public void updateData();

}
