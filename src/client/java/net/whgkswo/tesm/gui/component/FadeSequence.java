package net.whgkswo.tesm.gui.component;

public class FadeSequence {
    private int delay;
    private int fadeIn;
    private int duration;
    private int fadeOut;

    public FadeSequence(int fadeIn, int duration, int fadeOut) {
        this.fadeIn = fadeIn;
        this.duration = duration;
        this.fadeOut = fadeOut;
    }

    public FadeSequence(int delay, int fadeIn, int duration, int fadeOut) {
        this.delay = delay;
        this.fadeIn = fadeIn;
        this.duration = duration;
        this.fadeOut = fadeOut;
    }

    public int getDelay() {
        return delay;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public int getDuration() {
        return duration;
    }

    public int getFadeOut() {
        return fadeOut;
    }
}
