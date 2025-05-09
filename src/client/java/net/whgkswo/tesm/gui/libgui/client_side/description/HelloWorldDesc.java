package net.whgkswo.tesm.gui.libgui.client_side.description;

import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.gui.libgui.client_side.BackgroundLessScreenDesc;

public class HelloWorldDesc extends BackgroundLessScreenDesc {
    public HelloWorldDesc(){
        this("", "");
    }
    public HelloWorldDesc(String outsideName, String insideName) {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(256, 240);
        root.setInsets(Insets.ROOT_PANEL);

        WSprite icon = new WSprite(Identifier.ofVanilla("textures/item/redstone.png"));
        root.add(icon, 0, 2, 1, 1);

        WButton button = new WButton(Text.translatable("gui.examplemod.examplebutton"));
        root.add(button, 0, 3, 4, 1);

        WLabel label = new WLabel(Text.literal("Test"), 0xFFFFFF);
        root.add(label, 0, 4, 2, 1);

        WLabel outsideNameLabel = new WLabel(Text.literal(outsideName));
        root.add(outsideNameLabel, 0,5,1,1);

        WLabel insideNameLabel = new WLabel(Text.literal(insideName));
        root.add(insideNameLabel, 1,5,1,1);

        root.validate(this);
    }
}
