package net.whgkswo.tesm.gui.libgui.server_side;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.whgkswo.tesm.libgui.ExampleGuiDesc;

public class ExampleGuiScreen extends CottonInventoryScreen<ExampleGuiDesc> {
    public ExampleGuiScreen(ExampleGuiDesc gui, PlayerEntity player, Text title) {
        super(gui, player, title);
    }
}
