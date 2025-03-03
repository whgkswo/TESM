package net.whgkswo.tesm.calendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TimePeriod {
    MORNING(new InGameTimePeriod(4, 30, 6, 30)),
    DAY(new InGameTimePeriod(6, 50, 14, 40)),
    EVENING(new InGameTimePeriod(16, 40, 18, 40)),
    NIGHT(new InGameTimePeriod(19, 10, 3, 30)),
    MORNING_FOG(new InGameTimePeriod(4, 0, 6, 0)),
    MORNING_FOG_PEAK(new InGameTimePeriod(4, 45, 5, 0)),
    DUMMY(null)
    ;

    private InGameTimePeriod period;

    TimePeriod(InGameTimePeriod period){
        this.period = period;
    }

    public static List<TimePeriod> getAllPeriods(){
        return new ArrayList<>(Arrays.asList(MORNING,DAY,EVENING,NIGHT));
    }
    public static TimePeriod getPeriod(long time){
        long timeModulo = time % 24000;
        /*if(timeModulo > 22500 || timeModulo <= 1500){
            return MORNING;
        } else if (timeModulo > 2000 && timeModulo <= 10000) {
            return DAY;
        } else if (timeModulo > 11000 && timeModulo <= 13000) {
            return EVENING;
        } else if (timeModulo > 13500 && timeModulo <= 21500) {
            return NIGHT;
        }else {
            return DUMMY;
        }*/
        InGameTime inGameTime = new InGameTime(time);
        if(inGameTime.isIn(MORNING.period)){
            return MORNING;
        } else if (inGameTime.isIn(DAY.period)) {
            return DAY;
        } else if (inGameTime.isIn(EVENING.period)) {
            return EVENING;
        } else if (inGameTime.isIn(NIGHT.period)) {
            return NIGHT;
        }else {
            return DUMMY;
        }
    }

    public InGameTimePeriod getPeriod(){
        return period;
    }
    public InGameTime getStart(){
        return period.getStart();
    }
    public InGameTime getEnd(){
        return period.getEnd();
    }
}
