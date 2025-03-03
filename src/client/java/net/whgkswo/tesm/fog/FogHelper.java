package net.whgkswo.tesm.fog;

import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FogShape;
import net.whgkswo.tesm.calendar.InGameTime;
import net.whgkswo.tesm.calendar.InGameTimePeriod;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.calendar.TimePeriod;

public class FogHelper {
    public static final float MAX_MORNING_FOG_DENSITY = 0.3f;
    public static final float MIN_FOG_DENSITY = 0.1f;

    public static Fog getFog(){
        // 아침 안개
        InGameTime time = GlobalVariables.currentInGameTime;
        return new Fog(
                1.0f,          // fogStart
                90.0f,          // fogEnd
                FogShape.SPHERE, // fogShape
                0.95f,        // R
                0.95f,        // G
                1.05f,        // B
                getFogDensity(time.isIn(TimePeriod.MORNING_FOG.getPeriod()) ? FogType.MORNING : FogType.DEFAULT)         // A
        );
    }

    public static float getFogDensity(FogType type){
        if(type.equals(FogType.MORNING)){
            return getMorningFogDensity();
        }
        return MIN_FOG_DENSITY;
    }

    public static float getMorningFogDensity(){
        InGameTime time = GlobalVariables.currentInGameTime;

        InGameTimePeriod asc = new InGameTimePeriod(TimePeriod.MORNING_FOG.getStart(), TimePeriod.MORNING_FOG_PEAK.getStart());
        InGameTimePeriod desc = new InGameTimePeriod(TimePeriod.MORNING_FOG_PEAK.getEnd(), TimePeriod.MORNING_FOG.getEnd());

        if(time.isIn(asc)){ // 안개가 짙어지는 시간
            float dydx = (MAX_MORNING_FOG_DENSITY - MIN_FOG_DENSITY) / asc.getTickLength();
            return dydx * asc.getProgressRatio(time) * asc.getTickLength() + MIN_FOG_DENSITY;
        } else if(time.isIn(TimePeriod.MORNING_FOG_PEAK.getPeriod())){
            return MAX_MORNING_FOG_DENSITY;
        }else{ // 안개가 걷히는 시간
            float dydx = (MAX_MORNING_FOG_DENSITY - MIN_FOG_DENSITY) / desc.getTickLength();
            return MAX_MORNING_FOG_DENSITY - dydx * desc.getProgressRatio(time) * desc.getTickLength();
        }
    }

    public enum FogType{
        MORNING,
        DEFAULT
        ;
    }
}
