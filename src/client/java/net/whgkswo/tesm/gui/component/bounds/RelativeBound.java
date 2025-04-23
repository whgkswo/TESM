package net.whgkswo.tesm.gui.component.bounds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

@Getter
@AllArgsConstructor
public class RelativeBound {
    public static final RelativeBound FULL_SCREEN = new RelativeBound(1, 1, 0, 0);

    private double widthRatio;
    private double heightRatio;
    private double xOffsetRatio;
    private double yOffsetRatio;

    public RelativeBound(double widthRatio, double heightRatio){
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
    }

    public AbsolutePosition toAbsolutePosition(){
        Window window = MinecraftClient.getInstance().getWindow();
        int scaledWidth = window.getScaledWidth();
        int scaledHeight = window.getScaledHeight();

        return new AbsolutePosition(
                (int) (this.getXOffsetRatio() * scaledWidth),
                (int) (this.getYOffsetRatio() * scaledHeight),
                (int) ((this.getXOffsetRatio() + this.getWidthRatio()) * scaledWidth),
                (int) ((this.getYOffsetRatio() + this.getHeightRatio()) * scaledHeight)
        );
    }
}
