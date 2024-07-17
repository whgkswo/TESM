package net.whgkswo.tesm.musics.v2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TimePeriod {
    MORNING,
    DAY,
    EVENING,
    NIGHT,
    EXCEPTION
    ;
    public static List<TimePeriod> getAllPeriods(){
        return new ArrayList<>(Arrays.asList(MORNING,DAY,EVENING,NIGHT));
    }
    public static TimePeriod getPeriod(long time){
        long timeModulo = time % 24000;
        if(timeModulo > 22500 || timeModulo <= 1500){
            return MORNING;
        } else if (timeModulo > 2000 && timeModulo <= 10000) {
            return DAY;
        } else if (timeModulo > 11000 && timeModulo <= 13000) {
            return EVENING;
        } else if (timeModulo > 13500 && timeModulo <= 21500) {
            return NIGHT;
        }else {
            return EXCEPTION;
        }
    }
}
