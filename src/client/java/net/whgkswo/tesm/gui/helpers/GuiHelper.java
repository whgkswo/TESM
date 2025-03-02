package net.whgkswo.tesm.gui.helpers;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class GuiHelper {
    /*public static RenderLayer getLayer(Identifier id){
        String[] pathArr = id.getPath().split("/");
        String fileName = pathArr[pathArr.length - 1];
        fileName = fileName.substring(0, fileName.length() -4);

        return switch (fileName){
            case "downarrow", "interact_hud", "uparrow" -> RenderLayer.getGuiTexturedOverlay(id);

            default -> RenderLayer.getGui();
        };
    }*/
    public static RenderLayer getLayer(Identifier id){
        return RenderLayer.getGuiTexturedOverlay(id);
    }
}
