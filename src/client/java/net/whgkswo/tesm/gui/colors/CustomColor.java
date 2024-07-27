package net.whgkswo.tesm.gui.colors;

import java.awt.*;

public class CustomColor{
    private int r;
    private int g;
    private int b;
    private int a;
    public CustomColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        a = 255;
    }

    public CustomColor(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public int getA() {
        return a;
    }
    public float getFloatR(){
        return (float) r / 255;
    }
    public float getFloatG(){
        return (float) g / 255;
    }
    public float getFloatB(){
        return (float) b / 255;
    }
    public float getFloatA(){
        return (float) a / 255;
    }
}
