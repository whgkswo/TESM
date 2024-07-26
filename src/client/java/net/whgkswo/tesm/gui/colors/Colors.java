package net.whgkswo.tesm.gui.colors;

import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;

import java.util.HashMap;
import java.util.Map;

public class Colors {
    public static final Map<String, Identifier> COLORED_TEXTURES = new HashMap<>(){{
        put("aaa685", new Identifier(TESMMod.MODID, "textures/gui/base_aaa685.png"));
    }};
    public static String rgbToHex(int r, int g, int b){
        return String.format("#%02x%02x%02x", r, g, b);
    }
}
