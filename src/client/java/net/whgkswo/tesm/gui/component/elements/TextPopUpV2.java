package net.whgkswo.tesm.gui.component.elements;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.whgkswo.tesm.gui.Alignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.FadeSequence;
import net.whgkswo.tesm.gui.component.TransitionStatus;
import net.whgkswo.tesm.gui.component.bounds.Boundary;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;

public class TextPopUpV2 extends TextLabel{
    private FadeSequence fadeSequence;
    private int tick;
    private TransitionStatus status;

    public TextPopUpV2(CustomColor color, Text content, float textScale, Alignment contentAlignment,
                       RectangularBound bound, double textMarginRatio, FadeSequence fadeSequence) {
        super(color, content, textScale, contentAlignment, bound, textMarginRatio);
        this.fadeSequence = fadeSequence;
        status = fadeSequence.getDelay() == 0 ? TransitionStatus.FADING_IN : TransitionStatus.PENDING;
        color.setA(0);
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
        RenderingHelper.renderText(this.getContentAlignment(), context, getTextScale(),
                getContent(), bound.getxRatio(), bound.getyRatio(), color);
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
    public TransitionStatus getStatus() {
        return status;
    }
}
