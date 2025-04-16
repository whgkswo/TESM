package net.whgkswo.tesm.gui.component.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.FadeSequence;
import net.whgkswo.tesm.gui.component.TransitionStatus;
import net.whgkswo.tesm.gui.component.bounds.Boundary;

public class TextPopUp extends TextLabel{
    private FadeSequence fadeSequence;
    private int tick;
    private TransitionStatus status;
    private TesmColor color;
    private FilledBox background;

    public TextPopUp(Text content, EdgedBox background, float textScale, HorizontalAlignment contentAlignment,
                     double textMarginRatio, FadeSequence fadeSequence) {
        super(content, background, textScale, contentAlignment, textMarginRatio);
        this.fadeSequence = fadeSequence;
        status = fadeSequence.getDelay() == 0 ? TransitionStatus.FADING_IN : TransitionStatus.PENDING;
        String textColor = RenderingHelper.getTextHexCode(content);
        color = new TesmColor(textColor, 0);
    }
    @Override
    public void renderSelf(DrawContext context) {
        Boundary bound = background.getBound();
        update();
        if(color.getA() < 4){
            return;
        }

        RenderingHelper.renderText(this.getContentAlignment(), context, getFontScale(),
                getContent(), bound.getxRatio(), bound.getyRatio());
    }
    private void update(){
        switch (status){
            case FADING_IN -> {
                if(fadeSequence.getFadeIn() > 0){
                    color.addA(255 / fadeSequence.getFadeIn());
                }
            }
            case FADING_OUT -> {
                if(fadeSequence.getFadeOut() > 0){
                    color.addA(-255 / fadeSequence.getFadeOut());
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
