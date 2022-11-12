package summit.gui.menu;

public class MenuComponent extends Menu{
    private Menu parent;

    public MenuComponent(Menu parent, float relX, float relY, float relWidth, float relHeight){
        super(relX,
                relY,
                relWidth,
                relHeight);
        this.parent = parent;
    }
}
