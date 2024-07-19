package net.whgkswo.tesm.calendar;

import net.whgkswo.tesm.general.GlobalVariables;

public class Time {
    private int hour;
    private int minute;
    private int second;

    public Time(double hour, double minute, double second) {
        this.hour = (int) hour;
        this.minute = (int) minute;
        this.second = (int) second;
    }
    public String toString(TimeFormat format, boolean includeSecond){
        switch (format){
            case COLONAL_24H -> {
                return includeSecond ? String.format("%d:%d:%d",hour,minute,second) :
                        String.format("%d:%d",hour,minute);
            }
            case COLONAL_12H -> {
                String amPm = hour<=12 ? "AM" : "PM";
                return includeSecond ? String.format("%s %d:%d:%d",amPm,hour%12,minute,second) :
                        String.format("%s %d:%d",amPm,hour%12,minute);
            }
            case LINGUAL_24H -> {
                return includeSecond ? String.format("%d시 %d분 %d초", hour,minute,second) :
                        String.format("%d시 %d분", hour,minute);
            }
            case LINGUAL_12H -> {
                String amPm = hour<=12 ? "오전" : "오후";
                return includeSecond ? String.format("%s %d시 %d분 %d초", amPm,hour%12,minute,second) :
                        String.format("%s %d시 %d분", amPm,hour%12,minute);
            }
            default -> {
                return null;
            }
        }
    }

    public static Time getTime(){
        double time = (double) GlobalVariables.world.getTimeOfDay() % 24000;
        double hour = time/1000;
        time -= hour * 1000;
        double minute = time / (1000.0/60);
        time -= minute * (1000.0/60);
        double second = time / (1000.0/3600);
        hour += 6;
        return new Time(hour,minute,second);
    }
    public enum TimeFormat{
        COLONAL_24H,
        COLONAL_12H,
        LINGUAL_24H,
        LINGUAL_12H
    }
}
