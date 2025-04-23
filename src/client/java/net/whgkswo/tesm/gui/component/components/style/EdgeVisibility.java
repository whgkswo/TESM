package net.whgkswo.tesm.gui.component.components.style;

import net.whgkswo.tesm.gui.GuiDirection;

public record EdgeVisibility(boolean top, boolean left, boolean right, boolean bottom) {
    public static final EdgeVisibility FULL_EDGES = new EdgeVisibility(true, true, true, true);
    public static final EdgeVisibility NO_EDGES = new EdgeVisibility(false, false, false, false);
    public static final EdgeVisibility TOP_ONLY = new EdgeVisibility(true, false, false, false);
    public static final EdgeVisibility LEFT_ONLY = new EdgeVisibility(false, true, false, false);
    public static final EdgeVisibility RIGHT_ONLY = new EdgeVisibility(false, false, true, false);
    public static final EdgeVisibility BOTTOM_ONLY = new EdgeVisibility(false, false, false, true);
    public static final EdgeVisibility TOP_AND_BOTTOM = new EdgeVisibility(true, false, false, true);
    public static final EdgeVisibility LEFT_AND_RIGHT = new EdgeVisibility(false, true, true, false);

    public static EdgeVisibility of(boolean top, boolean left, boolean right, boolean bottom){
        return new EdgeVisibility(top, left, right, bottom);
    }

    public boolean isVisible(GuiDirection direction){
        switch (direction){
            case TOP -> {
                return top;
            }
            case LEFT -> {
                return left;
            }
            case RIGHT -> {
                return right;
            }
            case BOTTOM -> {
                return bottom;
            }
        }
        return false;
    }
}
