package net.whgkswo.tesm.gui.component.bounds;

public record AbsolutePosition(int x1, int x2, int y1, int y2) {
    public int getWidth(){
        return x2 - x1;
    }

    public int getHeight(){
        return y2 - y1;
    }
}
