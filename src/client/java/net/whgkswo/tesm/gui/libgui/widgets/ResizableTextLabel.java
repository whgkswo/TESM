package net.whgkswo.tesm.gui.libgui.widgets;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ResizableTextLabel extends WLabel {
    private float fontScale = 1.0f;

    public ResizableTextLabel(Text text, int color) {
        super(text, color);
    }

    public ResizableTextLabel(Text text) {
        super(text);
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.scale(fontScale, fontScale, 1.0f);
        super.paint(context, (int)(x/fontScale), (int)(y/fontScale), mouseX, mouseY);
        matrices.pop();
    }

    public void setFontScale(float fontScale){
        this.fontScale = fontScale;
    }
}
