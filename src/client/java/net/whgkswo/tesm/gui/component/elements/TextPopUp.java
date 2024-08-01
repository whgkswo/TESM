package net.whgkswo.tesm.gui.component.elements;

import net.minecraft.client.gui.DrawContext;
import net.whgkswo.tesm.gui.Alignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.TransitionStatus;
import net.whgkswo.tesm.gui.component.bounds.Boundary;

public class TextPopUp extends GuiComponent<Boundary> {
    private String content;
    private float textScale;
    private int fadeInInterval;
    private int duration;
    private int fadeOutInterval;
    private int tick;
    private TransitionStatus status;

    public TextPopUp(Boundary bound, CustomColor color, String content, float textScale, int duration, int fadeInterval) {
        super(new CustomColor(color.getR(), color.getG(), color.getB(), 0), bound);
        this.content = content;
        this.textScale = textScale;
        this.duration = duration;
        this.fadeInInterval = fadeInterval;
        this.fadeOutInterval = fadeInterval;
        status = TransitionStatus.FADING_IN;
    }
    public TextPopUp(Boundary bound, CustomColor color, String content, float textScale, int duration, int fadeInInterval, int fadeOutInterval) {
        super(new CustomColor(color.getR(), color.getG(), color.getB(), 0), bound);
        this.content = content;
        this.textScale = textScale;
        this.duration = duration;
        this.fadeInInterval = fadeInInterval;
        this.fadeOutInterval = fadeOutInterval;
        status = TransitionStatus.FADING_IN;
    }

    @Override
    public void render(DrawContext context) {
        Boundary bound = getRenderingBound();
        update();
        if(status != TransitionStatus.TERMINATED){
            RenderingHelper.renderText(Alignment.LEFT, context, textScale, content, bound.getxRatio(), bound.getyRatio(), getColor().getHexDecimalCode());
        }
        tick++;
    }
    private void update(){
        switch (status){
            case FADING_IN -> {
                if(fadeInInterval > 0){
                    getColor().addA(255 / fadeInInterval);
                }
            }
            case FADING_OUT -> {
                if(fadeOutInterval > 0){
                    getColor().addA(-255 / fadeOutInterval);
                }
            }
        }
        changeStatus();
    }
    private void changeStatus(){
        if(tick == fadeInInterval){
            status = TransitionStatus.STABLE;
        }
        if(tick == fadeInInterval + duration){
            status = TransitionStatus.FADING_OUT;
        }
        if(tick == fadeInInterval + duration + fadeOutInterval){
            status = TransitionStatus.TERMINATED;
        }
    }
    public String getContent() {
        return content;
    }

    public TransitionStatus getStatus() {
        return status;
    }
}
