package net.whgkswo.tesm.gui.component.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.whgkswo.tesm.gui.Alignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.FadeSequence;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.TransitionStatus;
import net.whgkswo.tesm.gui.component.bounds.Boundary;

public class TextPopUp extends GuiComponent<Boundary> {
    private Text content;
    private float textScale;
    private FadeSequence fadeSequence;
    private int tick;
    private TransitionStatus status;

    public TextPopUp(Boundary bound, CustomColor color, Text content, float textScale, FadeSequence fadeSequence) {
        super(new CustomColor(color.getR(), color.getG(), color.getB(), 0), bound);
        this.content = content;
        this.textScale = textScale;
        this.fadeSequence = fadeSequence;
        status = fadeSequence.getDelay() == 0 ? TransitionStatus.FADING_IN : TransitionStatus.PENDING;
    }

    @Override
    public void render(DrawContext context) {
        Boundary bound = getRenderingBound();
        update();
        if(getColor().getA() < 4){
            return;
        }
        int color = getColor().getHexDecimalCode();
        /*RenderingHelper.renderText(Alignment.LEFT, context, textScale, content, bound.getxRatio(), bound.getyRatio(), getColor().getHexDecimalCode());*/
        RenderingHelper.renderText(Alignment.LEFT, context, textScale,
                content, bound.getxRatio(), bound.getyRatio(), color);
    }
    private void update(){
        switch (status){
            case FADING_IN -> {
                if(fadeSequence.getFadeIn() > 0){
                    getColor().addA(255 / fadeSequence.getFadeIn());
                }
            }
            case FADING_OUT -> {
                if(fadeSequence.getFadeOut() > 0){
                    getColor().addA(-255 / fadeSequence.getFadeOut());
                }
            }
        }
        tick++;
        changeStatus();
    }
    private void changeStatus(){
        switch (status){
            case PENDING -> {
                if(tick == fadeSequence.getDelay()){
                    status = TransitionStatus.FADING_IN;
                    tick = 0;
                }
            }
            case FADING_IN -> {
                if(tick == fadeSequence.getFadeIn()){
                    status = TransitionStatus.STABLE;
                    tick = 0;
                }
            }
            case STABLE -> {
                if(tick == fadeSequence.getDuration()){
                    status = TransitionStatus.FADING_OUT;
                    tick = 0;
                }
            }
            case FADING_OUT -> {
                if(tick == fadeSequence.getFadeOut()){
                    status = TransitionStatus.TERMINATED;
                }
            }
        }
    }
}
